package cl.app;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
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
        List<Status> statuses = new ArrayList<Status>();
        String screenname = "cht_informatica";

        SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("asdads");
        JavaSparkContext sc = new JavaSparkContext(conf);

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
        JavaRDD<Status> rdd = sc.parallelize(statuses);
        System.out.println("Showing @" + screenname + "'s user timeline.");
        rdd.foreach(
                (VoidFunction<Status>) status ->
                        System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText())
        );
        System.out.println("count: "+statuses.size());

        // Create a local StreamingContext with two working thread and batch interval of 1 second
//        SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount");
        //JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(1));

        // Create a DStream that will connect to hostname:port, like localhost:9999
        //JavaReceiverInputDStream<String> lines = jssc.socketTextStream("localhost", 9999);
        //jssc.queueStream()

    }

}
