package scalaCourse.part1Basics

object  Expressions extends App {

  val x = 1 + 2  //EXPRESSION
  println(x)

  println(2 + 3 * 4)
  // + - * / & | ^ << >> >>> (right shift with zero extension)

  println(1 == 3)
  // == != > >= < <=

  println(!(1==x))
  // ! && ||

  var aVariable = 2
  aVariable += 3 // also works with -= *= /=  ... side effects
  println(aVariable)

    // INSTRUCTIONS(DO) vs EXPRESSIONS(VALUE)

    // IF expression
  val aCondition = true
  val aConditionValue = if(aCondition) 5 else 3 // IF EXPRESSION
  println(aConditionValue)

  var i = 0
  while (i < 10) {
    println(i)
    i += 1
  }
  // NEVER WRITE THIS AGAIN

  // EVERYTHING in Scala is an Expressions
  val aWeiredValue: Unit = (aVariable = 3) // Unit === void (don't return anything meaningful)
  println(aWeiredValue)
  // side effects: println(), whiles, reassigning

  // Code blocks
  // VALUE of code block is the last code block expression !
  val aCodeBlock = {
    val y = 2
    val z = y + 1

    if (z > 2) "hello" else "goodbye"
  }
  println(aCodeBlock)

  // 1. difference between "hello world" vs println("hello world")
  // 2.
  val someValue = {
    2 < 3
  }
  println(someValue)

  val someOtherValue = {
    if (someValue) 239 else 986
    42
  }
  println(someOtherValue)

  val aCodeBlock2 = {
    val y = 2
    val z = y + 1

    if (z > 1) "heloo" else 1
  }

}
