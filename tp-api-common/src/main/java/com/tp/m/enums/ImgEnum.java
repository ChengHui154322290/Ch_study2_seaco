package com.tp.m.enums;

/**
 * 图片宽度枚举类
 * @author zhuss
 * @2016年1月14日 下午7:42:47
 */
public interface ImgEnum {

	//像素
	public enum Width{
		
	  	WIDTH_72(72),//皮肤大图
		WIDTH_48(48),//皮肤小图
		WIDTH_1125(1125),//皮肤大图
		
		WIDTH_450(450),//450宽度,处理商品详情页
		WIDTH_800(800),//800宽度,处理商品详情页,点开放大
		
		WIDTH_715(715),//715宽度,适用 iphone6/6+
		WIDTH_610(610),//610宽度,适用 iphone4/5 
		 
		WIDTH_750(750),//750宽度,适用 iphone6/6+
		WIDTH_640(640),//640宽度,适用 iphone4/5
		
		WIDTH_346(346),//346宽度,适用 iphone6/6+
		WIDTH_296(296),//296宽度,适用 iphone4/5
		
		WIDTH_210(210),//210宽度,适用 iphone6/6+
		WIDTH_180(180),//180宽度,适用 iphone4/5
		
		WIDTH_94(94),//94宽度,适用 iphone6/6+
		WIDTH_80(80),//80宽度,适用iphone4/5
		
		WIDTH_76(76),//76宽度,适用 iphone6/6+
		WIDTH_65(65),//65宽度,适用 iphone4/5
		
		WIDTH_75(75),//75宽度,分类导航一级目录图标
		WIDTH_320(320),//120，分类导航一级目录图标
		WIDTH_120(120),//120，分类导航品牌图
		
		WIDTH_150(150),//150商品小图
		
		WIDTH_420(420),//420宽度,首页侧滑
		
		WIDTH_30(30),//30宽度,商品国旗
		
		WIDTH_360(360),//专题内商品头图
		
		WIDTH_0(0);//默认尺度，将调用原图宽度
		
		
   
	
		
		public Integer width;
		
		private Width(Integer width) {
			this.width = width;
		}
	}
}
