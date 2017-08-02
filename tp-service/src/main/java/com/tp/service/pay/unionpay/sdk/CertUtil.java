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
 *   xshu       2014-05-28       证书工具类.
 * =============================================================================
 */
package com.tp.service.pay.unionpay.sdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class CertUtil {
	/** 证书容器. */
	private static KeyStore keyStore = null;
	private static KeyStore appKeyStore = null;
	/** 密码加密证书 */
	private static X509Certificate encryptCert = null;
	private static X509Certificate appEncryptCert = null;
	/** 验证签名证书. */
	private static X509Certificate validateCert = null;
	private static X509Certificate appValidateCert = null;
	/** 签名证书存储Map. */
	private static Map<String, X509Certificate> certMap = new HashMap<String, X509Certificate>();
	/** 根据传入证书文件路径和密码读取指定的证书容器. */
	private static KeyStore certKeyStore = null;
	private static KeyStore appCertKeyStore = null;

	/**
	 * 初始化证书容器的方法
	 */
	static {
		init();
	}

	/**
	 * 初始化所有证书.
	 */
	public static void init() {
		initSignCert();
		initEncryptCert();
		// initValidateCert();
		initValidateCertFromDir();
	}

	/**
	 * 加载签名证书
	 */
	public static void initSignCert() {
		LogUtil.writeLog("加载签名证书开始");
		if (null != keyStore) {
			keyStore = null;
		}
		if (null != appKeyStore) {
			appKeyStore = null;
		}
		SDKConfig config = SDKConfig.getConfig();
		keyStore = getKeyInfo(config.getSignCertPath(), config.getSignCertPwd(), config.getSignCertType());
		LogUtil.writeLog("[" + config.getSignCertPath() + "][serialNumber=" + getSignCertId(false) + "]");
		appKeyStore = getKeyInfo(config.getAppSignCertPath(), config.getAppSignCertPwd(), config.getAppSignCertType());
		// 打印证书加载信息,供测试阶段调试
		LogUtil.writeLog("[" + config.getAppSignCertPath() + "][serialNumber=" + getSignCertId(true) + "]");
		LogUtil.writeLog("加载签名证书结束");
	}

	/**
	 * 根据传入的证书文件路径和证书密码加载指定的签名证书
	 */
	public static void initSignCert(String certFilePath, String certPwd, boolean forApp) {

		LogUtil.writeLog("加载证书文件[" + certFilePath + "]和证书密码[" + certPwd + "]的签名证书开始.");
		File files = new File(certFilePath);
		if (!files.exists()) {
			LogUtil.writeLog("证书文件不存在,初始化签名证书失败.");
			return;
		}
		
		KeyStore ks = getKeyInfo(certFilePath, certPwd, "PKCS12");
		if (forApp) {
			certKeyStore=ks;
		} else {
			appCertKeyStore=ks;
		}
		LogUtil.writeLog("加载证书文件[" + certFilePath + "]和证书密码[" + certPwd + "]的签名证书结束.");
	}

	/**
	 * 加载密码加密证书
	 */
	public static void initEncryptCert() {
		LogUtil.writeLog("加载密码加密证书开始");
		String path = SDKConfig.getConfig().getEncryptCertPath();
		if (null == path || "".equals(path)) {
			LogUtil.writeLog("pc加载密码加密证书路径是空");
			return;
		}
		String appPath = SDKConfig.getConfig().getAppEncryptCertPath();
		if (null == appPath || "".equals(appPath)) {
			LogUtil.writeLog("app加载密码加密证书路径是空");
			return;
		}
		CertificateFactory cf = null;
		FileInputStream in = null;
		try {
			cf = CertificateFactory.getInstance("X.509");
			in = new FileInputStream(path);
			encryptCert = (X509Certificate) cf.generateCertificate(in);
			// 打印证书加载信息,供测试阶段调试
			LogUtil.writeLog("[" + path + "][serialNumber=" + getEncryptCertId(false) + "]");
		} catch (CertificateException e) {
			LogUtil.writeErrorLog("pc加密证书加载失败", e);
		} catch (FileNotFoundException e) {
			LogUtil.writeErrorLog("pc加密证书加载失败,文件不存在", e);
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					LogUtil.writeErrorLog(e.toString());
				}
			}
		}
		LogUtil.writeLog("加载pc密码加密证书结束");
		
		try {
			cf = CertificateFactory.getInstance("X.509");
			in = new FileInputStream(appPath);
			appEncryptCert = (X509Certificate) cf.generateCertificate(in);
			// 打印证书加载信息,供测试阶段调试
			LogUtil.writeLog("[" + appPath + "][serialNumber=" + getEncryptCertId(true) + "]");
		} catch (CertificateException e) {
			LogUtil.writeErrorLog("app加密证书加载失败", e);
		} catch (FileNotFoundException e) {
			LogUtil.writeErrorLog("app加密证书加载失败,文件不存在", e);
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					LogUtil.writeErrorLog(e.toString());
				}
			}
		}
		LogUtil.writeLog("加载app密码加密证书结束");
	}

//	/**
//	 * 加载验证签名证书
//	 * 
//	 * @deprecated
//	 */
//	public static void initValidateCert() {
//		LogUtil.writeLog("加载验证签名证书");
//		String path = SDKConfig.getConfig().getValidateCertPath();
//		if (null == path || "".equals(path)) {
//			LogUtil.writeLog("验证签名证书路径为空");
//			return;
//		}
//		CertificateFactory cf = null;
//		FileInputStream in = null;
//		try {
//			cf = CertificateFactory.getInstance("X.509");
//			in = new FileInputStream(SDKConfig.getConfig().getValidateCertPath());
//			validateCert = (X509Certificate) cf.generateCertificate(in);
//		} catch (CertificateException e) {
//			LogUtil.writeErrorLog("验证签名证书加载失败", e);
//		} catch (FileNotFoundException e) {
//			LogUtil.writeErrorLog("验证签名证书加载失败,证书文件不存在", e);
//		} finally {
//			if (null != in) {
//				try {
//					in.close();
//				} catch (IOException e) {
//					LogUtil.writeErrorLog(e.toString());
//				}
//			}
//		}
//		LogUtil.writeLog("加载验证签名证书结束 ");
//	}

	/**
	 * 从指定目录下加载验证签名证书
	 * 
	 */
	public static void initValidateCertFromDir() {
		LogUtil.writeLog("从目录中加载验证签名证书开始.");
		certMap.clear();
		String filePath = SDKConfig.getConfig().getValidateCertDir();
		if (null == filePath || "".equals(filePath)) {
			LogUtil.writeLog("验证签名证书路径配置为空.");
			return;
		}

		CertificateFactory cf = null;
		FileInputStream in = null;

		try {
			cf = CertificateFactory.getInstance("X.509");
			File fileDir = new File(filePath);
			File[] files = fileDir.listFiles(new CerFilter());
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				in = new FileInputStream(file.getAbsolutePath());
				validateCert = (X509Certificate) cf.generateCertificate(in);
				certMap.put(validateCert.getSerialNumber().toString(), validateCert);
				// 打印证书加载信息,供测试阶段调试
				LogUtil.writeLog("[" + file.getAbsolutePath() + "][serialNumber=" + validateCert.getSerialNumber().toString() + "]");
			}
		} catch (CertificateException e) {
			LogUtil.writeErrorLog("验证签名证书加载失败", e);
		} catch (FileNotFoundException e) {
			LogUtil.writeErrorLog("验证签名证书加载失败,证书文件不存在", e);
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					LogUtil.writeErrorLog(e.toString());
				}
			}
		}
		LogUtil.writeLog("从目录中加载验证签名证书结束.");
	}

	/**
	 * 获取签名证书私钥
	 * 
	 * @return
	 */
	public static PrivateKey getSignCertPrivateKey(boolean forApp) {
		try {
			KeyStore ks = forApp?appKeyStore:keyStore;
			Enumeration<String> aliasenum = ks.aliases();
			String keyAlias = null;
			if (aliasenum.hasMoreElements()) {
				keyAlias = aliasenum.nextElement();
			}
			SDKConfig config = SDKConfig.getConfig();
			String signCertPwd = forApp?config.getAppSignCertPwd():config.getSignCertPwd();
			PrivateKey privateKey = (PrivateKey) ks.getKey(keyAlias, signCertPwd.toCharArray());
			return privateKey;
		} catch (Exception e) {
			LogUtil.writeErrorLog("获取签名证书的私钥失败", e);
			return null;
		}
	}

	/**
	 * 通过传入证书绝对路径和证书密码获取所对应的签名证书私钥
	 * 
	 * @param certPath
	 *            证书绝对路径
	 * @param certPwd
	 *            证书密码
	 * @return 证书私钥
	 */
	public static PrivateKey getSignCertPrivateKey(String certPath, String certPwd) {
		//FIXME 或需添加app私钥
		// 初始化指定certPath和certPwd的签名证书容器
		initSignCert(certPath, certPwd, false);
		try {
			Enumeration<String> aliasenum = certKeyStore.aliases();
			String keyAlias = null;
			if (aliasenum.hasMoreElements()) {
				keyAlias = aliasenum.nextElement();
			}
			PrivateKey privateKey = (PrivateKey) certKeyStore.getKey(keyAlias, certPwd.toCharArray());
			return privateKey;
		} catch (Exception e) {
			LogUtil.writeErrorLog("获取[" + certPath + "]的签名证书的私钥失败", e);
			return null;
		}
	}

	/**
	 * 获取加密证书公钥.密码加密时需要
	 * 
	 * @return
	 */
	public static PublicKey getEncryptCertPublicKey(boolean forApp) {
		try {
			if (forApp) {
				if (null == appEncryptCert) {
					initEncryptCert();
				}
				return appEncryptCert.getPublicKey();
			} else {

				if (null == encryptCert) {
					initEncryptCert();
				}
				return encryptCert.getPublicKey();
			}
		} catch (Exception e) {
			LogUtil.writeErrorLog("获取加密证书失败", e);
			return null;
		}
	}

	/**
	 * 验证签名证书
	 * 
	 * @return 验证签名证书的公钥
	 */
	public static PublicKey getValidateKey(boolean forApp) {
		try {
			// Enumeration<String> aliasenum = keyStore.aliases();
			// String keyAlias = null;
			// if (aliasenum.hasMoreElements()) {
			// keyAlias = (String) aliasenum.nextElement();
			// }
			// X509Certificate cert = (X509Certificate)
			// keyStore.getCertificate(keyAlias);
			if (forApp) {
				if (null == validateCert) {
					return null;
				}
				return validateCert.getPublicKey();
			} else {

				if (null == appValidateCert) {
					return null;
				}
				return appValidateCert.getPublicKey();
			}
		} catch (Exception e) {
			LogUtil.writeErrorLog("获取验证签名证书失败", e);
			return null;
		}
	}

	// add by changming.he 此处新增一个带certId参数的方法
	/**
	 * 通过certId获取证书Map中对应证书的公钥
	 * 
	 * @param certId
	 *            证书物理序号
	 * @return 通过证书编号获取到的公钥
	 */
	public static PublicKey getValidateKey(String certId) {
		X509Certificate cf = null;
		if (certMap.containsKey(certId)) {
			// 存在certId对应的证书对象
			cf = certMap.get(certId);
			return cf.getPublicKey();
		} else {
			// 不存在则重新Load证书文件目录
			initValidateCertFromDir();
			if (certMap.containsKey(certId)) {
				// 存在certId对应的证书对象
				cf = certMap.get(certId);
				return cf.getPublicKey();
			} else {
				LogUtil.writeErrorLog("没有certId=[" + certId + "]对应的验签证书文件,返回NULL.");
				return null;
			}
		}
	}

	/**
	 * 获取签名证书中的证书序列号
	 * 
	 * @return 证书的物理编号
	 */
	public static String getSignCertId(boolean forApp) {
		KeyStore ks = null;
		try {
			ks = forApp?appKeyStore:keyStore;
			Enumeration<String> aliasenum = ks.aliases();
			String keyAlias = null;
			if (aliasenum.hasMoreElements()) {
				keyAlias = aliasenum.nextElement();
			}
			X509Certificate cert = (X509Certificate) ks.getCertificate(keyAlias);
			return cert.getSerialNumber().toString();
		} catch (Exception e) {
			LogUtil.writeErrorLog("获取签名证书的序列号失败", e);
			if (null == ks) {
				LogUtil.writeErrorLog("keyStore实例化失败,当前为NULL");
			}
			return "";
		}
	}

	/**
	 * 获取加密证书的证书序列号
	 * 
	 * @return
	 */
	public static String getEncryptCertId(boolean forApp) {
		if (forApp) {
			
			if (null == appEncryptCert) {
				initEncryptCert();
			}
			return appEncryptCert.getSerialNumber().toString();
		} else {
			
			if (null == encryptCert) {
				initEncryptCert();
			}
			return encryptCert.getSerialNumber().toString();
		}
	}

	/**
	 * 获取签名证书公钥对象
	 * 
	 * @return
	 */
	public static PublicKey getSignPublicKey(boolean forApp) {
		try {
			KeyStore ks = forApp?appKeyStore:keyStore;
			Enumeration<String> aliasenum = ks.aliases();
			String keyAlias = null;
			if (aliasenum.hasMoreElements()) // we are readin just one
			// certificate.
			{
				keyAlias = (String) aliasenum.nextElement();
			}

			Certificate cert = ks.getCertificate(keyAlias);
			PublicKey pubkey = cert.getPublicKey();
			return pubkey;
		} catch (Exception e) {
			LogUtil.writeErrorLog(e.toString());
			return null;
		}
	}

	/**
	 * 将证书文件读取为证书存储对象
	 * 
	 * @param pfxkeyfile
	 *            证书文件名
	 * @param keypwd
	 *            证书密码
	 * @param type
	 *            证书类型
	 * @return 证书对象
	 */
	public static KeyStore getKeyInfo(String pfxkeyfile, String keypwd, String type) {
		try {
			LogUtil.writeLog("KeyStore Loading Start...");
			KeyStore ks = null;
			if ("JKS".equals(type)) {
				ks = KeyStore.getInstance(type);
			} else if ("PKCS12".equals(type)) {
				Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
				// ks = KeyStore.getInstance(type, "BC");
				ks = KeyStore.getInstance(type);
			}
			LogUtil.writeLog("传入的私钥证书路径为=>[" + pfxkeyfile + "],密码=[" + keypwd + "]");
			FileInputStream fis = new FileInputStream(pfxkeyfile);
			char[] nPassword = null;
			nPassword = null == keypwd || "".equals(keypwd.trim()) ? null : keypwd.toCharArray();
			if (null != ks) {
				ks.load(fis, nPassword);
			}
			fis.close();
			LogUtil.writeLog("KeyStore Loading End...");
			return ks;
		} catch (Exception e) {
			if (Security.getProvider("BC") == null) {
				LogUtil.writeLog("BC Provider not installed.");
			}
			LogUtil.writeErrorLog("读取私钥证书失败", e);
			if ((e instanceof KeyStoreException) && "PKCS12".equals(type)) {
				Security.removeProvider("BC");
			}
			return null;
		}
	}

	// 打印系统环境信息
	public static void printSysInfo() {
		System.out.println("======================= SYS INFO begin===========================");
		System.out.println("java_vendor:" + System.getProperty("java.vendor"));
		System.out.println("java_vendor_url:" + System.getProperty("java.vendor.url"));
		System.out.println("java_home:" + System.getProperty("java.home"));
		System.out.println("java_class_version:" + System.getProperty("java.class.version"));
		System.out.println("java_class_path:" + System.getProperty("java.class.path"));
		System.out.println("os_name:" + System.getProperty("os.name"));
		System.out.println("os_arch:" + System.getProperty("os.arch"));
		System.out.println("os_version:" + System.getProperty("os.version"));
		System.out.println("user_name:" + System.getProperty("user.name"));
		System.out.println("user_home:" + System.getProperty("user.home"));
		System.out.println("user_dir:" + System.getProperty("user.dir"));
		System.out.println("java_vm_specification_version:" + System.getProperty("java.vm.specification.version"));
		System.out.println("java_vm_specification_vendor:" + System.getProperty("java.vm.specification.vendor"));
		System.out.println("java_vm_specification_name:" + System.getProperty("java.vm.specification.name"));
		System.out.println("java_vm_version:" + System.getProperty("java.vm.version"));
		System.out.println("java_vm_vendor:" + System.getProperty("java.vm.vendor"));
		System.out.println("java_vm_name:" + System.getProperty("java.vm.name"));
		System.out.println("java_ext_dirs:" + System.getProperty("java.ext.dirs"));
		System.out.println("file_separator:" + System.getProperty("file.separator"));
		System.out.println("path_separator:" + System.getProperty("path.separator"));
		System.out.println("line_separator:" + System.getProperty("line.separator"));
		System.out.println("======================= SYS INFO end===========================");

	}

	/**
	 * 证书文件过滤器
	 * 
	 */
	static class CerFilter implements FilenameFilter {

		public boolean isCer(String name) {
			if (name.toLowerCase().endsWith(".cer")) {
				return true;
			} else {
				return false;
			}
		}

		public boolean accept(File dir, String name) {
			return isCer(name);
		}
	}

	/**
	 * 通过传入私钥证书路径，获取证书ID
	 * 
	 * @param path
	 * @param pwd
	 * @param certTp
	 * @return
	 */
	public static String getCertIdByCertPath(String path, String pwd, String certTp) {
		KeyStore ks = getKeyInfo(path, pwd, certTp);
		if (null == ks) {
			return "";
		}
		try {
			Enumeration<String> aliasenum = ks.aliases();
			String keyAlias = null;
			if (aliasenum.hasMoreElements()) {
				keyAlias = aliasenum.nextElement();
			}
			X509Certificate cert = (X509Certificate) ks.getCertificate(keyAlias);
			return cert.getSerialNumber().toString();
		} catch (Exception e) {
			LogUtil.writeErrorLog("获取签名证书的序列号失败", e);
			return "";
		}
	}

	/**
	 * 获取证书容器
	 * 
	 * @return
	 */
	public static Map<String, X509Certificate> getCertMap() {
		return certMap;
	}

	/**
	 * 设置证书容器
	 * 
	 * @param certMap
	 */
	public static void setCertMap(Map<String, X509Certificate> certMap) {
		CertUtil.certMap = certMap;
	}

}
