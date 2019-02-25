data class A5(
  val b: String = ""
)
data class A4(
  val a: A5 = A5()
)
data class A3(
  val a: A4 = A4()
)
data class A2(
  val a: A3 = A3()
)
data class A1(
  val a: A2 = A2()
)
data class A(
  val a: A1 = A1()
)
data class Root(
  val a: A = A()
)
