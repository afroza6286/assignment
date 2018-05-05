/*************   carriers  decomposed  *************/

carriers = LOAD 'hdfs://localhost:9000/data/raw/carriers/' USING PigStorage(',')
AS (code:chararray,description:chararray);

carriers_decomposed = FOREACH carriers GENERATE code, description, CurrentTime(), UniqueID();

DESCRIBE carriers_decomposed;

STORE carriers_decomposed INTO 'hdfs://localhost:9000/data/decomposed/carriers' using AvroStorage();


/*************   airports  decomposed  *************/


airports = LOAD 'hdfs://localhost:9000/data/raw/airports/' USING PigStorage(',')
AS (iata:chararray,airport:chararray,city:chararray,state:chararray,country:chararray,lat:chararray,long:chararray);

airports_decomposed = FOREACH airports GENERATE *, CurrentTime(), UniqueID();


DESCRIBE airports_decomposed;

STORE airports_decomposed INTO 'hdfs://localhost:9000/data/decomposed/airports' using AvroStorage();

/*************   planedate  decomposed  *************/


planedate = LOAD 'hdfs://localhost:9000/data/raw/planedate/' USING PigStorage(',')
AS (tailnum:chararray,type:chararray,manufacturer:chararray,issue_date:chararray,model:chararray,status:chararray,aircraft_type:chararray,engine_type:chararray,year:chararray);

planedate_decomposed = FOREACH planedate GENERATE *, CurrentTime(), UniqueID();


DESCRIBE planedate_decomposed;

STORE planedate_decomposed INTO 'hdfs://localhost:9000/data/decomposed/planedate' using AvroStorage();
