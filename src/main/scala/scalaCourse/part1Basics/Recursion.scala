package scalaCourse.part1Basics

import scala.annotation.tailrec

object Recursion extends App{

    def factorial(n: Int): Int =
      if(n <= 1 ) 1
      else {
        println(s"Computing factorial of $n - I first need factorial of ${n-1}")
        val result = n * factorial((n - 1))
        println(s"Computed factorial of $n")

        result
      }

  println(factorial(10))

  def anotherFactorial(n: Int): BigInt = {
    @tailrec
    def factHelper(x: Int, accu: BigInt): BigInt =
      if (x <= 1) accu
      else factHelper(x-1, x * accu)  // TAIL RECURSION = use recursive call as the LAST expression

    factHelper(n, 1)
  }

  println(anotherFactorial(5000))

  // WHEN YOU NEED LOOPS, USE _TAIL_ RECURSION
  @tailrec
  def aRepeatedFunction(aString: String, n: Int, accu: String): String = {
    if (n <= 0) accu
    else aRepeatedFunction(aString, n-1, accu + aString)
  }

  println(aRepeatedFunction("Hello_", 5, ""))


//  def Fib(n: Int): Int = {
////    if (n <= 2) 1
////    else Fib(n-1) + Fib(n-2)
////  }
  def Fib(n: Int): BigInt = {
    @tailrec
    def fibTailrec(i: Int, last: BigInt, lastToNext: BigInt): BigInt =
      if (i >=n) last
    else fibTailrec(i+1, last + lastToNext, last)

    if (n <= 2) 1
    else fibTailrec(2, 1 ,1)
  }

  println(Fib(10))
}
