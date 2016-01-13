# Reindeer Graph

A small example of using Neo4j's Bolt driver.

## Make it go...

First, start Neo4j 3.0.0-M02.

Build, then run:

```
mvn package
java -jar target/reindeer-graph-1.0-SNAPSHOT.jar
```

Expected output:

```
Hello, reindeer team!
Strongest pair: Blitzen + Donner = 15
Welcome, Bolt!
Strongest pair: Bolt + Rudolf = 20
```

Or, if there's a problem connecting you'll see:

```
Exception in thread "main" org.neo4j.driver.v1.exceptions.ClientException: Unable to connect to 'localhost' on port 7687, ensure the database is running and that there is a working network connection to it.
```
