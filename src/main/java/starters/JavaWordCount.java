package starters;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.SparkConf;

import scala.Tuple2;

public class JavaWordCount {
	public static void main(String[] args) {
		JavaSparkContext sc = new JavaSparkContext(new SparkConf().setAppName("Spark Word Count"));
		final Integer dim = Integer.parseInt(args[0]); 
		final JavaRDD<String> File = sc.textFile(args[1]);
		
		JavaPairRDD<String, List<Double>> idMap = File.mapToPair(new PairFunction<String, String, List<Double>>() {
			@Override
			public Tuple2<String, List<Double>> call(String s)
					throws Exception {
				String[] line = s.split(",");
				List<Double> val = new ArrayList<Double>(dim);
				for (int i = 1; i <= dim; i++) {
					val.add(Double.parseDouble(line[i]));
				}
				return new Tuple2<String, List<Double>>(line[0], val);
			}
		});
		idMap.cache();
		
		JavaPairRDD<String, Double> idHash = idMap.mapValues(new Function<List<Double>, Double>() {
			@Override
			public Double call(List<Double> list) throws Exception {
				return hashFunction(list);
			}
			
			private Double hashFunction(List<Double> list) {
				Double hash=0.0;
				for(int i = 0;i < dim; i++)
					hash+= (list.get(i)*(Math.pow(7,i)%255))%255;
				return hash;
			}
		}); 
				
		JavaPairRDD<Double, String> pairs = idHash.mapToPair(new PairFunction<Tuple2<String, Double>, Double, String>() {
			@Override
			public Tuple2<Double, String> call(Tuple2<String, Double> t)
					throws Exception {
				return new Tuple2<Double, String>(t._2, t._1);
			}
		}); 	
	}
	
	private static Double distance(List<Double> list1, List<Double> list2) {
		return null;
	}
}
