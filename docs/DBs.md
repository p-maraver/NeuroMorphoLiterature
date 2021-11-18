# How DBs are structured

## ApiLiterature
-   Located at: cng.gmu.edu
- There are 2 DB: literature-review and literature-main
- 
1. Dump DB 
````
mongodump --db literature-review --out ./dump
````

2. Restore DB
````
mongorestore --drop --db literature-review ./dump/literature-review
````