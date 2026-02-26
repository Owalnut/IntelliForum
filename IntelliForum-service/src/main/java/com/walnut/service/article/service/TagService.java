package com.walnut.service.article.service;

import com.walnut.api.model.vo.PageParam;
import com.walnut.api.model.vo.PageVo;
import com.walnut.api.model.vo.article.dto.TagDTO;

import java.util.List;

/**
 * 标签Service
 *
 * @author XuYifei
 * @date 2024-07-12
 */
public interface TagService {

    PageVo<TagDTO> queryTags(String key, PageParam pageParam);

    Long queryTagId(String tag);

    List<TagDTO> listAllUndeletedTags();

    List<TagDTO> listTagsCategory(Long categoryId);
}

