package cn.byteboy.demo.scala

object Demo {
  def main(args: Array[String]): Unit = {
    val myMap: Map[String, String] = Map("key1" -> "v")
    val value1: Option[String] = myMap.get("key1")
    val value2: Option[String] = myMap.get("key2")
    println(value1)
  }
}