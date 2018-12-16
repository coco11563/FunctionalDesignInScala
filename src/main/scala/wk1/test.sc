import java.util.ListResourceBundle

import scala.collection.immutable

println("welcome to the scala work Sheet")
object test {
  //{case "ping" => "pong"} this is wrong
  val f : String => String = {
    case "ping" => "pong"
  }
  val fpartial : PartialFunction[String, String] = {
    case "ping" => "pong"
  }
  //partialFunction[-A, +R]
  val exercisef : PartialFunction[List[Int], String] = {
    case Nil => "One"
    case x :: y :: rest => "two"
  }
  val exercisef2 : PartialFunction[List[Int], String] = {
    case Nil => "One"
    case x :: rest =>
      rest match {
        case Nil => "two"
      }
  }
}
test.f("ping")
//test.f("111") // match error
test.fpartial.isDefinedAt("fff") // can detect if this String has a map
test.fpartial.isDefinedAt("ping")
test.exercisef.isDefinedAt(List(1,2,3))
//still have warning
test.exercisef2.isDefinedAt(List(1,2,3)) // guarantee the outermost parameter match


val testCollection1 = List(1,2,3,4)
val testCollection2 = List("a","b","c")

for (
  i <- testCollection1;
  j <- testCollection2
) println(i + j)

def testFor(N : Int): immutable.Seq[(Int, Int)] = {
  for {
    x <- 2 to N
    y <- 2 to x
    if x % y == 0
  } yield (x , y)
}

def testFor1(N : Int): immutable.Seq[(Int, Int)] = {
  (2 to N) flatMap(
    x => {
      (2 to x) withFilter (y =>
        x % y == 0) map(y => (x, y))
    })
}

//def testFor2(N : Int) = {
//  (2 to N) map (x =>
//    (2 to x) flatMap (y => if ((x % y) == 0) (x , y)))
//}