package ricky

data class Primitives(
  val float: Number = 0,
  val int: Number = 0,
  val boolean: Boolean = false,
  val string: String = ""
)

data class Primitives1(
  val strings: List<String> = listOf(),
  val numbers: List<Number> = listOf(),
  val booleans: List<Boolean> = listOf()
)

data class Multi(
  val d1: List<Number> = listOf(),
  val d2: List<List<Number>> = listOf(),
  val d3: List<List<List<Number>>> = listOf(),
  val d15: List<List<List<List<List<List<List<List<List<List<List<List<List<List<List<Number>>>>>>>>>>>>>>> = listOf()
)
data class ObjectsElement(
  val data: String = ""
)

data class Arrays(
  val primitives: Primitives1 = Primitives1(),
  val multi: Multi = Multi(),
  val objects: List<ObjectsElement> = listOf()
)

data class Data(
  val primitives: Primitives = Primitives(),
  val arrays: Arrays = Arrays()
)

data class Root(
  val data: Data = Data()
)
