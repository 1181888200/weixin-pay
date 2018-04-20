/**
 * 消息列表
 * 2014-07-23
 */
package com.lwl.weixin.response.message;

import java.util.List;

/**
 * 
 */
public class NewsMessage extends BaseMessage {

	private int ArticleCount;

	private List<ArticleMessage> Articles;

	public int getArticleCount() {
		return ArticleCount;
	}

	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}

	public List<ArticleMessage> getArticles() {
		return Articles;
	}

	public void setArticles(List<ArticleMessage> articles) {
		Articles = articles;
	}
}