package ricky

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import java.io.Reader
import java.io.Writer
import java.nio.file.Files
import java.nio.file.Path

/**
 * @see Gson.fromJson
 */
inline fun <reified T> Gson.fromJson(json: String): T = fromJson(json, T::class.java)

/**
 * @see Gson.fromJson
 */
inline fun <reified T> Gson.fromJson(json: Reader): T = fromJson(json, T::class.java)

/**
 * @param json string to be serialized into a [JsonObject]
 * @see Gson.fromJson
 */
fun Gson.treeOf(json: String): JsonObject = fromJson(json)

val JsonArray.allPrimitive: Boolean get() = all { it.isJsonPrimitive }
val JsonArray.allObject: Boolean get() = all { it.isJsonObject }
val JsonArray.allArray: Boolean get() = all { it.isJsonArray }


private fun JsonPrimitive.generateValues(): Pair<String, String> = when {
  isNumber -> "Number" to "0"
  isString -> "String" to "\"\""
  isBoolean -> "Boolean" to "false"
  else -> "Any?" to "null"
}

private fun JsonArray.generateArrays(
  key: String,
  classes: MutableList<String> = mutableListOf(),
  arrays: MutableList<String> = mutableListOf()
): String = buildString {
  val value = this@generateArrays.firstOrNull()
  val type = when {
    allPrimitive -> (value as JsonPrimitive).generateValues().first
    allObject -> key.capitalize().also { (value as JsonObject).generateClasses(it, classes) }
    allArray -> (value as JsonArray).generateArrays(key, classes, arrays)
    else -> "Any?"
  }

  append("List<$type>")
}

/**
 * generates kotlin classes (as a String) from json
 * @param className Name of the first class
 * @param classes Used for recursion, it hods all the classes as a String
 */
fun JsonObject.generateClasses(
  className: String,
  classes: MutableList<String> = mutableListOf()
): List<String> {
  val set = entrySet()
  val init = StringBuilder()
  val constructor = StringBuilder()

  set.forEachIndexed { index, entry ->
    val (key, value) = entry

    init.append("  $key: ")
    constructor.append("    $key = ")

    when (value) {
      is JsonObject -> {
        val name = key.capitalize()
        value.generateClasses(name, classes)
        init.append(name)
        constructor.append("$name()")
      }
      is JsonPrimitive -> {
        val (type, def) = value.generateValues()
        init.append(type)
        constructor.append(def)
      }
      is JsonArray -> {
        init.append(value.generateArrays(key, classes))
        constructor.append("listOf()")
      }
    }

    if (index < set.size - 1) {
      init.appendln(",")
      constructor.appendln(",")
    }
  }


  classes += buildString {
    appendln("class $className(")
    appendln(init)
    appendln(") {")
    appendln("  constructor() : this(")
    appendln(constructor)
    appendln("  )")
    appendln("}")
  }

  return classes
}

/**
 * @param path file to write the generated kotlin source to
 */
fun JsonObject.writeTo(path: Path) {
  if (path.parent != null && Files.notExists(path.parent)) {
    Files.createDirectories(path.parent)
  }
  if (Files.notExists(path)) {
    Files.createFile(path)
    writeTo("${path.fileName}".capitalize().split(".").first(), Files.newBufferedWriter(path))
  }
}

/**
 * @param className Name of the first class
 * @param writer Writer to write the generated kotlin source to
 */
fun JsonObject.writeTo(className: String, writer: Writer) {
  writer.write(toKtSource(className))
  writer.close()
}

/**
 * @param className Name of the first class
 */
fun JsonObject.toKtSource(className: String): String = generateClasses(className).joinToString("")
