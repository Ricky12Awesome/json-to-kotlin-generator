# Json To Kotlin Generator
Turns json into kotlin classes

# Example
```json
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
```
generates
```kotlin
class ListObj(
  oof: Boolean
) {
  constructor() : this(
    oof = false
  )
}
class Map(
  test: Number,
  test2: Number
) {
  constructor() : this(
    test = 0,
    test2 = 0
  )
}
class Obj(
  oof: Number,
  oof2: Number
) {
  constructor() : this(
    oof = 0,
    oof2 = 0
  )
}
class Test(
  string: String,
  int: Number,
  float: Number,
  boolean: Boolean,
  list: List<Number>,
  matrix: List<List<Number>>,
  listObj: List<ListObj>,
  map: Map,
  obj: Obj
) {
  constructor() : this(
    string = "",
    int = 0,
    float = 0,
    boolean = false,
    list = listOf(),
    matrix = listOf(),
    listObj = listOf(),
    map = Map(),
    obj = Obj()
  )
}

```
