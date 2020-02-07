package scalaCourse.part1Basics

object CBNvsCBV extends App {

  def calledByValue(x: Long): Unit = {
    println(s"by value $x")
    println(s"by value $x")
  }

  def calledByName(x: => Long): Unit = {
    println(s"by name $x")
    println(s"by name $x")
  }

  calledByValue(System.nanoTime())
  calledByName(System.nanoTime())

  def infinite(): Int = 1 + infinite()

}
