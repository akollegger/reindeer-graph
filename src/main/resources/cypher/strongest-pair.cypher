// Find strongest pair of Reindeer in the team
MATCH (:Vehicle {type:"Sleigh"})<-[:PULLS]-(deer:Reindeer)
WITH deer LIMIT 1
MATCH (deer)-[:FOLLOWS*]->(other1)-[:PAIR]-(other2)
RETURN other1, other2, other1.strength + other2.strength as pairStrength
ORDER BY pairStrength DESC LIMIT 1