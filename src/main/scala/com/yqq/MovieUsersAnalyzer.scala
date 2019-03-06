package com.yqq

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

class SecondarySortKey(val first: Double, val second: Double)
  extends Ordered[SecondarySortKey] with Serializable{

  override def compare(other: SecondarySortKey): Int = {
    if (this.first - other.first != 0) {
      (this.first - other.first).toInt
    } else {
      if (this.first - other.first > 0) {
        Math.ceil(this.first - other.first).toInt
      }  else if (this.first - other.first < 0) {
        Math.floor(this.first - other.first).toInt
      } else {
        (this.first - other.first).toInt
      }
    }
  }

}


object RDDMovieUsersAnalyzer {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .set("spark.driver.memory", "4g")

    val spark = SparkSession
      .builder()
      .appName("RDDMovieUsersAnalyzer")
      .config(conf)
      .getOrCreate()

    val sc = spark.sparkContext
    sc.setLogLevel("FATAL")

    val dataPath = "file:///D:\\Projects\\sparktextbook\\ml-1m\\"

    var usersRDD = sc.textFile(dataPath + "users.dat")
    var moviesRDD = sc.textFile(dataPath + "movies.dat")
    var ratingsRDD = sc.textFile(dataPath + "ratings.dat")

    println("Highest rating movie in the movies: ")
    val movieInfo = moviesRDD.map(_.split("::")).map(x => (x(0), x(1))).cache()
    val ratings = ratingsRDD.map(_.split("::")).map(x => (x(0), x(1), x(2))).cache()

    val moviesAndRatings = ratings.map(x => (x._2, (x._3.toDouble, 1)))
      .reduceByKey((x, y) => (x._1 + y._1, x._2 + y._2))

    val avgRatings = moviesAndRatings.map(x => (x._1, x._2._1.toDouble/x._2._2))
    println(avgRatings)
    avgRatings.join(movieInfo).map(x => (x._2._1, x._2._2)).sortByKey(false)
      .take(10).foreach(record => println(record._2 + " --> rating: " + record._1))

    val usersGender = usersRDD.map(_.split("::")).map(x => (x(0), x(1)))
    val genderRatings =  ratings.map(x => (x._1, (x._1, x._2, x._3))).join(usersGender).cache()
    genderRatings.take(10).foreach(println)

    val maleFilterdRatings = genderRatings.filter(x => x._2._2.equals("M")).map(x => x._2._1)
    val femaleFilterdRatings = genderRatings.filter(x => x._2._2.equals("F")).map(x => x._2._1)

    println("Most favorate movies for male: ")
    maleFilterdRatings.map(x => (x._2, (x._3.toDouble, 1)))
      .reduceByKey((x, y) => (x._1 + y._1, x._2 + y._2))
      .map(x => (x._1, x._2._1.toDouble/x._2._2))
      .join(movieInfo)
      .map(item => (item._2._1, item._2._2))
      .sortByKey(false)
      .take(10)
      .foreach(record => println(record._2 + " --> rating:" + record._1))

    println("Sort by Timestamp and Rating: ")

    val pairWithSortkey = ratingsRDD.map(line => {
      val splited = line.split("::")
      (new SecondarySortKey(splited(3).toDouble, splited(2).toDouble), line)
    })

    val sorted = pairWithSortkey.sortByKey(false)

    val sortedResult =  sorted.map(sortedline => sortedline._2)
    sortedResult.take(10).foreach(println)

    spark.stop()
  }
}
