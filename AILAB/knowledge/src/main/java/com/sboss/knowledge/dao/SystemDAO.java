package com.sboss.knowledge.dao;

import java.io.Serializable;

import com.sboss.knowledge.model.AbstractEntity;

/**
 * 所有数据服务层的顶层定义接口，在这个接口下，我们分别使用：<br>
 * AbstractRelationalDBDAO支持关系型数据库
 * AbstractRepositoryDAO支持待查询功能非关系型数据库
 * AbstractHadoopDAO提供对hadoop的Mapreduce支持
 * @author yinwenjie
 */
public interface SystemDAO <T extends AbstractEntity>{
	/**
	 * 向数据层插入一个实体对象 
	 * @param Entity
	 */
	public void insert(T Entity);
	
	/**
	 * 向数据层传入带有唯一标示的对象，进行修改
	 * @param Entity
	 */
	public void update(T Entity);
	
	/**
	 * 向数据层传入T实体，数据层将根据实体主键信息决定是更新数据库还是向数据库写入新数据
	 * @param entity
	 */
	public void saveOrUpdate(T entity);
	
	/**
	 * 刷新数据层回话，在一些负责业务里面，可能出现一条数据先插入然后马上更新的情况<br>
	 * 这种情况下需要显示条用数据层的refresh方法，以保证insert在update之前完成
	 */
	public void refresh();
	
	/**
	 * 在数据层使用一个可以被串行化的唯一标示，找到相应的数据对象
	 * @param id
	 */
	public T getEntity(Serializable id);
	
	/**
	 * 在数据层删除指定的唯一标示的数据
	 * @param id
	 */
	public void delete(Serializable id);
}
