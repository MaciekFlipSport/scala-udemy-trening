package scalaCourse.part1Basics

object ValuesVariablesTypes extends App {

  val x: Int = 42
  println(x)

  // VAL ARE IMMUTABLE

  // COMPILER can infer types

  val aString: String = "hello"
  val anotherString: String = "another string"

  val aBoolean: Boolean = false
  val aChar: Char = 'a'
  val anInt: Int = x
  val aShort: Short = 333
  val aLong: Long = 289374029837498204L
  val aFloat: Float = 2.0f
  val aDouble: Double = 2.43

  // variables
  var aVariable: Int = 4

  aVariable = 5 //side effects


}
