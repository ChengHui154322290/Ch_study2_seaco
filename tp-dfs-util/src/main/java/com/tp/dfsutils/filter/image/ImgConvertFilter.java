package com.tp.dfsutils.filter.image;

import java.io.File;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;

import com.tp.dfsutils.file.BaseFile;
import com.tp.dfsutils.filter.FileFilter;
import com.tp.dfsutils.handle.ImgHandler;

/**
 * 图片转换
 * 
 * @author Administrator
 *
 */
public class ImgConvertFilter extends FileFilter<IMOperation> {

	@Override
	public void doFilter(BaseFile baseFile, IMOperation operation) throws Exception {
		File file = baseFile.getFile();
		operation.addImage(file.getAbsolutePath(), file.getAbsolutePath());

		ConvertCmd convert = new ConvertCmd();
		if (!File.separator.matches("/")) {// 如果是Linux，不需要设置
			String imPath = ImgHandler.searchImageMagickPath();
			if (imPath != null) {
				convert.setSearchPath(imPath);
			} else {
				throw new RuntimeException("Windows下必须设置SearchPath!");
			}
		}
		convert.run(operation);
	}

}
