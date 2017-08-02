package com.tp.service.usr;

import com.tp.model.usr.Department;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 部门表接口
  */
public interface IDepartmentService extends IBaseService<Department>{
	void save(Department department);
}
