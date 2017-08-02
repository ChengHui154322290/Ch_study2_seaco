package com.tp.service.ord;

import static com.tp.common.vo.Constant.DOCUMENT_TYPE.DSS_PAY;
import static com.tp.common.vo.Constant.DOCUMENT_TYPE.SEAGOOR_PAY;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.common.vo.ord.OrderCodeType;
import com.tp.common.vo.ord.RedisCacheKeyConstants;
import com.tp.redis.util.JedisDBUtil;
import com.tp.service.ord.IOrderCodeGeneratorService;

/**
 * 订单号生成器
 *
 * @author szy
 * @version 0.0.1
 */
@Service
public class OrderCodeGeneratorService implements IOrderCodeGeneratorService {

    private static final Logger log = LoggerFactory.getLogger(OrderCodeGeneratorService.class);

    private static final String DATE_PATTERN = "yyMMdd";
    /**
     * 索引字符串长度
     */
    private static final Integer INDEX_STRING_LENGTH = 8;
    /**
     * 编号长度
     */
    private static final Integer CODE_LENGTH = 20;

    private static final Long MAX_CODE = 10000000L;
    
    private static final long LAST_CODE=99L;
    @Autowired
    private JedisDBUtil jedisDBUtil;
    @Value("#{meta['isTest']}")
    private String isTest;

    public Long generate(OrderCodeType type) {
        String dateStr = dateString();
        String indexStr = indexString(type);

        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        switch (type) {
            case PARENT:
                sb.append(Constant.DOCUMENT_TYPE.SO_ORDER.code);
                break;
            case SON:
                sb.append(Constant.DOCUMENT_TYPE.SO_SUB_ORDER.code);
                break;
            case DSS_PAY:
                sb.append(Constant.DOCUMENT_TYPE.DSS_PAY.code);
                break;
            case SEAGOOR_PAY:
                sb.append(Constant.DOCUMENT_TYPE.SEAGOOR_PAY.code);
                break;
            case SEAGOOR_PAY_REFUND:
                sb.append(Constant.DOCUMENT_TYPE.SEAGOOR_PAY_REFUND.code);
                break;
            default:
                throw new IllegalArgumentException("未知的订单编号类型");
        }

        return Long.valueOf(sb.append(dateStr).append(indexStr).append(LAST_CODE).toString());
    }

    /**
     * 日期字符串
     *
     * @return
     */
    private String dateString() {
        Calendar currentTime = Calendar.getInstance();
        if (StringUtils.isNoneBlank(isTest) && (isTest.equalsIgnoreCase("YES") || isTest.equalsIgnoreCase("Y"))) {
            currentTime.set(Calendar.YEAR, currentTime.get(Calendar.YEAR) - 16);
        }
        return new SimpleDateFormat(DATE_PATTERN).format(currentTime.getTime());
    }

    /**
     * 自增码
     *
     * @return
     */
    public String indexString(OrderCodeType type) {
        long currentRandom = Math.abs(System.nanoTime() % 10);
        Long index = jedisDBUtil.incr((OrderCodeType.PARENT.equals(type) ? RedisCacheKeyConstants.ORDER_CODE_INDEX : RedisCacheKeyConstants.SUB_CODE_INDEX) + currentRandom);
        if (null == index) {
            log.error("生成订单编号异常：redis服务器获取自增值为空");
            index = System.currentTimeMillis();
        }
        if (currentRandom % 2 == 0) {
            index = MAX_CODE - 1 - index % MAX_CODE;
        }
        String idxStr = index.toString();
        int len = idxStr.length();
        StringBuilder sb = new StringBuilder(idxStr);
        if (len < INDEX_STRING_LENGTH) {
            return currentRandom + String.format("%07d", index);
        } else {
            return currentRandom + sb.delete(0, sb.length() - INDEX_STRING_LENGTH).toString();
        }
    }
}
