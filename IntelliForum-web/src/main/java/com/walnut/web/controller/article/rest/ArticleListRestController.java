
package com.walnut.web.controller.article.rest;

import com.walnut.api.model.vo.PageListVo;
import com.walnut.api.model.vo.PageParam;
import com.walnut.api.model.vo.ResVo;
import com.walnut.api.model.vo.article.dto.ArticleDTO;
import com.walnut.service.article.service.ArticleReadService;
import com.walnut.web.global.BaseViewController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 文章列表
 *
 * @author XuYifei
 */
@RequestMapping(path = "article/api/list")
@RestController
public class ArticleListRestController extends BaseViewController {
    @Autowired
    private ArticleReadService articleService;

    /**
     * 分类下的文章列表
     *
     * @param categoryId 类目id
     * @param page 请求页
     * @param size 分页数
     * @return 文章列表
     */
    @GetMapping(path = "data/category/{category}")
    public ResVo<PageListVo<ArticleDTO>> categoryDataList(@PathVariable("category") Long categoryId,
                                                          @RequestParam(name = "page") Long page,
                                                          @RequestParam(name = "size", required = false) Long size) {
        PageParam pageParam = buildPageParam(page, size);
        PageListVo<ArticleDTO> list = articleService.queryArticlesByCategory(categoryId, pageParam);
        return ResVo.ok(list);
    }

}

