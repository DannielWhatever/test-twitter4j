package cl.app;

import org.apache.hadoop.util.StringUtils;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction2;
import scala.Tuple2;
import scala.tools.scalap.scalax.util.StringUtil;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 29-02-2016.
 */
public abstract class Rules<Q> {


    protected Rules(){}

    protected final List<Tuple2<Q,Function<Status,Boolean>>> rules = new ArrayList<>();
    {
        initRules();
    }

    protected abstract void initRules();

    public void foreach(VoidFunction2<Q,Function<Status,Boolean>> f2) throws Exception{
        rules.forEach(tuple2 -> {
            try {
                f2.call(tuple2._1(),tuple2._2());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    /**
     * RuleConstructor
     *
     */

    protected RuleConstructor rulesFor(Q q){
        return new RuleConstructor(q);
    }


    class RuleConstructor{

        private final List<Tuple2<Q,Function<Status,Boolean>>> tempRules = new ArrayList<>();
        private final Q q;

        protected RuleConstructor(Q q){
            this.q = q;
        }

        RuleConstructor add(Function<Status,Boolean> predicate){
            rules.add(new Tuple2<>(q, predicate));
            return this;
        }

        void apply(){
            rules.addAll(tempRules);
        }

    }

}
