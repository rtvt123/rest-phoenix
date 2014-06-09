package util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Cons {
	public static String APP_PROPERTIES = "app.properties";
	public static String BLACKLIST_PROPERTIES = "blacklist.properties";
	public static String PHOENIX_PROPERTIES = "phoenix.properties";
	public static String REDIS_PROPERTIES = "redis.properties";
	
	public static String ROOT;
	
	public static String REDIS_HOST;
	public static int REDIS_PORT;
	public static int REDIS_DB;
	public static final String REDIS_HASH_VALUE_TRUE = "1";
	
	public static String PHOENIX_CONN_STR;
	
	public static Set<String> S_BLACK_LIST_QUERY = new HashSet<String>();
	
	public static long SLOW_RESPONSE_TIME;
	
	public static final String TOTAL_QUERY= "total_quert";
	public static final String QUERY_FROM_CACHE= "query_from_cache";
	public static final String QUERY_FROM_PHOENIX= "query_from_phoenix";
	
	public static final String DATE= "date";
	public static final String DAY_TIME= "day_time";
}
