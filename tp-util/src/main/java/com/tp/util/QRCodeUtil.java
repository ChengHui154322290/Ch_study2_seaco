/**  
 * Project Name:xg-dfs-util  
 * File Name:Test.java  
 * Package Name:com.tp.dfsutils.util  
 * Date:2016年8月11日上午8:44:52  
 * Copyright (c) 2016, seagoor All Rights Reserved.  
 *  
*/  
package com.tp.util;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;

/**  
 * ClassName:Test <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason:   TODO ADD REASON. <br/>  
 * Date:     2016年8月11日 上午8:44:52 <br/>  
 * @author   zhouguofeng  
 * @version    
 * @since   JDK 1.8  
 * @see        
 */
public class QRCodeUtil {
	/**
	 * @param fileUrl
	 *            文件绝对路径或相对路径
	 * @return 读取到的缓存图像
	 * @throws IOException
	 *             路径错误或者不存在该文件时抛出IO异常
	 */
	private static BufferedImage getBufferedImage(String fileUrl) throws IOException {
		File f = new File(fileUrl);
		return ImageIO.read(f);
	}

	/**
	 * @param savedImg
	 *            待保存的图像
	 * @param saveDir
	 *            保存的目录
	 * @param fileName
	 *            保存的文件名，必须带后缀，比如 "beauty.jpg"
	 * @param format
	 *            文件格式：jpg、png或者bmp
	 * @return
	 */
	public static boolean saveImage(BufferedImage savedImg, String saveDir, String fileName, String format) {
		boolean flag = false;

		// 先检查保存的图片格式是否正确
		String[] legalFormats = { "jpg", "JPG", "png", "PNG", "bmp", "BMP" };
		int i = 0;
		for (i = 0; i < legalFormats.length; i++) {
			if (format.equals(legalFormats[i])) {
				break;
			}
		}
		if (i == legalFormats.length) { // 图片格式不支持
			System.out.println("不是保存所支持的图片格式!");
			return false;
		}

		// 再检查文件后缀和保存的格式是否一致
		String postfix = fileName.substring(fileName.lastIndexOf('.') + 1);
		if (!postfix.equalsIgnoreCase(format)) {
			System.out.println("待保存文件后缀和保存的格式不一致!");
			return false;
		}

		String fileUrl = saveDir + fileName;
		File file = new File(fileUrl);
		try {
			flag = ImageIO.write(savedImg, format, file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return flag;
	}

	/**
	 * 待合并的两张图必须满足这样的前提，如果水平方向合并，则高度必须相等；如果是垂直方向合并，宽度必须相等。
	 * mergeImage方法不做判断，自己判断。
	 * 
	 * @param img1
	 *            待合并的第一张图
	 * @param img2
	 *            带合并的第二张图
	 * @return 返回合并后的BufferedImage对象
	 * @throws IOException
	 */
	private static BufferedImage mergeImage(BufferedImage img1, BufferedImage img2)
			throws IOException {
		int w1 = img1.getWidth();
		int h1 = img1.getHeight();
		int w2 = img2.getWidth();
		int h2 = img2.getHeight();
		// 从图片中读取RGB
		int[] ImageArrayOne = new int[w1 * h1];
		ImageArrayOne = img1.getRGB(0, 0, w1, h1, ImageArrayOne, 0, w1); // 逐行扫描图像中各个像素的RGB到数组中
		int[] ImageArrayTwo = new int[w2 * h2];
		ImageArrayTwo = img2.getRGB(0, 0, w2, h2, ImageArrayTwo, 0, w2);
		// 生成新图片
		BufferedImage DestImage = null;
		DestImage = new BufferedImage(w1,h1,BufferedImage.TYPE_INT_RGB);
        
		DestImage.setRGB(0, 0, w1, h1, ImageArrayOne, 0, w1); // 设置上半部分或左半部分的RGB
		// 加载水印图片文件
		Graphics2D resizedG = DestImage.createGraphics();
		resizedG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,(float) 1));
		resizedG.drawImage(img2, w1 / 2 - w2 / 2, h1 / 2 - h2 / 2, null);
		resizedG.dispose();
		 
		
		
		// 画图

		return DestImage;
	}
	/**
	 * 
	 * generateQRCode:(根据微信分享码的url生成带log). <br/>
	 * @author zhouguofeng
	 * @param url
	 * @param logoPath 本地logo图片目录
	 * @return
	 * @sinceJDK 1.8
	 */
	public static byte[] generateQRCode(String url, String logoPath) {
		byte[] btImg = getImageFromNetByUrl(url);

		// 读取待合并的文件
		BufferedImage bi1 = null;
		BufferedImage bi2 = null;
		BufferedImage destImg = null;
		try {
			bi1 = ImageIO.read(new ByteArrayInputStream(btImg));
		} catch (IOException e1) {

			e1.printStackTrace();

		}
		try {
			bi2 = getBufferedImage(logoPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 调用mergeImage方法获得合并后的图像
		try {
			destImg = mergeImage(bi1, bi2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ImageIO.write(destImg, "PNG", out);
		} catch (IOException e) {
			e.printStackTrace();

		}
		return out.toByteArray();

	}
	/**
	 * 待合并的两张图必须满足这样的前提，如果水平方向合并，则高度必须相等；如果是垂直方向合并，宽度必须相等。
	 * mergeImage方法不做判断，自己判断。
	 * 
	 * @param img1
	 *            待合并的第一张图
	 * @param img2
	 *            带合并的第二张图
	 * @return 返回合并后的BufferedImage对象
	 * @throws IOException
	 */
	private static BufferedImage mergeBackGroundImage(BufferedImage img1, BufferedImage img2)
			throws IOException {
		
		BufferedImage inputbig = new BufferedImage(235, 235, BufferedImage.TYPE_INT_BGR);
        Graphics2D g = (Graphics2D) inputbig.getGraphics();
        g.drawImage(img2, 0, 0,280,280,null); //画图
        g.dispose();
        inputbig.flush();
        img2= inputbig;
		
		int w1 = img1.getWidth();
		int h1 = img1.getHeight();
		int w2 = img2.getWidth();
		int h2 = img2.getHeight();
		// 从图片中读取RGB
		int[] ImageArrayOne = new int[w1 * h1];
		ImageArrayOne = img1.getRGB(0, 0, w1, h1, ImageArrayOne, 0, w1); // 逐行扫描图像中各个像素的RGB到数组中
		int[] ImageArrayTwo = new int[w2 * h2];
		ImageArrayTwo = img2.getRGB(0, 0, w2, h2, ImageArrayTwo, 0, w2);
		// 生成新图片
		BufferedImage DestImage = null;
		DestImage = new BufferedImage(w1,h1,BufferedImage.TYPE_INT_RGB);
        
		DestImage.setRGB(0, 0, w1, h1, ImageArrayOne, 0, w1); // 设置上半部分或左半部分的RGB
		// 加载水印图片文件
		Graphics2D resizedG = DestImage.createGraphics();
		resizedG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,(float) 1));
		resizedG.drawImage(img2, (w1 / 2 - w2 / 2)-15, (h1 / 2 - h2 / 2)-125, null);
		resizedG.dispose();
		 
		
		
		// 画图

		return DestImage;
	}

	/**
	 * 
	 * generateQRCode:(根据微信分享码的url生成带log). <br/>
	 * @author zhouguofeng
	 * @param url
	 * @param logoPath 本地logo图片目录
	 * @return
	 * @sinceJDK 1.8
	 */
	public static byte[] generateQRCode(String url, String logoPath,String backGroundLPath) {
		byte[] btImg = getImageFromNetByUrl(url);

		// 读取待合并的文件
		BufferedImage bi1 = null;
		BufferedImage bi2 = null;
		BufferedImage bi3 = null;
		BufferedImage destImg = null;
		BufferedImage destImgFinal = null;
		try {
			bi1 = ImageIO.read(new ByteArrayInputStream(btImg));
		} catch (IOException e1) {

			e1.printStackTrace();

		}
		try {
			bi2 = getBufferedImage(logoPath);
			bi3 = getBufferedImage(backGroundLPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 调用mergeImage方法获得合并后的图像
		try {
			destImg = mergeImage(bi1, bi2);
			destImgFinal=mergeBackGroundImage(bi3,destImg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ImageIO.write(destImgFinal, "PNG", out);
		} catch (IOException e) {
			e.printStackTrace();

		}
		return out.toByteArray();

	}

	/**
	 * 测试
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String url = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=gQE58DoAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL0JEcy1pX2psZnc4R2xCSkQzQk5wAAIEL9xjVwMEAAAAAA==";
		String logPath = "d:/logo.png";
		String backGround="d:/img.png";
		// 保存图像
		byte[] imageByte = generateQRCode(url, logPath,backGround);
		BufferedImage bi1;
		try {
			bi1 = ImageIO.read(new ByteArrayInputStream(imageByte));
			saveImage(bi1, "d:/", "hebing.png", "png");
			System.out.println("合并完毕!");

		} catch (IOException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	/**
	 * 根据地址获得数据的字节流
	 * 
	 * @param strUrl
	 *            网络连接地址
	 * @return
	 */
	private static byte[] getImageFromNetByUrl(String strUrl) {
		try {
			URL url = new URL(strUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5 * 1000);
			InputStream inStream = conn.getInputStream();// 通过输入流获取图片数据
			byte[] btImg = readInputStream(inStream);// 得到图片的二进制数据
			return btImg;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 从输入流中获取数据
	 * 
	 * @param inStream
	 *            输入流
	 * @return
	 * @throws Exception
	 */
	private static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}

}
