package cl.app.rule

import cl.app.`type`.Category
import twitter4j.Status

/**
  * Created by Daniel on 02-03-2016.
  */
object CategoryRules extends Rules[Category.Value]
  with Serializable{

  private def contain(status:Status,v:String):Boolean={
    status.getText.toLowerCase.contains(v)
  }

  override protected def initRules(): Unit = {


    RuleConstructor(Category.JAVA)
      .add(contain(_,"java"))
      .apply

    RuleConstructor(Category.NET)
      .add(contain(_,".net"))
      .apply

    RuleConstructor(Category.JAVASCRIPT)
      .add(contain(_,"javascript"))
      .apply

    RuleConstructor(Category.OTHER)
      .add(contain(_,"php"))
      .apply

  }


}
