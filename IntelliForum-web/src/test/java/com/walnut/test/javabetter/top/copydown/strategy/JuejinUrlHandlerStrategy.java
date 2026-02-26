package com.walnut.test.javabetter.top.copydown.strategy;

import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.text.UnicodeUtil;
import com.walnut.test.javabetter.top.copydown.HtmlSourceOption;
import com.walnut.test.javabetter.top.copydown.HtmlSourceResult;
import com.walnut.test.javabetter.top.copydown.HtmlSourceType;
import com.walnut.test.javabetter.top.furstenheim.CopyDown;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JuejinUrlHandlerStrategy extends Coverter implements UrlHandlerStrategy{

    public JuejinUrlHandlerStrategy(CopyDown copyDown, Document document) {
        super(copyDown, document);
    }

    @Override
    public boolean match(String url) {
        return url.contains("juejin.cn");
    }

    @Override
    public void handleOptions(HtmlSourceOption option) {
        // 掘金的链接
        option.setHtmlSourceType(HtmlSourceType.JUEJIN);
        option.setContentSelector("mark_content");
        option.setTitleKey("meta[itemprop='headline']");
        option.setAuthorKey("div[itemprop='author'] meta[itemprop='name']");
    }

    @Override
    public HtmlSourceResult convertToMD(HtmlSourceOption option) {
        HtmlSourceResult result = findJuejin(option);
        List<String> splits = StrSplitter.split(result.getMarkdown(), "\\n", 0, false, false);

        StrBuilder builder = StrBuilder.create();
        for (String str : splits) {
            builder.append(str);
            builder.append("\n");
        }

        String markdown = UnicodeUtil.toString(builder.toString());
        result.setMarkdown(markdown);
        return result;
    }

    /**
     * 查找掘金文章标题、作者、封面图
     *
     * @param option
     * @return
     */
    private HtmlSourceResult findJuejin(HtmlSourceOption option) {
        HtmlSourceResult result = HtmlSourceResult.builder().build();
        Document doc = getDocument();
        // 标题
        Elements title = doc.select(option.getTitleKey());
        result.setTitle(title.attr("content"));

        // 作者名
        Elements authorName = doc.select(option.getAuthorKey());
        result.setAuthor(authorName.attr("content"));

        // keywords
        Elements keywords = doc.select(option.getKeywordsKey());
        result.setKeywords(keywords.attr("content"));

        // description
        Elements description = doc.select(option.getDescriptionKey());
        result.setDescription(description.attr("content"));

        // 转载链接
        result.setSourceLink(option.getUrl());

        // 如果包含 .markdown-body 就直接从这里获取
        Elements markdownBody = doc.select(".markdown-body");
        if (markdownBody.size() > 0) {
            String input = markdownBody.html();
            result.setMarkdown(getCopyDown().convert(input));
            return result;
        }

        // 文章内容
        // 掘金的不是以 HTML 格式显示的，所以需要额外的处理
        // mark_content:"
        // ,is_english:d,is_original:g,user_index:13.31714372615673,original_type:d,original_author:e,content:e,ctime:"1650429118",mtime:"1650858329",rtime:"1650435284",draft_id:"7088517368665604127",view_count:36440,collect_count:346,digg_count:340,comment_count:239,hot_index:2401,is_hot:d,rank_index:.3438144,status:g,verify_status:g,audit_status:k,mark_content:"---\ntheme: awesome-green\n---\n
        // ",display_count:d}
        // div.global-component-box
        // audit_status:i,mark_content:"---\ntheme: devui-blue\n---\n\n\n# 自我介绍\n\n首页和大家介绍一下我，我叫阿杆（笔名及游戏名🤣），19级本科在读，双非院校，专业是数字媒体技术，但我主修软件工程，学习方向是后端开发，主要语👨‍💻。\n",display_count:b,is_markdown:g
        Pattern mdPattern = Pattern.compile(option.getContentSelector()+":\"(.*)\",display_count");
        for (Element scripts : doc.getElementsByTag("script")) {
            for (DataNode dataNode : scripts.dataNodes()) {
                String wholeData = dataNode.getWholeData();
                log.info("juejin dataNode:{}", wholeData);
                if (wholeData.contains(option.getContentSelector())) {
                    log.info("juejin contains");
                    // 内容
                    Matcher matcher = mdPattern.matcher(wholeData);
                    if (matcher.find()) {
                        String md = matcher.group(1);
                        log.info("find md text success{}", md);
                        result.setMarkdown(md);
                        return result;
                    }
                }
            }
        }
        return result;
    }
}

