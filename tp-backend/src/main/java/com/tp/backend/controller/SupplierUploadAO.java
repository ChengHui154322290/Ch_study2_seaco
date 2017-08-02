package com.tp.backend.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
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
import com.tp.dfsutils.file.ImgFile;
import com.tp.dfsutils.file.TxtFile;
import com.tp.service.IUploadService;

/**
 * <pre>
 * 供应商图片上传
 * </pre>
 *
 * @author Administrator
 * @version $Id: SupplierUploadAO.java, v 0.1 2014年12月25日 下午1:28:35 Administrator Exp $
 */
@Service
public class SupplierUploadAO {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IUploadService uploadService;

    private final static SimpleDateFormat fileNameFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Value("${upload.tmp.path}")
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
            retMap.put(SupplierConstant.UPLOADED_FILE_REAL_PATH, uploadService.upload(retFile));
        } else {
            logger.info("Request type is not MultipartHttpServletRequest。");
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
        response.setContentType("text/html;charset=utf-8");
        ServletOutputStream out = null;
        try {
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", -1);
            response.addHeader("Content-Disposition", "attachment; filename=" + getFileName(fileId));

            out = response.getOutputStream();
            File file = uploadService.downFile(fileId);
            //out.write(file.get);(new BufferedOutputStream(new FileOutputStream(file.getAbsoluteFile())));
            response.setStatus(HttpServletResponse.SC_OK);
            response.flushBuffer();
        } catch (final Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (final IOException e) {
                    logger.error(e.getMessage(), e);
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
            fileId = uploadService.upload(file);
        } catch (final Exception e) {
            logger.error("File upload exception :");
            logger.error(e.getMessage(), e);
        }
        return fileId;
    }

    /**
     * <pre>
     * 上传压缩文件
     * </pre>
     *
     * @return
     */
    @SuppressWarnings("unused")
    @Deprecated
    private String uploadRarOrZipFile(final File file) {
        final TxtFile info = new TxtFile();
        String fileId = null;
        info.setFile(file);
        final Map<MetaDataKey, String> map = new HashMap<MetaDataKey, String>();
        map.put(MetaDataKey.FILENAME, file.getName());
        map.put(MetaDataKey.CREATOR, SupplierConstant.UPLOAD_CREATOR);
        info.setMetaData(map);
        try {
        	fileId = uploadService.upload(file);
        } catch (final Exception e) {
            logger.error("File upload exception :");
            logger.error(e.getMessage(), e);
        }
        return fileId;
    }

    /**
     * <pre>
     * 上传图片类型
     * </pre>
     *
     * @return
     */
    @SuppressWarnings("unused")
    @Deprecated
    private String uploadImgFile(final File file) {
        final ImgFile info = new ImgFile();
        String fileId = null;
        info.setFile(file);
        final Map<MetaDataKey, String> map = new HashMap<MetaDataKey, String>();
        map.put(MetaDataKey.FILENAME, file.getName());
        map.put(MetaDataKey.CREATOR, SupplierConstant.UPLOAD_CREATOR);
        info.setMetaData(map);
        try {
        	fileId = uploadService.upload(file);
        } catch (final Exception e) {
            logger.error("File upload exception :");
            logger.error(e.getMessage(), e);
        }
        return fileId;
    }

}
