// remove all reindeer, and the sleigh
MATCH (n:Reindeer) DETACH DELETE n
MATCH (v:Vehicle) DETACH DELETE v