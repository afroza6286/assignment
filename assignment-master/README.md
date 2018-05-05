# assignment


################  spark installation  ########################

1.1 Install Spark

Here is the official site for downloading Spark 

https://spark.apache.org/downloads.html
tar –xvzf spark-2.2.1-bin-hadoop2.7.tgz

	Start Spark Master and slave by using below command
	
		sbin/start-master.sh -h 127.0.0.1 -p 7077
		sbin/start-slave.sh spark://127.0.0.1:7077

bin/spark-submit --packages org.apache.spark:spark-streaming-kafka-0-10_2.11:2.1.1 --master spark://127.0.0.1:7077 --class com.airline.Carriers --deploy-mode client /Users/cruise/Documents/developer/nadia/kafkaproject/airline/target/airline-0.0.1-SNAPSHOT.jar


bin/spark-submit --packages com.databricks:spark-avro_2.11:4.0.0 --master spark://127.0.0.1:7077 --class com.airline.Modelling --deploy-mode client /Users/cruise/Documents/developer/nadia/kafkaproject/airline/target/airline-0.0.1-SNAPSHOT.jar		

################  kafka installation  ########################

1.2 Install  kafka 
Here is the official site for downloading Kafka

https://kafka.apache.org/
http://redrockdigimark.com/apachemirror/kafka/1.0.0/kafka_2.11-1.0.0.tgz
tar -xvzf kafka_2.11-1.0.0.tgz

Start Zookeeper server and Kafka Server

bin/zookeeper-server-start.sh config/zookeeper.properties
bin/kafka-server-start.sh config/server.properties

Create Topic in Kafka

bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic Airports
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic Carriers
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic Planedate
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic OTP

Read the source data in topics

bin/kafka-console-producer.sh --broker-list localhost:9092 --topic Airports < /Users/cruise/Documents/developer/emirates/airports.csv
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic Planedate < /Users/cruise/Documents/developer/emirates/plane-data.csv
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic Carriers < /Users/cruise/Documents/developer/emirates/carriers.csv

Get data into consumer 

bin/kafka-console-consumer.sh --zookeeper localhost:2181 --bootstrap-server localhost:9092 --topic Airports --from-beginning
bin/kafka-console-consumer.sh --zookeeper localhost:2181 --bootstrap-server localhost:9092 --topic Planedate --from-beginning
bin/kafka-console-consumer.sh --zookeeper localhost:2181 --bootstrap-server localhost:9092 --topic Carriers --from-beginning


################  flume installation  ########################

1.3 Install  Flume 

Here is the official site for downloading Flume

https://flume.apache.org/download.html
apache-flume-1.8.0-bin.tar.gz

tar -xvzf apache-flume-1.8.0-bin.tar.gz

create conf file and put it in conf directory of flume

bin/flume-ng agent --conf conf --conf-file conf/Airports.conf --name flume1 -Dflume.root.logger=INFO,console

bin/flume-ng agent --conf conf --conf-file conf/Planedate.conf --name flume1 -Dflume.root.logger=INFO,console


################  hadoop installation  ########################

1.4 Install  Hadoop 

	Here is the official site for downloading Hadoop

http://hadoop.apache.org/releases.html

	After making change in conf file (core-site.xml, hdfs-site.xml, hadoop-env.sh) of hadoop, start namenode and datanode by using below command

sbin/start-dfs.sh

bin/hdfs dfs -mkdir -p /data/raw/
bin/hdfs dfs -mkdir -p /data/decomposed/
bin/hdfs dfs -mkdir -p /data/modelled/
bin/hdfs dfs -mkdir -p /data/schema/


################  pig installation  ########################

1.5 Install Pig 

	Here is the official site for downloading Pig

http://www-eu.apache.org/dist/pig/pig-0.17.0/

download pig-0.17.0.tar.gz 
tar -xvzf pig-0.17.0.tar.gz 

Created pig script and execute with below command

bin/pig -x local /Users/cruise/Documents/developer/emirates/add_uuid_timestamp.pig


################  zeppelin installation  ########################

1.6 Install Zeppelin 

	Here is the official site for downloading Zeppelin
http://www.apache.org/dyn/closer.cgi/zeppelin/zeppelin-0.7.3/zeppelin-0.7.3-bin-all.tgz
tar -xvzf zeppelin-0.7.3-bin-all.tgz

Start Zeppelin by using below command

bin/zeppelin-daemon.sh start


################  rstudio installation  ########################


1.7 Install Rstudio
 	Donwload Binary package of R and Rstudio

https://www.rstudio.com/products/rstudio/download/