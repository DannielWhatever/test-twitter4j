package cl.app;

import org.apache.spark.Accumulator;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import twitter4j.Status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author daniel.gutierrez
 */
public class App {

    public static void main(String args[]){

        List<Status> statuses = TwitterSource.getTweets("cht_informatica");

        SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("asdads");
        JavaSparkContext sc = new JavaSparkContext(conf);

        //Fill up Categories.
        Map<Category,Accumulator<Integer>> categories = new HashMap<>();
        for(Category category : Category.values()){
            categories.put(category,sc.accumulator(0));
        }


        // Parallelized with 2 partitions
        JavaRDD<Status> rddX = sc.parallelize(statuses, 2);
        Rules rules = new ConcretRules();


        rddX.foreach(status -> {
            rules.foreach((category,predicate)->{
                if(predicate.call(status)){
                    categories.get(category).add(1);
                }
            });
        });

        categories.forEach(
                (category,accumulator)-> System.out.println(category.getName()+" -> "+accumulator.value())
        );



    }

}
