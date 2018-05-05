Sys.setenv(JAVA_HOME="/Library/Java/JavaVirtualMachines/jdk1.8.0_71.jdk/Contents/Home")
Sys.setenv(HADOOP_CMD="/Users/cruise/hadoop/bin/hadoop")
Sys.setenv(HADOOP_HOME="/Users/cruise/hadoop")
Sys.setenv(HADOOP_COMMON_LIB_NATIVE_DIR="$HADOOP_HOME/lib/native")
Sys.setenv(SPARK_HOME="/Users/cruise/spark")
library(rJava)
.jinit()
library(SparkR, lib.loc = c(file.path(Sys.getenv("SPARK_HOME"), "R", "lib")))
library(rmr2)
library(rhdfs)

hdfs.init()

#sc <- sparkR.init(master = "spark://127.0.0.1:7077", sparkEnvir = list(spark.driver.memory="2g"),sparkPackages="com.databricks:spark-csv_2.11:1.5.0")
sc <- sparkR.init(master = "spark://127.0.0.1:7077", sparkEnvir = list(spark.driver.memory="2g"))

sparkR.session()
df <- read.df("hdfs://localhost:9000/data/raw/2008.csv", "csv", header = "true", inferSchema = "true", na.strings = "NA")

train = sample(df, withReplacement = FALSE, fraction = 0.5, seed = 42)
test = except(df, train)

filterdata = filter(test, test$Origin == 'MCO')

delaydata = select(filterdata, filterdata$Month, filterdata$DepDelay)
delay = summarize(groupBy(delaydata, delaydata$Month), count = n(delaydata$DepDelay)) 
#arrange(asc(delay$Month),delay$count)
head(delay)
