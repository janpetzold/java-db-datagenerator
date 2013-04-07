java-db-datagenerator
=====================
This is a tool I've written to randomly generate datasets based on existing values. It basically takes input data from existing table(s), stores them temporarily and than creates a configurable amount of new datasets into a configurable destination table:

<pre>
-------------      ---------------------      ----------------      ------------------
| source DB |  ->  | XML configuration |  ->  | Redis (temp) |  ->  | destination DB |
-------------      ---------------------      ----------------      ------------------
</pre>

So basically the source data is loaded from MySQL. The fields to include can be configured in an XML following this schema:

```
<field>
    <name>country</name>    
    <type>string</type>
    <source_table>source_cities</source_table>
    <source_column>Country</source_column>
    <target_table>destination_cities</target_table>
    <target_column>Country</target_column>
    </field>
```

You can set the type and source+destination table/column for each field. I tested the types "String", "Number" and "Date", these should work properly.

All source data is stored in the local [Redis](http://redis.io/) DB first. Afterwards you call "write" with the amount of datasets you want to write. The application automatically creates these datasets using the source data from Redis. The data is randomized, but that doesn't matter if you just need test data.

I included sample data from the free [Maxmind Geo DB](http://dev.maxmind.com/geoip/geolite). To get that running with MySQL follow these steps:

1. Download and install MySQL and Redis locally and start them.
2. Run the db/script.sql on your machine to create the local db with the source & destination tables.
3. Import the data from db/cities.sql (these are approx. 48000 datasets with cities from all around the world).
4. Load the data: java -jar datagenerator-jar-with-dependencies.jar -read
5. Write the generated test data: java -jar datagenerator-jar-with-dependencies.jar -write 100000

On my machine, reading of all entries took approx. 16 seconds, the writing of 100000 entries took 40 seconds.
