

object MapTest {
  def main(args: Array[String]) {
      val t = (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22)
      println(t)
      //val map = Map("sa" -> 1, "s" -> 2)
      //var map = Map[String, Int]()
      //map += "b"->3
      //val map = new scala.collection.mutable.HashMap[String, Int]
      val map = scala.collection.mutable.Map[String, Int]()
      map("a") = 1
      map("b") = 2

      println(map)

  }
}