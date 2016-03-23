package cl.app.rule

import cl.app.spark.custom.Rule
import twitter4j.Status

import scala.collection.mutable

/**
  * Created by Daniel on 02-03-2016.
  */
trait Rules[Q] {

  private val rules = mutable.ArrayBuffer.empty[Rule[Q]]

  protected def initRules()

  initRules()

  def get:List[Rule[Q]] = rules.toList

  def apply(status:Status,fn:Q=>Unit) = {
    get.foreach(rule=>{
      if(rule.predicate(status))
        fn(rule.group)
    })
  }


  protected def forCategory(q:Q):RuleConstructor =  RuleConstructor(q)

  protected case class RuleConstructor(val q: Q){

    private val tempRules = mutable.ArrayBuffer.empty[Rule[Q]]

    def addRule(predicate: Status=>Boolean): RuleConstructor = {
      tempRules.+=(new Rule[Q](q, predicate))
      return this
    }

    def build = rules.++=(tempRules)

  }






}
