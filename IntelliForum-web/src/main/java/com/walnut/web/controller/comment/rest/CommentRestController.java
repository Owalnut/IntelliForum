package com.walnut.web.controller.comment.rest;

import com.walnut.api.model.context.ReqInfoContext;
import com.walnut.api.model.enums.DocumentTypeEnum;
import com.walnut.api.model.enums.NotifyTypeEnum;
import com.walnut.api.model.enums.OperateTypeEnum;
import com.walnut.api.model.vo.PageParam;
import com.walnut.api.model.vo.ResVo;
import com.walnut.api.model.vo.comment.CommentSaveReq;
import com.walnut.api.model.vo.comment.dto.TopCommentDTO;
import com.walnut.api.model.vo.constants.StatusEnum;
import com.walnut.api.model.vo.notify.NotifyMsgEvent;
import com.walnut.core.permission.Permission;
import com.walnut.core.permission.UserRole;
import com.walnut.core.util.NumUtil;
import com.walnut.core.util.SpringUtil;
import com.walnut.service.article.conveter.ArticleConverter;
import com.walnut.service.article.repository.entity.ArticleDO;
import com.walnut.service.article.service.ArticleReadService;
import com.walnut.service.comment.repository.entity.CommentDO;
import com.walnut.service.comment.service.CommentReadService;
import com.walnut.service.comment.service.CommentWriteService;
import com.walnut.service.user.repository.entity.UserFootDO;
import com.walnut.service.user.service.UserFootService;
import com.walnut.web.controller.article.vo.ArticleDetailVo;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 评论
 *
 * @author XuYifei
 * @date : 2024-07-12
 **/
@RestController
@RequestMapping(path = "comment/api")
public class CommentRestController {
    @Autowired
    private ArticleReadService articleReadService;

    @Autowired
    private CommentReadService commentReadService;

    @Autowired
    private CommentWriteService commentWriteService;

    @Autowired
    private UserFootService userFootService;


    /**
     * 评论列表页
     *
     * @param articleId
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "list")
    public ResVo<List<TopCommentDTO>> list(Long articleId, Long pageNum, Long pageSize) {
        if (NumUtil.nullOrZero(articleId)) {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "文章id为空");
        }
        pageNum = Optional.ofNullable(pageNum).orElse(PageParam.DEFAULT_PAGE_NUM);
        pageSize = Optional.ofNullable(pageSize).orElse(PageParam.DEFAULT_PAGE_SIZE);
        List<TopCommentDTO> result = commentReadService.getArticleComments(articleId, PageParam.newPageInstance(pageNum, pageSize));
        return ResVo.ok(result);
    }

    /**
     * 保存评论
     *
     * @param req
     * @return
     */
//    @Permission(role = UserRole.LOGIN)
//    @PostMapping(path = "post")
//    @ResponseBody
//    public ResVo<String> save(@RequestBody CommentSaveReq req) {
//        if (req.getArticleId() == null) {
//            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "文章id为空");
//        }
//        ArticleDO article = articleReadService.queryBasicArticle(req.getArticleId());
//        if (article == null) {
//            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "文章不存在!");
//        }
//
//        // 保存评论
//        req.setUserId(ReqInfoContext.getReqInfo().getUserId());
//        req.setCommentContent(StringEscapeUtils.escapeHtml3(req.getCommentContent()));
//        commentWriteService.saveComment(req);
//
//        // 返回新的评论信息，用于实时更新详情也的评论列表
//        ArticleDetailVo vo = new ArticleDetailVo();
//        vo.setArticle(ArticleConverter.toDto(article));
//        // 评论信息
//        List<TopCommentDTO> comments = commentReadService.getArticleComments(req.getArticleId(), PageParam.newPageInstance());
//        vo.setComments(comments);
//
//        // 热门评论
//        TopCommentDTO hotComment = commentReadService.queryHotComment(req.getArticleId());
//        vo.setHotComment(hotComment);
//        String content = templateEngineHelper.render("views/article-detail/comment/index", vo);
//        return ResVo.ok(content);
//    }

    /**
     * 保存评论
     *
     * @param req
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @PostMapping(path = "/save")
    @ResponseBody
    public ResVo<ArticleDetailVo> saveComment(@RequestBody CommentSaveReq req) {
        if (req.getArticleId() == null) {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "文章id为空");
        }
        ArticleDO article = articleReadService.queryBasicArticle(req.getArticleId());
        if (article == null) {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "文章不存在!");
        }

        // 保存评论
        req.setUserId(ReqInfoContext.getReqInfo().getUserId());
        req.setCommentContent(StringEscapeUtils.escapeHtml3(req.getCommentContent()));
        commentWriteService.saveComment(req);

        // 返回新的评论信息，用于实时更新详情也的评论列c表
        ArticleDetailVo vo = new ArticleDetailVo();
        vo.setArticle(ArticleConverter.toDto(article));
        // 评论信息
        List<TopCommentDTO> comments = commentReadService.getArticleComments(req.getArticleId(), PageParam.newPageInstance());
        vo.setComments(comments);

        // 热门评论
        TopCommentDTO hotComment = commentReadService.queryHotComment(req.getArticleId());
        vo.setHotComment(hotComment);
        return ResVo.ok(vo);
    }

    /**
     * 删除评论
     *
     * @param commentId
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @RequestMapping(path = "delete")
    public ResVo<Boolean> delete(Long commentId) {
        commentWriteService.deleteComment(commentId, ReqInfoContext.getReqInfo().getUserId());
        return ResVo.ok(true);
    }

    /**
     * 收藏、点赞等相关操作
     *
     * @param commendId
     * @param type      取值来自于 OperateTypeEnum#code
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @GetMapping(path = "favor")
    public ResVo<Boolean> favor(@RequestParam(name = "commentId") Long commendId,
                                @RequestParam(name = "type") Integer type) {
        OperateTypeEnum operate = OperateTypeEnum.fromCode(type);
        if (operate == OperateTypeEnum.EMPTY) {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, type + "非法");
        }

        // 要求文章必须存在
        CommentDO comment = commentReadService.queryComment(commendId);
        if (comment == null) {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "评论不存在!");
        }

        UserFootDO foot = userFootService.saveOrUpdateUserFoot(DocumentTypeEnum.COMMENT,
                commendId,
                comment.getUserId(),
                ReqInfoContext.getReqInfo().getUserId(),
                operate);
        // 点赞、收藏消息
        NotifyTypeEnum notifyType = OperateTypeEnum.getNotifyType(operate);
        Optional.ofNullable(notifyType).ifPresent(notify -> SpringUtil.publishEvent(new NotifyMsgEvent<>(this, notify, foot)));
        return ResVo.ok(true);
    }

}

