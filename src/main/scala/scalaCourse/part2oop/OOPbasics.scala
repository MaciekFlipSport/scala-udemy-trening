package scalaCourse.part2oop

object OOPbasics extends App {

  val person = new Person("John", 26)
  println(person.age)
  println(person.x)
  person.greet("Daniel")
  person.greet()

  val adamMickiewicz = new Writer("Adam", "Mickiewicz", 1798)
  val panTadeusz = new Novel("Pan Tadeusz", 1834, adamMickiewicz)

  println(panTadeusz.authorAge())
  panTadeusz.isWrittenBy(adamMickiewicz)
  val newPanTadeusz: Novel = panTadeusz.copy(1840)
  newPanTadeusz.isWrittenBy(adamMickiewicz)
  println(newPanTadeusz.authorAge())


  val counter = new Counter
  counter.inc.print
  counter.inc.inc.inc.print
  counter.inc(10).print

}
// constructor
class Person(name: String, val age: Int) {
  // body
  val x = 2

  println(1 + 3)

  //method
  def greet(name: String): Unit = println(s"${this.name} says Hi, $name")

  //overloading
  def greet(): Unit = println(s"Hi, I am $name")

  // multiple constructors
  def this(name: String) = this(name, 0)
  // ?? not needed so much!
}
// class parameters are NOT FIELDS
// class parameters and class fields are two different things
//  => parameter = name: String (can't access person.age)
//  => fields = val name: String (CAN access person.age)


/*
Novel and a Writer

Writer: first name, surname, year
  -method fullname

Novel: name, year of realase, author
  -authorAge
  -isWrittenBy(author)
  -copy (new year of realse) = new instance of Novel
 */

/*
Counter class
  -receives an int value
  -method current count
  -method to incerement/decrement => new Counter
  - overload inc/dec to receive amount
 */

// 1.
class Writer(firstName: String, surName: String, val year: Int) {
  def fullName(): String = s"$firstName $surName"
}

class Novel(name: String, yearOfRelease: Int, author: Writer) {

  def authorAge(): Int = yearOfRelease - author.year
//  def isWrittenBy(): Unit = println(s"Novel written by ${author.fullName()}")
  def isWrittenBy(author: Writer) = author == this.author
  def copy(newYear: Int): Novel = new Novel(this.name, newYear, this.author)
}

// 2.
class Counter(val count: Int = 0) {
  def inc: Counter = {
    println("incrementing")
    new Counter(count + 1 )
  } //immutability
  def dec: Counter = {
    println("decrementing")
    new Counter(count - 1)
  }

  def inc(n: Int): Counter = {
    if (n <= 0) this
    else inc.inc(n-1)
  }
  def dec(n: Int): Counter = {
    if (n <= 0) this
    else dec.dec(n-1)
  }

  def print: Unit = println(count)

}