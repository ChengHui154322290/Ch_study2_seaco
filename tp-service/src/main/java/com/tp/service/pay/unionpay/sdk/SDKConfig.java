/**
 *
 * Licensed Property to China UnionPay Co., Ltd.
 * 
 * (C) Copyright of China UnionPay Co., Ltd. 2010
 *     All Rights Reserved.
 *
 * 
 * Modification History:
 * =============================================================================
 *   Author         Date          Description
 *   ------------ ---------- ---------------------------------------------------
 *   xshu       2014-05-28       MPI基本参数工具类
 * =============================================================================
 */
package com.tp.service.pay.unionpay.sdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 软件开发工具包 配制
 * 
 * @author szy
 *
 */
public class SDKConfig {

	public static final String FILE_NAME = "acp_sdk.properties";
	
	private volatile boolean inited = false;

	/** 前台请求URL. */
	private String frontRequestUrl;
	/** APP请求URL. */
	private String appRequestUrl;
	/** 后台请求URL. */
	private String backRequestUrl;
	/** 单笔查询 */
	private String singleQueryUrl;
	/** 批量查询 */
	private String batchQueryUrl;
	/** 批量交易 */
	private String batchTransUrl;
	/** 文件传输 */
	private String fileTransUrl;
	/** 签名证书路径. */
	private String signCertPath;
	private String appSignCertPath;
	/** 签名证书密码. */
	private String signCertPwd;
	private String appSignCertPwd;
	/** 签名证书类型. */
	private String signCertType;
	private String appSignCertType;
	/** 加密公钥证书路径. */
	private String encryptCertPath;
	private String appEncryptCertPath;
	/** 验证签名公钥证书路径. */
	private String validateCertPath;
	/** 验证签名公钥证书目录. */
	private String validateCertDir;
	/** 按照商户代码读取指定签名证书目录. */
	private String signCertDir;

	/** 跨行收单开通并支付交易. */
	private String cbFrontRequestUrl;
	/** 跨行收单支付. */
	private String cbBackRequestUrl;

	/** 配置文件中的前台URL常量. */
	public static final String MPI_FRONT_URL = "acpsdk.frontTransUrl";
	/** 配置文件中的APP-URL常量. */
	public static final String MPI_APP_URL = "acpsdk.appTransUrl";
	/** 配置文件中的后台URL常量. */
	public static final String MPI_BACK_URL = "acpsdk.backTransUrl";
	/** 配置文件中的单笔交易查询URL常量. */
	public static final String MPI_SIGNQ_URL = "acpsdk.singleQueryUrl";
	/** 配置文件中的批量交易查询URL常量. */
	public static final String MPI_BATQ_URL = "acpsdk.batchQueryUrl";
	/** 配置文件中的批量交易URL常量. */
	public static final String MPI_BATTRANS_URL = "acpsdk.batchTransUrl";
	/** 配置文件中的文件类交易URL常量. */
	public static final String MPI_FILETRANS_URL = "acpsdk.fileTransUrl";

	/** 配置文件中的文件类交易URL常量. */
	public static final String MPI_CB_FRONT_URL = "acpsdk.cbFrontTransUrl";
	/** 配置文件中的文件类交易URL常量. */
	public static final String MPI_CB_BACK_URL = "acpsdk.cbBackTransUrl";

	/** 配置文件中签名证书路径常量. */
	public static final String MPI_SIGNCERT_PATH = "acpsdk.signCert.path";
	public static final String MPI_APPSIGNCERT_PATH = "acpsdk.appSignCert.path";
	/** 配置文件中签名证书密码常量. */
	public static final String MPI_SIGNCERT_PWD = "acpsdk.signCert.pwd";
	public static final String MPI_APPSIGNCERT_PWD = "acpsdk.appSignCert.pwd";
	/** 配置文件中签名证书类型常量. */
	public static final String MPI_SIGNCERT_TYPE = "acpsdk.signCert.type";
	public static final String MPI_APPSIGNCERT_TYPE = "acpsdk.appSignCert.type";
	/** 配置文件中密码加密证书路径常量. */
	public static final String MPI_ENCRYPTCERT_PATH = "acpsdk.encryptCert.path";
	public static final String MPI_APPENCRYPTCERT_PATH = "acpsdk.appEncryptCert.path";
	/** 配置文件中验证签名证书路径常量. */
	public static final String MPI_VALIDATECERT_PATH = "acpsdk.validateCert.path";
	/** 配置文件中验证签名证书目录常量. */
	public static final String MPI_VALIDATECERT_DIR = "acpsdk.validateCert.dir";
	/** 配置文件中是否加密cvn2常量. */
	public static final String MPI_CVN_ENC = "acpsdk.cvn2.enc";
	/** 配置文件中是否加密cvn2有效期常量. */
	public static final String MPI_DATE_ENC = "acpsdk.date.enc";
	/** 配置文件中是否加密卡号常量. */
	public static final String MPI_PAN_ENC = "acpsdk.pan.enc";
	/** 配置文件中按商户代码读取指定签名证书路径常量. */
	public static final String MPI_SIGNCERT_DIR = "acpsdk.signCert.dir";
	/** 操作对象. */
	private static SDKConfig config;
	/** 属性文件对象. */
	private Properties properties;

	/**
	 * 默认构造函数
	 */
	private SDKConfig() {
		super();
	}

	/**
	 * 获取config对象.
	 * 
	 * @return
	 */
	public static SDKConfig getConfig() {
		if (null == config) {
			config = new SDKConfig();
		}
		return config;
	}

	/**
	 * 从properties文件加载
	 * 
	 * @param rootPath
	 *            不包含文件名的目录.
	 */
	public void loadPropertiesFromPath(String rootPath) {
		if (inited) {
			System.out.println("#####################已经初始化了！！");
			return ;
		}
		
		File file = new File(rootPath + File.separator + FILE_NAME);
		InputStream in = null;
		if (file.exists()) {
			try {
				in = new FileInputStream(file);
				properties = new Properties();
				properties.load(in);
				loadProperties(properties);
				inited=true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (null != in) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			// 由于此时可能还没有完成LOG的加载，因此采用标准输出来打印日志信息
			System.out.println(rootPath + FILE_NAME + "不存在,加载参数失败");
		}
	}

	/**
	 * 从classpath路径下加载配置参数
	 */
	public void loadPropertiesFromSrc() {
		if (inited) {
			System.out.println("#####################已经初始化了！！");
			return ;
		}
		
		InputStream in = null;
		try {
			// Properties pro = null;
			in = SDKConfig.class.getClassLoader().getResourceAsStream(FILE_NAME);
			if (null != in) {
				properties = new Properties();
				try {
					properties.load(in);
				} catch (IOException e) {
					throw e;
				}
			}
			loadProperties(properties);
			inited=true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 根据传入的 {@link #load(java.util.Properties)}对象设置配置参数
	 * 
	 * @param pro
	 */
	public void loadProperties(Properties pro) {
		if (inited) {
			System.out.println("#####################已经初始化了！！");
			return ;
		}
		System.out.println("======================准备初始化....");
		inited=true;
		
		String value = pro.getProperty(MPI_FRONT_URL);
		if (!SDKUtil.isEmpty(value)) {
			this.frontRequestUrl = value.trim();
		}
		value = pro.getProperty(MPI_APP_URL);
		if (!SDKUtil.isEmpty(value)) {
			this.appRequestUrl = value.trim();
		}
		value = pro.getProperty(MPI_BACK_URL);
		if (!SDKUtil.isEmpty(value)) {
			this.backRequestUrl = value.trim();
		}
		value = pro.getProperty(MPI_SIGNCERT_PATH);
		if (!SDKUtil.isEmpty(value)) {
			this.signCertPath = value.trim();
		}
		value = pro.getProperty(MPI_APPSIGNCERT_PATH);
		if (!SDKUtil.isEmpty(value)) {
			this.appSignCertPath = value.trim();
		}
		value = pro.getProperty(MPI_SIGNCERT_PWD);
		if (!SDKUtil.isEmpty(value)) {
			this.signCertPwd = value.trim();
		}
		value = pro.getProperty(MPI_APPSIGNCERT_PWD);
		if (!SDKUtil.isEmpty(value)) {
			this.appSignCertPwd = value.trim();
		}
		value = pro.getProperty(MPI_SIGNCERT_TYPE);
		if (!SDKUtil.isEmpty(value)) {
			this.signCertType = value.trim();
		}
		value = pro.getProperty(MPI_APPSIGNCERT_TYPE);
		if (!SDKUtil.isEmpty(value)) {
			this.appSignCertType = value.trim();
		}
		value = pro.getProperty(MPI_ENCRYPTCERT_PATH);
		if (!SDKUtil.isEmpty(value)) {
			this.encryptCertPath = value.trim();
		}
		value = pro.getProperty(MPI_APPENCRYPTCERT_PATH);
		if (!SDKUtil.isEmpty(value)) {
			this.appEncryptCertPath = value.trim();
		}
		value = pro.getProperty(MPI_VALIDATECERT_PATH);
		if (!SDKUtil.isEmpty(value)) {
			this.validateCertPath = value.trim();
		}
		value = pro.getProperty(MPI_VALIDATECERT_DIR);
		if (!SDKUtil.isEmpty(value)) {
			this.validateCertDir = value.trim();
		}
		value = pro.getProperty(MPI_BATQ_URL);
		if (!SDKUtil.isEmpty(value)) {
			this.batchQueryUrl = value.trim();
		}
		value = pro.getProperty(MPI_BATTRANS_URL);
		if (!SDKUtil.isEmpty(value)) {
			this.batchTransUrl = value.trim();
		}
		value = pro.getProperty(MPI_FILETRANS_URL);
		if (!SDKUtil.isEmpty(value)) {
			this.fileTransUrl = value.trim();
		}
		value = pro.getProperty(MPI_SIGNQ_URL);
		if (!SDKUtil.isEmpty(value)) {
			this.singleQueryUrl = value.trim();
		}
		value = pro.getProperty(MPI_SIGNCERT_DIR);
		if (!SDKUtil.isEmpty(value)) {
			this.signCertDir = value.trim();
		}

		value = pro.getProperty(MPI_CB_FRONT_URL);
		if (!SDKUtil.isEmpty(value)) {
			this.cbFrontRequestUrl = value.trim();
		}
		value = pro.getProperty(MPI_CB_BACK_URL);
		if (!SDKUtil.isEmpty(value)) {
			this.cbBackRequestUrl = value.trim();
		}
	}

	public String getFrontRequestUrl() {
		return frontRequestUrl;
	}

	public void setFrontRequestUrl(String frontRequestUrl) {
		this.frontRequestUrl = frontRequestUrl;
	}

	public String getBackRequestUrl() {
		return backRequestUrl;
	}

	public void setBackRequestUrl(String backRequestUrl) {
		this.backRequestUrl = backRequestUrl;
	}

	public String getSignCertPath() {
		return signCertPath;
	}

	public void setSignCertPath(String signCertPath) {
		this.signCertPath = signCertPath;
	}

	public String getSignCertPwd() {
		return signCertPwd;
	}

	public void setSignCertPwd(String signCertPwd) {
		this.signCertPwd = signCertPwd;
	}

	public String getSignCertType() {
		return signCertType;
	}

	public void setSignCertType(String signCertType) {
		this.signCertType = signCertType;
	}

	public String getEncryptCertPath() {
		return encryptCertPath;
	}

	public void setEncryptCertPath(String encryptCertPath) {
		this.encryptCertPath = encryptCertPath;
	}

	public String getAppSignCertPath() {
		return appSignCertPath;
	}

	public void setAppSignCertPath(String appSignCertPath) {
		this.appSignCertPath = appSignCertPath;
	}

	public String getAppSignCertPwd() {
		return appSignCertPwd;
	}

	public void setAppSignCertPwd(String appSignCertPwd) {
		this.appSignCertPwd = appSignCertPwd;
	}

	public String getAppSignCertType() {
		return appSignCertType;
	}

	public void setAppSignCertType(String appSignCertType) {
		this.appSignCertType = appSignCertType;
	}

	public String getAppEncryptCertPath() {
		return appEncryptCertPath;
	}

	public void setAppEncryptCertPath(String appEncryptCertPath) {
		this.appEncryptCertPath = appEncryptCertPath;
	}

	public String getValidateCertPath() {
		return validateCertPath;
	}

	public void setValidateCertPath(String validateCertPath) {
		this.validateCertPath = validateCertPath;
	}

	public String getValidateCertDir() {
		return validateCertDir;
	}

	public void setValidateCertDir(String validateCertDir) {
		this.validateCertDir = validateCertDir;
	}

	public String getSingleQueryUrl() {
		return singleQueryUrl;
	}

	public void setSingleQueryUrl(String singleQueryUrl) {
		this.singleQueryUrl = singleQueryUrl;
	}

	public String getBatchQueryUrl() {
		return batchQueryUrl;
	}

	public void setBatchQueryUrl(String batchQueryUrl) {
		this.batchQueryUrl = batchQueryUrl;
	}

	public String getBatchTransUrl() {
		return batchTransUrl;
	}

	public void setBatchTransUrl(String batchTransUrl) {
		this.batchTransUrl = batchTransUrl;
	}

	public String getFileTransUrl() {
		return fileTransUrl;
	}

	public void setFileTransUrl(String fileTransUrl) {
		this.fileTransUrl = fileTransUrl;
	}

	public String getSignCertDir() {
		return signCertDir;
	}

	public void setSignCertDir(String signCertDir) {
		this.signCertDir = signCertDir;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public String getCbFrontRequestUrl() {
		return cbFrontRequestUrl;
	}

	public void setCbFrontRequestUrl(String cbFrontRequestUrl) {
		this.cbFrontRequestUrl = cbFrontRequestUrl;
	}

	public String getCbBackRequestUrl() {
		return cbBackRequestUrl;
	}

	public void setCbBackRequestUrl(String cbBackRequestUrl) {
		this.cbBackRequestUrl = cbBackRequestUrl;
	}

	public String getAppRequestUrl() {
		return this.appRequestUrl;
	}

	public void setAppRequestUrl(String appRequestUrl) {
		this.appRequestUrl = appRequestUrl;
	}
}
