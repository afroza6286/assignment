package com.airline

/**
 * @author ${user.name}
 */

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession
import org.apache.spark.TaskContext
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.sql.types._
import org.apache.spark.sql.Row
import org.apache.kafka.clients.consumer.ConsumerRecord
//import org.apache.kafka.common.serialization.StringDeserializer
//import org.apache.kafka.common.serialization.StringSerializer
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.kafka.common.TopicPartition


object Otp {
  
    def foo(x : Array[String]) = x.foldLeft("")((a,b) => a + b)
    
    def main(args : Array[String]) {
        println( "Hello World!" )
        println("concat arguments = " + foo(args))
        val conf = new SparkConf().setAppName("Spark Kafka Streaming").setMaster("spark://127.0.0.1:7077")
        .set("spark.serializer","org.apache.spark.serializer.KryoSerializer")
        
        val sc = new SparkContext(conf)
      
        val ssc = new StreamingContext(sc, Seconds(30))          
        val spark = SparkSession
          .builder()
          .appName("OTP Streaming")
          .config("spark.driver.cores", "2")
          .getOrCreate()  
          
        //val offsets = Map(TopicPartition("test", 0) -> 2L)
        
        
        val kafkaParams = Map[String, Object](
          "bootstrap.servers" -> "localhost:9092",
          "value.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",
          "key.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",
          "group.id" -> "test1",
          "auto.offset.reset" -> "latest",
          "enable.auto.commit" -> (false: java.lang.Boolean)
        )
        
        val topics = Array("OTP")
        val stream = KafkaUtils.createDirectStream[String, String](
            ssc,
            PreferConsistent,
            Subscribe[String, String](topics, kafkaParams)
        )
        //iata airport city state country lat long
        //val schemaString = "iata airport city state country lat long"
        val schemaString = "iata"

        val fields = schemaString.split(" ").map(fieldname => StructField(fieldname, StringType, nullable = true))
        
        
        
        //stream.map(record => record.value().toString).saveAsTextFiles("")
        
        stream.foreachRDD { rdd =>
        	    println("let me check. ==  = = =   "  + rdd.collect().length + "    ---------------")
        	    val spark = SparkSession.builder.config(rdd.sparkContext.getConf).getOrCreate()
            import spark.implicits._
            
            val schema = StructType(fields)
            val messages = rdd.map(r => r.value.toString)
        	    if(messages.collect().length > 0){
        	    	  
        	    	  //val data = ssc.sparkContext.parallelize(Seq(rdd.collect().mkString("")))

        	    	  val mapData = messages.map(r => Row(r))
        	    	  
        	    	  val df = spark.createDataFrame(mapData, schema)
        	    	  
        	    	  df.printSchema()
        	    	  
        	    	  df.show(5)
        	    	  
        	    	  df.write.mode("append").csv("hdfs://127.0.0.1:9000/data/raw/otp")
        	    	  
        	    }
        }
        
        //stream.map(record => (record.key, record.value))
        stream.print()
        
        ssc.start()
        ssc.awaitTermination()
        //ssc.stop()
        
    }

}
