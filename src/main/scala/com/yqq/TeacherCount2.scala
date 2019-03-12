package com.yqq

import java.net.URL

import org.apache.spark.rdd.RDD
import org.apache.spark.{Partitioner, SparkConf, SparkContext}

import scala.collection.mutable

object TeacherCount2 {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("TeacherCount2").setMaster("local[*]")

    val sc = new SparkContext(conf)

    //读取数据
    val lines: RDD[String] = sc.textFile(args(0))

    //整理数据
    val subjectAndTeacher: RDD[(String, String)] = lines.map(line => {
      val url = new URL(line)
      val host = url.getHost
      val subject = host.substring(0, host.indexOf("."))
      val teacher = url.getPath.substring(1)
      (subject, teacher)
    })

    //先集合
    val reduced: RDD[((String, String), Int)] = subjectAndTeacher.map(x => (x,1)).reduceByKey(_+_)
    //这个RDD后面会多少使用，故cache
    reduced.cache()

    //计算有多少学科
    val subjects: Array[String] = reduced.map(_._1._1).distinct().collect()

    //自定义分区器
    val subPartitioner: SubjectPartitioner = new SubjectPartitioner(subjects)
    val partitioned: RDD[(String, (String, Int))] = reduced.map(t => (t._1._1,(t._1._2,t._2))).partitionBy(subPartitioner)

    //每个学科里中只有一个分区，在学科内部进行排序
    val res: RDD[(String, (String, Int))] = partitioned.mapPartitions(_.toList.sortBy(_._2._2).reverse.take(2).iterator)

    println(res.collect().toBuffer)

    sc.stop()

  }

}

class SubjectPartitioner(subjects: Array[String]) extends Partitioner{

  //有几个分区器
  override def numPartitions: Int = subjects.length+1

  override def getPartition(key: Any): Int = {
    val k = key.toString
    rules.getOrElse(k,0)

  }

  val rules = new mutable.HashMap[String,Int]()
  var i = 1
  for (sub <- subjects){
    rules += (sub -> i)
    i += 1
  }
}
