package ricky

import com.google.gson.GsonBuilder
import java.nio.file.Paths

fun main() {
  val gson = GsonBuilder().create()
  val json = """
    {
      "string": "oof",
      "int": 123,
      "float": 1.0,
      "boolean": true,
      "list": [1, 2, 3],
      "matrix": [
        [1, 2, 3],
        [4, 5, 6],
        [7, 8, 9]
      ],
      "listObj": [
        {
          "oof": true
        }
      ],
      "map": {
        "test": 32,
        "test2": -32
      },
      "obj": {
        "oof": 323,
        "oof2": 2390
      }
    }
  """.trimIndent()
  gson.treeOf(json).writeTo(Paths.get("out", "test.kt"))
}
