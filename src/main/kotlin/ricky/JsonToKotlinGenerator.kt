package ricky

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive

private val JsonArray.allPrimitive: Boolean get() = all { it.isJsonPrimitive }
private val JsonArray.allObject: Boolean get() = all { it.isJsonObject }
private val JsonArray.allArray: Boolean get() = all { it.isJsonArray }

data class JData(
  val name: String,
  val typeName: String,
  val defaultValue: String
) {
  constructor(name: String, jArray: JArray) : this(
    name = name,
    typeName = jArray.toString(),
    defaultValue = "listOf()"
  )

  constructor(name: String, jClass: JClass) : this(
    name = name,
    typeName = jClass.typeName,
    defaultValue = "${jClass.typeName}()"
  )

  override fun toString(): String = "val $name: $typeName = $defaultValue"
}

data class JArray(
  val typeName: String
) {
  constructor(jClass: JClass) : this(jClass.className)
  constructor(jClass: JArray) : this(jClass.toString())
  constructor(jData: JData) : this(jData.typeName)

  override fun toString(): String = "List<$typeName>"
}

data class JClass(
  val className: String,
  val index: Int,
  val data: List<JData>,
  val indent: Int
) {
  val typeName = if (index > 0) className + index else className
  override fun toString(): String = buildString {
    val space = buildString { repeat(indent) { append(" ") } }
    if (data.isNotEmpty()) {
      appendln("data class $typeName(")
      appendln(data.joinToString(",\n") { "$space$it" })
      appendln(")")
    } else {
      appendln("class $typeName")
    }
  }
}

class JsonToKotlinGenerator(
  val root: JsonObject,
  val rootName: String,
  val indent: Int = 2
) {
  val classes = mutableListOf<JClass>()

  fun classNameOf(str: String) = str.split("_").joinToString { it.capitalize() }
  fun valNameOf(str: String) = classNameOf(str).decapitalize()

  fun JsonPrimitive.generateData(key: String): JData = when {
    isBoolean -> JData(key, "Boolean", "false")
    isString -> JData(key, "String", "\"\"")
    isNumber -> JData(key, "Number", "0")
    else -> JData(key, "Any?", "null")
  }

  fun JsonArray.generateArrayData(key: String): JArray {
    val def = JArray("Any?")
    val first = firstOrNull() ?: return def
    val name = classNameOf(key) + "Element"
    return when {
      allPrimitive -> JArray((first as JsonPrimitive).generateData(key))
      allObject -> JArray((first as JsonObject).generateClass(name))
      allArray -> JArray((first as JsonArray).generateArrayData(key))
      else -> def
    }
  }

  fun JsonObject.generateClass(className: String): JClass {
    val set = entrySet()
    val data = mutableListOf<JData>()

    set.forEachIndexed { index, entry ->
      val (key, value) = entry
      val name = classNameOf(key)

      data += when (value) {
        is JsonPrimitive -> value.generateData(key)
        is JsonArray -> JData(key, value.generateArrayData(key))
        is JsonObject -> JData(key, value.generateClass(name))
        else -> JData(key, "Any?", "null")
      }
    }

    val index = classes.filter { it.className.startsWith(className) }.size

    return JClass(
      className = className,
      index = index,
      data = data,
      indent = indent
    ).also {
      classes += it
    }
  }

  fun generate(): List<JClass> {
    root.generateClass(rootName)
    return classes
  }

  companion object {
    fun generateFrom(
      json: JsonObject,
      rootClassName: String = "Root",
      indent: Int = 2
    ) = JsonToKotlinGenerator(json, rootClassName, indent).generate()
  }

}
