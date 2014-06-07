rest-phoenix
============

Restful service for Apache Phoenix

The primary aims of this project is create a rest service for any client programing language can use Hbase's SQL layer (Apache Phoenix) which only support Java programing language.

- Feartures:
	+ Flexible config
	+ Simple restful webservice with Restlet
	+ Query interactive with Phoenix via Restful api
	+ Query black list (Agree or not run a query have specific string)  
	+ Root user with no black list
	+ Caching query result with Redis
	+ Report total query, number of each query type (phoenix, redis)
	+ Log slow query

- How to run it
	+ Import all code and properties into your Eclipse project
	+ Export project to runable Jar file with main class is RestletServer
	+ java -jar <your_runable_jar>.jar 

- Service
	+ Query
		..* url
			http://localhost:9090/phoenix
		##### param
			query 	: phoenix's query
			ttl 	: time to save result in cache
			root 	: root account name with no black list
		##### sample
			http://localhost:9090:9090/phoenix?query=select%20*%20from%20test%20&root=admin&ttl=300
			// %20 = whitespace
			(http://localhost:9090:9090/phoenix?query=select * from test &root=admin&ttl=300)
		##### response
			{
				date: "2014-06-07",
				day_time: 1402074000,
				total_quert: "161",
				query_from_cache: "13",
				query_from_phoenix: "148"
			}

	+ Report
		##### url
			http://localhost:9090/report
		##### param
			num_day : number of recent day will response query statistic
		##### sample
			http://localhost:9090/report?num_day=1
		##### response
			{
				date: "2014-06-07",
				day_time: 1402074000,
				total_quert: "161000",
				query_from_cache: "13000",
				query_from_phoenix: "148000"
			}

- Config file
	+ app.properties 			:	config root account and slow time response
	+ phoenix.properties 		: 	config phoenix connection string
	+ blacklist.properties 		: 	blacklist query (eg: contain "delete" string)
	+ redis,properties			: 	config redis host, port, db for caching query result
