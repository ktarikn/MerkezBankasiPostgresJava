The application fatches and parses the data from https://www.tcmb.gov.tr/kurlar/today.xml when it runs it stores the data in a postgres table beneath the rates database (which is created first if does not exist)
Each row has values rate code, buy, sell, effective-buy, effective-sell, all according to Turkish Liras
The program supports CRUD operations with utilization of REST API, the how can be found in a later section
It will typically run on
http://localhost:8080/rates
the 8080 part may differ and your version could be seen on console when the code runs.
one of the
