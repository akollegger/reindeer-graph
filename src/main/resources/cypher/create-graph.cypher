// CREATE Reindeer Team
CREATE (dasher:Reindeer {name:"Dasher", strength: 8})
CREATE (dasher)-[:PAIR]->(dancer:Reindeer {name:"Dancer", strength: 6})
CREATE (prancer:Reindeer {name:"Prancer", strength: 5})
CREATE (prancer)-[:PAIR]->(vixen:Reindeer {name:"Vixen", strength: 6})
CREATE (comet:Reindeer {name:"Comet", strength: 7})
CREATE (comet)-[:PAIR]->(cupid:Reindeer {name:"Cupid", strength: 5})
CREATE (donner:Reindeer {name:"Donner", strength: 8})
CREATE (donner)-[:PAIR]->(blitzen:Reindeer {name:"Blitzen", strength: 7})
CREATE (rudolf:Reindeer {name:"Rudolf", strength: 9})

CREATE (sleigh:Vehicle {type:"Sleigh"})

CREATE (donner)-[:FOLLOWS]->(rudolf)
CREATE (blitzen)-[:FOLLOWS]->(rudolf)
CREATE (comet)-[:FOLLOWS]->(donner)
CREATE (cupid)-[:FOLLOWS]->(blitzen)
CREATE (prancer)-[:FOLLOWS]->(comet)
CREATE (vixen)-[:FOLLOWS]->(cupid)
CREATE (dasher)-[:FOLLOWS]->(prancer)
CREATE (dancer)-[:FOLLOWS]->(vixen)

CREATE (dasher)-[:PULLS]->(sleigh)
CREATE (dancer)-[:PULLS]->(sleigh)