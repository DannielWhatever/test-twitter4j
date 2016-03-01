package cl.app;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 29-02-2016.
 */
public class TwitterSource {

    private static Twitter twitter;
    static{
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("DsWFDg7QFvSKvQyjDEIuvbfL5")
                .setOAuthConsumerSecret("kR8B4k0EBFrnFWzflFHalw57sPetAAjLqyVfpvq36gPGtI5XOk")
                .setOAuthAccessToken("2755789148-QQQfRKwzA1fYBW3RQlCvPfnZlROHE7CTX8nHTLC")
                .setOAuthAccessTokenSecret("xloT86ISqVW5koFdiYFEhUPqxmzcOtvaRJWKd4SII5A4F");
        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
    }

    public static List<Status> getTweets(String screenname){

        List<Status> statuses = new ArrayList<>();

        try{
            for(int i=1;i<=5;i++) {
                int oldSize = statuses.size();
                statuses.addAll(twitter.getUserTimeline(screenname, new Paging(i, 200)));

                if(statuses.size()==oldSize)
                    break;
            }
        }catch(TwitterException e){
            e.printStackTrace();
        }

        return statuses;
    }
}
