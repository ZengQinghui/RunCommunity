package com.briup.run.common.util;

import java.util.ResourceBundle;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisUtils {
	public static JedisPool jedisPool;

	static {
		// ResourceBundle会查找classpath下的xxx.properties的文件,xxx是方法中指定的
		ResourceBundle resourceBundle = ResourceBundle.getBundle("redis");
		int maxTotal = Integer.parseInt(resourceBundle.getString("redis.pool.maxTotal"));
		int maxIdle = Integer.parseInt(resourceBundle.getString("redis.pool.maxIdle"));
		int maxWait = Integer.parseInt(resourceBundle.getString("redis.pool.maxWait"));
		String ip = resourceBundle.getString("redis.ip");
		int port = Integer.parseInt(resourceBundle.getString("redis.port"));

		JedisPoolConfig config = new JedisPoolConfig();
		// 设置最大连接数
		config.setMaxTotal(maxTotal);
		// 设置最大空闲数
		config.setMaxIdle(maxIdle);
		// 设置超时时间
		config.setMaxWaitMillis(maxWait);
		// 初始化连接池
		jedisPool = new JedisPool(config, ip, port);

	}

	public static void set(Object key, Object value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.set(SerializingUtils.serialize(key), SerializingUtils.serialize(value));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.close();
		}
	}

	public static Object get(Object key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			byte[] keyBytes = SerializingUtils.serialize(key);
			if (jedis.exists(keyBytes)) {
				return SerializingUtils.deserialize(jedis.get(keyBytes));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.close();
		}
		return null;
	}

	public static void del(Object key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.del(SerializingUtils.serialize(key));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.close();
		}
	}

	public static void clear() {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.flushDB();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.close();
		}
	}

	public static int getSize() {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.close();
		}
		return jedis.dbSize().intValue();
	}

	public static Jedis getResource() {
		return jedisPool.getResource();
	}

}
