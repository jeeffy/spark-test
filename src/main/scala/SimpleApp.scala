import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

object SimpleApp {
  def main(args: Array[String]) {
    val logFile = "/Users/jeeffy/str" // Should be some file on your system
    val conf = new SparkConf().setAppName("Simple Application")
    //conf.setMaster("spark://192.168.100.34:7077")
    //conf.setMaster("spark://192.168.100.101:7077")
    conf.setMaster("local[2]")
    val sc = new SparkContext(conf)
    //val logData = sc.textFile(logFile, 2).cache()
    val logData = sc.textFile(logFile, 2).cache()
    //logData.foreach(data =>println("------"+data))
    //logData.map(line=>"abc").foreach(println)
    val numAs = logData.filter(line => line.contains("a")).count()
    val numBs = logData.filter(line => line.contains("b")).count()
    println("Lines with a: %s, Lines with b: %s".format(numAs, numBs))
    sc.stop()
  }
}