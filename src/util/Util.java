package util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Properties;

import redis.clients.jedis.Jedis;
import util.factory.PhoenixConnectionFactory;
import util.factory.RedisConnectionFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Util {

	public void readPhoenixConf(){
		Properties prop = new Properties();
		InputStream input = null;
	 
		try {
			input = new FileInputStream(Cons.PHOENIX_PROPERTIES);
	 
			// load a properties file
			prop.load(input);
	 
			// get the property value and print it out
			Cons.PHOENIX_CONN_STR = prop.getProperty("connection_string");
			System.out.println(" --- PHOENIX_CONN_STR --- " + Cons.PHOENIX_CONN_STR);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void readRedisConf(){
		Properties prop = new Properties();
		InputStream input = null;
	 
		try {
			input = new FileInputStream(Cons.REDIS_PROPERTIES);
	 
			// load a properties file
			prop.load(input);
	 
			// get the property value and print it out
			Cons.REDIS_HOST = prop.getProperty("host");
			Cons.REDIS_PORT = Integer.parseInt(prop.getProperty("port"));
			Cons.REDIS_DB = Integer.parseInt(prop.getProperty("db"));
			System.out.println(" --- REDIS INFO --- " + Cons.REDIS_HOST + "	" + Cons.REDIS_PORT + "	" + Cons.REDIS_DB);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public int getIntFromString(String intVal){
		int result;
		try {
			result = Integer.parseInt(intVal);
		} catch (Exception e) {
			result = 0;
			e.printStackTrace();
		}
		return result;
	}
	
	public void incrByOne(String key) {
		try {
			Jedis jedis = RedisConnectionFactory.getInstance();
			jedis.incr(key);
			//jedis.quit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setRedisKey(String key, String value, int ttl) {
		try {
			Jedis jedis = RedisConnectionFactory.getInstance();
			jedis.set(key, value);
			jedis.expire(key, ttl);
			//jedis.quit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean checkExistedRedisKey(String key) {
		boolean isExisted = false;
		try {
			Jedis jedis = RedisConnectionFactory.getInstance();
			isExisted = jedis.exists(key);
			System.out.println(" --- REDIS KEY --- " + key + " --- IS_EXISTED --- " + isExisted);
			//jedis.quit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isExisted;
	}
	
	public String getRedisValue(String key) {
		String result;
		try {
			Jedis jedis = RedisConnectionFactory.getInstance();
			result = jedis.get(key);
			//System.out.println(" --- REDIS KEY --- " + key + " --- RESULT --- " + result);
			//jedis.quit();
		} catch (Exception e) {
			result = "";
			e.printStackTrace();
		}
		return result;
	}
	
	public void setRedisHash(String redisHashName, String key, String value) {
		try {
			Jedis jedis = RedisConnectionFactory.getInstance();
			jedis.hset(redisHashName, key, value);
			//jedis.quit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean checkExistedRedisHash(String redisHashName, String key) {
		boolean isExisted = false;
		try {
			Jedis jedis = RedisConnectionFactory.getInstance();
			isExisted = jedis.hexists(redisHashName, key);
			System.out.println("--- REDIS HASH NAME --- " + redisHashName + " --- REDIS HASH KEY --- " + key + " --- IS_EXISTED --- " + isExisted);
			//jedis.quit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isExisted;
	}
	
	public void initPhoenixQueryBlackList(){
		//Util.L_BLACK_LIST_QUERY.add(Util.BLACK_LIST_PHOENIX_QUERY_CREATE);
		Cons.L_BLACK_LIST_QUERY.add(Cons.BLACK_LIST_PHOENIX_QUERY_CREATE);
		Cons.L_BLACK_LIST_QUERY.add(Cons.BLACK_LIST_PHOENIX_QUERY_UPSERT);
		Cons.L_BLACK_LIST_QUERY.add(Cons.BLACK_LIST_PHOENIX_QUERY_ALTER);
		Cons.L_BLACK_LIST_QUERY.add(Cons.BLACK_LIST_PHOENIX_QUERY_DELETE);
		Cons.L_BLACK_LIST_QUERY.add(Cons.BLACK_LIST_PHOENIX_QUERY_DROP);
	}
	
	public boolean isOkPhoenixQuery(String query) {
		boolean status = true;
		String queryToLower = query.toLowerCase();
		
		for (String blackStr : Cons.L_BLACK_LIST_QUERY) {
			if (queryToLower.contains(blackStr)) {
				status = false;
			}
		}
		return status;
	}

	public String resultSetToJsonArr(ResultSet rs) {
		int count = 0;
		JsonArray jsArr = new JsonArray();

		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			count = rsmd.getColumnCount();

			while (rs.next()) {
				JsonObject jsObj = new JsonObject();

				for (int idx = 1; idx <= count; idx++) {
					jsObj.addProperty(rsmd.getColumnLabel(idx),
							rs.getString(idx));
				}
				jsArr.add(jsObj);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsArr.toString();
	}

	public String queryToPhoenix(String query) {
		PreparedStatement prst = null;
		ResultSet rset = null;
		Connection con = null;
		String result = null;
		try {
			//con = DriverManager.getConnection("jdbc:phoenix:bd1.local,bd2.local,bd37.local:2181");
			con = PhoenixConnectionFactory.getInstance().getConnection();
			prst = con.prepareStatement(query);
			rset = prst.executeQuery();
			con.commit();
			result = new Util().resultSetToJsonArr(rset);
			prst.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

//	public void insertToMysqlLog(String query, long response_time) {
//		String sql = null;
//		Connection con = null;
//		PreparedStatement prst = null;
//		try {
//			sql = "INSERT INTO log_phoenix_query (`query`, response_time, unix_time, date_time, `status`) VALUE (?, ?, unix_timestamp(), now(), 0)";
//			con = PhoenixConnectionFactory.getInstance().getConnection();
//			prst = con.prepareStatement(sql);
//			prst.setString(1, query);
//			prst.setLong(2, response_time);
//			prst.executeUpdate();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				prst.close();
//				con.close();
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
//	}
}
