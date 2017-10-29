package com.wangfanpinche.dao;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.wangfanpinche.dto.base.BaseEntity;
import com.wangfanpinche.vo.base.PageHelper;

@SuppressWarnings("rawtypes")
public interface BaseDao {
	
	public Session getCurrentSession();
	
	public void saveOrUpdate(BaseEntity o);

	/**
	 * 保存对象
	 * @param o
	 * @return 对象的ID
	 */
	public Serializable save(BaseEntity o);
	/**
	 * 保存对象
	 * @param o
	 * @return 对象的ID
	 */
	public void persist(BaseEntity o);
	
	/**
	 * 删除一个对象
	 * @param o 对象
	 */
	public void delete(BaseEntity o);

	/**
	 * 更新一个对象
	 * @param o 对象
	 */
	public void update(BaseEntity o);
	
	/**
	 * 通过主键获得对象
	 * @param c 类型
	 * @param id id
	 * @return 要获得的对象
	 */
	public <M> M getById(Class<M> c, Serializable id);
	
	/**
	 * 通过HQL语句获取一个对象
	 * @param hql HQL语句
	 * @param params 参数
	 * @return 对象
	 */
	public <M> M get(Class<M> resultType, String hql, Map<String, Object> params);
	public <M> M get(Class<M> resultType, String hql, Object...params);
	public <M> M get(Class<M> resultType, String hql);

	/**
	 * 通过HQL获得一个集合
	 * @param hql HQL语句
	 * @param params 参数
	 * @param page 开始页数
	 * @param rows 每页记录数
	 * @return
	 */
	public <M> List<M> find(Class<M> resultType, String hql, Map<String, Object> params, PageHelper ph);
	public <M> List<M> find(Class<M> resultType, String hql, Map<String, Object> params);
	public <M> List<M> find(Class<M> resultType, String hql,PageHelper ph, Object...params);
	public <M> List<M> find(Class<M> resultType, String hql, Object...params);
	public <M> List<M> find(Class<M> resultType, String hql, PageHelper ph);
	public <M> List<M> find(Class<M> resultType, String hql);
	
	/**
	 * 统计
	 * @param hql HQL语句
	 * @param params 参数
	 * @return 记录数
	 */
	public Long count(String hql, Map<String, Object> params);
	public Long count(String hql, Object...params);
	public Long count(String hql);
	
	/**
	 * 执行HQL语句
	 * @param hql HQL语句
	 * @param params 参数
	 * @return 影响的结果数目
	 */
	public int execute(String hql, Map<String, Object> params);
	public int execute(String hql, Object...params);
	public int execute(String hql);

	/**
	 * 通过SQL查询
	 * @param sql SQL语句
	 * @param params 参数
	 * @param page 开始页数
	 * @param rows 第页记录数
	 * @return
	 */
	public List findBySql(String sql, Map<String, Object> params, PageHelper ph);
	public List findBySql(String sql, Map<String, Object> params);
	public List findBySql(String sql, List<Object> params);
	public List findBySql(String sql, PageHelper ph);
	public List findBySql(String sql);
	
	/**
	 * 统计
	 * @param sql SQL语句
	 * @return 数目
	 */
	public BigInteger countBySql(String sql, Map<String, Object> params);
	public BigInteger countBySql(String sql);

	/**
	 * 执行SQL语句
	 * @param sql
	 * @param params
	 * @return
	 */
	public int executeSql(String sql, Map<String, Object> params);
	public int executeSql(String sql);

	<M> List<M> findBySql(Class<M> c, String sql, Map<String, Object> params);
}
