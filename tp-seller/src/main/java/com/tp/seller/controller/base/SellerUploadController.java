package com.tp.seller.controller.base;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.common.vo.supplier.SupplierConstant;
import com.tp.seller.ao.base.SellerUploadAO;

/**
 * <pre>
 * 供应商文件上传
 * </pre>
 *
 * @author 谢云峰
 * @version $Id: SupplierUploadController.java, v 0.1 2014年12月26日 上午10:27:58 Administrator Exp $
 */
@Controller
@RequestMapping("/seller/upload/")
public class SellerUploadController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SellerUploadAO sellerUploadAO;

    @RequestMapping("fileUpload")
    @ResponseBody
    public Map<String,Object> fileUpload(final HttpServletRequest request, @RequestParam(value = "fileName", required = true) final String fileName) {
        final Map<String, Object> map = new HashMap<String, Object>();
        try {
            final Map<String, Object> retMap = sellerUploadAO.uploadFile(request, fileName);
            if ((Boolean) retMap.get(SupplierConstant.SUCCESS_KEY)) {
                map.put(SupplierConstant.SUCCESS_KEY, true);
                map.put("fileUrl", retMap.get(SupplierConstant.UPLOADED_FILE_KEY));
                map.put("absUrl", retMap.get(SupplierConstant.UPLOADED_FILE_REAL_PATH));
            } else {
                map.put(SupplierConstant.SUCCESS_KEY, false);
                map.put(SupplierConstant.MESSAGE_KEY, retMap.get(SupplierConstant.MESSAGE_KEY));
            }
        } catch (final Exception e) {
            logger.error("文件上传异常：", e);
        }
        return map;
    }

    @RequestMapping("download")
    public void downloadAction(final HttpServletRequest request, @RequestParam(value = "fileId", required = true) final String fileId,
        final HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");
        sellerUploadAO.download(request, response, fileId);
    }

}
