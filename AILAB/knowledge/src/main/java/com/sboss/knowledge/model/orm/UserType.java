package com.sboss.knowledge.model.orm;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sboss.knowledge.model.IDEntity;

/**
 * 用户类型信息
 * @author monica
 *
 */
@Entity
@Table(name = "user_type")
public class UserType extends IDEntity {
    
    private static final long serialVersionUID = -1205063392214480018L;
	
	@Column(name = "USERTYPEVALUE")
	private String userTypeValue;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="userType")
	private Set<User> users;
	
	public String getUserTypeValue(){
		return userTypeValue;
	}
	
	public void setUserTypeValue(String userTypeValue){
		this.userTypeValue = userTypeValue;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
}