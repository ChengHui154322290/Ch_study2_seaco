package com.tp.redis.test;
//package com.tp.redis.test;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import com.tp.redis.util.DBJedisList;
//import com.tp.redis.util.JedisCacheUtil;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "classpath*:spring.xml" })
//public class TestDBMethod {
//
//	@Autowired
//	JedisCacheUtil jedisCacheUtil;
//
//	@Test
//	public void testAll() throws InterruptedException {
//		DBJedisList<String> strList = new DBJedisList<String>("test-user-000001", 1);
//		for (int i = 0; i < 100; i++) {
//			boolean j = strList.watchMethodCall(50, 60);
//			System.out.println("j===>" + j);
////			Thread.sleep(1000);
//		}
//
//		for (int i = 0; i < 100; i++) {
//			boolean j = jedisCacheUtil.watchMethodCall("user_id_0001", 30, 60);
//			System.out.println("j===>" + j);
////			Thread.sleep(1000);
//		}
//
//	}
//}
