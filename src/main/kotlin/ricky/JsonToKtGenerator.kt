package ricky

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import java.io.Reader

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


/**
 * @param root the root [JsonObject]
 * @param rootName name of the root object
 * @param generateAsDataClasses rather or not it should use data classes
 */
class JsonToKtGenerator(
  val root: JsonObject,
  val rootName: String = "Root",
  val generateAsDataClasses: Boolean = false
) {
  private val JsonArray.allPrimitive: Boolean get() = all { it.isJsonPrimitive }
  private val JsonArray.allObject: Boolean get() = all { it.isJsonObject }
  private val JsonArray.allArray: Boolean get() = all { it.isJsonArray }

  private val String.capitalize get() = split("_").joinToString("") { it.capitalize() }
  private val String.camelCase get() = capitalize.decapitalize()

  private val classes: MutableMap<String, String> = mutableMapOf()
  private val classIndexes: MutableMap<String, Int> = mutableMapOf()

  private fun JsonPrimitive.generateValues(): Pair<String, String> = when {
    isNumber -> "Number" to "0"
    isString -> "String" to "\"\""
    isBoolean -> "Boolean" to "false"
    else -> "Any?" to "null"
  }

  private fun JsonArray.generateArrays(key: String): String = buildString {
    val value = this@generateArrays.firstOrNull()
    val type = when {
      value == null -> "Any?"
      allPrimitive -> (value as JsonPrimitive).generateValues().first
      allObject -> key.capitalize().also { (value as JsonObject).generateClasses(it) }
      allArray -> (value as JsonArray).generateArrays(key)
      else -> "Any?"
    }

    append("List<$type>")
  }

  /**
   * generates kotlin classes (as a String) from a [JsonObject]
   * @param className Name of the class
   */
  private fun JsonObject.generateClasses(className: String): List<String> {
    val set = entrySet()
    val init = StringBuilder()
    val constructor = StringBuilder()

    set.forEachIndexed { index, entry ->
      val (key, value) = entry

      init.append("  val ${key.camelCase}: ")
      if (!generateAsDataClasses) constructor.append("    $key = ")

      when (value) {
        is JsonObject -> {
          val kName = key.capitalize
          val cName = kName + classIndexes.getOrElse(kName) { "" }

          classIndexes[kName] = classIndexes.getOrElse(kName) { 0 } + 1

          value.generateClasses(cName)
          if (generateAsDataClasses) {
            init.append("$cName = $cName()")
          } else {
            init.append(cName)
            constructor.append("$cName()")
          }
        }
        is JsonPrimitive -> {
          val (type, def) = value.generateValues()
          if (generateAsDataClasses) {
            init.append("$type = $def")
          } else {
            init.append(type)
            constructor.append(def)
          }
        }
        is JsonArray -> {
          val type = value.generateArrays(key)
          if (generateAsDataClasses) {
            init.append("$type = listOf()")
          } else {
            init.append(type)
            constructor.append("listOf()")
          }
        }
        else -> {
          if (generateAsDataClasses) {
            init.append("Any? = null")
          } else {
            init.append("Any?")
            constructor.append("null")
          }
        }
      }

      if (index < set.size - 1) {
        init.appendln(",")
        constructor.appendln(",")
      }
    }

    classes[className] = buildString {
      if (set.isNotEmpty()) {
        if (generateAsDataClasses) {
          appendln("data class $className(")
          appendln(init)
          appendln(")")
        } else {
          appendln("class $className(")
          appendln(init)
          appendln(") {")
          appendln("  constructor() : this(")
          appendln(constructor)
          appendln("  )")
          appendln("}")
        }
      } else {
        appendln("class $className")
      }
    }

    return classes.values.toList()
  }

  fun generate(): String = root.generateClasses(rootName).joinToString("")

}

/**
 * Generates kotlin classes (as a String) from a [JsonObject]
 * @param className Name of the first class
 * @param generateAsDataClasses rather or not it should use data classes
 */
fun JsonObject.generateKotlinSource(
  className: String,
  generateAsDataClasses: Boolean = false
): String = JsonToKtGenerator(this, className, generateAsDataClasses).generate()
