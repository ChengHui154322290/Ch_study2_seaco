package com.tp.dto.wx.message.resp;

import java.util.List;

/**
 * 
 * @ClassName: ArticleMessage 
 * @description: 图文消息实体类
 * @author: zhuss 
 * @date: 2015年7月3日 下午2:03:35 
 * @version: V1.0
 *
 */
public class ArticleRespMessage extends BaseRespMessage {
	
	public ArticleRespMessage(BaseRespMessage base) {
		super(base.getToUserName(), base.getFromUserName(),base.getCreateTime());
	}
	// 图文消息个数，限制为10条以内
	private int ArticleCount;
	// 多条图文消息信息，默认第一个item为大图
	private List<Article> Articles;

	public int getArticleCount() {
		return ArticleCount;
	}

	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}

	public List<Article> getArticles() {
		return Articles;
	}

	public void setArticles(List<Article> articles) {
		Articles = articles;
	}
}
