package scalaCourse.part1Basics

import scala.annotation.tailrec

object Recursive2 extends App{


  def aFactorial(n: Int): Int = {
    if (n<=1) 1
    else {
      println(f"Computing factorial of $n - I first need factorial of ${n-1}")
      val result = n * aFactorial(n-1)
      println(f"Computed factorial of $n")

      result
    }
  }

  println(aFactorial(100))

def anotherFactorial(n: Int): BigInt = {
  def factHelper(x: Int, accu: BigInt): BigInt =
    if (n <= 2) accu
    else factHelper(x - 1, x * accu)

    factHelper(n, 1)
}
  /* anotherFactorial(10) = factHelper(10, 1)
  = factHelper(9, 10 * 1)
  = factHelper(8, 9 * 10 * 1)
  = factHelper(7, 8 * 9 * 10 * 1  )
  ...
  = factHelper(2, 2 * 3 * 4)
  = factHelper(1, 1 * 2 * 3 ...)
  = 1 * 2 * 3 ... 10
*/

//
//  def aRepeatedFunction(aString: String, n: Int): String = {
//    if (n ==1) aString
//    else aString + aRepeatedFunction(aString, n-1)
//  }

  @tailrec
  def aRepeatedFunction(aString: String, n: Int, accu:String): String = {
      if (n <=1) accu
      else aRepeatedFunction(aString, n-1, aString + accu)
  }

  println(aRepeatedFunction("Okey", 6, ""))


  def aFibonacci(n: Int): BigInt = {
      def fiboTailrec(i:Int, last:Int, nextLast:Int): Int =
    if (i >= n) last
    else fiboTailrec(i+1, last+nextLast, last)

    if (n<= 2) 1
    else fiboTailrec(2, 1, 1)
  }



}


