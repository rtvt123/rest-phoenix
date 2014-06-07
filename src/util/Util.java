package util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;

import redis.clients.jedis.Jedis;
import util.factory.PhoenixConnectionFactory;
import util.factory.RedisConnectionFactory;

import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

public class Util {

	public String getStrFromPropertiesFile(String proFile, String key){
		String result = null;
		Configuration config;
		try {
			config = new PropertiesConfiguration(proFile);
			result = config.getString(key);
			System.out.println(" --- FILE --- " + proFile + " --- KEY --- " + key + " --- RESULT --- " + result);
		} catch (Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	public Set<String> getSetStrFromPropertiesFile(String proFile, String key){
		String strArrResult[];
		Set<String> sResult = null;
		Configuration config;
		try {
			config = new PropertiesConfiguration(proFile);
			strArrResult = config.getStringArray(key);
			sResult = Sets.newHashSet(strArrResult);
			System.out.println(" --- FILE --- " + proFile + " --- KEY --- " + sResult);
		} catch (Exception e){
			e.printStackTrace();
		}
		return sResult;
	}
	
	public int getIntFromParam(String intVal){
		int result;
		try {
			result = Integer.parseInt(intVal);
		} catch (Exception e) {
			result = 0;
			System.out.println("--- NOT NUMBER--- ");
		}
		return result;
	}
	
	public String getStrFromParam(String strVal){
		String result;
		try {
			result = strVal;
		} catch (Exception e) {
			result = "0";
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
	
	public boolean isOkPhoenixQuery(String query) {
		boolean status = true;
		String queryToLower = query.toLowerCase();
		
		for (String blackStr : Cons.S_BLACK_LIST_QUERY) {
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
