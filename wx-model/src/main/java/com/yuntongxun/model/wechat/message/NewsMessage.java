package com.yuntongxun.model.wechat.message;

import com.yuntongxun.model.wechat.wx.Article;
import lombok.Data;

import java.util.List;

/**
 * 文本消息
 *
 * @author lu
 */
@Data
public class NewsMessage extends BaseMessage {
    // 图文消息个数，限制为10条以内
    private int ArticleCount;
    // 多条图文消息信息，默认第一个item为大图
    private List<Article> Articles;
}