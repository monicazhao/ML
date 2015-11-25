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
 * 用户信息
 * @author monica
 *
 */
@Entity
@Table(name = "user_info")
public class User extends IDEntity {
	
	/**
     * 序列号
     */
    private static final long serialVersionUID = -3515989583691782453L;
	
	//用户编号，微信和QQ的userId可能相同，所以必须用id来作为主键
	@Column(name = "USERID", length=100 , nullable = false)
	private String userId;
	
	//用户类型，说明用户来源，比如是QQ用户，微信用户，微博用户等
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USERTYPE", nullable = false)
	private UserType userType;
	
	//用户内容，一个用户可对应多个内容
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private Set<UserContent> userContents;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}
	
}