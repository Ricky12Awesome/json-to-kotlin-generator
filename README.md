# Json To Kotlin Generator
Turns json into kotlin classes

## Want to use this in your own project?
Just put [this](https://github.com/Ricky12Awesome/json-to-kotlin-generator/blob/master/src/main/kotlin/ricky/JsonToKtGenerator.kt)
into your own project

## Usage
see [this](https://github.com/Ricky12Awesome/json-to-kotlin-generator/blob/master/src/test/kotlin/ricky/test.kt)
for more details
```kotlin
val gson = GsonBuilder().create()
val json = "Put Json String Here, like the example below."
gson.treeOf(json).writeTo(Paths.get("out", "test.kt"))
```

## Example
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

# TODO
* Option to use data classes over normal ones
