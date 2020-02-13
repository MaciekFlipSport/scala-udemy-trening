package scalaCourse.exercises

// GENERISISES
abstract class MyList[+A] {
  def head: A
  def tail: MyList[A]
  def isEmpty: Boolean
  def add[B >: A](int: B): MyList[B]
  def printElements: String
  // polymorphic call
  override def toString: String = "[" + printElements + "]"

  // higher-order functions
  def map[B](transformer: A => B): MyList[B]
  def flatMap[B](transformer: A => MyList[B]): MyList[B]
  def filter(predicate: A => Boolean): MyList[A]

  def ++[B >: A](list: MyList[B]): MyList[B]

  /*
  head = first element of the list
  tail = remainder of the list
  isEmpty = is this list empty
  add(int) => new list with this element added
  toString => a string representation of the list
   */

}

case object Empty extends MyList[Nothing] {
  def head: Nothing = throw new NoSuchElementException
  def tail: MyList[Nothing] = throw new NoSuchElementException
  def isEmpty: Boolean = true
  def add[B >: Nothing](int: B): MyList[B] = new Cons(int, Empty)
  def printElements: String = ""

  def map[B](transformer: Nothing => B): MyList[B] = Empty
  def flatMap[B](transformer: Nothing => MyList[B]): MyList[B] = Empty
  def filter(predicate: Nothing => Boolean): MyList[Nothing] = Empty

  def ++[B >: Nothing](list: MyList[B]): MyList[B] = list
}

case class Cons[+A](h: A, t: MyList[A]) extends MyList[A] {
  def head: A = h
  def tail: MyList[A] = t
  def isEmpty: Boolean  = false
  def add[B >: A](int: B): MyList[B]  = new Cons(int, this)
  def printElements: String =
    if (t.isEmpty) "" + h
    else h + " " + t.printElements

  def filter(predicate: A => Boolean): MyList[A] =
    if (predicate(h)) new Cons(h, t.filter(predicate))
    else t.filter(predicate)

  def map[B](transformer: A => B): MyList[B] =
    new Cons(transformer(h), t.map(transformer))

  /*
  [1,2] ++ [3,4,5]
    = new Cons(1, [2] ++ [3,4,5])
    = new Cons(1, new Cons(2, Empty ++ [3,4,5]))
    = new Cons(1, new Cons(2, new Cons(3, new Cons(4, new Cons(5)))))
   */
  def ++[B >: A](list: MyList[B]): MyList[B] = new Cons(h, t ++ list)

  /*
    [1,2].flatMap(n => [n, n+1])
    = [1,2] ++ [2].flatMap(n => [n, n+1])
    = [1,2] ++ [2,3] ++ Empty.flatMap(n => [n, n+1])
    = [1,2] ++ [2,3] ++ Empty
    = [1,2,2,3]
   */
  def flatMap[B](transformer: A => MyList[B]): MyList[B] =
    transformer(h) ++ t.flatMap(transformer)

}
//trait MyPredicate[-T] {
//  def test(elem: T): Boolean
//}

//trait MyTransformer[-A, B] {
//  def transform(elem: A): B
//}

object ListTest extends App {
  val list = new Cons(1, Empty)
  println(list.head)

  val list1 = new Cons(1, new Cons(2, new Cons(3, Empty)))
  println(list1.tail.head)
  println(list1.add(4).head)

  println(list1.toString)

  val listOfInteger: MyList[Int] = new Cons(1, new Cons(2, new Cons(3, Empty)))
  val anotherListOfInteger: MyList[Int] = new Cons(4, new Cons(5, Empty))
  val listOfStrings: MyList[String] = new Cons("Hello", new Cons("Scala", Empty))

  println(listOfInteger.toString)
  println(listOfStrings.toString)

  //  println(listOfInteger.map(new Function1[Int, Int] {
  //    override def apply(elem: Int): Int = elem * 2
  //  }).toString)
  //  val double: Int => Int = _ * 2
  //  println(listOfInteger.map(elem => double(elem)).toString)
  println(listOfInteger.map(elem => elem * 2).toString)  //  THE BEST
  println(listOfInteger.map(_ * 2).toString)  // FASTEST WAY

  //  println(listOfInteger.filter(new Function1[Int, Boolean] {
  //    override def apply(elem: Int): Boolean = elem % 2 == 0
  //  }).toString)
  //  val moduloTwo: Int => Boolean = _ %2 == 0
  //  println(listOfInteger.filter(element => moduloTwo(element)).toString)
  println(listOfInteger.filter(element => element %2 == 0).toString)  //  THE BEST
  println(listOfInteger.filter(_ %2 == 0).toString)  // FASTEST WAY

  println((listOfInteger ++ anotherListOfInteger).toString)
  //  println(listOfInteger.flatMap(new Function1[Int, MyList[Int]] {
  //    override def apply(elem: Int): MyList[Int] = new Cons(elem, new Cons(elem + 1, Empty))
  //  }))
  //  val difList: Int => MyList[Int] = a => Cons(a, Cons(a + 1, Empty))
  //  println(listOfInteger.flatMap(elem => difList(elem)).toString)
  println(listOfInteger.flatMap(elem => Cons(elem, Cons(elem + 1, Empty))).toString)
}