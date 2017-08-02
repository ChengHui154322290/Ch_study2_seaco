package com.tp.service.cms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import freemarker.cache.MruCacheStorage;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * FreeMarker的工具类
 * 
 * @author szy
 * @version 0.0.1
 * 
 */
public class FreeMarkerUtils
{
	private static final Logger logger = LogManager.getLogger(FreeMarkerUtils.class);
	/** 模板根路径 */
	private static String templeteRootPath = null;
	/** 生成静态文件根路径 */
	private static String outputRootPath = "";
	/** 编码格式 UTF-8 */
	private static final String ENCODING = "UTF-8";
	/** 路径分割符 */
	public static final String PATH_SEPARATOR = "/";

	/**
	 * 基于文件系统来加载模板 tempPath,模板路径，如：E:/templates/,/home/user/template 如果该参数为空则从配置文件中读取
	 */
	public static Configuration getDirectoryFMCFG(String tempPath)
	{
		/*Configuration cfg = new Configuration();
		try
		{
			if (StringUtils.isEmpty(tempPath))
			{
				templeteRootPath = "";//ConfigurationUtil.getInstance().getFreemarkConfig(SysConstants.FREEMARK_CONFIG_TEMPLETE_ROOT_PATH);
			}
			else
			{
				templeteRootPath = tempPath;
			}
			// 模板文件存放路径
			File file = new File(new StringBuffer(templeteRootPath).append(File.separator).toString());
			// 设置要解析的模板所在的目录，并加载模板文件
			cfg.setDirectoryForTemplateLoading(file);
			cfg.setObjectWrapper(new DefaultObjectWrapper());
			cfg.setDefaultEncoding(ENCODING);
			cfg.setOutputEncoding(ENCODING);
			cfg.setEncoding(Locale.getDefault(), ENCODING);
			// 设置文件编码为UTF-8
			// cfg.setEncoding(Locale.getDefault(), ENCODING);
			logger.info("初始化FreeMarker配置成功！" + "模板文件目录：" + templeteRootPath);
			return cfg;
		}
		catch (IOException e)
		{
			logger.error("初始化FreeMarker配置出错" + templeteRootPath, e);
			return null;
		}*/
		
		Configuration cfg = new Configuration();  
        //指定模板如何查看数据模型  
        cfg.setObjectWrapper(new DefaultObjectWrapper());  
        
        cfg.setClassForTemplateLoading(FreeMarkerUtils.class,tempPath); 
        
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		cfg.setDefaultEncoding(ENCODING);
		cfg.setOutputEncoding(ENCODING);
		cfg.setEncoding(Locale.getDefault(), ENCODING);
        //设置缓存 字符编码 等.  
        cfg.setCacheStorage(new MruCacheStorage(20, 260));  
        return cfg;

	}

	/**
	 * 基于file来加载模板,模板路径，如：E:/templates/
	 * @throws IOException 
	 */
	public static Configuration getFileDirectoryFMCFG(File file) throws IOException
	{
		Configuration cfg = new Configuration();  
        //指定模板如何查看数据模型  
        cfg.setObjectWrapper(new DefaultObjectWrapper());  
        cfg.setDirectoryForTemplateLoading(file); 
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		cfg.setDefaultEncoding(ENCODING);
		cfg.setOutputEncoding(ENCODING);
		cfg.setEncoding(Locale.getDefault(), ENCODING);
        //设置缓存 字符编码 等.  
        cfg.setCacheStorage(new MruCacheStorage(20, 260));  
        return cfg;

	}
	
	/**
	 * 基于webroot加载模板 如：setServletContextForTemplateLoading(context, "/ftl")就是 /WebRoot/ftl目录。
	 */
	/*public static Configuration getServletContextFMCFG(ServletContext context, String tempPath)
	{

		Configuration cfg = new Configuration();
		try
		{
			// 设置要解析的模板所在的目录，并加载模板文件
			cfg.setServletContextForTemplateLoading(context, tempPath);
			// 设置文件编码为UTF-8
			cfg.setEncoding(Locale.getDefault(), ENCODING);
			logger.info("初始化FreeMarker配置成功！" + "web路径：" + context.getContextPath() + "模板目录：" + tempPath);
			return cfg;
		}
		catch (Exception e)
		{
			logger.error("初始化FreeMarker配置出错" + "web路径：" + context.getContextPath() + "模板目录：" + tempPath, e);
			return null;
		}
	}*/

	/**
	 * 根据类路径加载freemark模板配置， 即模板文件放到类路径下，如：${projectPath}/org/foo/util/template 注：pathPrefix参数需要以“/”开头
	 */
	public static Configuration getClassPathFMCFG(Class clazz, String pathPrefix)
	{
		Configuration cfg = new Configuration();
		try
		{
			cfg.setClassForTemplateLoading(clazz, pathPrefix);
			// 设置文件编码为UTF-8
			cfg.setEncoding(Locale.getDefault(), ENCODING);
			logger.info("初始化FreeMarker配置成功！" + "类路径：" + clazz + "目录：" + pathPrefix);
			return cfg;
		}
		catch (Exception e)
		{
			logger.error("初始化FreeMarker配置出错" + "类路径：" + clazz + "目录：" + pathPrefix, e);
			return null;
		}
	}

	/**
	 * 生成静态html文件
	 * 
	 * @param cfg freemarker配置文件
	 * @param data 用来初始数据的结果集
	 * @param templateFileName， ftl模版路径
	 * @param outFileName，静态文件名称(可带路径)，如：shtml/test.shtml
	 * @param isAbsPath 是否绝对路径
	 */
	public static void generateSHtmlFile(Configuration cfg, Map<String, Object> data, String templateFileName, String outFileName)
	{
		Writer out = null;
		try
		{
			logger.info("开始生成静态文件" + templateFileName + "生成文件名称" + outFileName);
			if (cfg == null)
			{
				new Throwable("freemarker初始化异常");
			}
			// 初始化模板跟
			// initFreeMarker(null);
			// 获取模板,并设置编码方式，这个编码必须要与页面中的编码格式一致
			Template template = cfg.getTemplate(templateFileName, ENCODING);
			template.setEncoding(ENCODING);
			template.setOutputEncoding(ENCODING);
			template.setLocale(Locale.CHINA);
			// 相对路径则使用默认的appPath加上输入的文件路径
			outFileName = new StringBuffer(outputRootPath).append(File.separator).append(outFileName).toString();

			String outPath = outFileName.substring(0, outFileName.lastIndexOf(PATH_SEPARATOR));
			// 创建文件目录
			FileUtils.forceMkdir(new File(outPath));
			File outFile = new File(outFileName);
			out = new OutputStreamWriter(new FileOutputStream(outFile), "GBK");

			// 处理模版
			template.process(data, out);
			out.flush();
			logger.info("Freemarker,生成文件成功。原模板文件：" + templateFileName + "，生成文件：" + outFileName);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error("Freemarker,生成文件成功。原模板文件：" + templateFileName + "，生成文件：" + outFileName, e);
		}
		finally
		{
			try
			{
				if (out != null)
				{
					out.close();
				}
			}
			catch (IOException e)
			{
				logger.error("关闭Write对象出错", e);
			}
		}
	}

	/**
	 * @param templateName 模板名字
	 * @param root 模板根 用于在模板内输出结果集
	 * @param out 输出对象 具体输出到哪里
	 */
	public static void processTemplate(Configuration cfg, Map<?, ?> root, String templateName, Writer out)
	{
		try
		{
			// 获得模板
			Template template = cfg.getTemplate(templateName, ENCODING);
			template.setEncoding(ENCODING);
			template.setOutputEncoding(ENCODING);
			template.setLocale(Locale.CHINA);
			// 生成文件（这里是我们是生成html）
			template.process(root, out);
			out.flush();
		}
		catch (IOException e)
		{
			logger.error("处理模板异常", e);
		}
		catch (TemplateException e)
		{
			logger.error("处理模板异常", e);
		}
		finally
		{
			try
			{
				out.close();
				out = null;
			}
			catch (IOException e)
			{
				logger.error("关闭Write对象出错", e);
			}
		}
	}

	/**
	 * @param templateName 模板名字
	 * @param root 模板根 用于在模板内输出结果集
	 * @param string 输出html代码片段
	 */
	public static String processTemplateString(Configuration cfg, Map<?, ?> root, String templateName, StringWriter out)
	{
		String str = "";
		try
		{
			// 获得模板
			Template template = cfg.getTemplate(templateName, ENCODING);
			template.setEncoding(ENCODING);
			template.setOutputEncoding(ENCODING);
			template.setLocale(Locale.CHINA);
			template.process(root, out);
			str = out.toString();
			out.flush();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			logger.error("处理模板异常", e);
		}
		catch (TemplateException e)
		{
			e.printStackTrace();
			logger.error("处理模板异常", e);
		}
		finally
		{
			try
			{
				out.close();
				out = null;
			}
			catch (IOException e)
			{
				logger.error("关闭Write对象出错", e);
			}
		}
		return str;
	}
	
	/**
	 * 生成静态html文件
	 * 
	 * @param cfg freemarker配置文件
	 * @param data 用来初始数据的结果集
	 * @param templateFileName， ftl模版路径
	 * @param outFileName，静态文件名称(可带路径)，如：shtml/test.shtml
	 * @param isAbsPath 是否绝对路径
	 */
	public static void generateOut(Configuration cfg, Map<String, Object> data, String templateFileName, String outFileName)
	{
		// Writer out = null;
		try
		{
			logger.info("开始生成静态文件" + templateFileName + "生成文件名称" + outFileName);
			if (cfg == null)
			{
				new Throwable("freemarker初始化异常");
			}
			// 获取模板,并设置编码方式，这个编码必须要与页面中的编码格式一致
			Template template = cfg.getTemplate(templateFileName, ENCODING);
			template.setEncoding(ENCODING);
			template.setOutputEncoding(ENCODING);
			template.setLocale(Locale.CHINA);
			// 相对路径则使用默认的appPath加上输入的文件路径
			outFileName = new StringBuffer(outputRootPath).append(File.separator).append(outFileName).toString();
			PrintWriter out = new PrintWriter(System.out);
			// 处理模版
			template.process(data, out);
			out.flush();
			logger.info("Freemarker,生成文件成功。原模板文件：" + templateFileName + "，生成文件：" + outFileName);
		}
		catch (Exception e)
		{
			logger.error("Freemarker,生成文件成功。原模板文件：" + templateFileName + "，生成文件：" + outFileName, e);
		}
		finally
		{

		}
	}

	public String loadTemplate(String templateName) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		try
		{
			File file = new File(templeteRootPath + "/" + templateName);
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), ENCODING));
			String line = reader.readLine();
			while (line != null)
			{
				sb.append(line);
				sb.append("/r/n");
				line = reader.readLine();
			}
			reader.close();
		}
		catch (IOException e)
		{
			throw new Exception("Loading template Error:", e);
		}
		return sb.toString();
	}

	public void saveTemplate(String templateName, String templateContent) throws Exception
	{
		try
		{
			File file = new File(templeteRootPath + "/" + templateName);
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), ENCODING));
			out.write(templateContent);
			out.flush();
		}
		catch (IOException e)
		{
			throw new Exception("Write template Error", e);
		}
	}

	public static void main(String[] args)
	{
		
	}
}
