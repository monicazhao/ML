package com.sboss.knowledge.model.orm;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sboss.knowledge.model.IDEntity;

/**
 * 算法信息
 * @author monica
 *
 */
@Entity
@Table(name = "algorithm")
public class Algorithm extends IDEntity {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 368117260254802953L;
	

	/**
	 * 算法ID
	 */
	@Column(name = "ALGORITHMID")
	private Long algorithmId;
	
	//算法生效日期
	@Column(name = "EFFECTIVEDATE", nullable=true)
	private Date effectiveDate;
	
	//算法的数据来源
	@Column(name = "DATASOURCE", length = 200, nullable = true)
	private String dataSource;
	
	//算法模型
	@Column(name = "MODEL", length = 200, nullable = true)
	private String model;
	
	//算法版本
	@Column(name = "ALGORITHMVERSION", length = 100, nullable = false)
	private String algorithmVersion;
	
	//算法名称
	@Column(name = "ALGORITHMNAME", length = 100, nullable = false)
	private String algorithmName;
	
	//算法描述
	@Column(name = "ALGORITHMDESC", length = 200, nullable = true)
	private String algorithmDesc;
	
	//一个算法可对应多个内容
	@OneToMany(fetch=FetchType.LAZY, mappedBy="algorithm")
	private Set<Content> contents;

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getAlgorithmVersion() {
		return algorithmVersion;
	}

	public void setAlgorithmVersion(String algorithmVersion) {
		this.algorithmVersion = algorithmVersion;
	}

	public String getAlgorithmName() {
		return algorithmName;
	}

	public void setAlgorithmName(String algorithmName) {
		this.algorithmName = algorithmName;
	}

	public String getAlgorithmDesc() {
		return algorithmDesc;
	}

	public void setAlgorithmDesc(String algorithmDesc) {
		this.algorithmDesc = algorithmDesc;
	}

	public Set<Content> getContents() {
		return contents;
	}

	public void setContents(Set<Content> contents) {
		this.contents = contents;
	}

	public Long getAlgorithmId() {
		return algorithmId;
	}

	public void setAlgorithmId(Long algorithmId) {
		this.algorithmId = algorithmId;
	}

}