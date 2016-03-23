package cl.app.rule

import cl.app.`type`.Category
import cl.app.`type`.Category.{OTHER, JAVASCRIPT, NET, JAVA}
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


    forCategory(JAVA)
      .addRule(contain(_,"java"))
      .build

    forCategory(NET)
      .addRule(contain(_,".net"))
      .build

    forCategory(JAVASCRIPT)
      .addRule(contain(_,"javascript"))
      .build

    forCategory(OTHER)
      .addRule(contain(_,"php"))
      .build

  }


}
