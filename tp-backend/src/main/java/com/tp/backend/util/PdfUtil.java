package com.tp.backend.util;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.io.DOMReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.pdf.PDFEncryption;
import org.xhtmlrenderer.simple.ImageRenderer;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import com.tp.util.ResourceUtil;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public final class PdfUtil {
	public static final String PDF_FONT_URL = "/WEB-INF/class/econtractTemplate/";// pdf模板相对路径
	public static final String PDF_OWNER_PASS_WORD="xgFirst";
	private static final Logger logger = LoggerFactory.getLogger(PdfUtil.class);
	public static void main(String[] asrg){
		String url = PdfUtil.class.getClassLoader().getResource("econtractTemplate").getPath();
		System.out.println(url);
		String context = generateFile("C:/xg/workspace/integral/xg-backend/src/main/resources/econtractTemplate/","PRE_PAY_ECONTRACT.xml",new HashMap<String,Object>());
		System.out.println(context);
		createPdfFile(context,"C:/xg/workspace/integral/xg-backend/src/main/resources/econtractTemplate/roleLists.pdf");

	}
	
	public ByteArrayOutputStream getPdfByTemplate(String templateName,Map<?,?> params) throws DocumentException, IOException{
		String context = generateFile("C:/xg/workspace/tk/xg-backend/src/main/resources/econtractTemplate",templateName,params);
		return createPdfFile(context);
	}
	public static void htmlToImage(File f,String imageType) throws IOException{
		if (f.exists()) {
			String output = f.getAbsolutePath();
			output = output.substring(0, output.lastIndexOf(".")) +imageType;
			ImageRenderer.renderToImage(f, output, 700);
		}   
	}
	/**
	 * 根据文件路径创建PDF文件
	 * @param parameters
	 * @param toUrl
	 */
	public static void createPdfFile(final String content,final String toUrl) {
		ByteArrayOutputStream baos = null;
		FileOutputStream fs = null;
		try {
			baos = createPdfFile(content);
			if (null != baos) {
				fs = new FileOutputStream(new File(toUrl));
				baos.writeTo(fs);
			}
		} catch (IOException e) {
			logger.error("合同生成PDF文件  IOException:" + e);
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(baos);
			IOUtils.closeQuietly(fs);
		}
	}
	public static String generateFile(String templatePath, String templateName, Map<?, ?> root) {
		try {
			Configuration config = new Configuration();
			config.setDirectoryForTemplateLoading(new File(templatePath));
			config.setObjectWrapper(new DefaultObjectWrapper());
			Template template = config.getTemplate(templateName, "UTF-8");
			StringWriter  out = new StringWriter();
			template.process(root, out);
			return transHtml(out.getBuffer().toString());
		} catch (IOException e) {
			System.out.println(e);
		} catch (TemplateException e) {
			System.out.println(e);
		}
		return "";
	}

	public static String generateFileUseRelativePath(String templatePath, String templateName, Map<?, ?> root) {
		try {
			Configuration config = new Configuration();
			URL url = PdfUtil.class.getClassLoader().getResource(templatePath);
			config.setDirectoryForTemplateLoading(new File(url.getPath()));
			config.setObjectWrapper(new DefaultObjectWrapper());
			Template template = config.getTemplate(templateName, "UTF-8");
			StringWriter  out = new StringWriter();
			template.process(root, out);
			return  out.getBuffer().toString();
		//	return transHtml(out.getBuffer().toString());
		} catch (IOException e) {
			System.out.println(e);
		} catch (TemplateException e) {
			System.out.println(e);
		}
		return "";
	}
	
	public static String transHtml(String context){
		ByteArrayInputStream in = new ByteArrayInputStream(context.getBytes());
		Tidy tidy = new Tidy();
        tidy.setQuiet(true);                   
        tidy.setShowWarnings(false); //不显示警告信息
        tidy.setIndentContent(true);//
        tidy.setSmartIndent(true);
        tidy.setIndentAttributes(false);
        tidy.setWraplen(1024); //多长换行
        tidy.setCharEncoding(org.w3c.tidy.Configuration.UTF8); // 文件编码为 UTF8
        //输出为xhtml
        tidy.setXHTML(true);
        
        org.w3c.dom.Document dom = tidy.parseDOM(in, null);

        Document doc = new DOMReader().read(dom);
		doc.setXMLEncoding("UTF-8");
        return doc.asXML();
	}
	
	/**
	 * 创建PDF文件流
	 * @param parameters
	 * @param toUrl
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public static ByteArrayOutputStream createPdfFile(final String line) throws DocumentException, IOException {
		String content = line.replaceAll("&nbsp;", "");
		if(!content.startsWith("<")){ 
			content = content.substring(content.indexOf('<')); 
		}
		content = content.replaceAll("&", "");
		content = content.replaceAll("<br>", "<br/>");
		content = content.replaceAll("</br>", "<br/>");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		File ttc = ResourceUtil.getResourceFile("class/resources/econtractTemplate/simsun.ttc");
		if (!ttc.exists()) {
			ttc = ResourceUtil.getResourceFile("class/resources/econtractTemplate/SIMSUN.TTC");
		}
		String fontPath=ttc.getAbsolutePath();
		ITextRenderer renderer = new ITextRenderer();
		ITextFontResolver fontResolver = renderer.getFontResolver();
		if(null!=fontPath) {
			fontResolver.addFont(fontPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		}
		renderer.setDocumentFromString(content);
		PDFEncryption encryption = new PDFEncryption(null, PDF_OWNER_PASS_WORD.getBytes(), PdfWriter.ALLOW_PRINTING);
		renderer.setPDFEncryption(encryption);
		renderer.layout();
		renderer.createPDF(baos);
		renderer.finishPDF();
		return baos;
	}

	/**
	 * 根据文件路径创建PDF文件
	 * @param parameters
	 * @param toUrl
	 */
	public static void createPdfFileUseRelativePath(final String content,final String toUrl) {
		ByteArrayOutputStream baos = null;
		FileOutputStream fs = null;
		try {
			baos = createPdfFileUseRelativePath(content);
			if (null != baos) {
				fs = new FileOutputStream(new File(toUrl));
				baos.writeTo(fs);
			}
		} catch (IOException e) {
			logger.error("合同生成PDF文件  IOException:" + e);
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(baos);
			IOUtils.closeQuietly(fs);
		}
	}

	/**
	 * 根据文件路径创建PDF文件
	 * @param parameters
	 * @param toUrl
	 */
	public static void createPdfFileUseRelativePathToOutputStream(final String content,final OutputStream out) {
		ByteArrayOutputStream baos = null;
		FileOutputStream fs = null;
		try {
			baos = createPdfFileUseRelativePath(content);
			if (null != baos) {
				//fs = new FileOutputStream(new File(toUrl));
				out.write(baos.toByteArray());
				//baos.writeTo(fs);
			}
		} catch (IOException e) {
			logger.error("生成PDF文件  IOException:" + e);
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(baos);
			IOUtils.closeQuietly(fs);
		}
	}

	public static ByteArrayOutputStream createPdfFileUseRelativePath(final String line) throws DocumentException, IOException {
		String content = line.replaceAll("&nbsp;", "");
		if(!content.startsWith("<")){
			content = content.substring(content.indexOf('<'));
		}
		content = content.replaceAll("&", "");
		content = content.replaceAll("<br>", "<br/>");
		content = content.replaceAll("</br>", "<br/>");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String path = PdfUtil.class.getClassLoader().getResource("econtractTemplate").getPath();
		if (!path.endsWith(File.separator)) path+=File.separator;
		File ttc = new File( path+"simsun.ttc");
		if (!ttc.exists()) {
			ttc = new File(path+"SIMSUN.TTC");

		}
		String fontPath=ttc.getAbsolutePath();
		ITextRenderer renderer = new ITextRenderer();
		ITextFontResolver fontResolver = renderer.getFontResolver();
		if(null!=fontPath) {
			fontResolver.addFont(fontPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		}
		renderer.setDocumentFromString(content);
		PDFEncryption encryption = new PDFEncryption(null, PDF_OWNER_PASS_WORD.getBytes(), PdfWriter.ALLOW_PRINTING);
		renderer.setPDFEncryption(encryption);
		renderer.layout();
		renderer.createPDF(baos);
		renderer.finishPDF();
		return baos;
	}
}
