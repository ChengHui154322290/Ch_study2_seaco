package com.tp.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.RowBounds;

import com.tp.common.vo.PageInfo;


@Intercepts({@Signature(type=StatementHandler.class, method="prepare", args={Connection.class})})
public class PageInterceptor implements Interceptor {	
	private static String databasetype;
	Logger logger = Logger.getLogger(PageInterceptor.class.getName());
	@SuppressWarnings("resource")
	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler)invocation.getTarget();
		Object objconnection = invocation.getArgs()[0];
		Connection connection = (Connection)objconnection;
		MetaObject metaStatement = SystemMetaObject.forObject(statementHandler); //获取meta元数据
		while (metaStatement.hasGetter("h")) {
			Object object = metaStatement.getValue("h");
			metaStatement = SystemMetaObject.forObject(object);
		}
		while (metaStatement.hasGetter("target")) {
			Object object = metaStatement.getValue("target");
			metaStatement = SystemMetaObject.forObject(object);
		}
		
		if(!databasetype.equals("mysql")) { //目前只支持mysql
			return invocation.proceed();
		}		
				
		BoundSql boundSql = (BoundSql)metaStatement.getValue("delegate.boundSql");
		String oldSql = boundSql.getSql(); //老的sql
		String newSql = null;
		RowBounds rowBounds = (RowBounds)metaStatement.getValue("delegate.rowBounds");
		if(rowBounds.equals(RowBounds.DEFAULT) ) { //没有分页信息RowBounds
			Object parameterObject = boundSql.getParameterObject();
			if(parameterObject == null || !metaStatement.hasGetter("delegate.boundSql.parameterObject.pageInfo")) { //无分页参数PageInfo
				return invocation.proceed();
			}
			PageInfo<?> pageInfo = (PageInfo<?>)metaStatement.getValue("delegate.boundSql.parameterObject.pageInfo");
			if(pageInfo == null) {
				return invocation.proceed();
			}
			
			if(pageInfo.getRecords() == null) {
				MappedStatement mappedStatement = (MappedStatement)metaStatement.getValue("delegate.mappedStatement");
				setAdditionParams(metaStatement,boundSql);
				if(!setRecordCount(connection, mappedStatement, boundSql, pageInfo)) {
					Object object = invocation.proceed();
					return object;
				}else{
				}
			}
			int offset = (pageInfo.getPage() - 1) * pageInfo.getSize();
			if(offset < 0) {
				offset = 0;
			}
			rowBounds = new RowBounds(offset, pageInfo.getSize());
		}
	
		newSql = oldSql + " limit " + rowBounds.getOffset() + ", " + rowBounds.getLimit();
		metaStatement.setValue("delegate.boundSql.sql", newSql); //设置新sql
		metaStatement.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET); //取消原有的内存分页
		metaStatement.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);

		return invocation.proceed();
	}
	
	public Object plugin(Object target) {
  		if (target instanceof StatementHandler) {
  			return Plugin.wrap(target, this);
  		} else {
  			return target;
  		}
	}

	public void setProperties(Properties properties) {
		databasetype = properties.getProperty("databasetype");
	}
	
	private boolean setRecordCount(Connection connection, MappedStatement mappedStatement, BoundSql boundSql, PageInfo<?> pageInfo) {
		String cntSql = "SELECT COUNT(0) FROM (" + boundSql.getSql() + ") recordCount";
		BoundSql cntBndSql = new BoundSql(mappedStatement.getConfiguration(), cntSql, boundSql.getParameterMappings(), boundSql.getParameterObject());

		boolean ok = true;
		PreparedStatement cntStmt = null;
		ResultSet rs = null;
		try {
			cntStmt = connection.prepareStatement(cntSql);
			ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, boundSql.getParameterObject(), cntBndSql);
			parameterHandler.setParameters(cntStmt); //对SQL绑定参数设值
			
			rs = cntStmt.executeQuery();
			int recordCount = 0;
			if(rs.next()) {
				recordCount = rs.getInt(1);
			}
			pageInfo.setRecords(recordCount);
		} catch (SQLException e) {
			ok = false;
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if(cntStmt != null) {
				try {
					cntStmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return ok;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setAdditionParams(final MetaObject metaStatement,final BoundSql boundSql){
		HashMap<?,?> additionalParameters = (HashMap<?,?>)metaStatement.getValue("delegate.boundSql.additionalParameters");
		if(null!=additionalParameters && additionalParameters.get("_parameter")!=null && boundSql.getParameterObject() instanceof Map){
			for(Map.Entry<?, ?> entry:additionalParameters.entrySet()){
				if(!"_parameter".equals(entry.getKey())){
					((Map)boundSql.getParameterObject()).put(entry.getKey(), entry.getValue());
				}
			}
		}
	}
}
