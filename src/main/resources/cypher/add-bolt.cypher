// Pair Bolt with Rudolf
MATCH (blitzen:Reindeer {name:"Blitzen"})-[blitzenToRudolf]->(rudolf:Reindeer {name:"Rudolf"})
WITH rudolf,blitzen,blitzenToRudolf
DELETE blitzenToRudolf
CREATE (bolt {name:"Bolt", strength:11})-[:PAIR]->(rudolf), (blitzen)-[:FOLLOWS]->(bolt)
RETURN bolt,rudolf,blitzen
