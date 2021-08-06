package scalaCourse.part3fp

object MapFlatMapFilterFor extends App {

  val list = List(1, 2, 3)
  println(list)

  // map
  println(list.map(_ + 1))
  println(list.map(_ + " is a number"))

  // filter
  println(list.filter(_ % 2 == 0))

  // flatMap
  val toPair = (x: Int) => List(x, x + 1)
  println(list.flatMap(toPair))

  //print all combinations between two lists
  val number = List(1, 2, 3, 4)
  val chars = List('a', 'b', 'c', 'd')
  // List(a1, a2, .....)

  // ITERATIONS ! ! ! IMPORTANT
  val combinations = number.flatMap(n => chars.map(c => s"$n$c"))
  println(combinations)

  //if I have to combine 3 lists:
  val colors = List("black", "yellow")
  val combinationsOf3 = number.flatMap(
    n => chars.flatMap(c => colors.map(colors => s"$n$c-$colors"))
  )
  println(combinationsOf3)

  // foreach
  list.foreach(println)

  //for-comprehensions
  val forCombinations = for {
    n <- number
    c <- chars
    colors <- colors
  } yield s"$n$c-$colors"

  println(forCombinations)

  //for-comprehenstion with filter
  val forCombinationsWithFilter = for {
    n <- number if n % 2 == 0
    c <- chars
    colors <- colors
  } yield s"$n$c-$colors"

  println(forCombinationsWithFilter)

  //syntax overload
  list.map { x =>
    x * 2
  }

  /*
  1. MyList supports for comprehensions?
    map(f: A => B) => MyList[B]
    filter(p: A => Boolean) => MyList[A]
    flatMap(f: A => MyList[B]) => MyList[B]
  2. A small collection of at most ONE element - Maybe[+T]
    -map ,flatMap, filter
 */

}
