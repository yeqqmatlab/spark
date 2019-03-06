package com.yqq

class Rational(n:Int,d:Int) {

  require(d != 0)

  private val g = gcd(n.abs,d.abs)

  private def gcd(a: Int, b: Int): Int =
    if (b==0) a else gcd(b,a%b)


  val num = n/g
  val denom = d/g

  override def toString = num + "/" + denom

//  def add(that : Rational):Rational =
//    new Rational(num*that.denom+that.num*denom,denom*that.denom)denom

    def +(that : Rational):Rational =
      new Rational(num*that.denom+that.num*denom,denom*that.denom)

    def +(i:Int):Rational =
      new Rational(num+i*denom,denom)

    def *(that : Rational):Rational =
      new Rational(this.num*that.num,this.denom*that.denom)

    def lessThan(that: Rational) =
      this.num*that.denom < that.num*this.denom

    def this(n:Int) = this(n,1)
}



object Rational{
  implicit def intToR(x:Int) = new Rational(x)
  def main(args: Array[String]): Unit = {
    val x =new Rational(1,2)
    val y =new Rational(1,3)
    val z = x.+(y)
    val zz = x + y
    val xy = x * y
    val kk = x + 3
    val tt = 6 + x
    println("z--->"+z)
    println("zz--->"+zz)
    println("xy--->"+xy)
    println("kk--->"+kk)
    println("tt--->"+tt)
    println(x lessThan y)
      val test_gcd = new Rational(6,9)
      println(test_gcd)
  }
}
