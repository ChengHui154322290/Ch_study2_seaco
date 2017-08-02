package com.tp.db;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.springframework.core.PriorityOrdered;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * only support for dbcp
 * @author szy
 *
 */
public class MysqlSlaveRunable implements Runnable, PriorityOrdered {

	private static final Log logger = LogFactory.getLog(MysqlSlaveRunable.class);

	private static final String SELECT_1_AS_NUM_SQL = "select 1 as num";

	private static final String SLAVE = "slave";

	private Map<String, AbstractRoutingDataSource> dataSourceMap;

	private Map<String, ?> dbcpSourceMap;

	private Map<String, Object> slaveTargetDataSources = new HashMap<String, Object>();//

	private Map<String, Object> removeList = new HashMap<String, Object>();

	private Set<String> removeDataSourceBeanIdSet = new HashSet<String>();

	private Set<Mysql> mysqlSlaves = new HashSet<Mysql>();

	private volatile boolean flag = true;

	private volatile boolean printSqlException;

	@Override
	public void run() {
		if (flag) {
			this.watch();
		}
	}

	public void initData() {
		for (Map.Entry<String, AbstractRoutingDataSource> entry : dataSourceMap.entrySet()) {
			AbstractRoutingDataSource ards = entry.getValue();
			this.filterSlaveDataSource(ards);
		}

		for (Map.Entry<String, ?> entry : dbcpSourceMap.entrySet()) {
			if (entry.getKey().toLowerCase().contains(SLAVE)) {

				BasicDataSource bds = null;
				Mysql mysql = new Mysql();
				if (entry.getValue() instanceof BasicDataSource) {
					bds = (BasicDataSource) entry.getValue();
					String[] get_ip_port = get_ip_port(bds.getUrl());
					mysql.setDriverClassName(bds.getDriverClassName());
					mysql.setUrl(bds.getUrl());
					mysql.setPassWord(bds.getPassword());
					mysql.setIp(get_ip_port[0]);
					mysql.setPort(Integer.parseInt(get_ip_port[1]));
					mysql.setDbName(get_db_name(bds.getUrl()));
					mysql.setUserName(bds.getUsername());
					mysql.setBeanId(entry.getKey());
					mysqlSlaves.add(mysql);
				}

				ComboPooledDataSource cpds = null;
				if (entry.getValue() instanceof ComboPooledDataSource) {
					cpds = (ComboPooledDataSource) entry.getValue();
					String[] get_ip_port = get_ip_port(cpds.getJdbcUrl());
					mysql.setDriverClassName(cpds.getDriverClass());
					mysql.setUrl(cpds.getJdbcUrl());
					mysql.setPassWord(cpds.getPassword());
					mysql.setIp(get_ip_port[0]);
					mysql.setPort(Integer.parseInt(get_ip_port[1]));
					mysql.setDbName(get_db_name(cpds.getJdbcUrl()));
					mysql.setUserName(cpds.getUser());
					mysql.setBeanId(entry.getKey());
					mysqlSlaves.add(mysql);
				}

				ComboPooledDataSource mcpds = null;
				if (entry.getValue() instanceof ComboPooledDataSource) {
					mcpds = (ComboPooledDataSource) entry.getValue();
					String[] get_ip_port = get_ip_port(mcpds.getJdbcUrl());
					mysql.setDriverClassName(mcpds.getDriverClass());
					mysql.setUrl(mcpds.getJdbcUrl());
					mysql.setPassWord(mcpds.getPassword());
					mysql.setIp(get_ip_port[0]);
					mysql.setPort(Integer.parseInt(get_ip_port[1]));
					mysql.setDbName(get_db_name(mcpds.getJdbcUrl()));
					mysql.setUserName(mcpds.getUser());
					mysql.setBeanId(entry.getKey());
					mysqlSlaves.add(mysql);
				}

			}
		}
	}

	private synchronized void watch() {
		for (Map.Entry<String, ?> entry : dbcpSourceMap.entrySet()) {
			if (entry.getKey().toLowerCase().contains(SLAVE)) {

				String url = "";
				DataSource ds = null;
				BasicDataSource bds = null;
				if (entry.getValue() instanceof BasicDataSource) {
					bds = (BasicDataSource) entry.getValue();
					ds = bds;
					url = bds.getUrl();
				}

				ComboPooledDataSource cpds = null;
				if (entry.getValue() instanceof ComboPooledDataSource) {
					cpds = (ComboPooledDataSource) entry.getValue();
					ds = cpds;
					url = cpds.getJdbcUrl();
				}

				ComboPooledDataSource mcpds = null;
				if (entry.getValue() instanceof ComboPooledDataSource) {
					mcpds = (ComboPooledDataSource) entry.getValue();
					ds = mcpds;
					url = cpds.getJdbcUrl();
				}

				Mysql mysql = null;
				for (Mysql mysqlDO : mysqlSlaves) {
					if (mysqlDO.getBeanId().equals(entry.getKey())) {
						mysql = mysqlDO;
						break;
					}
				}
				if (null != mysql) {
					boolean watchOneNode = watchOneNode(ds, url);
					if (watchOneNode) {
						addSlaveDataSource(mysql.getDbName());
					} else {
						removeSlaveDataSource(mysql.getDbName());
					}
				}
			}
		}

	}

	private void removeSlaveDataSource(String dbname) {
		String dataSourceBeanId = "";
		for (Mysql m : mysqlSlaves) {
			if (m.getDbName().equals(dbname)) {
				dataSourceBeanId = m.getBeanId();
				break;
			}
		}

		if (!"".equals(dataSourceBeanId)) {
			for (Map.Entry<String, AbstractRoutingDataSource> ard : dataSourceMap.entrySet()) {
				AbstractRoutingDataSource ds = (AbstractRoutingDataSource) ard.getValue();
				removeSlaveDataSource(ds, dataSourceBeanId);
			}
		}

	}

	private void addSlaveDataSource(String dbname) {
		String dataSourceBeanId = "";
		for (Mysql m : mysqlSlaves) {
			if (m.getDbName().equals(dbname)) {
				dataSourceBeanId = m.getBeanId();
				break;
			}
		}

		Map<String, Object> returnAddMap = returnAddMapNew(dataSourceBeanId);

		for (Map.Entry<String, AbstractRoutingDataSource> ard : dataSourceMap.entrySet()) {
			AbstractRoutingDataSource ds = (AbstractRoutingDataSource) ard.getValue();
			addSlaveDataSource(ds, returnAddMap, dataSourceBeanId);
		}

	}

	private Map<String, Object> returnAddMapNew(String dataSourceBeanId) {
		Map<String, Object> map = new HashMap<String, Object>();

		BasicDataSource bds = null;
		Object bean = dbcpSourceMap.get(dataSourceBeanId);
		if (bean instanceof BasicDataSource) {
			bds = (BasicDataSource) dbcpSourceMap.get(dataSourceBeanId);
			for (Map.Entry<String, Object> ent : removeList.entrySet()) {
				BasicDataSource as = (BasicDataSource) ent.getValue();
				if (bds.getUrl().equals(as.getUrl())) {
					map.put(ent.getKey(), ent.getValue());
					break;
				}
			}
		}

		ComboPooledDataSource cpds = null;
		if (bean instanceof ComboPooledDataSource) {
			cpds = (ComboPooledDataSource) dbcpSourceMap.get(dataSourceBeanId);
			for (Map.Entry<String, Object> ent : removeList.entrySet()) {
				ComboPooledDataSource as = (ComboPooledDataSource) ent.getValue();
				if (cpds.getJdbcUrl().equals(as.getJdbcUrl())) {
					map.put(ent.getKey(), ent.getValue());
					break;
				}
			}
		}

		ComboPooledDataSource mcpds = null;
		if (bean instanceof ComboPooledDataSource) {
			mcpds = (ComboPooledDataSource) dbcpSourceMap.get(dataSourceBeanId);
			for (Map.Entry<String, Object> ent : removeList.entrySet()) {
				ComboPooledDataSource as = (ComboPooledDataSource) ent.getValue();
				if (mcpds.getJdbcUrl().equals(as.getJdbcUrl())) {
					map.put(ent.getKey(), ent.getValue());
					break;
				}
			}
		}

		return map;
	}

	private void addSlaveDataSource(Object ards, Map<String, Object> addMap, String dataSourceBeanId) {
		boolean isNeedAdd = false;
		for (Map.Entry<String, Object> e : addMap.entrySet()) {
			if (removeList.containsKey(e.getKey())) {
				isNeedAdd = true;
			}
		}
		if (isNeedAdd) {
			for (Map.Entry<String, AbstractRoutingDataSource> ent : dataSourceMap.entrySet()) {
				Field[] fieldlist = ards.getClass().getSuperclass().getDeclaredFields();
				for (int i = 0; i < fieldlist.length; i++) {
					Field f = fieldlist[i];
					f.setAccessible(true);
					String name = f.getName();
					if (name.equals("targetDataSources")) {
						try {
							Map<Object, Object> targetDataSources = (Map<Object, Object>) f.get(ards);
							for (Map.Entry<String, Object> e : addMap.entrySet()) {
								if (!targetDataSources.containsKey(e.getKey())) {
									targetDataSources.put(e.getKey(), e.getValue());
									removeList.remove(e.getKey());
									removeDataSourceBeanIdSet.remove(dataSourceBeanId);
								}
							}
							f.set(ards, targetDataSources);
							call_after_properties_set(ards);
							logger.info("addDataSources dataSourceBeanId==>" + dataSourceBeanId + " successful");
						} catch (IllegalArgumentException e) {
							logger.error(e.getMessage(), e);
						} catch (IllegalAccessException e) {
							logger.error(e.getMessage(), e);
						}
					}
				}

				// after add view logs
				for (Map.Entry<String, AbstractRoutingDataSource> drd : dataSourceMap.entrySet()) {
					Object ds = drd.getValue();
					fieldlist = ds.getClass().getSuperclass().getDeclaredFields();
					for (int i = 0; i < fieldlist.length; i++) {
						Field f = fieldlist[i];
						f.setAccessible(true);
						String name = f.getName();
						if (name.equals("targetDataSources")) {
							try {
								Map<Object, Object> targetDataSources = (Map<Object, Object>) f.get(ards);
								for (Map.Entry<Object, Object> slave : targetDataSources.entrySet()) {
									logger.info(" after add targetDataSources key is==>" + slave.getKey());
								}
							} catch (IllegalArgumentException e) {
								logger.error(e.getMessage(), e);
							} catch (IllegalAccessException e) {
								logger.error(e.getMessage(), e);
							}
						}
					}
				}
			}
		}
	}

	private void removeSlaveDataSource(Object ards, String dataSourceBeanId) {
		if (removeDataSourceBeanIdSet.contains(dataSourceBeanId)) {
			// logger.info("removeDataSourceBeanIdSet.contains(dataSourceBeanId) removeDataSourceBeanIdSet.contains(dataSourceBeanId) ");
			return;
		} else {
			logger.info("removeSlaveDataSource dataSourceBeanId==>" + dataSourceBeanId);
		}

		BasicDataSource dbcp = null;
		Object o = dbcpSourceMap.get(dataSourceBeanId);
		if (o instanceof BasicDataSource) {
			dbcp = (BasicDataSource) dbcpSourceMap.get(dataSourceBeanId);
		}

		ComboPooledDataSource cpds = null;
		if (o instanceof ComboPooledDataSource) {
			cpds = (ComboPooledDataSource) dbcpSourceMap.get(dataSourceBeanId);
		}

		ComboPooledDataSource mcpds = null;
		if (o instanceof ComboPooledDataSource) {
			mcpds = (ComboPooledDataSource) dbcpSourceMap.get(dataSourceBeanId);
		}

		Field[] fieldlist = ards.getClass().getSuperclass().getDeclaredFields();
		for (int i = 0; i < fieldlist.length; i++) {
			Field f = fieldlist[i];
			f.setAccessible(true);
			String name = f.getName();
			if (name.equals("targetDataSources")) {
				try {
					Map<Object, Object> targetDataSources = (Map<Object, Object>) f.get(ards);
					String removeKey = "";
					for (Map.Entry<Object, Object> entry : targetDataSources.entrySet()) {
						if (null != dbcp) {
							BasicDataSource value = (BasicDataSource) entry.getValue();
							if (entry.getKey().toString().toLowerCase().contains(SLAVE) && value.getUrl().equals(dbcp.getUrl())) {
								removeKey = entry.getKey().toString();
								removeList.put(removeKey, entry.getValue());
								break;
							}
						}

						if (null != cpds) {
							ComboPooledDataSource value = (ComboPooledDataSource) entry.getValue();
							if (entry.getKey().toString().contains(SLAVE) && value.getJdbcUrl().equals(cpds.getJdbcUrl())) {
								removeKey = entry.getKey().toString();
								removeList.put(removeKey, entry.getValue());
								break;
							}
						}

						if (null != mcpds) {
							ComboPooledDataSource value = (ComboPooledDataSource) entry.getValue();
							if (entry.getKey().toString().contains(SLAVE) && value.getJdbcUrl().equals(mcpds.getJdbcUrl())) {
								removeKey = entry.getKey().toString();
								removeList.put(removeKey, entry.getValue());
								break;
							}
						}

					}
					targetDataSources.remove(removeKey);
					removeDataSourceBeanIdSet.add(dataSourceBeanId);
					f.set(ards, targetDataSources);
					call_after_properties_set(ards);
					logger.info("after remove targetDataSources size==>" + targetDataSources.size());
					for (Map.Entry<Object, Object> entry : targetDataSources.entrySet()) {
						logger.info("after remove targetDataSources key==>" + entry.getKey());
					}
					if (dbcp != null) {
						clear_dbcp_datasource_pool(dbcp);
					}
				} catch (IllegalArgumentException e) {
					logger.error(e.getMessage(), e);
				} catch (IllegalAccessException e) {
					logger.error(e.getMessage(), e);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}

		// after remove view logs
		for (Map.Entry<String, AbstractRoutingDataSource> drd : dataSourceMap.entrySet()) {
			Object ds = drd.getValue();
			fieldlist = ds.getClass().getSuperclass().getDeclaredFields();
			for (int i = 0; i < fieldlist.length; i++) {
				Field f = fieldlist[i];
				f.setAccessible(true);
				String name = f.getName();
				if (name.equals("targetDataSources")) {
					try {
						Map<Object, Object> targetDataSources = (Map<Object, Object>) f.get(ards);
						for (Map.Entry<Object, Object> slave : targetDataSources.entrySet()) {
							logger.info(" after move targetDataSources key is==>" + slave.getKey());
						}
					} catch (IllegalArgumentException e) {
						logger.error(e.getMessage(), e);
					} catch (IllegalAccessException e) {
						logger.error(e.getMessage(), e);
					}
				}
			}
		}
	}

	private void clear_dbcp_datasource_pool(BasicDataSource dbcp) throws IllegalAccessException {
		Field[] dbcp_filed_list = dbcp.getClass().getDeclaredFields();
		for (Field field : dbcp_filed_list) {
			field.setAccessible(true);
			String dbcp_name = field.getName();
			if (dbcp_name.equals("connectionPool")) {
				GenericObjectPool connectionPool = (GenericObjectPool) field.get(dbcp);
				connectionPool.clear();
				logger.info("dbcp datasource connectionPool.clear()");
			}
		}
	}

	private void call_after_properties_set(Object ards) throws IllegalAccessException {
		Method m1 = null;
		try {
			m1 = ards.getClass().getSuperclass().getDeclaredMethod("afterPropertiesSet");
		} catch (NoSuchMethodException e) {
			logger.error(e.getMessage(), e);
		} catch (SecurityException e) {
			logger.error(e.getMessage(), e);
		}
		try {
			if (null != m1) {
				m1.invoke(ards);
			}
		} catch (InvocationTargetException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void filterSlaveDataSource(Object ards) {
		Field[] fieldlist = ards.getClass().getSuperclass().getDeclaredFields();
		for (int i = 0; i < fieldlist.length; i++) {
			Field f = fieldlist[i];
			f.setAccessible(true);
			String name = f.getName();
			if (name.equals("targetDataSources")) {
				try {
					Map<Object, Object> targetDataSources = (Map<Object, Object>) f.get(ards);
					for (Map.Entry<Object, Object> entry : targetDataSources.entrySet()) {
						if (entry.getKey().toString().toLowerCase().contains(SLAVE)) {
							slaveTargetDataSources.put(entry.getKey().toString(), entry.getValue());
						}

					}
				} catch (IllegalArgumentException e) {
					logger.error(e.getMessage(), e);
				} catch (IllegalAccessException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

	private static String[] get_ip_port(String dburl) {
		dburl = dburl.trim();
		String[] split = dburl.split("jdbc:mysql://");
		String[] split2 = split[1].split("/");
		String[] split3 = split2[0].split(":");
		return split3;
	}

	private static String get_db_name(String dburl) {
		String[] split = dburl.split("3306/");
		String dbname = "";
		if (split[1].indexOf("?") != -1) {
			String[] split2 = split[1].split("\\?");
			dbname = split2[0];
		} else {
			dbname = split[1];
		}
		return dbname.trim();
	}

	// ///////////////////////////////////////////////
	private static int executeQuery(DataSource dataSource, String sql, Map<String, Object> map) {
		Pattern p = Pattern.compile("(#[^#]*#)");
		Matcher m = p.matcher(sql);
		List<String> result = new ArrayList<String>();
		while (m.find()) {
			String group = m.group();
			result.add(group.substring(1, group.length() - 1));
			sql = sql.replaceAll(group, "?");
		}
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
		} catch (SQLException e) {
			logger.error("SQLException for dataSource.getConnection()");
			// logger.error("SQLException for:" + e.getMessage(), e);
		}
		int count = 0;
		PreparedStatement stmt = null;
		try {
			if (null != conn) {
				stmt = conn.prepareStatement(sql);
				int size = result.size();
				for (int j = 0; j < size; j++) {
					Object object = map.get(result.get(j));
					if (null != object) {
						stmt.setString(j + 1, object.toString());
					} else {
						stmt.setString(j + 1, null);
					}
				}
				count = stmt.executeUpdate();
			}
		} catch (SQLException e) {
			// logger.error("JDBC executeQuery exception for=>" +
			// e.getMessage(), e);
		} finally {
			close(conn, stmt, null);
		}
		return count;
	}

	private List<Map<String, String>> queryAll(DataSource dataSource, String sql, Map<String, Object> map) {
		Pattern p = Pattern.compile("(#[^#]*#)");
		Matcher m = p.matcher(sql);
		List<String> result = new ArrayList<String>();
		while (m.find()) {
			String group = m.group();
			result.add(group.substring(1, group.length() - 1));
			sql = sql.replaceAll(group, "?");
		}

		List<String> colums = new ArrayList<String>();// getColumNames(sql);
		colums.add("num");

		Connection conn = null;
		try {
			conn = dataSource.getConnection();
		} catch (SQLException e) {
			if (printSqlException) {
				logger.error("SQLException for:" + e.getMessage(), e);
			} else {
				logger.error("queryAll SQLException for dataSource.getConnection()");
			}
		} catch (Exception e) {
			if (printSqlException) {
				logger.error("Exception for:" + e.getMessage(), e);
			} else {
				logger.error("queryAll SQLException for dataSource.getConnection()");
			}
		}
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			if (null != conn) {
				stmt = conn.prepareStatement(sql);
				int size = result.size();
				for (int j = 0; j < size; j++) {
					stmt.setString(j + 1, map.get(result.get(j)).toString());
				}
				rs = stmt.executeQuery();
				while (rs.next()) {
					Map<String, String> row = new HashMap<String, String>();
					for (String string : colums) {
						String value = rs.getString(string);
						row.put(string, value);
					}
					resultList.add(row);
				}
			}
		} catch (SQLException e) {
			if (printSqlException) {
				logger.error("JDBC queryAll SQLException for:" + e.getMessage(), e);
			} else {
				logger.error("queryAll SQLException for executeQuery");
			}
		} catch (Exception e) {
			if (printSqlException) {
				logger.error("JDBC queryAll Exception for:" + e.getMessage(), e);
			} else {
				logger.error("queryAll Exception for executeQuery");
			}
		} finally {
			close(conn, stmt, rs);
		}
		return resultList;
	}

	private static void close(Connection conn, PreparedStatement stmt, ResultSet rs) {
		try {
			if (null != rs) {
				rs.close();
			}
		} catch (SQLException e) {
			// igore
		}
		try {
			if (null != stmt) {
				stmt.close();
			}
		} catch (SQLException e) {
			// igore
		}
		try {
			if (null != conn) {
				conn.close();
			}
		} catch (SQLException e) {
			// igore
		}
	}

	private static String parseDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	private static List<String> getColumNames(String sql) {
		List<String> list = new ArrayList<String>();
		String columns = sql.substring(sql.indexOf("select ") + 7, sql.indexOf("from "));
		if (null != columns) {
			for (String str : columns.split(",")) {
				str = str.trim();
				if (str.contains(" as ")) {
					String[] split = str.split(" as ");
					list.add(split[1].trim());
				} else if (str.contains(" ")) {
					str = str.replaceAll(" +", " ");
					String[] split = str.split(" ");
					list.add(split[1].trim());
				} else {
					list.add(str.trim());
				}
			}
		}
		return list;
	}

	private boolean watchOneNode(DataSource dataSource, String url) {
		int failTimes = 0;
		int count = 5;
		for (int i = 0; i < count; i++) {
			List<Map<String, String>> queryAll = null;
			try {
				queryAll = queryAll(dataSource, SELECT_1_AS_NUM_SQL, new HashMap<String, Object>());
			} catch (Exception e) {
				failTimes++;
			}
			if (null == queryAll || queryAll.isEmpty()) {
				failTimes++;
			} else {
				// logger.info("############################# num is =>" +
				// queryAll.get(0).get("num") + " i is=>" + i);
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			}
		}
		if (failTimes == count) {
			logger.error("WATCH SLAVE MYSQL ==>" + url + " IS FALSE");
			return false;
		}
		return true;
	}

	public Set<Mysql> getMysqlSlaves() {
		return mysqlSlaves;
	}

	public void setMysqlSlaves(Set<Mysql> mysqlSlaves) {
		this.mysqlSlaves = mysqlSlaves;
	}

	public Map<String, AbstractRoutingDataSource> getDataSourceMap() {
		return dataSourceMap;
	}

	public void setDataSourceMap(Map<String, AbstractRoutingDataSource> dataSourceMap) {
		this.dataSourceMap = dataSourceMap;
	}

	public Map<String, ?> getDbcpSourceMap() {
		return dbcpSourceMap;
	}

	public void setDbcpSourceMap(Map<String, ?> dbcpSourceMap) {
		this.dbcpSourceMap = dbcpSourceMap;
	}

	public Map<String, Object> getSlaveTargetDataSources() {
		return slaveTargetDataSources;
	}

	public void setSlaveTargetDataSources(Map<String, Object> slaveTargetDataSources) {
		this.slaveTargetDataSources = slaveTargetDataSources;
	}

	public Map<String, Object> getRemoveList() {
		return removeList;
	}

	public void setRemoveList(Map<String, Object> removeList) {
		this.removeList = removeList;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	@Override
	public int getOrder() {
		return LOWEST_PRECEDENCE - 12;
	}

	public boolean isPrintSqlException() {
		return printSqlException;
	}

	public void setPrintSqlException(boolean printSqlException) {
		this.printSqlException = printSqlException;
	}

}
