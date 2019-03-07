package com.yqq
import scala.io.Source

object LongLines {

  def processFile(filename: String, width: Int){
    val sources = Source.fromFile(filename)
    for (line <- sources.getLines()){
      processLine(filename,width,line)
    }

  }

  def processLine(filename: String, width: Int, line: String){
    if(line.length > width){
      println(filename+":"+line.trim)
    }

  }

  def main(args: Array[String]): Unit = {
    LongLines.processFile("/Users/yqq/data/words.txt",16)
  }

}
