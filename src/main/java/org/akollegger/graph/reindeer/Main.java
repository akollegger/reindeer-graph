package org.akollegger.graph.reindeer;


import org.neo4j.driver.v1.*;

/**
 * Run the Reindeer Graph!
 *
 * <ol>
 *     <li>Create a team of reindeer, pulling a sleigh</li>
 *     <li>Find the strongest pair of reindeer in the team</li>
 *     <li>Add Bolt, paired up with Rudolf</li>
 *     <li>Find the strongest pair, again</li>
 * </ol>
 */
public class Main {

    final static String CREATE_REINDEER_GRAPH =
        "CREATE (dasher:Reindeer {name:\"Dasher\", strength: 8})\n" +
        "CREATE (dasher)-[:PAIR]->(dancer:Reindeer {name:\"Dancer\", strength: 6})\n" +
        "CREATE (prancer:Reindeer {name:\"Prancer\", strength: 5})\n" +
        "CREATE (prancer)-[:PAIR]->(vixen:Reindeer {name:\"Vixen\", strength: 6})\n" +
        "CREATE (comet:Reindeer {name:\"Comet\", strength: 7})\n" +
        "CREATE (comet)-[:PAIR]->(cupid:Reindeer {name:\"Cupid\", strength: 5})\n" +
        "CREATE (donner:Reindeer {name:\"Donner\", strength: 8})\n" +
        "CREATE (donner)-[:PAIR]->(blitzen:Reindeer {name:\"Blitzen\", strength: 7})\n" +
        "CREATE (rudolf:Reindeer {name:\"Rudolf\", strength: 9})\n" +
        "CREATE (sleigh:Vehicle {type:\"Sleigh\"})\n" +
        "CREATE (donner)-[:FOLLOWS]->(rudolf)\n" +
        "CREATE (blitzen)-[:FOLLOWS]->(rudolf)\n" +
        "CREATE (comet)-[:FOLLOWS]->(donner)\n" +
        "CREATE (cupid)-[:FOLLOWS]->(blitzen)\n" +
        "CREATE (prancer)-[:FOLLOWS]->(comet)\n" +
        "CREATE (vixen)-[:FOLLOWS]->(cupid)\n" +
        "CREATE (dasher)-[:FOLLOWS]->(prancer)\n" +
        "CREATE (dancer)-[:FOLLOWS]->(vixen)\n" +
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
            "CREATE (bolt:Reindeer {name:\"Bolt\", strength:11})-[:PAIR]->(rudolf), (blitzen)-[:FOLLOWS]->(bolt)\n" +
            "RETURN bolt,rudolf,blitzen";

    final static String REMOVE_REINDEER_GRAPH =
                    "MATCH (v:Vehicle)-[r*]-(reindeer) DETACH DELETE v,reindeer";

    public static void main(String[] args) {

        final boolean clean = args.length > 0 && args[0].equals("clean");

        Driver driver = GraphDatabase.driver( "bolt://localhost" );
        Session boltSession = driver.session();

        // optionally, remove any pre-existing Reindeer graph
        if (clean) boltSession.run(REMOVE_REINDEER_GRAPH);

        // Create initial graph
        boltSession.run(CREATE_REINDEER_GRAPH);

        System.out.println("Hello, reindeer team!");

        // find strongest pair
        ResultCursor resultCursor = boltSession.run(FIND_STRONGEST_PAIR);

        if (resultCursor.single()) { // expect a single result. this is true _only_ if there is just 1 result
            Node deer1 = resultCursor.value(0).asNode();
            Node deer2 = resultCursor.value(1).asNode();
            System.out.printf("Strongest pair: %s + %s = %d\n",
                    deer1.value("name").asString(),
                    deer2.value("name").asString(),
                    deer1.value("strength").asInt() + deer2.value("strength").asInt());
        }

        // add Bolt to the team, paired with Rudolf
        boltSession.run(PAIR_BOLT_WITH_RUDOLF);

        System.out.println("Welcome, Bolt!");

        // find the strongest pair, again
        resultCursor = boltSession.run(FIND_STRONGEST_PAIR);

        if (resultCursor.single()) {
            Node deer1 = resultCursor.value(0).asNode();
            Node deer2 = resultCursor.value(1).asNode();
            System.out.printf("Strongest pair: %s + %s = %d\n",
                    deer1.value("name").asString(),
                    deer2.value("name").asString(),
                    deer1.value("strength").asInt() + deer2.value("strength").asInt());
        }

        boltSession.close();

        try {
            driver.close();
        } catch(Exception e) { /** aw, darn */ }
    }

}
