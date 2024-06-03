The application fatches and parses the data from https://www.tcmb.gov.tr/kurlar/today.xml when it runs it stores the data in a postgres table beneath the rates database (which is created first if does not exist)

Each row has values rate code, buy, sell, effective-buy, effective-sell, all according to Turkish Liras

The program supports CRUD operations with utilization of REST API, the how can be found in a later section

It will typically run on: 
http://localhost:8080/rates
The 8080 part may differ and your version could be seen on console when the code runs.

One of the features of the application is that it can show how much X cash from Rate AAA equals in BBB currency which can be reached with
http://localhost:8080/rates/exchange?from=AAA&to=BBB&cash=X
The order of the definitions can differ.

While the /rates and /rates/exchange parts are open to public, other features require authorization.

For Authorization, create table in the rates database called 'users' and add users with desired userid , username (integer) , password (varchar(60)) and role (varchar(60)) attributes [the column names need to be exactly as specified the types next to them may change but must be compatible].
For the role section enter either ADMIN or USER

I recommend using PostMan for the next few instructions:
Send a post request to http://localhost:8080/authenticate with body set to:
{
    "username":"{YOUR_USERNAME}",
    "password":"{YOUR_PASSWORD}"
}
change the username and password fields according to how you filled the row for the user in the database
Send the request while the code is running and you'll get a JSON Web Token, you'll use it to reach the other, not yet specified parts of the app.

To use the JWT, Add a header to your GET request with Key set to Authorization and Value set to: Bearer {Generated JWT}
Note: JWTs generated here include time of generation so they will be unique for each run.
With that set, you will have access to the fields the role you "logged in" as

The CRUD operations are 

To Create either,
Add:
http://localhost:8080/rates/add?kod=AAA&buy=X&sell=Y&ebuy=Z&esell=W
(The values of all rates can be in form of floating numbers)
or
Create:
http://localhost:8080/rates/create?kod=AAA&buy=X&sell=Y
which automatically sets ebuy and esell to the entered buy and sell values.

To Read:
http://localhost:8080/rates already reads all the data

To Update either:
http://localhost:8080/rates/update?kod=AAA&buy=X&sell=Y&ebuy=Z&esell=W
or
http://localhost:8080/rates/post?kod=AAA&buy=X&sell=Y
again, sets ebuy and esell values to inputted buy and sell values
Both operations add the rate if it does not exist already

To Delete:
http://localhost:8080/rates/remove?kod=AAA

All of the above operations (except for Read obviously) require Admin role to access (which can be changed in the SecurityConfig.java file)
