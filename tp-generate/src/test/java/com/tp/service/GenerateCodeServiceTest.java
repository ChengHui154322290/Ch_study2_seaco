package com.tp.service;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tp.model.Model;
import com.tp.service.GenerateCodeService;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:beans.xml" })
public class GenerateCodeServiceTest {

	@Autowired
	private GenerateCodeService generateCodeService;
	/**
	 * 生成代码方法
	 */
	@Test
	public void testCreateCode() {
		generateCodeService.createCode();
	}

	@Test
	public void testQueryModelList() {
		List<Model> list = generateCodeService.queryModelList();
		Assert.assertTrue(list.size()>0);
	}

}
