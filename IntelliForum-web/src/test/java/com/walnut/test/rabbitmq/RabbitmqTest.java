package com.walnut.test.rabbitmq;

import com.walnut.api.model.enums.NotifyTypeEnum;
import com.walnut.api.model.event.MessageQueueEvent;
import com.walnut.core.common.CommonConstants;
import com.walnut.service.notify.service.RabbitmqService;
import com.walnut.test.BasicTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitmqTest extends BasicTest {

    @Autowired
    private RabbitmqService rabbitmqService;

    @Test
    public void testProductRabbitmq() {
        rabbitmqService.publishDirectMsg(
                new MessageQueueEvent<String>(NotifyTypeEnum.PRAISE, "test msg"),
                CommonConstants.MESSAGE_QUEUE_KEY_NOTIFY
        );
//        try {
//            rabbitmqService.publishMsg(
//                    CommonConstants.EXCHANGE_NAME_DIRECT,
//                    BuiltinExchangeType.DIRECT,
//                    CommonConstants.QUERE_KEY_PRAISE,
//                    "lvmenglou test msg");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

//    @Test
//    public void testConsumerRabbitmq() {
//        try {
//            rabbitmqService.consumerMsg(CommonConstants.EXCHANGE_NAME_DIRECT, CommonConstants.QUERE_KEY_PRAISE, CommonConstants.QUERE_KEY_PRAISE);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}

