package scalaCourse.part1Basics

object Functions extends App {

  def aFUnction(a: String, b: Int): String = {
    a + " " + b
  }

  println(aFUnction("hello", 3))

  def aParameterlessFunction(): Int = 42
  println(aParameterlessFunction())
  println(aParameterlessFunction)

  def aRepeatedFunction(aString: String, n: Int): String = {
    if (n==1) aString
    else aString + aRepeatedFunction(aString, n-1)
  }

  // WHEN YUO NEED LOOPS, USE RECURSION.
  println(aRepeatedFunction("hello", 3))

  def aFunctionWithSideEffects(aString: String): Unit = println(aString)

  def aBigFunction(n: Int): Int = {
    def aSmallerFunction(a: Int, b: Int): Int = a + b

    aSmallerFunction(n, n-1)
  }

  /*
  1. A greeting function (name, age) => "Hi my name is $name and I am $age years old
  2. Factorial function 1 * 2 * 3 * 4
  3. A Fibonacci function
    f(1) = 1
    f(2) = 1
    f(n) = f(n-1) + f(n-2)
  4. Test if number is prime
   */


  // 1.
  def greeting(name: String, age: Int): Unit = {
    println(s"Hi my name is $name and I am $age years old")
  }

  // 2.
  def factorialFunction(n: Int): Int = {
    if (n <= 1) n
    else n * factorialFunction(n-1)
  }
  println(factorialFunction(5))

  // 3.
  def Fib(n: Int): Int = {
    if (n <= 2) 1
    else Fib(n-1) + Fib(n-2)
  }

  println(Fib(8))

  // 4.
  def testPrime(n: Int): Boolean = {
    def isPrimeUntil(t: Int): Boolean =
      if (t <= 1) true
      else n % t != 0 && isPrimeUntil(t-1)

    isPrimeUntil(n / 2)
  }

  println(testPrime((37)))
  println(testPrime(2003))
  println(testPrime((37*17)))


}
