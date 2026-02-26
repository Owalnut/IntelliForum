package com.walnut.service.article.service;

import com.walnut.api.model.vo.PageListVo;
import com.walnut.api.model.vo.PageParam;
import com.walnut.api.model.vo.article.dto.ArticleDTO;

/**
 * @author XuYifei
 * @date 2024-07-12
 */
public interface ArticleRecommendService {
    /**
     * 文章关联推荐
     *
     * @param article
     * @param pageParam
     * @return
     */
    PageListVo<ArticleDTO> relatedRecommend(Long article, PageParam pageParam);
}

