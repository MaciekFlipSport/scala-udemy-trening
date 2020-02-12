package scalaCourse.part2oop

import scalaCourse.part2oop.Generics.MyList

object PackagingAndImports extends  App {

  // package members are accessible by their simple name

    // import the package
  val myList = new MyList[Int]

}
