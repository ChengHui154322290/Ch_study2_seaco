package com.tp.service.mmp.groupbuy;

import com.alibaba.fastjson.JSON;
import com.tp.common.util.mem.Sms;
import com.tp.dao.mmp.GroupbuyGroupDao;
import com.tp.dao.mmp.GroupbuyInfoDao;
import com.tp.dao.mmp.GroupbuyJoinDao;
import com.tp.dao.mmp.TopicDao;
import com.tp.model.mmp.GroupbuyGroup;
import com.tp.model.mmp.GroupbuyInfo;
import com.tp.model.mmp.GroupbuyJoin;
import com.tp.model.mmp.Topic;
import com.tp.service.mem.ISendSmsService;
import com.tp.service.mem.MailService;
import com.tp.service.mmp.groupbuy.IGroupbuySendMsgService;
import com.tp.util.BeanUtil;
import com.tp.util.DateUtil;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ldr on 2016/3/17.
 */
@Service
public class GroupbuySendMsgService implements IGroupbuySendMsgService {

    private String msg = "温馨提示,您参与的【{name}】已组团成功!成团价:￥{price}，请在{time}之前到\"我的团\"中查看购买!{url}";

    @Autowired
    private GroupbuyJoinDao groupbuyJoinDao;

    @Autowired
    private GroupbuyGroupDao groupbuyGroupDao;

    @Autowired
    private TopicDao topicDao;

    @Autowired
    private GroupbuyInfoDao groupbuyInfoDao;

    @Autowired
    private ISendSmsService sendSmsService;

    private Logger logger = Logger.getLogger(this.getClass());

    @Value("${m.groupbuy.notice.url}")
    private String noticeUrl;

    @Override
    @Async
    public void sendMsg(Long groupId) {

        try {
            if (groupId == null) return;

            GroupbuyGroup groupbuyGroup = groupbuyGroupDao.queryById(groupId);
            if (groupbuyGroup.getFactAmount().intValue() < groupbuyGroup.getPlanAmount()) {
                return;
            }
            Topic topic = topicDao.queryById(groupbuyGroup.getTopicId());
            Date endTime = topic.getEndTime();
            String date = DateUtil.format(endTime, DateUtil.NEW_FORMAT);
            GroupbuyInfo groupbuyInfo = groupbuyInfoDao.queryById(groupbuyGroup.getGroupbuyId());
            String price = groupbuyInfo.getGroupPrice().toString();
            String url = String.format(noticeUrl, groupbuyInfo.getId(), groupbuyGroup.getId());
            logger.info("GROUPBUY_LAUNCH_SUCCESS_SEND_MSG:GROUP_ID=" + groupId);
            GroupbuyJoin groupbuyJoinQuery = new GroupbuyJoin();
            groupbuyJoinQuery.setGroupId(groupId);
            List<GroupbuyJoin> groupbuyJoinList = groupbuyJoinDao.queryByParam(BeanUtil.beanMap(groupbuyJoinQuery));
            if (CollectionUtils.isEmpty(groupbuyJoinList)) return;
            List<Sms> smsList = new ArrayList<>();
            String msgC = msg.replace("{name}", groupbuyJoinList.get(0).getTopicName()).replace("{price}", price).replace("{time}", date).replace("{url}", url);
            for (GroupbuyJoin groupbuyJoin : groupbuyJoinList) {
                String mobile = groupbuyJoin.getMemberName();
                if (StringUtils.isBlank(mobile) || !NumberUtils.isNumber(mobile) || mobile.length() != 11) {
                    logger.warn("GROUPBUY_SEND_MESSAGE_ERROR,USER_HAS_NO_MOBILE_NO:"+String.valueOf(mobile));
                    continue;

                }
                Sms sms = new Sms();
                sms.setMobile(mobile);
                sms.setContent(msgC);
                smsList.add(sms);
            }
            if (smsList.isEmpty()) return;
            sendSmsService.batchSendSms(smsList);
        } catch (Exception e) {
            logger.error("GROUPBUY_SEND_MSG_ERROR:", e);
            logger.error("GROUPBUY_SEND_MSG_ERROR:GROUP_ID=" + groupId);
        }

    }
}
