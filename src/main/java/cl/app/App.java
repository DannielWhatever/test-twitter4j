package cl.app;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.List;

/**
 * @author daniel.gutierrez
 */
public class App {

    public static void main(String args[]){
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("DsWFDg7QFvSKvQyjDEIuvbfL5")
                .setOAuthConsumerSecret("kR8B4k0EBFrnFWzflFHalw57sPetAAjLqyVfpvq36gPGtI5XOk")
                .setOAuthAccessToken("2755789148-QQQfRKwzA1fYBW3RQlCvPfnZlROHE7CTX8nHTLC")
                .setOAuthAccessTokenSecret("xloT86ISqVW5koFdiYFEhUPqxmzcOtvaRJWKd4SII5A4F");
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        try {
            List<Status> statuses;
            String screenname = "cht_informatica";
            statuses = twitter.getUserTimeline(screenname);
            System.out.println("Showing @" + screenname + "'s user timeline.");
            for (Status status : statuses) {
                System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
            }
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());
            System.exit(-1);
        }
    }

}
