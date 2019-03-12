package com.yqq

import org.apache.spark.{SparkConf, SparkContext}
import java.net.URL

import org.apache.spark.rdd.RDD

object TeacherCount {

  def main(args: Array[String]): Unit = {
    //本地多线程跑,集群环境是多线程
    val  conf = new SparkConf().setAppName("TeacherCount").setMaster("local[*]")

    val sc = new SparkContext(conf)

    //read data
    val lines = sc.textFile(args(0))

    //整理数据
    val subjectAndTeacher = lines.map(line => {
      val url = new URL(line)
      val host = url.getHost
      val subject = host.substring(0, host.indexOf("."))
      val teacher = url.getPath.substring(1)
      (subject, teacher)
    })
//    val arr = subjectAndTeacher.collect()
//    println(arr.toBuffer)

    //聚合
   val reduced: RDD[((String, String), Int)] = subjectAndTeacher.map(x =>(x,1)).reduceByKey(_+_)
//    val arr2 = reduced.collect();
//    println(arr2.toBuffer)

    //按学科分组
    val grouped: RDD[(String, Iterable[((String, String), Int)])] = reduced.groupBy(_._1._1)
    val arr3 = grouped.collect()
    println(arr3.toBuffer)

    //按学科二次排序
    val result: RDD[(String, List[((String, String), Int)])] = grouped.mapValues(_.toList.sortBy(_._2).reverse.take(2))

    //收集结果
    val arr = result.collect()

    println(arr.toBuffer)


    sc.stop()

  }

}
