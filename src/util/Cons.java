package util;

import java.util.ArrayList;
import java.util.List;

public class Cons {
	public static String PHOENIX_PROPERTIES = "phoenix.properties";
	public static String REDIS_PROPERTIES = "redis.properties";
	
	public static String REDIS_HOST;
	public static int REDIS_PORT;
	public static int REDIS_DB;
	public static final String REDIS_HASH_VALUE_TRUE = "1";
	
	public static String PHOENIX_CONN_STR;
	
	public static List<String> L_BLACK_LIST_QUERY = new ArrayList<String>();
	public static final String BLACK_LIST_PHOENIX_QUERY_SELECT_ALL = "select * ";
	public static final String BLACK_LIST_PHOENIX_QUERY_CREATE = "create ";
	public static final String BLACK_LIST_PHOENIX_QUERY_UPSERT = "upsert ";
	public static final String BLACK_LIST_PHOENIX_QUERY_ALTER = "alter ";
	public static final String BLACK_LIST_PHOENIX_QUERY_DELETE = "delete ";
	public static final String BLACK_LIST_PHOENIX_QUERY_DROP = "drop ";
	
	public static final long SLOW_RESPONSE_TIME = 3000;
	
	public static final String TOTAL_QUERY= "total_quert";
	public static final String QUERY_FROM_CACHE= "query_from_cache";
	public static final String QUERY_FROM_PHOENIX= "query_from_phoenix";
}
