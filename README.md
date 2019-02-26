# Json To Kotlin Generator
Turns json into kotlin classes

### Want to use this in your own project?
Just put [this](https://github.com/Ricky12Awesome/json-to-kotlin-generator/blob/master/src/main/kotlin/ricky/JsonToKotlinGenerator.kt)
into your own project

### Usage
see [test.kt](https://github.com/Ricky12Awesome/json-to-kotlin-generator/blob/master/src/test/kotlin/ricky/test.kt)
for more details
```kotlin
val gson = GsonBuilder().create()
val root = gson.treeOf("Json Text Here")
// this is the generate list of JClasses
val source = JsonToKotlinGenerator.generateKotlinSource(root, "Root")
// this is the text that would be in the kotlin file
val text = source.joinToString("\n")
```

### Examples
See [Examples](https://github.com/Ricky12Awesome/json-to-kotlin-generator/blob/master/example/table.md) for examples
