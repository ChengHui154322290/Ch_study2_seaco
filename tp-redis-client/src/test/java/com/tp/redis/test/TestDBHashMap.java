package com.tp.redis.test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tp.redis.util.DBHashMap;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring.xml" })
public class TestDBHashMap {

	@Test
	public void testAll() throws InterruptedException {

		Integer key = 10;
		DBHashMap<PersonDO> map = new DBHashMap<PersonDO>(key.toString(), 10);
		for (int i = 0; i < 2; i++) {
			PersonDO p = new PersonDO();
			p.setCode("code" + i);
			p.setName("name" + i);
			map.put(p.getCode(), p);
		}

		System.out.println("get=>" + map.get("code1"));

		Set<String> set = map.fieldSet();
		for (String str : set) {
			System.out.println(str);
		}

		System.out.println("containsField code1 =>" + map.containsField("code1"));
		System.out.println("containsField code0 =>" + map.containsField("code0"));
		
		PersonDO p = new PersonDO();
		p.setCode("code1");
		p.setName("name1");
		System.out.println(map.containsValue(p));

		System.out.println("++++++++++==");
		List<PersonDO> list = map.values();
		for (PersonDO personDO : list) {
			System.out.println(personDO.getCode());
			System.out.println(personDO.getName());
		}

		System.out.println("keyValues");
		Map<String, PersonDO> entrys = map.keyValues();
		for (Map.Entry<String, PersonDO> entry : entrys.entrySet()) {
			System.out.println(entry.getKey());
			System.out.println(entry.getValue().getCode());
			System.out.println(entry.getValue().getName());
		}

		System.out.println("remove=>" + map.remove("code1"));
		System.out.println("map size=>" + map.size());
		System.out.println("clearAllFields=>" + map.clearAllFields());
		System.out.println("after clear map size is=>" + map.size());
		System.out.println("++++++++++++++++++after clearAllFields++++++++++++++++++++");
		
		entrys = map.keyValues();
		for (Map.Entry<String, PersonDO> entry : entrys.entrySet()) {
			System.out.println(entry.getKey());
			System.out.println(entry.getValue().getCode());
			System.out.println(entry.getValue().getName());
		}
		
	}

}
