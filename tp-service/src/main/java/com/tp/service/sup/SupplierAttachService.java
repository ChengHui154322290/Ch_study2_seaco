package com.tp.service.sup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.Constant;
import com.tp.dao.sup.SupplierAttachDao;
import com.tp.dao.sup.SupplierImageDao;
import com.tp.model.sup.SupplierAttach;
import com.tp.model.sup.SupplierImage;
import com.tp.service.BaseService;
import com.tp.service.sup.ISupplierAttachService;

@Service
public class SupplierAttachService extends BaseService<SupplierAttach> implements ISupplierAttachService {

	@Autowired
	private SupplierAttachDao supplierAttachDao;
	@Autowired
	private SupplierImageDao supplierImageDao;
	
	@Override
	public BaseDao<SupplierAttach> getDao() {
		return supplierAttachDao;
	}

    @Override
    public SupplierAttach getSupplierBySupplierId(final Long supplierId) {
    	Map<String,Object> params = new HashMap<String,Object>();
    	params.put("supplierId", supplierId);
    	params.put("status", Constant.DEFAULTED.YES);
    	SupplierAttach supplierAttach = super.queryUniqueByParams(params);
        if (null != supplierAttach) {
            setSupplierImageList(supplierAttach, supplierAttach.getSupplierId());
        }
        return supplierAttach;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateAttachInfoBySupplierId(final SupplierAttach supplierAttach){
        if (null == supplierAttach || supplierAttach.getSupplierId() == null) {
            return;
        }
        supplierAttach.setStatus(Constant.DEFAULTED.YES);
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("supplierId", supplierAttach.getSupplierId());
        params.put("status", Constant.DEFAULTED.YES);
        final List<SupplierAttach> supplierAttachs = supplierAttachDao.queryByParam(params);
        if (CollectionUtils.isNotEmpty(supplierAttachs)) {
            final SupplierAttach attachDoOld = supplierAttachs.get(0);
            supplierAttach.setId(attachDoOld.getId());
            supplierAttachDao.updateNotNullById(supplierAttach);
        }
        // 更新附件主体信息
        updateBySupplierIdBatch(supplierAttach.getSupplierImageList(), supplierAttach.getSupplierId());
    }

    /**
     * 批量更新图片信息
     *
     * @param supplierImageList
     * @throws DAOException
     */
    public void updateBySupplierIdBatch(final List<SupplierImage> supplierImageList, final Long supplierId){
        if (null != supplierImageList && supplierImageList.size() > 0) {
            final Map<String, List<SupplierImage>> typeMapList = generateListByMap(supplierImageList);
            for (final String imageType : typeMapList.keySet()) {
                updateSupplierImageByType(typeMapList.get(imageType), supplierId, imageType);
            }
        }
    }
    /**
     * <pre>
     * 设置供应商多图片信息
     * </pre>
     *
     * @param supplierAttach
     * @param supplierId
     */
    private void setSupplierImageList(final SupplierAttach supplierAttach, final Long supplierId) {
    	Map<String,Object> params = new HashMap<String,Object>();
    	params.put("supplierId", supplierId);
    	params.put("status", Constant.DEFAULTED.YES);
    	List<SupplierImage> imageList = supplierImageDao.queryByParam(params);
    	supplierAttach.setSupplierImageList(imageList);
    }
    /**
     * 根据类型更新图片
     *
     * @throws DAOException
     */
    private void updateSupplierImageByType(final List<SupplierImage> supplierImageList, final Long supplierId, final String imageType){
        if (null != supplierImageList && supplierImageList.size() > 0) {
            supplierImageDao.updateImageStatusBySupplierId(supplierId, Constant.DEFAULTED.NO, imageType, supplierImageList.get(0).getUpdateUser());
            final List<SupplierImage> supplierImageDOs = new ArrayList<SupplierImage>();
            for (final SupplierImage imageDTO : supplierImageList) {
                if (imageDTO.getSupplierId() != null) {
                    imageDTO.setName(imageDTO.getImageUrl());
                    imageDTO.setStatus(Constant.ENABLED.YES);
                    supplierImageDOs.add(imageDTO);
                }
            }
            supplierImageDao.batchInsert(supplierImageDOs);
        }
    }
    
    /**
     * 商品图片
     *
     * @return
     */
    private Map<String, List<SupplierImage>> generateListByMap(final List<SupplierImage> supplierImageList) {
        final Map<String, List<SupplierImage>> retList = new HashMap<String, List<SupplierImage>>();
        if (null != supplierImageList && supplierImageList.size() > 0) {
            List<SupplierImage> imageList = null;
            for (final SupplierImage imageDTO : supplierImageList) {
                if (retList.containsKey(imageDTO.getImageType())) {
                    imageList = retList.get(imageDTO.getImageType());
                } else {
                    imageList = new ArrayList<SupplierImage>();
                }
                imageList.add(imageDTO);
                retList.put(imageDTO.getImageType(), imageList);
            }
        }
        return retList;
    }
}
