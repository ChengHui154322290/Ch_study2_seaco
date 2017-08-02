package com.tp.dfsutils.file;

import java.io.File;

import com.tp.dfsutils.constants.WaterMarkGravity;
import com.tp.dfsutils.handle.ImgHandler;

/**
 * 
 * @describe
 * @author 叶礼锋
 * 
 *         2014-12-22 下午2:16:18
 */
@SuppressWarnings(value = { "unused", "deprecation", "serial", "unchecked", "static-access", "rawtypes" })
public class ImgFile extends BaseFile {
	// composite -dissolve 60 -gravity SouthEast -geometry +10+10 Z:\B.png
	// Z:\A.jpg NEW.jpg

	/** 宽度 */
	private Integer rawWidth = null;

	/** 0%~100% 一般不要低于75d */
	private Double quality = null;

	/** 开启渐进加载,限JPEG */
	private boolean interlace = false;

	/** 水印 */
	private WaterMark watermark = null;

	public Integer getRawWidth() {
		return rawWidth;
	}

	public void setRawWidth(Integer rawWidth) {
		this.rawWidth = rawWidth;
	}

	public Double getQuality() {
		return quality;
	}

	public void setQuality(Double quality) {
		this.quality = quality;
	}

	public ImgFile() {
		super.setHandler(new ImgHandler(this));
	}

	public boolean isInterlace() {
		return interlace;
	}

	public void setInterlace(boolean interlace) {
		this.interlace = interlace;
	}

	public WaterMark getWatermark() {
		return watermark;
	}

	public void setWatermark(WaterMark watermark) {
		this.watermark = watermark;
	}

	public class WaterMark {

		/** 水印图片 */
		private File wmfile = null;

		/** 透明度 0:完全透明 */
		private int dissolve = 100;

		/** 坐标起始方位 默认右下方 */
		private WaterMarkGravity gravity = WaterMarkGravity.SouthEast;

		/** 宽度 */
		private Integer width = null;

		/** 高度 */
		private Integer high = null;

		/** 相对起始位置的偏移量(像素) */
		private int x = 0;

		/** 相对起始位置的偏移量(像素) */
		private int y = 0;

		public File getWmfile() {
			return wmfile;
		}

		public void setWmfile(File wmfile) {
			this.wmfile = wmfile;
		}

		public int getDissolve() {
			return dissolve;
		}

		public void setDissolve(int dissolve) {
			this.dissolve = dissolve;
		}

		public WaterMarkGravity getGravity() {
			return gravity;
		}

		public void setGravity(WaterMarkGravity gravity) {
			this.gravity = gravity;
		}

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}

		public Integer getHigh() {
			return high;
		}

		public void setHigh(Integer high) {
			this.high = high;
		}

		public Integer getWidth() {
			return width;
		}

		public void setWidth(Integer width) {
			this.width = width;
		}
	}
}
