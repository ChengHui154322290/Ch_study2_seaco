package com.tp.service.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.bse.NavigationCategoryRangeDao;
import com.tp.dto.sch.BrandRange;
import com.tp.dto.sch.enums.NavigationType;
import com.tp.model.bse.NavigationCategoryRange;
import com.tp.model.usr.UserInfo;
import com.tp.service.BaseService;
import com.tp.service.bse.INavigationCategoryRangeService;

import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class NavigationCategoryRangeService extends BaseService<NavigationCategoryRange> implements INavigationCategoryRangeService {

	@Autowired
	private NavigationCategoryRangeDao navigationCategoryRangeDao;
	
	@Override
	public BaseDao<NavigationCategoryRange> getDao() {
		return navigationCategoryRangeDao;
	}

	@Override
	@Transactional
	public void add(List<String> brands, List<String> categories, Long categoryId, UserInfo user) {
		navigationCategoryRangeDao.delByCategoryIds(Arrays.asList(categoryId));
		Date cur = new Date();

		for (String b : brands) {
			NavigationCategoryRange range = new NavigationCategoryRange();
			range.setCreateUser(user.getUserName());
			range.setCreateTime(cur);
			range.setUpdateUser(user.getUserName());
			range.setUpdateTime(cur);
			range.setStatus(0);
			range.setCategoryId(categoryId);
			range.setContent(b);
			range.setType(NavigationType.BRAND.getCode());
			insert(range);
		}
		for (String c : categories) {
			NavigationCategoryRange range = new NavigationCategoryRange();
			range.setCreateUser(user.getUserName());
			range.setCreateTime(cur);
			range.setUpdateUser(user.getUserName());
			range.setUpdateTime(cur);
			range.setStatus(0);
			range.setCategoryId(categoryId);
			range.setContent(c);
			range.setType(NavigationType.CATEGORY.getCode());
			insert(range);
		}
	}

	@Override
	public void add(List<BrandRange> brandRangeList, Long categoryId, UserInfo user) {
		navigationCategoryRangeDao.delByCategoryIds(Arrays.asList(categoryId));
		Date cur = new Date();
		for (BrandRange b : brandRangeList) {
			NavigationCategoryRange range = new NavigationCategoryRange();
			range.setCreateUser(user.getUserName());
			range.setCreateTime(cur);
			range.setUpdateUser(user.getUserName());
			range.setUpdateTime(cur);
			range.setStatus(0);
			range.setCategoryId(categoryId);
			range.setContent(b.getBrandId().toString());
			range.setSort(b.getSort());
			range.setType(NavigationType.BRAND.getCode());
			navigationCategoryRangeDao.insert(range);
		}
	}
}
