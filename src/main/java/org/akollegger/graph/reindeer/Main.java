package org.akollegger.graph.reindeer;


import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.ResultCursor;
import org.neo4j.driver.v1.Session;

/**
 * Run the Reindeer Graph!
 */
public class Main {

    final static String CREATE_REINDEER_GRAPH =
            "CREATE (dasher:Reindeer {name:\"Dasher\", strength: 3})\n" +
            "CREATE (dancer:Reindeer {name:\"Dancer\"})\n" +
            "CREATE (prancer:Reindeer {name:\"Prancer\"})\n" +
            "CREATE (vixen:Reindeer {name:\"Vixen\"})\n" +
            "CREATE (comet:Reindeer {name:\"Comet\"})\n" +
            "CREATE (cupid:Reindeer {name:\"Cupid\"})\n" +
            "CREATE (donner:Reindeer {name:\"Donner\"})\n" +
            "CREATE (blitzen:Reindeer {name:\"Blitzen\"})\n" +
            "CREATE (rudolf:Reindeer {name:\"Rudolf\"})\n" +
            "\n" +
            "CREATE (sleigh:Vehicle {type:\"Sleigh\"})\n" +
            "\n" +
            "CREATE (donner)-[:FOLLOWS]->(rudolf)\n" +
            "CREATE (blitzen)-[:FOLLOWS]->(rudolf)\n" +
            "CREATE (comet)-[:FOLLOWS]->(donner)\n" +
            "CREATE (cupid)-[:FOLLOWS]->(blitzen)\n" +
            "CREATE (prancer)-[:FOLLOWS]->(comet)\n" +
            "CREATE (vixen)-[:FOLLOWS]->(cupid)\n" +
            "CREATE (dasher)-[:FOLLOWS]->(prancer)\n" +
            "CREATE (dancer)-[:FOLLOWS]->(vixen)\n" +
            "\n" +
            "CREATE (dasher)-[:PULLS]->(sleigh)\n" +
            "CREATE (dancer)-[:PULLS]->(sleigh)";

    final static String FIND_STRONGEST_PAIR =
            "MATCH (:Vehicle {type:\"Sleigh\"})<-[:PULLS]-(deer:Reindeer)\n" +
            "WITH deer LIMIT 1\n" +
            "MATCH (deer)-[:FOLLOWS*]->(other1)-[:PAIR]-(other2)\n" +
            "RETURN other1, other2, other1.strength + other2.strength as pairStrength \n" +
            "ORDER BY pairStrength DESC LIMIT 1";

    final static String PAIR_BOLT_WITH_RUDOLF =
            "MATCH (blitzen:Reindeer {name:\"Blitzen\"})-[blitzenToRudolf]->(rudolf:Reindeer {name:\"Rudolf\"})\n" +
                    "WITH rudolf,blitzen,blitzenToRudolf\n" +
                    "DELETE blitzenToRudolf\n" +
                    "CREATE (bolt {name:\"Bolt\", strength:11})-[:PAIR]->(rudolf), (blitzen)-[:FOLLOWS]->(bolt)\n" +
                    "RETURN bolt,rudolf,blitzen\n";

    public static void main(String[] args) {
        Driver driver = GraphDatabase.driver( "bolt://localhost" );
        Session boltSession = driver.session();


        // Create initial graph
        boltSession.run(CREATE_REINDEER_GRAPH);

        System.out.println("Hello, reindeer team!");

        // find strongest pair
        ResultCursor resultCursor = boltSession.run(FIND_STRONGEST_PAIR);

        if (resultCursor.next()) {
            System.out.printf("Strongest reindeer pair: %s %s\n",
                    resultCursor.value(0).asNode().value("name").asString(),
                    resultCursor.value(1).asNode().value("name").asString());
        }

        // add Bolt to the team, paired with Rudolf
        boltSession.run(PAIR_BOLT_WITH_RUDOLF);

        System.out.println("Welcome, Bolt!");

        // find the strongest pair, again
        resultCursor = boltSession.run(FIND_STRONGEST_PAIR);

        if (resultCursor.next()) {
            System.out.printf("Strongest reindeer pair: %s %s\n",
                    resultCursor.value(0).asNode().value("name").asString(),
                    resultCursor.value(1).asNode().value("name").asString());
        }

        boltSession.close();
    }

    private static void initalizeGraph(Session boltSession) {


    }

}
