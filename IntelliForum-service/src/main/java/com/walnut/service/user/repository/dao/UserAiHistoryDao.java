package com.walnut.service.user.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.walnut.service.user.repository.entity.UserAiHistoryDO;
import com.walnut.service.user.repository.mapper.UserAiHistoryMapper;
import org.springframework.stereotype.Repository;

import jakarta.annotation.Resource;

@Repository
public class UserAiHistoryDao extends ServiceImpl<UserAiHistoryMapper, UserAiHistoryDO> {

    @Resource
    private UserAiHistoryMapper userAiHistoryMapper;
}


