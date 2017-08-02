package com.tp.seller.ao.base;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.tp.common.vo.supplier.CommonUtil;
import com.tp.common.vo.supplier.SupplierConstant;
import com.tp.dfsutils.constants.MetaDataKey;
import com.tp.dfsutils.file.GeneralFile;
import com.tp.dfsutils.service.DfsService;

/**
 * <pre>
 * 供应商图片上传
 * </pre>
 *
 * @author Administrator
 * @version $Id: SupplierUploadAO.java, v 0.1 2014年12月25日 下午1:28:35 Administrator Exp $
 */
@Service
public class SellerUploadAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(SellerUploadAO.class);

    @Autowired
    private DfsService dfsService;

    private final static SimpleDateFormat fileNameFormat = new SimpleDateFormat("yyyy-MM-dd");

    public final static String EXCEL_FORMAT = "application/vnd.ms-excel";
    public final static String IMAGE_FORMAT = "image/jpeg";

    @Value("${seller.upload.tmp.path}")
    private String uploadTempPath;

    /**
     * 文件上传
     *
     * @param request
     * @param fileName
     * @param dest
     * @return
     * @throws Exception
     */
    public Map<String, Object> uploadFile(final HttpServletRequest request, final String fileName) throws Exception {
        final Map<String, Object> retMap = new HashMap<String, Object>();
        File retFile = null;
        if (request instanceof MultipartHttpServletRequest) {
            final MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            final MultipartFile multipartFile = multipartRequest.getFile(fileName);
            if (null == multipartFile || multipartFile.isEmpty()) {
                retMap.put(SupplierConstant.SUCCESS_KEY, false);
                retMap.put(SupplierConstant.MESSAGE_KEY, "找不到文件：" + fileName);
                return retMap;
                // throw new Exception("找不到文件："+fileName);
            }
            final long fileSize = multipartFile.getSize();
            final Map<String, Object> fileSizeCheckMap = CommonUtil.checkFileSize(fileSize, multipartFile.getOriginalFilename());
            if (!(Boolean) fileSizeCheckMap.get(SupplierConstant.SUCCESS_KEY)) {
                retMap.put(SupplierConstant.SUCCESS_KEY, false);
                retMap.put(SupplierConstant.MESSAGE_KEY, fileSizeCheckMap.get(SupplierConstant.MESSAGE_KEY));
                return retMap;
            }
            final String newName = generateFileName();
            final String format = CommonUtil.getFileFormat(multipartFile.getOriginalFilename());
            final File destFile = new File(uploadTempPath + "/" + fileNameFormat.format(new Date()));
            if (!destFile.exists()) {
                destFile.mkdirs();
            }
            retFile = new File(destFile, newName + "." + format);
            FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), retFile);
            final String upFile = uploadFile(retFile);
            if (null == upFile) {
                retMap.put(SupplierConstant.SUCCESS_KEY, false);
                retMap.put(SupplierConstant.MESSAGE_KEY, "文件上传到错误。");
                return retMap;
            }
            retMap.put(SupplierConstant.SUCCESS_KEY, true);
            retMap.put(SupplierConstant.FILE_SIZE_KEY, fileSize);
            retMap.put(SupplierConstant.UPLOADED_FILE_KEY, upFile);
            retMap.put(SupplierConstant.UPLOADED_FILE_REAL_PATH, upFile);
        } else {
            LOGGER.info("request请求类型不对。");
            // throw new Exception("request请求类型不对。");
        }
        return retMap;
    }

    /**
     * 下载相应文件
     *
     * @param response
     * @param file
     * @param type
     * @throws IOException
     */
    public void download(final HttpServletRequest request, final HttpServletResponse response, final String fileId) {
        ServletOutputStream out = null;
        try {
            final String fileName = getFileName(fileId);
            response.setContentType(getContentType(fileName));
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", -1);
            response.addHeader("Content-Disposition", "attachment; filename=" + fileName);

            out = response.getOutputStream();
            final byte[] buff = dfsService.getFileBytes(fileId);
            if (null == buff) {
                throw new FileNotFoundException("dfs未找到：" + fileId);
            }
            final int len = buff.length;
            out.write(buff, 0, len);
            response.setStatus(HttpServletResponse.SC_OK);
            response.flushBuffer();
        } catch (final Exception e) {
            LOGGER.error("异常：", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (final IOException e) {
                    LOGGER.error("", e);
                }
            }
        }
    }

    /**
     * 文件下载
     *
     * @param response
     * @param file
     */
    public void download(final HttpServletResponse response, final File file) {
        ServletOutputStream out = null;
        try {
            // 以流的形式下载文件。
            final InputStream fis = new BufferedInputStream(new FileInputStream(file));
            final byte[] buff = new byte[fis.available()];
            fis.read(buff);
            fis.close();

            out = response.getOutputStream();
            if (null == file) {
                throw new FileNotFoundException("未找到要下载的文件");
            }
            final int len = buff.length;
            out.write(buff, 0, len);

            response.setContentType(getContentType(file.getName()));
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", -1);
            response.addHeader("Content-Disposition", "attachment; filename=" + file.getName());
            response.setStatus(HttpServletResponse.SC_OK);
            response.flushBuffer();
            // 清空response
        } catch (final Exception e) {
            LOGGER.error("异常：{}", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (final IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * <pre>
     * 获取文件名称
     * </pre>
     *
     * @return
     */
    private String getFileName(final String fileId) {
        final String format = CommonUtil.getFileFormat(fileId);
        return generateFileName() + "." + format;
    }

    /**
     * 生成文件名称 uuid
     *
     * @return
     */
    public static String generateFileName() {
        return UUID.randomUUID().toString();
    }

    /**
     * <pre>
     *   文件上传  没有任何压缩处理
     *   现在只支持rar zip 和基本的图片格式
     * </pre>
     *
     * @param file
     * @return
     */
    public String uploadFile(final File file) {
        final GeneralFile info = new GeneralFile();
        String fileId = null;
        info.setFile(file);
        final Map<MetaDataKey, String> map = new HashMap<MetaDataKey, String>();
        map.put(MetaDataKey.FILENAME, file.getName());
        map.put(MetaDataKey.CREATOR, SupplierConstant.UPLOAD_CREATOR);
        info.setMetaData(map);
        try {
            fileId = dfsService.uploadFile(info);
        } catch (final Exception e) {
            LOGGER.error("上传文件异常！", e);
        }
        return fileId;
    }

    /**
     * 根据文件名称获取下载方式
     *
     * @param fileType
     * @return
     */
    private String getContentType(final String fileName) {
        final String ImageFormat = ".JPG.JPEG.GIF.PNG.BMP";
        if (null == fileName) {
            // 默认为excel下载方式
            return EXCEL_FORMAT;
        }
        if (fileName.endsWith("xls") || fileName.endsWith("xlsx")) {
            return EXCEL_FORMAT;
        }
        final String fileFormat = CommonUtil.getFileFormat(fileName);
        if (null == fileFormat) {
            return EXCEL_FORMAT;
        }
        if (ImageFormat.indexOf(fileFormat.toUpperCase()) > -1) {
            return IMAGE_FORMAT;
        }
        return EXCEL_FORMAT;
    }

    /**
     * @deprecated <pre>
     * 上传压缩文件
     * </pre>
     * @return
     */
    // private String uploadRarOrZipFile(File file) {
    // TxtFile info=new TxtFile();
    // String fileId = null;
    // info.setFile(file);
    // Map<MetaDataKey,String> map=new HashMap<MetaDataKey,String>();
    // map.put(MetaDataKey.FILENAME, file.getName());
    // map.put(MetaDataKey.CREATOR, SupplierConstant.UPLOAD_CREATOR);
    // info.setMetaData(map);
    // try {
    // fileId = dfsService.uploadFile(info);
    // } catch(Exception e){
    // logger.error("上传文件异常！",e);
    // }
    // return fileId;
    // }

    /**
     * 上传图片类型 </pre>
     * 
     * @return
     */
    // private String uploadImgFile(File file) {
    // ImgFile info=new ImgFile();
    // String fileId = null;
    // info.setFile(file);
    // Map<MetaDataKey,String> map=new HashMap<MetaDataKey,String>();
    // map.put(MetaDataKey.FILENAME, file.getName());
    // map.put(MetaDataKey.CREATOR, SupplierConstant.UPLOAD_CREATOR);
    // info.setMetaData(map);
    // try {
    // fileId = dfsService.uploadFile(info);
    // } catch(Exception e){
    // logger.error("上传文件异常！",e);
    // }
    // return fileId;
    // }

    /**
     * 设置下载文件中文件的名称
     *
     * @param filename
     * @param request
     * @return
     */
    public static String encodeFilename(final String filename, final HttpServletRequest request) {
        final String agent = request.getHeader("USER-AGENT");
        try {
            if ((agent != null) && (-1 != agent.indexOf("MSIE") || -1 != agent.indexOf("rv:11.0"))) {
                String newFileName = URLEncoder.encode(filename, "UTF-8");
                newFileName = StringUtils.replace(newFileName, "+", "%20");
                if (newFileName.length() > 150) {
                    newFileName = new String(filename.getBytes("GB2312"), "ISO8859-1");
                    newFileName = StringUtils.replace(newFileName, " ", "%20");
                }
                return newFileName;
            }
            return filename;
        } catch (final Exception ex) {
            return filename;
        }
    }

}
