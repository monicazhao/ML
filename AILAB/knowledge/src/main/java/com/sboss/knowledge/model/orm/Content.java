package com.sboss.knowledge.model.orm;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sboss.knowledge.model.IDEntity;

/**
 * 内容
 * @author monica
 *
 */
@Entity
@Table(name = "content")
public class Content extends IDEntity {

	private static final long serialVersionUID = -7788354832917368824L;
	
	//算法编号，多个内容可对应一个算法
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ALGORITHMID", nullable = false)
	private Algorithm algorithm;
	
	//内容来源（前端用户传入的url地址或者文本内容）
	@Column(name = "SOURCEURL", length = 200, nullable = false)
	private String sourceUrl;
	
	//内容对应在文件服务器上的地址
	@Column(name = "CONTENTLINK", length = 100, nullable = false)
	private String contentLink;
	
	//内容分析结果对应在文件服务器上的地址
	@Column(name = "ANALYSISRESULTLINK", length = 100, nullable = true)
	private String analysisResultLink;
	
	//内容分析时间
	@Column(name = "EXECUTETIME", length = 100, nullable = true)
	private String executeTime;
	
	//内容类型（图片、文本、视频、音频）
	@Column(name = "CONTENTTYPE", nullable = false)
	private int contentType;
	
	//内容长度（可用于判断用户对内容兴趣度的真实性）
	@Column(name = "CONTENTLENGTH", nullable = false)
	private Long contentLength;
	
	//内容信息——一个内容可对应多个联系
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "content")
	private Set<UserContent> userContents;

	public Algorithm getAlgorithmId() {
		return algorithm;
	}

	public void setAlgorithmId(Algorithm algorithm) {
		this.algorithm = algorithm;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public String getContentLink() {
		return contentLink;
	}

	public void setContentLink(String contentLink) {
		this.contentLink = contentLink;
	}

	public String getAnalysisResultLink() {
		return analysisResultLink;
	}

	public void setAnalysisResultLink(String analysisResultLink) {
		this.analysisResultLink = analysisResultLink;
	}

	public String getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(String executeTime) {
		this.executeTime = executeTime;
	}

	public int getContentType() {
		return contentType;
	}

	public void setContentType(int contentType) {
		this.contentType = contentType;
	}

	public Long getContentLength() {
		return contentLength;
	}

	public void setContentLength(Long contentLength) {
		this.contentLength = contentLength;
	}	

}