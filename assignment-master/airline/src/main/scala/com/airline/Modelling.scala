package com.airline


import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._
import org.apache.spark.sql.types.{StructField, StructType, StringType}
import org.apache.spark.sql.Row
import com.databricks.spark.avro._

import scala.collection.mutable.ArrayBuffer

object Modelling {
  def main(args : Array[String]) {
	  val conf = new SparkConf().setAppName("Spark Kafka Streaming").setMaster("spark://127.0.0.1:7077")
    .set("spark.serializer","org.apache.spark.serializer.KryoSerializer")
            
    val spark = SparkSession
      .builder()
      .appName("Modelling")
      .config("spark.driver.cores", "2")
      .getOrCreate()  
      
    import spark.implicits._
    
    /******************    airports modelling and processing   **************************/
    val airports = spark.read.format("com.databricks.spark.avro").load("hdfs://localhost:9000/data/decomposed/airports")
    airports.printSchema()
    
    val airportscolumns = airports.columns
    val airportsschema = StructType(airportscolumns.map(field => StructField(field, StringType, true)))
    
    val trimairportsmap = airports.rdd.map{ r =>
    	  val newRow = ArrayBuffer[String]() 
    	  for(i <- 0 until r.length){
    	 	  newRow += r(i).toString().replaceAll("""\"""", "").trim()
    	  }
    	  val row = Row.fromSeq(newRow.toSeq)
    	  row
    }
	  
	  val newairports = spark.createDataFrame(trimairportsmap, airportsschema)
       
    newairports.show(10)
    
    newairports.write.mode("overwrite").parquet("hdfs://localhost:9000/data/modelled/airports")
    
    /*****************   carriers modelling and processing  ****************************/
    
	  val carriers = spark.read.format("com.databricks.spark.avro").load("hdfs://localhost:9000/data/decomposed/carriers")
    
    val carrierscolumns = carriers.columns
    val carriersschema = StructType(carrierscolumns.map(field => StructField(field, StringType, true)))
    
    val trimcarriersmap = carriers.rdd.filter(r => (r(0) != "Code")).map{ r =>
    	  val newRow = ArrayBuffer[String]() 
    	  for(i <- 0 until r.length){
    	 	  newRow += r(i).toString().replaceAll("""\"""", "").replaceAll("""\\""", "").trim()
    	  }
    	  Row.fromSeq(newRow.toSeq)
    }
	   
	  val newcarriers = spark.createDataFrame(trimcarriersmap, carriersschema)
	  
	  newcarriers.show(10)
	     
	  newcarriers.write.mode("overwrite").parquet("hdfs://localhost:9000/data/modelled/carriers")
	  
	  /*****************   planedate modelling and processing  ****************************/
	  
	  
	  val planedate = spark.read.format("com.databricks.spark.avro").load("hdfs://localhost:9000/data/decomposed/planedate")
    
    val planedatecolumns = planedate.columns
    val planedateschema = StructType(planedatecolumns.map(field => StructField(field, StringType, true)))
    
    val trimplanedatemap = planedate.rdd.map{ r =>
    	  val newRow = ArrayBuffer[String]() 
    	  for(i <- 0 until r.length){
    	 	  if(r(i) != null && r(i) != ""){
    	 	    newRow += r(i).toString().replaceAll("""\"""", "").trim()
    	 	  } else {
    	 	 	  newRow += ""
    	 	  }
    	  }
    	  Row.fromSeq(newRow.toSeq)
    }
	   
	  val newplanedate = spark.createDataFrame(trimplanedatemap, planedateschema)
	  
	  newplanedate.show(10)
	  
	  newplanedate.write.mode("overwrite").parquet("hdfs://localhost:9000/data/modelled/planedate")
	  
	  
  }
}