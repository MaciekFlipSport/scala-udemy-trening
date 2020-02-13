package scalaCourse.part3fp

object AnonymousFunctions extends App{

  // anonymous function (LAMBDA)
  //  val doubler: Int => Int = x => x * 2
  val doubler = (x: Int) => x * 2 // the same as above

  // multiple parameters in a lambda
  val adder = (a: Int, b: Int) => a + b
  //  val adder: (Int, Int) => Int = (a: Int, b: Int) => a + b // the same as above

  // no params
  val justDoSomething = () => 3
  //  val justDoSomething: () => Int = () => 3 // the same as above

  // careful
  println(justDoSomething)  // function itself
  println(justDoSomething())  // call !!!!!!

  // curly braces with lambdas
  val stringToInt = { (str: String) =>
    str.toInt
  }

  // MORE syntactic sugar
  val niceIncrementer: Int => Int = _ + 1 // equivalent to x => x +1
  val niceAdder: (Int, Int) => Int = _ + _  // equivalent to (a,b) => a + b

  /*
   1. MyList replace all FunctionX calls with lambdas
   2. Rewrite the "special" adder as anonymous function
   */

  val superAdd = (x: Int) => (y: Int) => x + y
  println(superAdd(3)(4))
}
