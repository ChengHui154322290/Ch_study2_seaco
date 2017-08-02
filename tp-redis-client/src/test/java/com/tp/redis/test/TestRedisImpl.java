package com.tp.redis.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tp.redis.test.domain.PersonDO;
import com.tp.redis.util.DBJedisList;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.redis.util.JedisDBUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring.xml" })
public class TestRedisImpl {

	@Autowired
	JedisCacheUtil jedisCacheUtil;

	@Autowired
	JedisDBUtil jedisDBUtil;

	@Test
	public void testAll() {

		for (int i = 0; i < 100; i++) {
			jedisCacheUtil.setCache("cache_key" + i, "cache_value1", 100);
			System.out.println(jedisCacheUtil.getCache("cache_key" + i));
		}

		jedisCacheUtil.setCacheString("cache_key2", "cache_value2", 100);

		System.out.println(jedisCacheUtil.getCacheString("cache_key2"));

		jedisDBUtil.setDB("name1", "value1");
		System.out.println(jedisDBUtil.getDB("name1"));

		jedisDBUtil.setDBString("name2", "value2");
		System.out.println(jedisDBUtil.getDBString("name2"));

		int user_id = 13;
		DBJedisList<PersonDO> redis = new DBJedisList<PersonDO>("uid_" + user_id, user_id);
		redis.clear();

		PersonDO p = new PersonDO(1, "user_001");
		p.setId(1);
		p.setName("name");

		redis.add(p);
		PersonDO p1 = redis.get(0);
		System.out.println(p1);
		System.out.println(p1.equals(p));

		// System.out.println(redis.remove(p1));
		p.setName("name1");
		System.out.println(redis.set(0, p));

		PersonDO p2 = redis.get(0);
		System.out.println(p2);
		System.out.println("==============");

		PersonDO p3 = p2;
		p3.setName("name_3");
		redis.add(p3);

		for (PersonDO person : redis.getList()) {
			System.out.println(":" + person.getName());
		}

		System.out.println(redis.remove(3));

		PersonDO p4 = new PersonDO();
		p4.setId(4);
		p4.setName("name4");
		System.out.println(redis.remove(p4));

		System.out.println(redis.size());
		System.out.println(redis.get(5));

		List<PersonDO> new_list = new ArrayList<PersonDO>();
		new_list.add(p4);
		int i = redis.addAll(new_list);
		System.out.println("==>" + i);
		//
		// for (PersonDO person : redis.getList()) {
		// System.out.println(person.getName());
		// }

		// System.out.println(redis.getList().toArray());

		// long i=redis.add(p);
		// System.out.println(i);
		// System.out.println(redis.get(0));

		// // redis.close();
		//
		// List<String> list = redis.getList();
		// for (String string : list) {
		// System.out.println(string);
		// }
		// redis.remove(0);
		// System.out.println("===== remove 0 ======");
		// for (String string : redis.getList()) {
		// System.out.println(string);
		// }
		//
		// redis.clear();
		// redis.set(16, "aa");
		// System.out.println("===== clear all =====");
		// for (String string : redis.getList()) {
		// System.out.println(string);
		// }
		// List<String> carts = new ArrayList<String>();
		// for (int i = 0; i < 10; i++) {
		// carts.add("test" + i);
		// }
		//
		// for (String string : carts) {
		// System.out.println(string);
		// }

		// Jedis jedis = redis.getJedis();
		// jedis.set("person:100".getBytes(), SerializeUtil.serialize(new
		// PersonDO(100, "zhangsan")));
		// jedis.set("person:101".getBytes(), SerializeUtil.serialize(new
		// PersonDO(101, "bruce")));
		//
		// byte[] data100 = jedis.get(("person:100").getBytes());
		// PersonDO person100 = (PersonDO) SerializeUtil.unserialize(data100);
		// System.out.println(String.format("person:100->id=%s,name=%s",
		// person100.getId(), person100.getName()));
		//
		// byte[] data101 = jedis.get(("person:101").getBytes());
		// PersonDO person101 = (PersonDO) SerializeUtil.unserialize(data101);
		// System.out.println(String.format("person:101->id=%s,name=%s",
		// person101.getId(), person101.getName()));

	}
}
