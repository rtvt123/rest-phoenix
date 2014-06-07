package resource.report;

import java.sql.Date;

import org.apache.log4j.Logger;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import util.Cons;
import util.ConvertDate;
import util.Util;

/**
 * Resource which has only one representation.
 */
public class ReportResource extends ServerResource {
	static Logger LOGGER = Logger.getLogger(ReportResource.class.getName());
	
    @Get
    public String represent() {
    	int dayTime, num_day;
    	String result = null;
    	Date date;
		Util util = new Util();
		ConvertDate cv = new ConvertDate();
		JsonObject jsObj;
		JsonArray jsArrResult = new JsonArray();
		
		try {
			dayTime = cv.getUnixTime("DAY", 0);
			System.out.println("--- dayTime --- " + dayTime);
			
			num_day = util.getIntFromParam(getQueryValue("num_day"));
			for (int i = 0; i < num_day; i++) {
				dayTime = dayTime - (86400 * i);
				jsObj = new JsonObject();
				date = new Date (dayTime*1000L);
				jsObj.addProperty(Cons.DATE, date.toString());
				jsObj.addProperty(Cons.DAY_TIME, dayTime);
				jsObj.addProperty(Cons.TOTAL_QUERY, util.getRedisValue(dayTime + Cons.TOTAL_QUERY));
				jsObj.addProperty(Cons.QUERY_FROM_CACHE, util.getRedisValue(dayTime + Cons.QUERY_FROM_CACHE));
				jsObj.addProperty(Cons.QUERY_FROM_PHOENIX, util.getRedisValue(dayTime + Cons.QUERY_FROM_PHOENIX));
				
				jsArrResult.add(jsObj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
        return jsArrResult.toString();
    }
}