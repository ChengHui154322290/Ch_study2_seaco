package com.tp.service.usr;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.usr.DepartmentDao;
import com.tp.model.usr.Department;
import com.tp.service.BaseService;
import com.tp.service.usr.IDepartmentService;

@Service
public class DepartmentService extends BaseService<Department> implements IDepartmentService {

	@Autowired
	private DepartmentDao departmentDao;
	
	@Override
	public BaseDao<Department> getDao() {
		return departmentDao;
	}
	@Override
	public void save(Department department){
		department.setUpdateTime(new Date());
		if(null != department.getId()){
			departmentDao.updateNotNullById(department);
		}else{
			department.setCreateTime(new Date());
			departmentDao.insert(department);
		}
	}
}
