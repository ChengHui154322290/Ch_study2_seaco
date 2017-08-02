package com.tp.dfsutils.filter.image;

import java.io.File;

import org.im4java.core.CompositeCmd;
import org.im4java.core.IMOperation;

import com.tp.dfsutils.file.BaseFile;
import com.tp.dfsutils.file.ImgFile;
import com.tp.dfsutils.filter.FileFilter;
import com.tp.dfsutils.handle.ImgHandler;

/**
 * 图片合成
 * 
 * @author Administrator
 *
 */
public class ImgCompositeFilter extends FileFilter<IMOperation> {

	@Override
	public void doFilter(BaseFile baseFile, IMOperation operation) throws Exception {
		ImgFile imgFile = ImgFile.class.cast(baseFile);
		ImgFile.WaterMark watermark = imgFile.getWatermark();
		if (watermark != null) {
			File file = imgFile.getFile();
			File wmFile = watermark.getWmfile();

			if (wmFile != null && wmFile.exists()) {
				operation.addImage(wmFile.getAbsolutePath(), file.getAbsolutePath(), file.getAbsolutePath());
				CompositeCmd composite = new CompositeCmd();
				if (!File.separator.matches("/")) {// 如果是Linux，不需要设置
					String imPath = ImgHandler.searchImageMagickPath();
					if (imPath != null) {
						composite.setSearchPath(imPath);
					} else {
						throw new RuntimeException("Windows下必须设置SearchPath!");
					}
				}
				composite.run(operation);
			} else {
				throw new RuntimeException("找不到水印图片");
			}

		}
	}

}
