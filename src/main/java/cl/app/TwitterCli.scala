package cl.app

import java.util

import twitter4j._
import twitter4j.conf.ConfigurationBuilder

import scala.collection.JavaConverters._

/**
 *
 * @author daniel.gutierrez
 */
object TwitterCli {


  private var twitter: Twitter = null

  val cb = new ConfigurationBuilder()
  cb.setDebugEnabled(true)
    .setOAuthConsumerKey("DsWFDg7QFvSKvQyjDEIuvbfL5")
    .setOAuthConsumerSecret("kR8B4k0EBFrnFWzflFHalw57sPetAAjLqyVfpvq36gPGtI5XOk")
    .setOAuthAccessToken("2755789148-QQQfRKwzA1fYBW3RQlCvPfnZlROHE7CTX8nHTLC")
    .setOAuthAccessTokenSecret("xloT86ISqVW5koFdiYFEhUPqxmzcOtvaRJWKd4SII5A4F")
  twitter = new TwitterFactory(cb.build()).getInstance()


  def getTweets(screenname: String): List[Status] = {
    val statuses = new util.ArrayList[Status]
    try {
      var i = 1
      var done = false
      while (i <= 5 && !done) {
        val oldSize = statuses.size
        statuses.addAll(twitter.getUserTimeline(screenname, new Paging(i, 200)))
        if (statuses.size == oldSize)
          done = true
        i += 1
      }
    } catch {
      case e: TwitterException => e.printStackTrace
    }
    return statuses.asScala.toList
  }

}
