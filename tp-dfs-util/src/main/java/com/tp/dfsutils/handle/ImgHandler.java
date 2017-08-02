package com.tp.dfsutils.handle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.commons.io.IOUtils;
import org.im4java.core.IMOperation;

import com.tp.dfsutils.file.ImgFile;
import com.tp.dfsutils.filter.FileFilter;
import com.tp.dfsutils.filter.image.ImgCompositeFilter;
import com.tp.dfsutils.filter.image.ImgConvertFilter;
import com.tp.dfsutils.filter.image.composite.ImgWatermarkFilter;
import com.tp.dfsutils.filter.image.convert.ImgCompresseFilter;
import com.tp.dfsutils.filter.image.convert.ImgInterlaceFilter;
import com.tp.dfsutils.filter.image.convert.ImgReSizeFilter;

/**
 * 
 * @describe
 * @author 叶礼锋
 * 
 *         2014-12-22 下午2:52:41
 */
@SuppressWarnings(value = { "unused", "deprecation", "serial", "unchecked", "static-access", "rawtypes" })
public class ImgHandler implements FileHandle {

	private ImgFile imgFile = null;
	private static String imageMagickPath = null;

	public ImgHandler(ImgFile imgFile) {
		this.imgFile = imgFile;
	}

	public void handle() throws Exception {
		File file = imgFile.getFile();
		File originalFile = imgFile.getOriginalFile();

		// 原图保留
		originalFile = file;
		file = File.createTempFile("TMP", originalFile.getName(), originalFile.getParentFile());
		imgFile.setOriginalFile(originalFile);
		imgFile.setFile(file);
		IOUtils.copy(new FileInputStream(originalFile), new FileOutputStream(file));

		compositeHandle();
		convertHandle();
	}

	public void compositeHandle() throws Exception {
		FileFilter<IMOperation> watermarkFilter = new ImgWatermarkFilter();
		FileFilter<IMOperation> compositeFilter = new ImgCompositeFilter();

		// 生成任务链
		watermarkFilter.setSuccessor(compositeFilter);

		IMOperation operation = new IMOperation();
		watermarkFilter.doFilter(imgFile, operation);
	}

	public void convertHandle() throws Exception {
		FileFilter<IMOperation> compresseFilter = new ImgCompresseFilter();
		FileFilter<IMOperation> reSizeFilter = new ImgReSizeFilter();
		FileFilter<IMOperation> interlaceFilter = new ImgInterlaceFilter();
		FileFilter<IMOperation> convertFilter = new ImgConvertFilter();

		// 生成任务链
		compresseFilter// 压缩处理
				.setSuccessor(reSizeFilter)// 缩放处理
				.setSuccessor(interlaceFilter)// 渐进处理
				.setSuccessor(convertFilter);// 执行

		IMOperation operation = new IMOperation();
		compresseFilter.doFilter(imgFile, operation);
	}

	/**
	 * 从环境变量中搜索ImageMagick的路径，限Windows
	 * 
	 * @return
	 */
	public static String searchImageMagickPath() {
		if (imageMagickPath == null) {
			String path = System.getenv("PATH");
			if (path != null && path.length() > 0) {
				for (String ph : path.split(";")) {
					if (ph.toUpperCase().indexOf("IMAGEMAGICK") > 0) {
						imageMagickPath = ph;
						break;
					}
				}
			}
		}
		return imageMagickPath;
	}

}
