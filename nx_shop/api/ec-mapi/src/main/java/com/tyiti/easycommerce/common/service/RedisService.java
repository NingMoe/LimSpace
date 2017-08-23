package com.tyiti.easycommerce.common.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis处理
 * 
 * @author Bolor
 *
 * @date 2016年3月3日
 *
 */
@Service
public class RedisService {

	// 用户session key前缀
	private static String USER_PRENAME = "USER_";

	@Value("${queueRedisHost}")
	private String REDIS_HOST;
	@Value("${queueRedisPort}")
	private String REDIS_PORT;

	private static JedisPool pool = null;

	private Log logger = LogFactory.getLog(this.getClass());

	public JedisPool getPool() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(3);
		poolConfig.setMaxIdle(3);

		JedisPool pool = new JedisPool(poolConfig, REDIS_HOST,
				Integer.parseInt(REDIS_PORT));
		return pool;
	}

	public void initPool() {
		if (pool == null) {
			pool = getPool();
		}
	}

	public void Destroy() {
		if (null != pool) {
			pool.destroy();
			pool = null;
		}
	}

	/**
	 * 删除
	 * 
	 * @param sessionId
	 * @param userId
	 * @return
	 */
	public synchronized Long del(String sessionId) {
		Jedis jedis = null;
		Long res = 0L;
		try {
			initPool();
			jedis = pool.getResource();
			res = jedis.del(USER_PRENAME + sessionId);
		} catch (Exception e) {
			res = -1L;
			e.printStackTrace();
			logger.error(e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return res;
	}

	/**
	 * 新增
	 * 
	 * @param sessionId
	 * @param userId
	 * @param time
	 * @return
	 */
	public synchronized Long add(String sessionId, Integer userId, Integer time) {
		Jedis jedis = null;
		Long res = 0L;
		try {
			initPool();
			jedis = pool.getResource();
			res = jedis.setnx(USER_PRENAME + sessionId, String.valueOf(userId));
			jedis.expire(USER_PRENAME + sessionId, time);
		} catch (Exception e) {
			res = -1L;
			e.printStackTrace();
			logger.error(e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return res;
	}

	/**
	 * 修改
	 * 
	 * @param sessionId
	 * @param userId
	 * @param time
	 * @return
	 */
	public synchronized Integer set(String sessionId, Integer userId,
			Integer time) {
		Jedis jedis = null;
		Integer res = 0;
		try {
			initPool();
			jedis = pool.getResource();
			String _res = jedis.set(USER_PRENAME + sessionId, String.valueOf(userId));
			jedis.expire(USER_PRENAME + sessionId, time);
			if (_res != null && "OK".equalsIgnoreCase(_res)) {
				res = 1;
			}
		} catch (Exception e) {
			res = -1;
			e.printStackTrace();
			logger.error(e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return res;
	}
	
	public synchronized Integer set(String key, String value, int expireSeconds) {
		Jedis jedis = null;
		key = USER_PRENAME + key;
		Integer ret = 0;
		try {
			initPool();
			jedis = pool.getResource();
			String res = jedis.set(key, value);
			jedis.expire(key, expireSeconds);
			if (res != null && "OK".equalsIgnoreCase(res)) {
				ret = 1;
			}
		} catch (Exception e) {
			ret = -1;
			e.printStackTrace();
			logger.error(e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return ret;
	}

	/**
	 * 获取
	 * 
	 * @param sessionId
	 * @param userId
	 * @return
	 */
	public synchronized Integer get(String sessionId) {
		Jedis jedis = null;
		String time = null;
		Integer res = 0;
		try {
			initPool();
			jedis = pool.getResource();
			time = jedis.get(USER_PRENAME + sessionId);
			if (time != null) {
				res = Integer.parseInt(time);
			}
		} catch (Exception e) {
			res = -1;
			e.printStackTrace();
			logger.error(e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return res;
	}

	public static void main(String[] args) throws Exception {
		RedisService sfs = new RedisService();

		System.out.println(sfs.get("session"));

		System.out.println(sfs.add("session", 1, 3));

		System.out.println(sfs.del("session"));

		System.out.println(sfs.get("session"));

		try {
			Thread.sleep(3);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(sfs.get("session"));

		System.out.println(sfs.set("session", 2, 3));

		System.out.println(sfs.get("session"));

		try {
			Thread.sleep(2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(sfs.get("session"));

		try {
			Thread.sleep(2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(sfs.get("session"));

		System.out.println(sfs.set("session", 3, 2));

		try {
			Thread.sleep(3);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(sfs.get("session"));

		System.out.println(sfs.del("session"));
	}
}