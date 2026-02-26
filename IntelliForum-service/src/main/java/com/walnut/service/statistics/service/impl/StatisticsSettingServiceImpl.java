package com.walnut.service.statistics.service.impl;

import com.walnut.api.model.vo.statistics.dto.StatisticsCountDTO;
import com.walnut.api.model.vo.statistics.dto.StatisticsDayDTO;
import com.walnut.api.model.vo.user.dto.UserFootStatisticDTO;
import com.walnut.core.cache.RedisClient;
import com.walnut.service.article.service.ArticleReadService;
import com.walnut.service.article.service.ColumnService;
import com.walnut.service.statistics.service.RequestCountService;
import com.walnut.service.statistics.service.StatisticsSettingService;
import com.walnut.service.user.service.UserFootService;
import com.walnut.service.user.service.UserService;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 * 数据统计后台接口
 *
 * @author XuYifei
 * @date 2024-07-12
 */
@Slf4j
@Service
public class StatisticsSettingServiceImpl implements StatisticsSettingService {

    @Autowired
    private RequestCountService requestCountService;

    @Autowired
    private UserService userService;

    @Autowired
    private ColumnService columnService;

    @Autowired
    private UserFootService userFootService;

    @Autowired
    private ArticleReadService articleReadService;

    @Override
    public void saveRequestCount(String host) {

        Integer count = RedisClient.hGet(RequestCountService.REQUEST_COUNT_PREFIX + Date.valueOf(LocalDate.now()), host, Integer.class);
        if (count != null) {
            RedisClient.hSet(RequestCountService.REQUEST_COUNT_PREFIX + Date.valueOf(LocalDate.now()), host, count);
        } else {
            RedisClient.hSet(RequestCountService.REQUEST_COUNT_PREFIX + Date.valueOf(LocalDate.now()), host, 1);
        }

        // 以下是直接访问DB的逻辑，要操作两次数据库，访问压力太大
//        RequestCountDO requestCountDO = requestCountService.getRequestCount(host);
//        if (requestCountDO == null) {
//            requestCountService.insert(host);
//        } else {
//            // 改为数据库直接更新
//            requestCountService.incrementCount(requestCountDO.getId());
//        }
    }

    @Override
    public StatisticsCountDTO getStatisticsCount() {
        // 从 user_foot 表中查询点赞数、收藏数、留言数、阅读数
        UserFootStatisticDTO userFootStatisticDTO = userFootService.getFootCount();
        if (userFootStatisticDTO == null) {
            userFootStatisticDTO = new UserFootStatisticDTO();
        }
        return StatisticsCountDTO.builder()
                .userCount(userService.getUserCount())
                .articleCount(articleReadService.getArticleCount())
                .pvCount(requestCountService.getPvTotalCount())
                .tutorialCount(columnService.getTutorialCount())
                .commentCount(userFootStatisticDTO.getCommentCount())
                .collectCount(userFootStatisticDTO.getCollectionCount())
                .likeCount(userFootStatisticDTO.getPraiseCount())
                .readCount(userFootStatisticDTO.getReadCount())
                .starPayCount(0)
                .build();
    }

    @Override
    public List<StatisticsDayDTO> getPvUvDayList(Integer day) {
        return requestCountService.getPvUvDayList(day);
    }

}

