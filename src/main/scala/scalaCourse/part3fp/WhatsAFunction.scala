package scalaCourse.part3fp

object WhatsAFunction extends App {

  // DREAM: use functions as first class elements
  // problem: oop

  val doubler = new MtFunction[Int, Int] {
    override def apply(element: Int): Int = element * 2
  }

  println(doubler(2))

  // function types = Function1[A, B]
  val stringToIntConventer = new Function[String, Int] {
    override def apply(string: String): Int =string.toInt
  }

  println(stringToIntConventer("3") +  4)

  val adder: ((Int, Int) => Int) = (v1: Int, v2: Int) => v1 + v2

  // Function types Function2[A, B, R] === (A,B) => R

  // ALL SCALA FUNCTIONS ARE OBJECTS

  /*
  1. a function which takes 2 strings and concatanates them
  2. transform the MyPredicate and MyTransform into function types
  3. define a function which takes an int and returns another function which takes an int and returns an int
      - what;s the type of this function
      - how to do it
   */

  // 1.
  // NORMAL IMPLEMENTATION
//  val stringConcate: (String, String) => String = new Function2[String, String, String] {
//    override def apply(v1: String, v2: String): String = v1 + v2
//  } 0
  val stringConcate: (String, String) => String = (s1, s2) => s"$s1  $s2"
  println(stringConcate("string1", "string2"))

  // 2.

  // 3.
  // Function1[Int, Function1[Int, Int]]
//  val specialFunction: (Int => (Int, Int)) = (s1, s2) => Function1[s1, s2] // ???
  val superAdder: Function1[Int, Function1[Int, Int]] = new Function[Int, Function1[Int, Int]] {
    override def apply(x: Int): Function1[Int, Int] = new Function1[Int, Int] {
      override def apply(y: Int): Int = x + y
    }
  }

  val adder3 = superAdder(3)
  println(adder3(4))
  println(superAdder(3)(4))  // curried function


}

trait MtFunction[A, B] {
  def apply(element: A): B
}

