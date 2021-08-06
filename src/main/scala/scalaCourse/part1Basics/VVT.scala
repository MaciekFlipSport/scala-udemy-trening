package scalaCourse.part1Basics

object VVT extends App {
  val x: Int = 2
  println(x)

  // VALS ARE IMMUTABLE
  // COMPILER can  infer types

  def aRepeatedFunction(aString: String, n: Int): String = {
    if (n ==1) aString
    else aString + aRepeatedFunction(aString, n-1)
  }

  println(aRepeatedFunction("Eloszki", 3))


  def greetingFunction(name: String, age: String): Unit = {
    println(f"Hi my name is $name, and I'm $age old")
  }

  greetingFunction("Maciek", "30")

  def factorial(n: Int): BigInt = {
    if (n<=1) n
    else n * factorial(n-1)
  }
  println(factorial(50))

  def aFibonacci(n: Int): BigInt = {
    if (n<=2) 1
    else aFibonacci(n-1) + aFibonacci(n-2)
  }

   println(aFibonacci(6))

  def aPrime(n: Int): Boolean = {
    @scala.annotation.tailrec
    def isPrimeUntil(t: Int): Boolean =
      if (t<=1) true
      else n % t !=0 && isPrimeUntil(t-1)

    isPrimeUntil(n /2)
  }
  println(aPrime(37))
}

