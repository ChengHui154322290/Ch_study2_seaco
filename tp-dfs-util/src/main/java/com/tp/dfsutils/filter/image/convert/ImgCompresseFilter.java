package com.tp.dfsutils.filter.image.convert;

import org.im4java.core.IMOperation;

import com.tp.dfsutils.file.BaseFile;
import com.tp.dfsutils.file.ImgFile;
import com.tp.dfsutils.filter.FileFilter;

/**
 * 图片压缩过滤器
 * 
 * @describe
 * @author 叶礼锋
 * 
 *         2014-12-23 上午2:01:10
 */
@SuppressWarnings(value = { "unused", "deprecation", "serial", "unchecked", "static-access", "rawtypes" })
public class ImgCompresseFilter extends FileFilter<IMOperation> {

	@Override
	public void doFilter(BaseFile baseFile, IMOperation operation) throws Exception {
		ImgFile imgFile = ImgFile.class.cast(baseFile);
		Double quality = imgFile.getQuality();
		if (quality != null) {
			if (quality >= 0d && quality <= 100d) {
				operation.quality(quality);// 命令配置
			} else {
				throw new RuntimeException("质量取值范围必须在[0~100]区间");
			}
		}
		if (successor != null) {
			successor.doFilter(baseFile, operation);
		}
	}
}
