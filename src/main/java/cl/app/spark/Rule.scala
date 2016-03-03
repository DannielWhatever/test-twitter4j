package cl.app.spark

import twitter4j.Status

/**
  * Created by Daniel on 03-03-2016.
  */
class Rule[Q](q:Q,f:Status=>Boolean) extends Serializable{

  def group = q
  def predicate = f

}