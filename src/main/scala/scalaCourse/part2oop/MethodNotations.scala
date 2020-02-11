package scalaCourse.part2oop

object MethodNotations extends App {

  class Person(val name: String, val favouriteMovie: String, val age: Int=0) {
    def +(newName: String): Person = new Person(s"$name ($newName)", favouriteMovie)
    def unary_+ : Person = new Person(name, favouriteMovie, age+1)
    def learns(topic: String): String = s"$name learns $topic"
    def learnsScala(): String = this learns "Scala"
    def apply(times: Int): String = s"$name watched $favouriteMovie $times times"

    def likes(movie: String): Boolean = movie == favouriteMovie
    def hangOutWith(person: Person): String = s"${this.name} is hanging out with ${person.name}"
    def unary_! : String = s"$name, what the heck"
    def isAlive: Boolean = true
    def apply(): String = s"Hi my name is $name and I like $favouriteMovie"
  }

  val mary = new Person("Mary", "Inception")
  println(mary.likes("Inception"))
  println(mary likes "Inception") // equivalent
  // infix notation = operator notation (syntactic sugar)

  // "operators" in Scala
  val tom = new Person("Tom", "Fight Club")
  println(mary hangOutWith tom)
  //  println(mary.hangOutWith(tom)) -- the same

  // ALL OPERATORS ARE METHODS
  println(1 + 2)
  println(1.+(2))

  // prefix notation
  val x = -1  // equivalent with 1.unary_-
  val y = 1.unary_-
  // unary_ prefix only works with - + ~ !

  println(!mary)
  println(mary.unary_!)

  // postfix notation
  println(mary.isAlive)
  println(mary isAlive)

  // apply
  println(mary.apply())
  println(mary())  //equivalent  APPLY is very useful !!!


  /*
  1. Overload the + operator
      mary + "the rockstar" => new person "Mary (the rockstar)"

  2. Add an age to the Person class
      Add a unary + operator => new person with the age + 1

  3. Add "learns" method in the Person class => "Mary learns Scala"
      Add a learnScala method, calls learns method with "Scala"
      Use it in postfix notation

  4. Overload the apply method
      mary.apply(2) => "MAry watched Inception 2 times"
 */


  // 1.
  println((mary + "Sabrina")())

  // 2.
  val maryAgeOne = +mary
  println(maryAgeOne.age)

  // 3.
  println(mary learnsScala())

  // 4.
  println(mary.apply(3))

}
