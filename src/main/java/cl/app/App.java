package cl.app;

import cl.app.domain.*;
import cl.app.domain.Category;
import org.apache.spark.Accumulator;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

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

        Category category_sociable = new Category("sociable");

        //TODO:  categories
        Map<Category,Accumulator<Integer>> categories = new HashMap<>();
        categories.put(category_sociable,sc.accumulator(0));

        //TODO: list of rules
        List<Tuple2<Category,Function<Status,Boolean>>> rules = new ArrayList<>();
        rules.add(new Tuple2<>(
                category_sociable,
                status -> status.getText().toLowerCase().contains("java"))
        );

        // Parallelized with 2 partitions
        JavaRDD<Status> rddX = sc.parallelize(statuses, 2);


        rddX.foreach(status -> {
            rules.forEach(tuple2->{
                Category category = tuple2._1();
                Function<Status,Boolean> predicate = tuple2._2();
                try {
                    if(predicate.call(status)){
                        categories.get(category).add(1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });

        categories.forEach(
                (category,accumulator)-> System.out.println(category.getName()+" -> "+accumulator.value())
        );



/*        JavaRDD<Status> rddY = rddX.filter(status -> rules.get(0).call(status));

        rddY.foreach(
                (VoidFunction<Status>) status ->
                        System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText())
        );

        System.out.println("count: "+rddY.count());*/

    }

}
