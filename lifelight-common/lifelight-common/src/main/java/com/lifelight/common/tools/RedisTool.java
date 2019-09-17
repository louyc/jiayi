package com.lifelight.common.tools;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisTool {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private RedisTemplate redisTemplate;

	/**
	 * 压栈
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long push(String key, String value) {
		return stringRedisTemplate.opsForList().leftPush(key, value);
	}

	/**
	 * 出栈
	 * 
	 * @param key
	 * @return
	 */
	public String pop(String key) {
		return stringRedisTemplate.opsForList().leftPop(key);
	}

	/**
	 * 入队
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long in(String key, String value) {
		return stringRedisTemplate.opsForList().rightPush(key, value);
	}

	/**
	 * 出队
	 * 
	 * @param key
	 * @return
	 */
	public String out(String key) {
		return stringRedisTemplate.opsForList().leftPop(key);
	}

	/**
	 * 栈/队列长
	 * 
	 * @param key
	 * @return
	 */
	public Long length(String key) {
		return stringRedisTemplate.opsForList().size(key);
	}

	/**
	 * 范围检索
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public List<String> range(String key, int start, int end) {
		return stringRedisTemplate.opsForList().range(key, start, end);
	}

	/**
	 * 移除
	 * 
	 * @param key
	 * @param i
	 * @param value
	 */
	public void remove(String key, long i, String value) {
		stringRedisTemplate.opsForList().remove(key, i, value);
	}

	/**
	 * 检索
	 * 
	 * @param key
	 * @param index
	 * @return
	 */
	public String index(String key, long index) {
		return stringRedisTemplate.opsForList().index(key, index);
	}

	/**
	 * 置值
	 * 
	 * @param key
	 * @param index
	 * @param value
	 */
	public void set(String key, long index, String value) {
		stringRedisTemplate.opsForList().set(key, index, value);
	}

	/**
	 * 裁剪
	 * 
	 * @param key
	 * @param start
	 * @param end
	 */
	public void trim(String key, long start, int end) {
		stringRedisTemplate.opsForList().trim(key, start, end);
	}

	/**
	 * 根据key value赋值
	 * 
	 * @param key
	 * @param value
	 */
	public void put(String key, String value) {
		stringRedisTemplate.opsForValue().set(key, value);
	}

	/**
	 * 根据key value赋值
	 * 
	 * @param key
	 * @param value
	 */
	public void put(String key, String value, long timeout, TimeUnit unit) {
		stringRedisTemplate.opsForValue().set(key, value, timeout, unit);
	}

	/**
	 * 根据key获取 value赋值
	 * 
	 * @param key
	 */
	public String get(String key) {
		return stringRedisTemplate.opsForValue().get(key);
	}

	/**
	 * redis锁，时长：10秒
	 * 
	 * @param lockStr
	 * @return
	 */
	public boolean acquireLock(String lockStr) {
		redisTemplate.opsForValue().getOperations().expire(lockStr, 10, TimeUnit.SECONDS);
		return redisTemplate.opsForValue().setIfAbsent(lockStr, lockStr);
	}

	public void removeLock(String lockStr) {
		redisTemplate.delete(lockStr);
	}

	/**
	 * 向key对应的map中添加缓存对象
	 * 
	 * @param key
	 * @param map
	 */
	public <T> void addMap(String key, Map<String, T> map) {
		stringRedisTemplate.opsForHash().putAll(key, map);
	}

	/**
	 * 向key对应的map中添加缓存对象
	 * 
	 * @param key
	 *            cache对象key
	 * @param field
	 *            map对应的key
	 * @param value
	 *            值
	 */
	public void addMap(String key, String field, String value, Date date) {
		stringRedisTemplate.opsForHash().put(key, field, value);
		if (null != date)
			stringRedisTemplate.opsForHash().getOperations().expireAt(key, date);
	}

	/**
	 * 获取map缓存中的某个对象
	 * 
	 * @param key
	 * @param field
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getMapField(String key, String field, Class<T> clazz) {
		return (T) stringRedisTemplate.boundHashOps(key).get(field);
	}

	/**
	 * 删除map中的某个对象
	 * 
	 * @param key
	 *            map对应的key
	 * @param field
	 *            map中该对象的key
	 */
	public void delMapField(String key, String... field) {
		BoundHashOperations<String, String, ?> boundHashOperations = stringRedisTemplate
				.boundHashOps(key);
		boundHashOperations.delete(field);
	}

}
