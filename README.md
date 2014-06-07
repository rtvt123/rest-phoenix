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
	+ Report query total, number of each query type
	+ Log slow query
