package com.tp.service.pay.util;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.tp.util.Base64;

/**
 * 图片文件与base64互转
 * @author szy
 *
 */
public class ImageUtil {
	public static void main(String[] args) {
		// 测试从Base64编码转换为图片文件
//
//		String strImg = "这里放64位编码";
//		saveBase64ToFile(strImg, "D:/tmp/getWxImage.jpg");

		// 测试从图片文件转换为Base64编码
		System.out.println(toBase64String("D:/tmp/getWxImage.jpg"));

	}

	public static String toBase64String(String imgFilePath) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		byte[] data = null;

		// 读取图片字节数组
		try {
			InputStream in = new FileInputStream(imgFilePath);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 对字节数组Base64编码
		return Base64.encode(data);// 返回Base64编码过的字节数组字符串
	}

	public static boolean saveBase64ToFile(String imgStr, String imgFilePath) {// 对字节数组字符串进行Base64解码并生成图片
		if (imgStr == null) // 图像数据为空
			return false;
		try {
			// Base64解码
			byte[] bytes = Base64.decode(imgStr);
			for (int i = 0; i < bytes.length; ++i) {
				if (bytes[i] < 0) {// 调整异常数据
					bytes[i] += 256;
				}
			}
			// 生成jpeg图片
			OutputStream out = new FileOutputStream(imgFilePath);
			out.write(bytes);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}