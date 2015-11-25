package com.sboss.knowledge.dao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.sboss.knowledge.exception.BizException;
import com.sboss.knowledge.exception.ResponseCode;
import com.sboss.knowledge.model.AbstractEntity;
import com.sboss.knowledge.model.PageEntity;
import com.sboss.knowledge.model.SQLEntity;
import com.sboss.knowledge.model.SQLUtils;

/**
 * 该抽象类用于支撑关系型数据库的数据访问层。
 * @author yinwenjie 
 * @param <T> 继承于AbstractEntity的所有数据对象 
 */
public abstract class AbstractRelationalDBDAO<T extends AbstractEntity> implements SystemDAO<T> {
	/**
	 * 获取该实体类的真是子类
	 * @return
	 */
	protected abstract Class<T> getEntityClass();
	
	/**
	 * 直接关联到本系统的数据源
	 */
	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;
	
	/**
	 * 获取当前数据模型的完整实体类名
	 * @return
	 */
	protected String getEntityName(){
		return getEntityClass().getName();
	}

	/* (non-Javadoc)
	 * @see com.helome.springcore.orm.IHelomeDAO#insert(com.helome.springcore.entity.AbstractEntity)
	 */
	@Override
	public void insert(T entity) {
		Session session = sessionFactory.getCurrentSession();
		session.save(entity);
	}

	/* (non-Javadoc)
	 * @see com.helome.springcore.orm.IHelomeDAO#update(com.helome.springcore.entity.AbstractEntity)
	 */
	@Override
	public void update(T entity) {
		Session session = sessionFactory.getCurrentSession();
		session.update(entity);
	}
	
	/* (non-Javadoc)
	 * @see com.tuan800.im.usercenter.dao.ITUANDAO#saveOrUpdate(com.tuan800.im.usercenter.entity.AbstractEntity)
	 */
	@Override
	public void saveOrUpdate(T entity) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(entity);
	}

	/* (non-Javadoc)
	 * @see com.helome.springcore.orm.IHelomeDAO#getEntity(java.io.Serializable)
	 */
	@Override
	public T getEntity(Serializable id) {
		Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		T object = (T)session.get(this.getEntityClass(), id);
		
		return object;
	}

	/* (non-Javadoc)
	 * @see com.helome.springcore.orm.IHelomeDAO#delete(java.io.Serializable)
	 */
	@Override
	public void delete(Serializable id) {
		T object = this.getEntity(id);
		if(object == null) {
			return;
		}
		
		//开始删除
		Session session = sessionFactory.getCurrentSession();
		session.delete(object);
		session.flush();
	}
		
	/* (non-Javadoc)
	 * @see com.ruimi.laolaibao.usercenter.dao.RUIMIDAO#refresh()
	 */
	@Override
	public void refresh() {
		Session session = sessionFactory.getCurrentSession();
		session.flush();
	}

	/**
	 * 执行一个HQL查询，适合执行insert或者update的语句
	 * @param sqlName 设置在XML文件中的，唯一的查询条件标示
	 * @param parameters 查询参数
	 * @return 满足要求的(受影响的)数据总量
	 * @throws Exception
	 */
	public int executeHQLFile(String sqlName, Map<String, Object> parameters) throws BizException {
		SQLEntity sqlEntity = SQLUtils.getSQlEntity(sqlName);
		if (sqlEntity.getExe_type().equals(SQLUtils.SELECT_TYPE)) {
			throw new BizException("This sql not is Query HQL" , ResponseCode._501);
		}
		if (sqlEntity.getSql_type().equals(SQLUtils.SQL)) {
			throw new BizException("This is HQL", ResponseCode._501);
		}
		String sql = getFileSQL(sqlName, parameters);
		return this.sessionFactory.getCurrentSession().createQuery(sql).setProperties(parameters).executeUpdate();
	}
	
	/**
	 * 执行一个SQL查询，适合执行insert或者update的语句
	 * @param sqlName 设置在XML文件中的，唯一的查询条件标示 
	 * @param parameters 查询参数
	 * @return 满足要求的(受影响的)数据总量
	 * @throws Exception
	 */
	public int executeSQLFile(String sqlName, Map<String, Object> parameters) throws BizException {
		SQLEntity sqlEntity = SQLUtils.getSQlEntity(sqlName);
		if (sqlEntity.getExe_type().equals(SQLUtils.SELECT_TYPE)) {
			throw new BizException("This sql is Query SQL" , ResponseCode._501);
		}
		if (sqlEntity.getSql_type().equals(SQLUtils.HQL)) {
			throw new BizException("This is HQL" , ResponseCode._501);
		}
		String sql = getFileSQL(sqlName, parameters);
		SQLQuery sqlQuery = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
		Query query = sqlQuery.setProperties(parameters);
		return query.executeUpdate();
	}
	
	/**
	 * 执行一个HQL查询，获取满足查询条件的所有查询结果
	 * @param sqlName 设置在XML文件中的，唯一的查询条件标示
	 * @param parameters 查询参数
	 * @return 满足查询条件的所有查询结果
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public List<T> queryByHqlFile(String sqlName, Map<String, Object> parameters) throws BizException {
		SQLEntity sqlEntity = null;
		sqlEntity = SQLUtils.getSQlEntity(sqlName);
		if (!sqlEntity.getExe_type().equals(SQLUtils.SELECT_TYPE)) {
			throw new BizException("This sql not is Query HQL" , ResponseCode._501);
		}
		if (sqlEntity.getSql_type().equals(SQLUtils.SQL)) {
			throw new BizException("This is HQL" , ResponseCode._501);
		}
		
		String hql = getFileSQL(sqlName, parameters);
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery(hql);
		query = query.setProperties(parameters);
		
		List<T> list = query.list();
		return (list == null || list.isEmpty())?null:list;
	}
	
	/**
	 * 执行一个SQL查询，获取满足查询条件的所有查询结果
	 * @param sqlName 设置在XML文件中的，唯一的查询条件标示
	 * @param parameters 查询参数
	 * @return 满足查询条件的所有查询结果
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> queryBySqlFile(String sqlName, Map<String, Object> parameters) throws BizException {
		SQLEntity sqlEntity = SQLUtils.getSQlEntity(sqlName);
		if (!sqlEntity.getExe_type().equals(SQLUtils.SELECT_TYPE)) {
			throw new BizException("This sql not is Query SQL" , ResponseCode._501);
		}
		if (sqlEntity.getSql_type().equals(SQLUtils.HQL)) {
			throw new BizException("This is HQL" , ResponseCode._501);
		}
		
		List<Object[]> list = null;
		String sql = getFileSQL(sqlName, parameters);
		Session session = this.sessionFactory.getCurrentSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		Query query = sqlQuery.setProperties(parameters);
		list = query.list();
		
		return (list == null || list.isEmpty())?null:list;
	}
	
	/**
	 * 执行一个基于HQL的分页查询，返回满足条件的当前页的信息（注意：这里并不包括查询的数据总量）
	 * @param sqlName 设置在XML文件中的，唯一的查询条件标示
	 * @param parameters 查询参数
	 * @param nowPage 需要返回的当前页，页码从0页开始索引
	 * @param maxRow 每一页的数据量，不能小于等于0。如果发现这样的参数传入，则默认为20条记录一页。
	 * @return 返回满足条件的当前页的信息（注意：这里并不包括查询的数据总量）
	 * @throws Exception
	 */
	public PageEntity queryByPageHQLFile(String sqlName,Map<String, Object> parameters,int nowPage,int maxRow) throws BizException {
		String sql = getFileSQL(sqlName, parameters);
		return findByPage(sql, parameters, nowPage, maxRow);
	}
	
	/**
	 * 执行一个基于SQL的分页查询，返回满足条件的当前页的信息（注意：这里并不包括查询的数据总量）
	 * @param sqlName 设置在XML文件中的，唯一的查询条件标示
	 * @param parameters 查询参数
	 * @param nowPage 需要返回的当前页，页码从0页开始索引
	 * @param maxRow 每一页的数据量，不能小于等于0。如果发现这样的参数传入，则默认为20条记录一页。
	 * @return 返回满足条件的当前页的信息（注意：这里并不包括查询的数据总量）
	 * @throws Exception
	 */
	public PageEntity queryByPageSQLFile(String sqlName,Map<String, Object> parameters,int nowPage,int maxRow) throws BizException {
		String sql = getFileSQL(sqlName, parameters);
		return findBySQLPage(sql, parameters, nowPage, maxRow);
	}
	
	protected String getFileSQL(String sqlName, Map<String, Object> parameters) {
		SQLEntity sqlEntity = SQLUtils.getSQlEntity(sqlName);
		Map<String, String> formatParameter = new HashMap<String, String>();
		if (!parameters.isEmpty()) {
			for (String key : parameters.keySet()) {
				Object obj = parameters.get(key);
				if (obj instanceof String) {
					if (StringUtils.isNotBlank(String.valueOf(obj))) {
						formatParameter.put(key, sqlEntity.getParameter().get(key));
					} else {
						formatParameter.put(key, "");
					}
				} else {
					if (obj == null) {
						formatParameter.put(key, "");
					} else {
						formatParameter.put(key,sqlEntity.getParameter().get(key));
					}
				}
			}
		}

		for (String key : sqlEntity.getParameter().keySet()) {
			if (!formatParameter.containsKey(key)) {
				formatParameter.put(key, "");
			}
		}

		StrSubstitutor strSubstitutor = new StrSubstitutor(formatParameter);
		String sql = strSubstitutor.replace(sqlEntity.getSql());
		return sql;
	}
	
	/**
	 * 私有化的方法，用于在分页查询成功后，构建出相应的查询结构，并进行返回
	 * @param maxPage
	 * @param maxCount
	 * @param maxRow
	 * @param nowPage
	 * @param results
	 * @return
	 */
	private PageEntity buildUpSQLPageModule(int maxPage, int maxCount, int maxRow, int nowPage, List<Object[]> resultsByObject) {
		PageEntity pageModule = new PageEntity();
		pageModule.setMaxPage(maxPage);
		pageModule.setMaxCount(maxCount);
		pageModule.setMaxPageRows(maxRow);
		pageModule.setNowPage(nowPage);
		pageModule.setResultsByObject(resultsByObject);
		return pageModule;
	}
	
	/**
	 * 私有化的方法，用于在分页查询成功后，构建出相应的查询结构，并进行返回
	 * @param maxPage
	 * @param maxCount
	 * @param maxRow
	 * @param nowPage
	 * @param results
	 * @return
	 */
	private PageEntity buildUpPageModule(int maxPage, int maxCount, int maxRow, int nowPage, List<T> results) {
		PageEntity pageModule = new PageEntity();
		pageModule.setMaxPage(maxPage);
		pageModule.setMaxCount(maxCount);
		pageModule.setMaxPageRows(maxRow);
		pageModule.setNowPage(nowPage);
		pageModule.setResults(results);
		return pageModule;
	}
	
	/**
	 * @param sql
	 * @param condition
	 * @param nowPage
	 * @param maxRow
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private PageEntity findBySQLPage(String sql, Map<String, Object> condition, int nowPage, int maxRow) {
		/*
		 * 1、首先需要根据传入的HQL信息，构造所需要的带有count信息的hql查询信息（以便查询出总的数据条数）
		 * 2、然后才是根据查询语句查询正式的数据信息
		 * */
		Session session = this.sessionFactory.getCurrentSession();
		// TODO 去掉分页查询总数
//		//=======构造查询总条数的语句
//		String contString = new String("select count(*) ");
//		//如果sql中本来就存在select关键字，则要替换这个关键字和from关键字之间的所有信息
//		String lowerSQL = sql.toLowerCase(Locale.CHINA);
//		if(lowerSQL.indexOf("select") != -1) {
//			int endSubIndex = lowerSQL.indexOf("from");
//			contString = contString + sql.substring(endSubIndex);
//		} else {
//			contString = contString + sql;
//		}
//		//=======开始查询数量
//		Query query = session.createSQLQuery(contString.toString());
//		query.setProperties(condition);
//		List<Object> countValue = query.list();
//		if (countValue == null || countValue.size() == 0) {
//			return buildUpSQLPageModule(0, 0, maxRow, nowPage , new LinkedList<Object[]>());
//		}
//		int count = Integer.parseInt(countValue.get(0).toString());
//		if (count == 0) {
//			return buildUpSQLPageModule(0, 0, maxRow, nowPage, new LinkedList<Object[]>());
//		}
		// TODO 总数设置成0
		int count = 0;	
		
		//=======开始正式查询数据
		Query results = session.createSQLQuery(sql);
		results.setProperties(condition);
		List<Object[]> list = results.setFirstResult(nowPage * maxRow).setMaxResults(maxRow).list();
		if(list == null || list.isEmpty()) {
			list = null;
		}
		
		int maxPage = count / maxRow + (count % maxRow == 0 ? 0 : 1);
		return buildUpSQLPageModule(maxPage, count, maxRow, nowPage, list);
	}
	
	/**
	 * @param hql
	 * @param condition
	 * @param nowPage
	 * @param maxRow
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private PageEntity findByPage(String hql, Map<String, Object> condition, int nowPage, int maxRow) {
		Session session = this.sessionFactory.getCurrentSession();
		/*
		 * 1、首先需要根据传入的HQL信息，构造所需要的带有count信息的hql查询信息（以便查询出总的数据条数）
		 * 2、然后才是根据查询语句查询正式的数据信息
		 * */
		//=======构造查询总条数的语句
		// TODO 去掉分页查询总数
//		String contString = new String("select count(*) as _count_ ");
//		//如果sql中本来就存在select关键字，则要替换这个关键字和from关键字之间的所有信息
//		String lowerSQL = hql.toLowerCase(Locale.CHINA);
//		if(lowerSQL.indexOf("select") != -1) {
//			int endSubIndex = lowerSQL.indexOf("from");
//			contString = contString + hql.substring(endSubIndex);
//		} else {
//			contString = contString + hql;
//		}
//		//TODO 由于未知原因，在进行count统计时，去掉HQL中可能带有的fetch关键字
//		contString = contString.replaceAll("fetch", "");
//		contString = contString.replaceAll("FETCH", "");
//		
//		//=======开始查询数量
//		Query query = session.createQuery(contString.toString());
//		query.setProperties(condition);
//		List<Object> countValue = query.list();
//		if (countValue == null || countValue.size() == 0) {
//			return buildUpPageModule(0, 0, maxRow, nowPage , new LinkedList<T>());
//		}
		
//		int count = Integer.parseInt(countValue.get(0).toString());
//		if (count == 0) {
//			return buildUpPageModule(0, 0, maxRow, nowPage, new LinkedList<T>());
//		}
		// TODO 总数设置成0
		int count = 0;
		Query results = session.createQuery(hql);
		results.setProperties(condition);
		List<T> list = results.setFirstResult(nowPage * maxRow).setMaxResults(maxRow).list();
		if(list == null || list.isEmpty()) {
			list = null;
		}
		
		int maxPage = count / maxRow + (count % maxRow == 0 ? 0 : 1);	
		//构造分页信息
		return buildUpPageModule(maxPage, count, maxRow, nowPage, list);
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(@Qualifier("sessionFactory")SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
