package com.tp.service.mmp.groupbuy;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.PageInfo;
import com.tp.dao.mmp.GroupbuyGroupDao;
import com.tp.dao.mmp.TopicDao;
import com.tp.dto.mmp.enums.TopicStatus;
import com.tp.model.mmp.GroupbuyGroup;
import com.tp.model.mmp.Topic;
import com.tp.service.BaseService;
import com.tp.service.mmp.groupbuy.IGroupbuyGroupService;

import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class GroupbuyGroupService extends BaseService<GroupbuyGroup> implements IGroupbuyGroupService {

    @Autowired
    private GroupbuyGroupDao groupbuyGroupDao;

    @Autowired
    TopicDao topicDao;

    private Logger logger = Logger.getLogger(this.getClass());

    @Override
    public BaseDao<GroupbuyGroup> getDao() {
        return groupbuyGroupDao;
    }


    @Override
    public Integer updateExpiredGroup() {
        List<GroupbuyGroup> groupbuyGroupList = groupbuyGroupDao.queryProcessingGroup();
        if (CollectionUtils.isEmpty(groupbuyGroupList)) return 0;
        Set<Long> topicIds = new HashSet<>();
        for (GroupbuyGroup groupbuyGroup : groupbuyGroupList) {
            topicIds.add(groupbuyGroup.getTopicId());
        }

        if (topicIds.isEmpty()) return 0;
        Date cur = new Date();
        List<Topic> topicList = topicDao.queryTopicInfoList(new ArrayList<>(topicIds));

        List<Long> terminateGroupIds = new ArrayList<>();
        for (GroupbuyGroup groupbuyGroup : groupbuyGroupList) {
            Date endTime = groupbuyGroup.getEndTime();

            Topic topic = getTopic(topicList, groupbuyGroup);
            if (topic == null || topic.getEndTime() == null) {
                logger.error("TERMINATE_GROUP_ERROR.TOPIC_END_TIME_IS_NULL.TOPIC_ID=" + groupbuyGroup.getTopicId());
            } else {
                if (endTime.after(topic.getEndTime())) {
                    endTime = topic.getEndTime();
                }
            }
            if (endTime.before(cur)) {
                terminateGroupIds.add(groupbuyGroup.getId());
            } else if (topic.getStatus() == null || topic.getStatus().intValue() != TopicStatus.PASSED.ordinal()) {
                terminateGroupIds.add(groupbuyGroup.getId());
            }
        }
        if (CollectionUtils.isEmpty(terminateGroupIds)) return 0;
        logger.info("TERMINATE_GROUP.IDS:" + terminateGroupIds);
        int count = groupbuyGroupDao.terminateExpiredGroup(terminateGroupIds);
        logger.info("GROUPBUY_GROUP_UPDATE_EXPIRED_COUNT:" + count);
        return count;
    }

    private Topic getTopic(List<Topic> topicList, GroupbuyGroup groupbuyGroup) {
        for (Topic topic : topicList) {
            if (topic.getId().equals(groupbuyGroup.getTopicId())) {
                return topic;
            }
        }
        return null;
    }

    @Override
    public PageInfo<GroupbuyGroup> query(GroupbuyGroup groupbuyGroup) {

        GroupbuyGroup query = new GroupbuyGroup();
        if (groupbuyGroup != null) {
            query.setGroupbuyId(groupbuyGroup.getGroupbuyId());
            query.setTopicName(StringUtils.isBlank(groupbuyGroup.getTopicName()) ? null : groupbuyGroup.getTopicName());
            query.setStatus(groupbuyGroup.getStatus());
            query.setMemberName(StringUtils.isBlank(groupbuyGroup.getMemberName()) ? null : groupbuyGroup.getMemberName());
            query.setMemberId(groupbuyGroup.getMemberId());
            query.setStartTime(groupbuyGroup.getStartTime());
            query.setEndTime(groupbuyGroup.getEndTime());
            query.setStartPage(groupbuyGroup.getStartPage() == null ? 1 : groupbuyGroup.getStartPage() < 1 ? 1 : groupbuyGroup.getStartPage());
            query.setPageSize(groupbuyGroup.getPageSize() == null ? 10 : groupbuyGroup.getPageSize() < 1 ? 10 : groupbuyGroup.getPageSize());
        } else {
            query.setStartPage(groupbuyGroup.getStartPage() == null ? 1 : groupbuyGroup.getStartPage() < 1 ? 1 : groupbuyGroup.getStartPage());
            query.setPageSize(groupbuyGroup.getPageSize() == null ? 10 : groupbuyGroup.getPageSize() < 1 ? 10 : groupbuyGroup.getPageSize());
        }

        List<GroupbuyGroup> list = groupbuyGroupDao.queries(query);
        Integer count = groupbuyGroupDao.queriesCount(query);
        PageInfo<GroupbuyGroup> page = new PageInfo<>(query.getStartPage(), query.getPageSize());
        page.setRecords(count);
        page.setRows(list);
        page.setTotal(count % query.getPageSize() > 0 ? (count / query.getPageSize() + 1) : count / query.getPageSize());
        return page;
    }
}
