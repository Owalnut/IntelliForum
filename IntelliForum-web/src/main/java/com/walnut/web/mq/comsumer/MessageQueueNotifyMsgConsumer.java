package com.walnut.web.mq.comsumer;

import com.walnut.api.model.enums.NotifyTypeEnum;
import com.walnut.api.model.event.MessageQueueEvent;
import com.walnut.service.comment.repository.entity.CommentDO;
import com.walnut.service.user.repository.entity.UserFootDO;
import com.walnut.service.user.repository.entity.UserRelationDO;


public interface MessageQueueNotifyMsgConsumer {
    void saveCommentNotify(MessageQueueEvent<CommentDO> event);

    void saveReplyNotify(MessageQueueEvent<CommentDO> event);

    void saveArticleNotify(MessageQueueEvent<UserFootDO> event);

    void saveArticleNotify(UserFootDO foot, NotifyTypeEnum notifyTypeEnum);

    void removeArticleNotify(MessageQueueEvent<UserFootDO> event);

    void saveFollowNotify(MessageQueueEvent<UserRelationDO> event);

    void removeFollowNotify(MessageQueueEvent<UserRelationDO> event);

    void saveRegisterSystemNotify(Long userId);

}

