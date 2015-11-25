package com.sboss.knowledge.model.orm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sboss.knowledge.model.IDEntity;

/**
 * 用户与内容之间的关系
 * @author monica
 *
 */
@Entity
@Table(name = "user_content")
public class UserContent extends IDEntity {

	private static final long serialVersionUID = 2721091363008562313L;
	
	//用户编号——一个用户可对应多个内容
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER", nullable = false)
	private User user;
	
	//内容编号——一个内容可对应多个用户
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTENT", nullable = false)
	private Content content;
	
	//用户是否阅读过指定文本
	@Column(name = "ISCONCERN", nullable = false)
	private boolean isConcern;
	
	//内容与用户关联分析的存储地址
	@Column(name = "USERANALYSISRESULTLINK", length = 200, nullable = true)
	private String userAnalysisResultLink;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	public boolean isConcern() {
		return isConcern;
	}

	public void setConcern(boolean isConcern) {
		this.isConcern = isConcern;
	}

	public String getUserAnalysisResultLink() {
		return userAnalysisResultLink;
	}

	public void setUserAnalysisResultLink(String userAnalysisResultLink) {
		this.userAnalysisResultLink = userAnalysisResultLink;
	}
	
}