package ricky

import com.google.gson.GsonBuilder
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
  val gson = GsonBuilder().create()
  val from = JsonToKtGenerator::class.java.classLoader.getResource("test.json")
  val to = Paths.get("test/json.kt")
  val root = gson.treeOf(from.readText())
  val source = root.generateKotlinSource("Root", true)

  if (Files.notExists(to.parent)) Files.createDirectories(to.parent)
  if (Files.notExists(to)) Files.createFile(to)

  Files.write(to, source.toByteArray())
}
