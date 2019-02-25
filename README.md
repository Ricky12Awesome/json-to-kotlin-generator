# Json To Kotlin Generator
Turns json into kotlin classes

### Want to use this in your own project?
Just put [this](https://github.com/Ricky12Awesome/json-to-kotlin-generator/blob/master/src/main/kotlin/ricky/JsonToKtGenerator.kt)
into your own project

### Usage
see [test.kt](https://github.com/Ricky12Awesome/json-to-kotlin-generator/blob/master/src/test/kotlin/ricky/test.kt)
for more details
```kotlin
val gson = GsonBuilder().create()
val root = gson.treeOf("Json Text Here")
// this is the generate text for the kotlin file
val source = root.generateKotlinSource("Root", true)
```

### Examples
See [Examples](https://github.com/Ricky12Awesome/json-to-kotlin-generator/blob/master/examples/contents.md) for examples
