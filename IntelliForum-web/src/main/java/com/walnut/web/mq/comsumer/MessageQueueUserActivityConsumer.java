package com.walnut.web.mq.comsumer;

import com.walnut.service.comment.repository.entity.CommentDO;
import com.walnut.service.user.repository.entity.UserFootDO;
import com.walnut.service.user.repository.entity.UserRelationDO;

public interface MessageQueueUserActivityConsumer {

    void commentAndReply(CommentDO commentDO);

    void collect(UserFootDO footDO);

    void cancelCollect(UserFootDO footDO);

    void praise(UserFootDO footDO);

    void cancelPraise(UserFootDO footDO);

    void follow(UserRelationDO userRelationDO);

    void cancelFollow(UserRelationDO userRelationDO);
}

