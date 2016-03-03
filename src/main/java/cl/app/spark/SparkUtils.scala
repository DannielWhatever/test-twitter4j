package cl.app.spark

import org.apache.spark.{SparkContext, Accumulator}

import scala.collection.mutable
import scala.reflect.ClassTag

/**
  * Created by Daniel on 03-03-2016.
  */
object SparkUtils {

  def increment(opt:Option[Accumulator[Int]],i:Int=1):Unit = opt.get.add(i)

  def buildMapCounting[T:ClassTag](enum: Enumeration, sc: SparkContext): mutable.Map[T ,Accumulator[Int]] = {
    val mapCounting = mutable.Map.empty[T,Accumulator[Int]]
    enum.values.foreach(value => mapCounting.put(value.asInstanceOf[T],sc.accumulator(0)))
    return mapCounting
  }

}
