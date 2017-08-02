package com.tp.dfsutils.filter.image.convert;

import java.io.File;

import org.im4java.core.IMOperation;

import com.tp.dfsutils.file.BaseFile;
import com.tp.dfsutils.file.ImgFile;
import com.tp.dfsutils.filter.FileFilter;

public class ImgInterlaceFilter extends FileFilter<IMOperation> {

	@Override
	public void doFilter(BaseFile baseFile, IMOperation operation) throws Exception {
		ImgFile imgFile = ImgFile.class.cast(baseFile);
		File file = imgFile.getFile();
		boolean interlace = imgFile.isInterlace();
		if (interlace) {
			if (file.getName().matches(".+(?i)((JPEG)|(JPG)|(JPE))$")) {
				operation.interlace("Plane");// 交叉渐进渲染
			} else {
				throw new RuntimeException("渐进格式仅限JPEG!");
			}
		}
		if (successor != null) {
			successor.doFilter(baseFile, operation);
		}
	}

}
