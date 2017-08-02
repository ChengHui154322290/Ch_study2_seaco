package com.tp.dfsutils.filter.image.composite;

import org.im4java.core.IMOperation;

import com.tp.dfsutils.file.BaseFile;
import com.tp.dfsutils.file.ImgFile;
import com.tp.dfsutils.filter.FileFilter;

public class ImgWatermarkFilter extends FileFilter<IMOperation> {

	@Override
	public void doFilter(BaseFile baseFile, IMOperation operation) throws Exception {
		ImgFile imgFile = ImgFile.class.cast(baseFile);
		ImgFile.WaterMark watermark = imgFile.getWatermark();
		if (watermark != null) {
			operation.dissolve(watermark.getDissolve());
			operation.gravity(watermark.getGravity().name());
			operation.geometry(watermark.getWidth(), watermark.getHigh(), watermark.getX(), watermark.getY());
		}
		if (successor != null) {
			successor.doFilter(baseFile, operation);
		}
	}

}
