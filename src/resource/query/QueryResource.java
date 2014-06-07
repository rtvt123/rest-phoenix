package resource.query;

import org.apache.log4j.Logger;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import util.Cons;
import util.ConvertDate;
import util.Util;

/**
 * Resource which has only one representation.
 */
public class QueryResource extends ServerResource {
	static Logger LOGGER = Logger.getLogger(QueryResource.class.getName());
	
    @Get
    public String represent() {
    	int ttl;
    	long startTime, endTime, response_time;
		boolean isOk = true, isExisted = true;
		String query = null, result = null, queryType, strDayTime, root = null;
		Util util = new Util();
		ConvertDate cv = new ConvertDate();
		
		try {
			startTime = System.currentTimeMillis();
			queryType = Cons.QUERY_FROM_CACHE;
			strDayTime = String.valueOf(cv.getUnixTime("DAY", 0));
			System.out.println("--- strDayTime --- " + strDayTime);

	    	query = getQueryValue("query");
	    	ttl = util.getIntFromParam(getQueryValue("ttl"));
	    	
	    	try {
	    		root = util.getStrFromParam(getQueryValue("root"));
			} catch (Exception e) {
				System.out.println("--- NOT ROOT--- ");
			}
	    	
	    	// check blask list if not root 
	    	if (root == null || !root.equals(Cons.ROOT)) {
		    	isOk = util.isOkPhoenixQuery(query);
			}
	    	
	    	// process query
			if (isOk) {
				// check existed result in cache layer
				isExisted = util.checkExistedRedisKey(query);
				
				if (isExisted) {
					result = util.getRedisValue(query);
				} else {
					// query to Phoenix
					result = util.queryToPhoenix(query);
					
					// set cache
					if (ttl > 0) {
						util.setRedisKey(query, result, ttl);
					}
					
					// set queryType
					queryType = Cons.QUERY_FROM_PHOENIX;
				}
			} 
			System.out.println("--- QUERY TYPE--- " + queryType);
			
			
			// increase total query by one
			util.incrByOne(strDayTime + Cons.TOTAL_QUERY);
			
			// increase query type by one
			if (queryType.equals(Cons.QUERY_FROM_CACHE)) {
				util.incrByOne(strDayTime + Cons.QUERY_FROM_CACHE);
			} else if (queryType.equals(Cons.QUERY_FROM_PHOENIX)) {
				util.incrByOne(strDayTime + Cons.QUERY_FROM_PHOENIX);
			}
			
			endTime = System.currentTimeMillis();
			response_time = endTime - startTime;
			System.out.println("--- QUERY --- " + query + " 	--- RESPONSE TIME --- " + response_time);
			LOGGER.info("--- QUERY --- " + query + " 	--- RESPONSE TIME --- " + response_time);
			
			// log slow query into file
			if (response_time > Cons.SLOW_RESPONSE_TIME) {
				LOGGER.info("--- QUERY --- " + query + " 	--- RESPONSE TIME --- " + response_time);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.warn("--- QUERY --- " + query + "	--- EXCEPTION --- ", e);
		}
        return result;
    }
}