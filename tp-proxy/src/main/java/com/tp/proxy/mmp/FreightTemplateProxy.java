package com.tp.proxy.mmp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tp.common.util.mmp.AssertUtil;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.enums.FreightCalculateType;
import com.tp.dto.mmp.enums.FreightType;
import com.tp.dto.ord.kuaidi100.Result;
import com.tp.exception.ServiceException;
import com.tp.model.mmp.FreightTemplate;
import com.tp.proxy.BaseProxy;
import com.tp.proxy.mmp.callBack.Callback;
import com.tp.service.IBaseService;
import com.tp.service.mmp.IFreightTemplateService;
import com.tp.util.BeanUtil;
import com.tp.util.DateUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 代理层
 *
 * @author szy
 */
@Service
public class FreightTemplateProxy extends BaseProxy<FreightTemplate> {

    @Autowired
    private IFreightTemplateService freightTemplateService;

    @Override
    public IBaseService<FreightTemplate> getService() {
        return freightTemplateService;
    }


    public ResultInfo<FreightTemplate> getById(final Long id) {
        final ResultInfo<FreightTemplate> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() {
                FreightTemplate template = freightTemplateService.queryById(id);
                result.setData(template);
            }
        });
        return result;
    }

    public ResultInfo save(final String templateName, final Integer freightType,
                           final Integer calculateMode, final String fee, final String fullBy, final String feeFullBy, final Long userId) {
        final ResultInfo result = new ResultInfo();
        this.execute(result, new Callback() {
            @Override
            public void process() {
                FreightTemplate ft = new FreightTemplate();
                if (StringUtils.isBlank(templateName)) {
                    throw new ServiceException("模板名称必填", 910);
                }
                ft.setName(templateName);
                if (calculateMode.intValue() == FreightCalculateType.MBY.getValue()) {
                    // 国内的"满包邮"只能添加一条
                    if (freightType.intValue() == FreightType.INTERNAL.getValue()) {
                        FreightTemplate query = new FreightTemplate();
                        query.setFreightType(FreightType.INTERNAL.getValue());
                        query.setCalculateMode(FreightCalculateType.MBY.getValue());
                        List<FreightTemplate> ftList = freightTemplateService.queryByObject(query);
                        if (ftList.size() >= 1)
                            throw new ServiceException("国内满包邮记录已存在", 910);
                    }
                    if (StringUtils.isBlank(fullBy))
                        throw new ServiceException("满额必填", 910);

                    try {
                        Integer.valueOf(fullBy);
                    } catch (Exception ex) {
                        throw new ServiceException("满额必须为数值", 910);
                    }
                    // 满x元包邮
                    ft.setFreePostage(Float.valueOf(fullBy));
                    if (StringUtils.isBlank(feeFullBy))
                        throw new ServiceException("邮费必填", 910);
                    try {
                        Integer.valueOf(feeFullBy);
                    } catch (Exception ex) {
                        throw new ServiceException("邮费必须为数值", 910);
                    }
                    // 邮费
                    ft.setPostage(Float.valueOf(feeFullBy));
                } else {
                    if (StringUtils.isBlank(fee))
                        throw new ServiceException("邮费必填", 910);
                    try {
                        Integer.valueOf(fee);
                    } catch (Exception ex) {
                        throw new ServiceException("邮费必须为数值", 910);
                    }
                    // 邮费
                    ft.setPostage(Float.valueOf(fee));
                    ft.setFreePostage(0F);
                }
                AssertUtil.notNull(userId, "用户Id为空");
                ft.setFreightType(freightType);
                ft.setCalculateMode(calculateMode);
                ft.setCreatorId(userId);

                ft.setAftPostage(0F);
                ft.setCreateTime(new Date());
                ft.setModifyTime(new Date());
                ft.setIsDelete(0);
                ft.setValidateCode("");
                ft.setPlatformLevel(0);
                freightTemplateService.insert(ft);
            }
        });
        return result;
    }

    public ResultInfo edit(final Long id, final String templateName, final Integer freightType,
                           final Integer calculateMode, final String fee, final String fullBy, final String feeFullBy, final Long userId) {
        ResultInfo result = new ResultInfo();
        this.execute(result, new Callback() {
            @Override
            public void process() {
                // 国内的"满包邮"只能添加一条
                if (calculateMode.intValue() == FreightCalculateType.MBY.getValue() &&
                        freightType.intValue() == FreightType.INTERNAL.getValue()) {
                    FreightTemplate query = new FreightTemplate();
                    query.setFreightType(FreightType.INTERNAL.getValue());
                    query.setCalculateMode(FreightCalculateType.MBY.getValue());
                    List<FreightTemplate> ftList = freightTemplateService.queryByObject(query);
                    if (ftList.size() >= 1) {
                        FreightTemplate ft = ftList.get(0);
                        if (ft.getId().longValue() != id)
                            throw new ServiceException("国内满包邮记录已存在", 910);
                    }

                }
                FreightTemplate ft = freightTemplateService.queryById(id);
                AssertUtil.notBlank(templateName, "模板名称必填", 910);

                ft.setName(templateName);
                if (calculateMode.intValue() == FreightCalculateType.MBY.getValue()) {
                    AssertUtil.notBlank(fullBy, "满额必填", 910);

                    try {
                        Float.valueOf(fullBy);
                    } catch (Exception ex) {
                        throw new ServiceException("满额必须为数值", 910);
                    }
                    // 满x元包邮
                    ft.setFreePostage(Float.valueOf(fullBy));
                    AssertUtil.notBlank(feeFullBy, "邮费必填", 910);

                    try {
                        Integer.valueOf(feeFullBy);
                    } catch (Exception ex) {
                        throw new ServiceException("邮费必须为数值", 910);
                    }
                    // 邮费
                    ft.setPostage(Float.valueOf(feeFullBy));
                } else {
                    AssertUtil.notBlank(fee, "邮费必填", 910);

                    AssertUtil.isNumber(fee, "邮费必须为数值", 910);

                    // 邮费
                    ft.setPostage(Float.valueOf(fee));
                }
                AssertUtil.notNull(userId, "用户Id为空");
                ft.setFreightType(freightType);
                ft.setCalculateMode(calculateMode);
                ft.setCreatorId(userId);
                freightTemplateService.updateNotNullById(ft);
            }
        });
        return result;
    }

    public PageInfo<FreightTemplate> getFreightTemplate(String templateName,
                                                        Integer freightType, Integer calculateMode, Integer pageNo, Integer pageSize) {
        FreightTemplate ft = new FreightTemplate();
        if (null != templateName && !("".endsWith(templateName)))
            ft.setName(templateName);
        if (null != freightType && 2 != freightType)
            ft.setFreightType(freightType);
        if (null != calculateMode && 2 != calculateMode)
            ft.setCalculateMode(calculateMode);

        return freightTemplateService.queryPageByObject(ft, new PageInfo<FreightTemplate>(pageNo, pageSize));
    }


    @SuppressWarnings("unchecked")
    public JSONObject toJson(PageInfo<FreightTemplate> pageObj, int rows, int page)
            throws Exception {
        List<FreightTemplate> fts = new ArrayList<>();
        if (null != pageObj && null != pageObj.getRows())
            fts = pageObj.getRows();
        JSONArray rowArray = new JSONArray();
        for (FreightTemplate pojo : fts) {
            JSONObject row = new JSONObject();
            row.put("id", pojo.getId());
            row.put("name", pojo.getName());
            if (pojo.getCalculateMode().intValue() == FreightCalculateType.MBY.getValue()) {
                row.put("calculateMode", FreightCalculateType.MBY.getDescription());
                row.put("detailInfo", "邮费：" + pojo.getPostage() + "元，满：" + pojo.getFreePostage() + "元包邮");
            } else {
                row.put("calculateMode", FreightCalculateType.TYYZ.getDescription());
                row.put("detailInfo", "邮费：" + pojo.getPostage() + "元");
            }
            if (pojo.getFreightType().intValue() == FreightType.EXTERNAL.getValue())
                row.put("freightType", FreightType.EXTERNAL.getDescription());
            else
                row.put("freightType", FreightType.INTERNAL.getDescription());

            row.put("createTime", DateUtil.formatDate(pojo.getCreateTime(), DateUtil.NEW_FORMAT));
            rowArray.add(row);
        }
        JSONObject res = new JSONObject();
        res.put("rows", rowArray);
        res.put("records", rowArray.size());
        int totalRecord = pageObj.getRecords();
        int totalPage = totalRecord % rows == 0 ? totalRecord
                / rows : totalRecord / rows + 1;
        res.put("total", totalPage);
        res.put("page", page);
        return res;
    }
}
