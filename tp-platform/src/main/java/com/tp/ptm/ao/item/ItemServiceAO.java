package com.tp.ptm.ao.item;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.TextAttribute;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.tp.common.vo.Constant;
import com.tp.common.vo.BseConstant.DictionaryCode;
import com.tp.common.vo.BseConstant.TaxRateEnum;
import com.tp.common.vo.CmtConstant.ItemReviewConstant;
import com.tp.common.vo.Constant.DEFAULTED;
import com.tp.common.vo.StorageConstant.App;
import com.tp.common.vo.mem.MemberConstant.IsTop;
import com.tp.common.vo.prd.ItemConstant;
import com.tp.common.vo.supplier.entry.SupplierType;
import com.tp.dfsutils.constants.ImageWidthNorms;
import com.tp.dfsutils.constants.MetaDataKey;
import com.tp.dfsutils.file.ImgFile;
import com.tp.dfsutils.service.DfsService;
import com.tp.dfsutils.util.DfsDomainUtil;
import com.tp.dto.mmp.TopicItemBbtDTO;
import com.tp.model.bse.Brand;
import com.tp.model.bse.Category;
import com.tp.model.bse.ClearanceChannels;
import com.tp.model.bse.DictionaryInfo;
import com.tp.model.bse.DistrictInfo;
import com.tp.model.bse.Spec;
import com.tp.model.bse.SpecGroup;
import com.tp.model.bse.TaxRate;
import com.tp.model.cmt.ItemReview;
import com.tp.model.mmp.FreightTemplate;
import com.tp.model.mmp.TopicItem;
import com.tp.model.prd.ItemDetail;
import com.tp.model.prd.ItemPictures;
import com.tp.model.prd.ItemSku;
import com.tp.model.sup.SupplierInfo;
import com.tp.proxy.bse.TaxRateProxy;
import com.tp.proxy.prd.ItemImportProxy;
import com.tp.proxy.prd.ItemProxy;
import com.tp.ptm.dtos.ItemDto;
import com.tp.ptm.dtos.ItemHotDto;
import com.tp.result.bse.SpecGroupResult;
import com.tp.result.sup.SupplierResult;
import com.tp.service.bse.IBrandService;
import com.tp.service.bse.ICategoryService;
import com.tp.service.bse.IClearanceChannelsService;
import com.tp.service.bse.IDictionaryInfoService;
import com.tp.service.bse.ISpecGroupService;
import com.tp.service.cmt.IItemReviewService;
import com.tp.service.mmp.IFreightTemplateService;
import com.tp.service.mmp.ITopicItemService;
import com.tp.service.mmp.remote.IRemoteForItemService;
import com.tp.service.prd.IItemDetailService;
import com.tp.service.prd.IItemPicturesService;
import com.tp.service.prd.IItemRemoteService;
import com.tp.service.prd.IItemSkuService;
import com.tp.service.stg.IInventoryQueryService;
import com.tp.service.sup.IPurchasingManagementService;
import com.tp.util.ExcelUtil;

@Service
public class ItemServiceAO {
	
	private Logger logger = LoggerFactory.getLogger(ItemServiceAO.class);
	
	@Autowired
	private IRemoteForItemService remoteForItemService;
	
	@Autowired
	private DfsDomainUtil dfsDomainUtil;
	
//	@Value("${pc.item.domain}")
	private String pc_item_domain;
	
//	@Value("${wap.item.domain}")
	private String wap_item_domain;
	
	@Autowired
	private IItemSkuService itemSkuService;
	@Autowired
	private IItemDetailService itemDetailService;
	
	@Autowired
	private ITopicItemService topicItemService;
	
	@Autowired
	private IInventoryQueryService inventoryQueryService;
	
	@Autowired
	private ItemCacheServiceAO cacheServiceAO;
	
	@Autowired
	private IItemRemoteService itemRemoteService;
	
	@Autowired
	private ICategoryService categoryService;
	
	@Autowired
	private IItemPicturesService itemPicturesService;
	
	@Autowired
	private DfsService dfsService;
	
	@Autowired
	private IBrandService brandService;
	
	@Autowired
	private IItemReviewService itemReviewService;
	
	@Autowired
	private IPurchasingManagementService purchasingManagementService;
	
	@Autowired
	private ISpecGroupService specGroupService;
	
	@Autowired
	private IDictionaryInfoService dictionaryInfoService;
	
	@Autowired
	private IFreightTemplateService freightTemplateService;
	
	@Autowired
	private TaxRateProxy taxRateProxy;
	
	@Autowired
	private ItemProxy itemProxy;
	@Autowired
	private IClearanceChannelsService clearanceChannelsService;
	
	@Value("${upload.tmp.path}")
	private String uploadTempPath;
	
	public static final String ADV_TEMPLATE="/fonts/adv_template.png";
	
	//#999999
	public final static Color COLOR_999 = new Color(153,153,153);
	//#666666
	public final static Color COLOR_666 = new Color(102,102,102);
	//#ff9701
	public final static Color COLOR_MT_PRICE = new Color(255,151,1);
	
	/** 最大记录数  */
	public final static Integer MAX_PAGE_SIZE = 200;
	
	public ItemDto selectItemInfoBySku(String sku){
		if(StringUtils.isBlank(sku)){
			return null;
		}
		ItemDto cacheDto = cacheServiceAO.selectCacheRecommand(sku);
		if(null!=cacheDto){
			return cacheDto;
		}
		
		//获得商品当前价
		TopicItemBbtDTO topicItemDO = null;
		try {
			topicItemDO = remoteForItemService.getTopicItemBySku(sku);
		} catch (Exception e) {
			logger.error("selectItemInfoBySku >>> remoteForItemService.getTopicItemBySku {}",e.getMessage());
		}
		if(null==topicItemDO){
			return null;
		}
		ItemDto dto = new ItemDto();
		Double topicPrice = topicItemDO.getTopicPrice();
		Double salePrice = topicItemDO.getSalePrice();
		dto.setSku(sku);
		dto.setCurrent_price(topicPrice);
		dto.setOriginal_price(salePrice);
		dto.setTitle(topicItemDO.getName());
		dto.setProduct_pic(dfsDomainUtil.getSnapshotUrl(topicItemDO.getTopicImage(),ImageWidthNorms.WIDTH100));
		
		String topicId = topicItemDO.getTopicId().toString();
		if(null!=topicItemDO.getIsPc()&&topicItemDO.getIsPc()){
			String pc_url = pc_item_domain.concat("/itemDetail-").concat(topicId).concat("-").concat(sku).concat(".htm");
			dto.setPc_url(pc_url);
		}
		
		if(null!=topicItemDO.getIsWap()&&topicItemDO.getIsWap()){
			//http://m.meitun.com/cmphtml/pages/product_details.html?specialid=1178&productid=09110100030101
			// http://m.meitun.com/pdetails.html?sid=5196&pid=13020203030101
			String wap_url = wap_item_domain.concat("/product_details.html?sid=").concat(topicId).concat("&pid=").concat(sku);
			dto.setWap_url(wap_url);
		}
		
		int discount = (int)((topicPrice/salePrice)*10);
		dto.setDiscount(discount*1.0);
		
		// 获得商品卖点
		ItemSku skuDO = itemSkuService.selectSkuInfoBySkuCode(sku);
		if(null!=skuDO){
			Long detailId = skuDO.getDetailId();
			ItemDetail detailDO = itemDetailService.queryById(detailId);
			String desc = detailDO.getSubTitle();
			dto.setDesc(desc);
		}
		//获得广告图片
		String oldAdvField = cacheServiceAO.selectCacheRecommandAdvField(sku);
		String imageField = topicItemDO.getTopicImage();
		try {
			oldAdvField = genItemAdvImage(dto.getTitle(), dto.getCurrent_price(), dto.getOriginal_price(), discount, imageField, oldAdvField);
			dto.setAdvImgUrl(dfsDomainUtil.getFileFullUrl(oldAdvField));
			cacheServiceAO.insertCacheRecommandAdvField(sku, oldAdvField);
		} catch (Exception e) {
			logger.error("gen adv image error = {}",e);
		}
		
		//获取品牌名称
		Brand brandDO = brandService.queryById(skuDO.getBrandId());
		dto.setBrandName(brandDO.getName());
		//获取品类名称，默认获取中类，如果种类是“其他”这种类型，再获取小类
		Category categoryDO = categoryService.queryById(skuDO.getCategoryId());//这里获取的是小类的：skuDO.getCategoryId()
		logger.info("品类类型，大中小-{}，品类id：{}",categoryDO.getLevel(),categoryDO.getId());
		
		Category categoryDO1 = categoryService.queryById(categoryDO.getParentId());//获取中类
		if("其他".equals(categoryDO1.getName().trim())){//如果中类的名称是其他，则获取小类名称
			dto.setCategoryName(categoryDO.getName());
		}else{
			dto.setCategoryName(categoryDO1.getName());
		}
		
		//获取置顶评论
		//获取逻辑：品类名称：sku对应的置顶评价1条
		ItemReview itemReviewDO = new ItemReview();
		itemReviewDO.setIsTop(IsTop.TOP);
		itemReviewDO.setIsCheck(ItemReviewConstant.ISCHECK.CHECKED);
		String prdid = null;
		//如果品类为‘其他’，则获取其下随机小类sku评论信息
		if(2 == categoryDO.getLevel().intValue() && "其他".equals(categoryDO.getName().trim())){
			logger.info("中类-其他 类型，获取小类sku评论信息....");
			Category categoryDO2 = new Category();
			categoryDO2.setParentId(categoryDO.getId());
			List<Category> list1 = categoryService.queryByObject(categoryDO2);
		
			ItemSku skuDO1 = new ItemSku();
		
			List<ItemSku> skuDOTemp = null;
			catPrdid : for(Category tempCategoryDO : list1){
				skuDO1.setCategoryId(tempCategoryDO.getId());
				skuDOTemp = itemSkuService.queryByObject(skuDO1);
				if(CollectionUtils.isNotEmpty(skuDOTemp)){
					prdid = skuDOTemp.get(0).getPrdid();
					break catPrdid;
				}
			}
			
		}else{
			prdid= skuDO.getPrdid();
		}
		logger.info("获取prdid={}评论信息",prdid);
		if(StringUtils.isNotEmpty(prdid)){
			itemReviewDO.setPid(prdid);
			try{
				List<ItemReview> list  = itemReviewService.queryByObject(itemReviewDO);
				if(CollectionUtils.isNotEmpty(list)){
					ItemReview reviewDO = list.get(0);
					dto.setContent(reviewDO.getContent());
					dto.setCommnetUser(reviewDO.getUserName());
				}else{
					logger.info("评论获取异常{}",sku);
				}
			}catch(Exception e){
				logger.info("获取评论异常！{}",e.getMessage());
			}
		}
		
		cacheServiceAO.insertCacheRecommand(dto);
		return dto;
	}
	
	public ItemHotDto selectItemInfoBySku(String sku,Long topicId){
		ItemHotDto hotDto = cacheServiceAO.selectCacheHot(sku, topicId);
		if(null!=hotDto){
			return hotDto;
		}
		
		TopicItem itemDO = new TopicItem();
		itemDO.setSku(sku);
		itemDO.setTopicId(topicId);
		List<TopicItem> itemDoList = topicItemService.queryByObject(itemDO);
		if(CollectionUtils.isNotEmpty(itemDoList)){
			TopicItem topicItemDO = itemDoList.get(0);
			ItemHotDto itemHotDto = new ItemHotDto();
			itemHotDto.setCurrent_price(topicItemDO.getTopicPrice());
			itemHotDto.setOriginal_price(topicItemDO.getSalePrice());
			int discount = (int)((topicItemDO.getTopicPrice()/topicItemDO.getSalePrice())*10);
			itemHotDto.setDiscount(discount*1.0);
			itemHotDto.setSku(sku);
			itemHotDto.setTopicId(topicId);
			
			int inventory = inventoryQueryService.querySalableInventory(App.PROMOTION, topicId.toString(), 
					sku, topicItemDO.getStockLocationId(), DEFAULTED.YES.equals(topicItemDO.getReserveInventoryFlag()));
			
			//int inventory = inventoryQueryService.selectInvetory(App.PROMOTION, topicId.toString(), sku/*,topicItemDO.getStockLocationId()*/);
			itemHotDto.setInventory(inventory);
			Integer volume = itemRemoteService.getSalesCountBySku(sku);
			itemHotDto.setVolume(volume);
			String pic = topicItemDO.getTopicImage();
			if(StringUtils.isBlank(pic)){
				ItemSku skuDO = itemSkuService.selectSkuInfoBySkuCode(sku);
				ItemPictures picturesDO = new ItemPictures();
				picturesDO.setMain(Constant.DEFAULTED.YES);
				picturesDO.setDetailId(skuDO.getDetailId());
				List<ItemPictures> picturesDOs = itemPicturesService.queryByObject(picturesDO);
				if(CollectionUtils.isNotEmpty(picturesDOs)){
					pic = picturesDOs.get(0).getPicture();
				}
			}
			itemHotDto.setPic(dfsDomainUtil.getSnapshotUrl(pic, ImageWidthNorms.WIDTH520) );
			//获取大类、中类、小类的名称和code
			Long categoryId = topicItemDO.getCategoryId();
			List<Category> categoryDOList = categoryService.getParentCategoryList(categoryId);
			if(CollectionUtils.isNotEmpty(categoryDOList)){
				for (Category categoryDO : categoryDOList) {
					if(categoryDO.getLevel()==1){
						itemHotDto.setCat_large_code(categoryDO.getCode());
						itemHotDto.setCat_large_name(categoryDO.getName());
					}
					if(categoryDO.getLevel()==2){
						itemHotDto.setCat_middle_code(categoryDO.getCode());
						itemHotDto.setCat_middle_name(categoryDO.getName());
					}
					if(categoryDO.getLevel()==3){
						itemHotDto.setCat_code(categoryDO.getCode());
						itemHotDto.setCat_name(categoryDO.getName());
					}
				}
			}

			cacheServiceAO.insertCacheHot(itemHotDto);
			return itemHotDto;
		}
		return null;
	}

	private String genItemAdvImage(String title,Double price,Double originPrice,int discount,String imageField,String oldAdvImageFields )throws Exception{
		if(StringUtils.isBlank(imageField)||
				StringUtils.isBlank(title)||
				null==originPrice||
				null==price){
			return null;
		}
		File destFileDir = new File(uploadTempPath);
		if (!destFileDir.exists()) {
			destFileDir.mkdirs();
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(title);
		sb.append(price);
		sb.append(originPrice);
		sb.append(discount);
		sb.append(imageField);
		
		//获取原广告图片，并进行meta比较，如果meta相同，则不进行图片生成，否则重新生成
		logger.info(">>>> 获取原广告图片，并进行meta比较，如果meta相同，则不进行图片生成，否则重新生成 {}",sb);
		if(StringUtils.isNotBlank(oldAdvImageFields)){
			Map<MetaDataKey, String> metas = dfsService.getFileMetaData(oldAdvImageFields);
			if(null!=metas){
				String exInfo = metas.get(MetaDataKey.EXINFO);
				if(exInfo.equals(sb.toString())){
					return oldAdvImageFields;
				}
			}
		}
		
		//下载原图
		logger.info(">>>> 下载原图 {} abspath={}",imageField,destFileDir.getAbsolutePath());
		byte[] data = dfsService.getFileBytes(imageField);
		if(null==data){
			return null;
		}
		String suffix = imageField.substring(imageField.lastIndexOf("."));
		File originBigImage = File.createTempFile("TMP", suffix, destFileDir);
		IOUtils.write(data, new FileOutputStream(originBigImage));
		
		//缩放图片为65
		logger.info(">>>> 缩放图片为80 ");
		ImgFile imgFile=new ImgFile();
		imgFile.setFile(originBigImage);
		
		if(".jpg".equals(suffix)){//对其他格式 .png等的支持
			imgFile.setInterlace(true);
		}
		imgFile.setRawWidth(ImageWidthNorms.WIDTH80);
		imgFile.handle();
		File smallFile = imgFile.getFile();
		
		File out = DrawAdvImage(title, price, originPrice, discount,
				destFileDir, suffix, smallFile);

		//上传图片
		logger.info(">>>> 上传图片到dfs ");
		ImgFile advImgFile = new ImgFile();
		advImgFile.setFile(out);
		Map<MetaDataKey, String> metaData = new HashMap<MetaDataKey, String>();
		metaData.put(MetaDataKey.EXINFO, sb.toString());
		advImgFile.setMetaData(metaData);
		String advImageField = dfsService.uploadFile(advImgFile);
		
		logger.info(">>>> 删除临时文件 ");
		//原图
		originBigImage.delete();
		//原图缩略图
		imgFile.clear();
		smallFile.delete();
		//生成的广告图
		out.delete();
		//生成的广告图上传产生的临时文件
		advImgFile.clear();
		logger.info(">>>> 处理完毕 {} ",advImageField);
		return advImageField;
	}
	/**
	 * 绘制广告图片
	 * @param title
	 * 		商品标题
	 * @param price
	 * 		商品当前价
	 * @param originPrice
	 * 		商品原价
	 * @param discount
	 * 		折扣
	 * @param destFileDir
	 * 		目标文件夹
	 * @param fileFormat
	 * 		文件类型
	 * @param productImage
	 * 		商品图片
	 * @return
	 * @throws IOException
	 */
	private File DrawAdvImage(String title, Double price, Double originPrice,
			int discount, File destFileDir, String fileFormat, File productImage)
			throws IOException {
		URL url = this.getClass().getResource(ADV_TEMPLATE);
		String path = url.getPath();
		File advTemplate = new File(path);
		BufferedImage	advTemplateImage = ImageIO.read(advTemplate);
		
		Graphics2D g = (Graphics2D)advTemplateImage.getGraphics();
		//设置画笔
		g.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		//消除锯齿
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		BufferedImage pimage = ImageIO.read(productImage);
		
		//绘制商品图片
		logger.info(">>>> 绘制商品图片 ");
		g.drawImage(pimage, 15, 50, null);
		
		int fontSize = 26;
		Font fontFamily = LoadfontUtils.loadFontByName(LoadfontUtils.FONT_MSYH);
		fontFamily = fontFamily.deriveFont(fontSize*1.0f);
		
		//绘制标题
		logger.info(">>>> 绘制标题 font= "+fontFamily);
		int x = 114;
		int y = 85;
		//#666666
		g.setColor(COLOR_666);
		g.setFont(fontFamily);
		title = subString(title, 34);
		g.drawString(title, x, y);
		
		//绘制西客价
		logger.info(">>>> 绘制西客价 ");
		String priceStr = "西客价：";
		y = 128;
		fontSize = 22;
		fontFamily = fontFamily.deriveFont(fontSize*1.0f);
		//#999999
		g.setColor(COLOR_999);
		g.drawString(priceStr, x, y);
		
		//绘制西客价格
		String mtPrice = "¥"+price;
		x = 225;
		fontSize = 28;
		fontFamily = fontFamily.deriveFont(fontSize*1.0f);
		g.setFont(fontFamily);
		//#ff9701
		g.setColor(COLOR_MT_PRICE);
		g.drawString(mtPrice, x, y);
		
		if(discount<10){
			//绘制原价
			logger.info(">>>> 绘制原价");
			String originPriceStr = String.valueOf(originPrice);
			fontSize=22;
			fontFamily = fontFamily.deriveFont(fontSize*1.0f);
			Map<TextAttribute, Object> maps = new HashMap<TextAttribute, Object>();
			maps.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
			maps.put(TextAttribute.FONT, fontFamily);
			maps.put(TextAttribute.SIZE, fontSize);
			g.setFont(new Font(maps));
			x = x+(int)g.getFontMetrics().getStringBounds(mtPrice, g).getWidth()+30;
			//#999999
			g.setColor(COLOR_999);
			g.drawString(originPriceStr, x, y);
			
			//绘制折扣底图
			logger.info(">>>> 绘制折扣底图 ");
			x = x+(int)g.getFontMetrics().getStringBounds(originPriceStr, g).getWidth()+15;
			y = 102;
			int ow = 100;
			int oh = 30;
			//#ff9701
			g.setColor(COLOR_MT_PRICE);
			Shape shape = new RoundRectangle2D.Double(x, y, ow, oh, 1D, 1D);  
	        g.draw(shape);   
	        g.fill(shape);
	        
	        //绘制折扣
	        logger.info(">>>> 绘制折扣 ");
	        String discountStr = discount+"折";
	        y = 128;
	        x = x+(ow/3);
	        g.setColor(Color.WHITE);
	        fontSize = 24;
	        fontFamily = fontFamily.deriveFont(fontSize*1.0f);
	        g.setFont(fontFamily);
	        g.drawString(discountStr, x, y);
		}
        
        //绘图完毕
        logger.info(">>>> 绘图完毕 ");
		g.dispose();
		
		//将图片写入硬盘
		logger.info(">>>> 将图片写入硬盘 ");
		File out = File.createTempFile("TMP", fileFormat, destFileDir);
		ImageIO.write(advTemplateImage, "jpg", out);
		return out;
	}
	
//	public static void main(String[] args)throws Exception {
////		File originBigImage = new File("/Users/beck/Desktop/big.jpg");
////		ImgFile imgFile=new ImgFile();
////		imgFile.setFile(originBigImage);
////		imgFile.setInterlace(true);
////		imgFile.setRawWidth(ImageWidthNorms.WIDTH80);
////		imgFile.handle();
//		
//		String basePath = "E:\\images\\";
//		ItemServiceAO ao = new ItemServiceAO();
//		File productImage = new File(basePath+"a.jpg");
//		String title = "康贝 柔湿巾'80片三联包有效预防红";
//		title=subString(title, 34);
//		Double price = 15.8;
//		Double originPrice = 15.8;
//		int discount = 10;
//		File destFileDir = new File(basePath);
//		String fileFormat = productImage.getName().substring(productImage.getName().indexOf("."));
//		
//		ao.DrawAdvImage(title, price, originPrice, discount, destFileDir, fileFormat, productImage);
//		
//	}
	
	/**
	 * 文本截取，按照给定的长度截取字符，一个中文占两个英文字符宽度
	 * 
	 * @param input
	 *            输入文本
	 * @param maxLen
	 *            英文字符宽度，最大值
	 * @return
	 */
	public static String subString(String input, Integer maxLen) {
		if (StringUtils.isNotBlank(input)) {
			int len = 0;
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < input.length(); i++) {
				char c = input.charAt(i);
				if (isLetter(c)) {
					len = len + 1;
				} else {
					len = len + 2;
				}
				if (len > maxLen) {
					break;
				}
				sb.append(c);
			}
			if (len <= maxLen) {
				return input;
			}
			return sb.toString() + "...";
		} else {
			return input;
		}
	}
	
	/**
	 * 判断是不是字母数字或单字节字符
	 * @param c
	 * @return
	 */
	public static boolean isLetter(char c){
		int k = 0x80;
		return c/k==0?true:false;
	}
	
	/**
	 * 获取优质商品
	 * @param startPage
	 * @return
	 */
	/*public List<ItemHighQualityDto> selectHighQuality(ItemHighQualityQuery highQualityQuery){
		List<ItemHighQualityDto> list = new ArrayList<ItemHighQualityDto>();
		
		ItemDetail detailDO = new ItemDetail();
		detailDO.setQualityGoods(ItemDetailConstant.HIGH_QUALITY);
		detailDO.setPageSize(highQualityQuery.getPageSize());
		detailDO.setStartPage(highQualityQuery.getPageNo());
		
		List<DetailDO> detailDOList = itemDetailService.selectDynamicPageQuery(detailDO);
		List<Long> detailIds = new ArrayList<Long>();
		if(CollectionUtils.isNotEmpty(detailDOList)){
			for(DetailDO detailDOTemp : detailDOList){
				detailIds.add(detailDOTemp.getId());
			}
		}
		
		List<SkuDO> listSku =  itemSkuService.selectByDetailIds(detailIds);
		if(CollectionUtils.isNotEmpty(listSku)){
			for(SkuDO skuDOTemp:listSku){
				ItemHighQualityDto itemhighQuality=new ItemHighQualityDto();
				for(DetailDO detailDOTemp : detailDOList){
					if(skuDOTemp.getDetailId().longValue() == detailDOTemp.getId().longValue()){
						itemhighQuality.setAgeEndKey(detailDOTemp.getAgeEndKey());
						itemhighQuality.setAgeStartKey(detailDOTemp.getAgeStartKey());
						itemhighQuality.setSku(skuDOTemp.getSku());
//						itemhighQuality.setDetailId(detailDOTemp.getId());
						list.add(itemhighQuality);
					}
				}
			}
		}
		
		return list;
	}*/
	
	private final static String excelPath = "template/sku-template.xls";
	private final static String hitaoExcelPath="template/hitao-sku-template.xls";
	public void downloadBaseData(HttpServletRequest request, HttpServletResponse response,String waveSign) {

		// 供应商列表
		List<SupplierInfo> supplierList =  new ArrayList<SupplierInfo>();
		List<SupplierType> supplierTypeList = new ArrayList<SupplierType>();
		supplierTypeList.add(SupplierType.PURCHASE);
		supplierTypeList.add(SupplierType.ASSOCIATE);
		supplierTypeList.add(SupplierType.SELL);
		int batchNums = 1000;
		SupplierResult result = purchasingManagementService.getSuppliersByTypes(supplierTypeList,1, batchNums);
		supplierList.addAll(result.getSupplierInfoList());
		int totalCount = 0; 
		if(null!=result){
			totalCount = result.getTotalCount().intValue();
		}
		int pageNums = totalCount % batchNums > 0 ? (totalCount / batchNums) +1 : (totalCount / batchNums);
		if(pageNums>=2){
			for(int i = 2 ;i <= pageNums;i++){
				SupplierResult result1 =purchasingManagementService.getSuppliersByTypes(supplierTypeList, i, batchNums);
				supplierList.addAll(result1.getSupplierInfoList());
			}
		}
		
		String[][] data1 = null;
		String[][] data2 = null;
		String[][] data3 = null;
		String[][] data4 = null;
		String[][] data5 = null;
		String[][] data6 = null;
		String[][] data7 = null;
		String[][] data8 = null;
		String[][] data9 = null;
		String[][] data10 = null;
		String[][] data11 = null;
		String[][] data12 = null;

		if (null != supplierList && !supplierList.isEmpty()) {
			data1 = new String[supplierList.size()][2];
			int i = 0;
			for (SupplierInfo s : supplierList) {
				data1[i][0] = s.getName();
				data1[i][1] = s.getId().toString();
				i++;
			}
		}

		// 品牌
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("status", Constant.ENABLED.YES);
		List<Brand> brandList = brandService.queryByParam(params);
		if (null != brandList && !brandList.isEmpty()) {
			data2 = new String[brandList.size()][2];
			int i = 0;
			for (Brand s : brandList) {
				data2[i][0] = s.getName();
				data2[i][1] = s.getId().toString();
				i++;
			}
		}
		// 分类
		params.clear();
		params.put("status", Constant.ENABLED.YES);
		List<Category> categoryList = categoryService.queryByParam(params);
		List<Category> categorySmallList = new ArrayList<Category>();
		Map<Long ,Category> cateogryLargeMap = new HashMap<Long,Category>();
		Map<Long ,Category> cateogryMediumMap = new HashMap<Long,Category>();
		for (Category c : categoryList) {
			if (c.getLevel().equals(1)) {
				cateogryLargeMap.put(c.getId(), c);
			} else if (c.getLevel().equals(2)) {
				cateogryMediumMap.put(c.getId(), c);
			} else if (c.getLevel().equals(3)) {
				categorySmallList.add(c);
			}
		}

		//大类	大类ID	中类	中类ID	小类	小类ID
		if (null != categorySmallList && !categorySmallList.isEmpty()) {
			data3 = new String[categorySmallList.size()][6];
			int i = 0 ;
			for(Category smallCategory : categorySmallList){
				
				Set<Long> mediumkey = cateogryMediumMap.keySet();
				String largeName = "";
				Long largeId = 0l ;
				String mediumName = "";
				Long mediumId = 0l ;
				String smallName = smallCategory.getName();
				Long smallId = smallCategory.getId() ;
				for (Iterator<Long> it = mediumkey.iterator(); it.hasNext();) {
					Long id = it.next();
					if(id.equals(smallCategory.getParentId())){
						Category category = cateogryMediumMap.get(id);
						mediumId = id;
						largeId  = category.getParentId();
						mediumName = category.getName();
						break;
					}
				}
				Set<Long> largekey = cateogryLargeMap.keySet();
				for (Iterator<Long> it = largekey.iterator(); it.hasNext();) {
					Long id = it.next();
					if(id.equals(largeId)){
						Category category = cateogryLargeMap.get(id);
						largeName  = category.getName();
						break;
					}
				}
				data3[i][0]=largeName;
				data3[i][1]=largeId.toString();
				data3[i][2]=mediumName;
				data3[i][3]=mediumId.toString();
				data3[i][4]=smallName;
				data3[i][5]=smallId.toString();
				i++;
			}
		}
		

		// 销售属性组
		SpecGroup specGroupDO = new SpecGroup();
		specGroupDO.setStatus(Constant.ENABLED.YES);
		List<SpecGroupResult>  specGroupList = specGroupService.getAllSpecGroupResult();
		
		if (null != specGroupList && !specGroupList.isEmpty()) {
			int specSize = 0; 
			for (SpecGroupResult specGroupResult : specGroupList) {
				if(CollectionUtils.isNotEmpty(specGroupResult.getSpecDoList())){
					 specSize += specGroupResult.getSpecDoList().size();
				}
			}
			
			data4 = new String[specSize][4];
			int i = 0;
			for (SpecGroupResult specGroupResult : specGroupList) {
				SpecGroup specGroup=  specGroupResult.getSpecGroup();
				String specGroupName =  specGroup.getName();
				Long specGroupId = specGroup.getId();
				if(CollectionUtils.isNotEmpty(specGroupResult.getSpecDoList())){
					for(Spec spec: specGroupResult.getSpecDoList()){
						data4[i][0] = specGroupName;
						data4[i][1] = specGroupId.toString();
						data4[i][2] = spec.getSpec();
						data4[i][3] = spec.getId().toString();
						i++;
					}
				}
			}
		}

		// 单位
		params.clear();
		params.put("code", DictionaryCode.c1001.getCode());
		List<DictionaryInfo> unitList = dictionaryInfoService.queryByParam(params);

		if (null != unitList && !unitList.isEmpty()) {
			data5 = new String[unitList.size()][2];
			int i = 0;
			for (DictionaryInfo s : unitList) {
				data5[i][0] = s.getName();
				data5[i][1] = s.getId().toString();
				i++;
			}
		}

		// 运费模板
		List<FreightTemplate> freightTemplateList = new ArrayList<FreightTemplate>();
		try {
			freightTemplateList =freightTemplateService
					.selectByCalculateMode(ItemConstant.SINGLE_FREIGHTTEMPLATE_STATUS);
			if (null != freightTemplateList && !freightTemplateList.isEmpty()) {
				data6 = new String[freightTemplateList.size()][2];
				int i = 0;
				for (FreightTemplate s : freightTemplateList) {
					data6[i][0] = s.getName();
					data6[i][1] = s.getId().toString();
					i++;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		//国家，海关，税率
		if(waveSign.equals("isWave")){
			// 增值税率
			params.clear();
			params.put("type", TaxRateEnum.ADDEDVALUE.getType());
			params.put("status", Constant.ENABLED.YES);
			
			List<TaxRate> addedValueRateList = taxRateProxy.queryByParam(params).getData();
			if(CollectionUtils.isNotEmpty(addedValueRateList)){
				data8 = new String[addedValueRateList.size()][2];
				int i = 0;
				for (TaxRate s : addedValueRateList) {
					data8[i][0] = s.getRate()+"% - "+ s.getRemark();
					data8[i][1] = s.getId().toString();
					i++;
				}
				
			}
			// 消费税率
			params.clear();
			params.put("type", TaxRateEnum.EXCISE.getType());
			params.put("status", Constant.ENABLED.YES);
	        
			List<TaxRate> exciseRateList = taxRateProxy.queryByParam(params).getData();
			if(CollectionUtils.isNotEmpty(exciseRateList)){
				data9 = new String[exciseRateList.size()][2];
				int i = 0;
				for (TaxRate s : exciseRateList) {
					data9[i][0] = s.getRate()+"% - "+ s.getRemark();
					data9[i][1] = s.getId().toString();
					i++;
				}
				
			}
			// 关税率
			params.clear();
			params.put("type", TaxRateEnum.CUSTOMS.getType());
			params.put("status", Constant.ENABLED.YES);
	        
			List<TaxRate> customsRateList = taxRateProxy.queryByParam(params).getData();
			if(CollectionUtils.isNotEmpty(customsRateList)){
				data10 = new String[customsRateList.size()][2];
				int i = 0;
				for (TaxRate s : customsRateList) {
					data10[i][0] = s.getRate()+"% - "+ s.getRemark();
					data10[i][1] = s.getId().toString();
					i++;
				}
				
			}
			// 国家
			List<DistrictInfo>  countryList = itemProxy.getAllCountryList();
			if(CollectionUtils.isNotEmpty(countryList)){
				data11 = new String[countryList.size()][2];
				int i = 0;
				for (DistrictInfo s : countryList) {
					data11[i][0] = s.getName();
					data11[i][1] = s.getId().toString();
					i++;
				}
			}
			
			List<ClearanceChannels>  list  = clearanceChannelsService.getAllClearanceChannelsByStatus(2); 
			
			if(CollectionUtils.isNotEmpty(list)){
				data12 = new String[list.size()][2];
				int i = 0 ;
				for (ClearanceChannels s : list) {
					data12[i][0] = s.getName();
					data12[i][1] = s.getId().toString();
					i++;
				}
			}
		
		}
		
		

		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", -1);
        response.setContentType("application/x-download");
		response.addHeader("Content-Disposition", "attachment; filename="
				+ "import_sku.xls");
		String savePath =  ItemImportProxy.class.getResource("/").getPath()+excelPath;
		if(waveSign.equals("isWave")){
			savePath =  ItemImportProxy.class.getResource("/").getPath()+hitaoExcelPath;
		}
		
		File file = new File(savePath);
		byte[] buff;
		ServletOutputStream out = null;
		try {
			Workbook wb  = null ;
			wb = writeExcelTemplate(file, 0, data2,data3,data4, data5, data1,data8,data9,data10,data11,data12);
			FileOutputStream fileoutputstream = new FileOutputStream(file);
			wb.write(fileoutputstream);
			out = response.getOutputStream();
			buff = FileUtils.readFileToByteArray(file);
			if (null == buff) {
				throw new FileNotFoundException("服务器没有找到模板!");
			}
			int len = buff.length;
			out.write(buff, 0, len);
			response.setStatus(HttpServletResponse.SC_OK);
			response.flushBuffer();
		} catch (InvalidFormatException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} catch (org.apache.poi.openxml4j.exceptions.InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	private Workbook writeExcelTemplate(File templateFile, int sheetIndex,
			String [][] data2,String [][] data3,String[][] data4,
			String[][] data5,String[][] data6,
			String[][] data8,String[][] data9, String[][]data10,
			String[][] data11,String[][] data12)
			throws InvalidFormatException, IOException, org.apache.poi.openxml4j.exceptions.InvalidFormatException {
		
		InputStream in  = new FileInputStream(templateFile);
		Workbook wb = WorkbookFactory.create(in);
		
		// 遍历Excel Sheet， 依次读取里面的内容
		if (sheetIndex > wb.getNumberOfSheets()) {
			return null;
		}
		Sheet sheet = wb.getSheetAt(sheetIndex);
		// 遍历表格的每一行
		int rowStart = sheet.getFirstRowNum();
		// 写EXCEL数据区域内容
		int colNum = 0;
		writeExcelTemplate(sheet, rowStart + 1,colNum, data2);
		colNum += 2;
		writeExcelTemplate(sheet, rowStart + 1,colNum, data3);
		colNum += 6;
		writeExcelTemplate(sheet, rowStart + 1,colNum, data4);
		colNum += 4;
		writeExcelTemplate(sheet, rowStart + 1,colNum, data5);
		colNum += 2;
		writeExcelTemplate(sheet, rowStart + 1,colNum, data6);
		
		if(null!=data8&&data8.length>0){
			colNum += 2;
			writeExcelTemplate(sheet, rowStart + 1,colNum, data8);
		}
		if(null!=data9&&data9.length>0){
			colNum += 2;
			writeExcelTemplate(sheet, rowStart + 1,colNum, data9);
		}
		if(null!=data10&&data10.length>0){
			colNum += 2;
			writeExcelTemplate(sheet, rowStart + 1,colNum, data10);
		}
		if(null!=data11&&data11.length>0){
			colNum += 2;
			writeExcelTemplate(sheet, rowStart + 1,colNum, data11);
		}
		if(null!=data12&&data12.length>0){
			colNum += 2;
			writeExcelTemplate(sheet, rowStart + 1,colNum, data12);
		}
		
		return wb;
	}
	
	public void writeExcelTemplate(Sheet sheet,int rowIndex,int colIndex,String [] [] datas){
		if(null!=datas){
			//写入数据到模板中
			for(int i = 0 ;i < datas.length; i++){
				Row row = sheet.getRow(i+1);
				if(null == row ){
					row = sheet.createRow(i + 1);
				}
				for(int j= 0 ; j< datas[i].length; j ++){
					Cell cell = row.createCell((short)(colIndex+j));
					cell.setCellValue(datas[i][j]);
				}
			}
		}
	}
}
