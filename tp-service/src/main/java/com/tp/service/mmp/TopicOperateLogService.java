package com.tp.service.mmp;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.TopicOperateLogDao;
import com.tp.dto.mmp.enums.OperStatus;
import com.tp.model.mmp.*;
import com.tp.service.BaseService;
import com.tp.service.mmp.ITopicOperateLogService;
import com.tp.util.DateUtil;

import java.lang.reflect.Field;
import java.util.*;

@Service
public class TopicOperateLogService extends BaseService<TopicOperateLog> implements ITopicOperateLogService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TopicOperateLogDao topicOperateLogDao;

    @Override
    public BaseDao<TopicOperateLog> getDao() {
        return topicOperateLogDao;
    }


    @Override
    @Async
    public void saveTopicOperateDetailLog(Map<TopicItem, TopicItemChange> meta, Topic topicNEW, Topic topicOLD, PolicyInfo policyNEW, PolicyInfo policyORD, Long userId, String userName) {
        try {
            List<TopicOperateLog> topicOperateLogs = getTopicOperateLogs(meta, topicNEW, topicOLD, policyNEW, policyORD, userId, userName);
            if (!topicOperateLogs.isEmpty()) {
                this.insertBatch(topicOperateLogs);
            }
        } catch (Exception e) {
            logger.error("SAVE_TOPIC_OPERATE_DETAIL_LOG_ERROR" ,e);
        }
    }


    @Override
    public void insertBatch(List<TopicOperateLog> topicOperateLogList) {
        try {
            topicOperateLogDao.insertBatch(topicOperateLogList);
        } catch (Exception e) {
            logger.error("BATCH_INSERT_TOPIC_OPERATE_LOG_ERROR", e);
        }
    }

    private List<TopicOperateLog> getTopicOperateLogs(Map<TopicItem, TopicItemChange> meta, Topic topicNEW, Topic topicOLD, PolicyInfo policyNEW, PolicyInfo policyORD, Long userId, String userName) {
        Date cur = new Date();
        List<TopicOperateLog> topicOperateLogs = new ArrayList<>();
        for (Map.Entry<TopicItem, TopicItemChange> entry : meta.entrySet()) {
            TopicOperateLog operateLog = new TopicOperateLog();
            operateLog.setCreateTime(cur);
            operateLog.setCreateUserId(userId);
            operateLog.setCreateUserName(userName);
            operateLog.setRemark("");
            operateLog.setTopicId(topicOLD.getId());
            TopicItemChange change = entry.getValue();
            TopicItem item = entry.getKey();
            OperStatus status = OperStatus.parse(change.getOperStatus());
            if (status == null) {
                operateLog.setType("");
                operateLog.setContent("");
            } else if (status == OperStatus.MODIFY) {
                StringBuilder builder = new StringBuilder();
                builder.append("SKU:").append(change.getSku()).append("  ");
                builder.append(compareNumber(change.getSortIndex(), item.getSortIndex(), "排序"));
                builder.append(compareNumber(change.getLimitAmount(), item.getLimitAmount(), "限购数量"));
                builder.append(compareNumber(change.getLimitTotal(), change.getSourceLimitTotal(), "限购总量"));
                builder.append(compareNumber(change.getTopicPrice(), item.getTopicPrice(), "促销价"));
                builder.append(compareNumber(change.getSalePrice(), item.getSalePrice(), "市场价"));
                builder.append(compareNumber(change.getPurchaseMethod(), item.getPurchaseMethod(), "购买方式"));
                builder.append(compareString(change.getName(), item.getName(), "商品名称"));
                builder.append(compareString(change.getTopicImage(), item.getTopicImage(), "图片"));
                operateLog.setType(status.name() + "-ITEM");
                operateLog.setContent(builder.length()>1024? builder.substring(0,1024): builder.toString());
            } else {
                operateLog.setType(status.name() + "-ITEM");
                StringBuilder builder = new StringBuilder();
                builder.append("SKU:" ).append(change.getSku()).append("  商品名称:").append(change.getName()).append("  ").append("限购数量:").append(change.getLimitAmount()).append("  限购总量:").append(change.getLimitTotal()).append("  促销价:").append(change.getTopicPrice())
                        .append(" 市场价:").append(change.getSalePrice()).append("  购买方式:").append(change.getPurchaseMethod()).append("  商品图片:").append(change.getTopicImage())
                        .append(("  仓库:")).append(change.getStockLocationId());
                operateLog.setContent(builder.toString());
            }
            topicOperateLogs.add(operateLog);

        }

        String changeTopicInfo = compareBean(topicNEW, topicOLD, Arrays.asList("createTime", "createUser", "updateTime", "updateUser"));
        if (changeTopicInfo.length() > 0) {
            TopicOperateLog operateLog = new TopicOperateLog();
            operateLog.setCreateTime(cur);
            operateLog.setCreateUserId(userId);
            operateLog.setCreateUserName(userName);
            operateLog.setRemark("");
            operateLog.setTopicId(topicOLD.getId());
            operateLog.setType(OperStatus.MODIFY.name() + "-TOPIC");
            operateLog.setContent(changeTopicInfo.length()>1024? changeTopicInfo.substring(0,1024): changeTopicInfo);
            topicOperateLogs.add(operateLog);
        }


        String changePolicyInfo = compareBean(policyNEW, policyORD, Arrays.asList("createTime", "createUser", "updateTime", "updateUser"));
        if (changePolicyInfo.length() > 0) {
            TopicOperateLog operateLog = new TopicOperateLog();
            operateLog.setCreateTime(cur);
            operateLog.setCreateUserId(userId);
            operateLog.setCreateUserName(userName);
            operateLog.setRemark("");
            operateLog.setTopicId(topicOLD.getId());
            operateLog.setType(OperStatus.MODIFY.name() + "-POLICY");
            operateLog.setContent(changePolicyInfo);
            topicOperateLogs.add(operateLog);
        }
        return topicOperateLogs;
    }

    private String compareBean(Object newObj, Object oldObj, List<String> ignoreList) {
        if (new Object() == null || oldObj == null) return "";

        StringBuilder builder = new StringBuilder();
        Field[] fields = newObj.getClass().getDeclaredFields();
        try {


            for (Field field : fields) {
                field.setAccessible(true);
                if (ignoreList != null && ignoreList.contains(field.getName())) continue;
                if (field.get(newObj) == null && field.get(oldObj) == null) continue;

                if (field.getType()==String.class) {
                    String newStr = (String) field.get(newObj);
                    String oldStr = (String) field.get(oldObj);
                    builder.append(compareString(newStr, oldStr, field.getName().toUpperCase()));
                } else if (field.getType().getName().equals("int") || field.getType()==Integer.class) {
                    Integer newInt = (Integer) field.get(newObj);
                    Integer oldInt = (Integer) field.get(oldObj);
                    builder.append(compareNumber(newInt, oldInt, field.getName().toUpperCase()));
                }else if(field.getType()==Double.class) {
                    Double newInt = (Double) field.get(newObj);
                    Double oldInt = (Double) field.get(oldObj);
                    builder.append(compareNumber(newInt, oldInt, field.getName().toUpperCase()));
                }else if(field.getType()==Long.class){
                    Long newInt = (Long) field.get(newObj);
                    Long oldInt = (Long) field.get(oldObj);
                    builder.append(compareNumber(newInt, oldInt, field.getName().toUpperCase()));
                }

                else if (field.getType()==Date.class) {
                    Date newDa = (Date) field.get(newObj);
                    Date oldDa = (Date) field.get(oldObj);
                    builder.append(compareDate(newDa, oldDa, field.getName().toUpperCase()));
                }
            }
        } catch (Exception e) {
            logger.error("COMPARE_BEAN_ERROR",e);
        }
        return builder.toString();
    }

    private String compareNumber(Number newVal, Number oldVal, String name) {
        if ((newVal == null && oldVal == null) || (newVal != null && newVal.equals(oldVal))) return StringUtils.EMPTY;
        return new StringBuilder().append(name).append(":").append(newVal).append("(").append(oldVal).append(")  ").toString();
    }

    private String compareString(String newVal, String oldVal, String name) {
        if (StringUtils.equals(newVal, oldVal)) {
            return StringUtils.EMPTY;
        }
        return new StringBuilder().append(name).append(":").append(newVal).append("(").append(oldVal).append(")  ").toString();
    }

    private String compareDate(Date newDa, Date oldDa, String name) {
        if ((newDa == null && oldDa == null) || (newDa != null && newDa.equals(oldDa))) return StringUtils.EMPTY;
        return new StringBuilder().append(name).append(":").append(DateUtil.format(newDa, DateUtil.NEW_FORMAT)).append("(").append(DateUtil.format(oldDa, DateUtil.NEW_FORMAT)).append(")  ").toString();
    }


}
