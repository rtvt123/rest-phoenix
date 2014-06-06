rest-phoenix
============

Restful service for Apache Phoenix

The primary aims of this project is create a rest service for any client programing language can use Hbase's SQL layer (Apache Phoenix) which only support Java programing language.

- Feartures:
	+ Flexible config with properties file
	+ Simple rest service with Restlet
	+ Query interactive with Phoenix via Restful api
	+ Query black list (Agree or not run a query)
	+ Caching query result with Redis
	+ Report query total, number of each query type, slow query, ...
