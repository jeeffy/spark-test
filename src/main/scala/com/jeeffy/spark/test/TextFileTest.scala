package com.jeeffy.spark.test

/**
  * Created by jeeffy on 11/23/16.
  */
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

// -Dmaster.spark=local
object TextFileTest {
  def main(args: Array[String]) {
    val logFile = "data/test.log" // Should be some file on your system
    val conf = new SparkConf().setAppName("Spark Test")
    val sc = new SparkContext(conf)
    val logData = sc.textFile(logFile, 2).cache()
    val numAs = logData.filter(line => line.contains("a")).count()
    val numBs = logData.filter(line => line.contains("b")).count()
    println(s"Lines with a: $numAs, Lines with b: $numBs")
    sc.stop()
  }
}