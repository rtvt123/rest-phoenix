package util.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import util.Cons;

public class RedisConnectionFactory {
	private static Jedis jedis = null;
	
	public static Jedis getInstance() {
		if (jedis == null) {
			jedis = new Jedis(Cons.REDIS_HOST, Cons.REDIS_PORT);
		} else {
			try {				
				if (!jedis.isConnected()) {
					jedis = new Jedis(Cons.REDIS_HOST, Cons.REDIS_PORT);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		jedis.select(Cons.REDIS_DB);
		return jedis;
	}
}