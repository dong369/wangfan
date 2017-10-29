package com.wangfanpinche.dao.impl;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wangfanpinche.dao.BaseDao;
import com.wangfanpinche.dto.base.BaseEntity;
import com.wangfanpinche.vo.base.PageHelper;

@Repository
public class BaseDaoImpl implements BaseDao {

	@Resource
	private SessionFactory sessionFactory;

	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public Serializable save(BaseEntity o) {
		if (o != null) {
			o.setId(UUID.randomUUID().toString());
			o.setCreateDateTime(LocalDateTime.now());
			o.setModifyDateTime(o.getCreateDateTime());
			o.setDeleted(false);
			return getCurrentSession().save(o);
		}
		return null;
	}

	@Override
	public void saveOrUpdate(BaseEntity o) {
		if (o != null) {
			if (!StringUtils.hasText(o.getId())) {
				o.setId(UUID.randomUUID().toString());
			}
			o.setCreateDateTime(LocalDateTime.now());
			o.setModifyDateTime(o.getCreateDateTime());
			o.setDeleted(false);
			getCurrentSession().saveOrUpdate(o);
		}
	}

	@Override
	public <M> M getById(Class<M> c, Serializable id) {
		return getCurrentSession().get(c, id);
	}

	@Override
	public void delete(BaseEntity o) {
		if (o != null) {
			getCurrentSession().delete(o);
		}
	}

	@Override
	public void update(BaseEntity o) {
		if (o != null) {
			o.setModifyDateTime(LocalDateTime.now());
			getCurrentSession().update(o);
		}
	}

	@Override
	public Long count(String hql) {
		org.hibernate.query.Query<Number> q = getCurrentSession().createQuery(hql, Number.class);
		return q.getSingleResult().longValue();
	}

	@Override
	public Long count(String hql, Map<String, Object> params) {
		org.hibernate.query.Query<Number> q = getCurrentSession().createQuery(hql, Number.class);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				Object obj = params.get(key);
				// 这里考虑传入的参数是什么类型，不同类型使用的方法不同
				if (obj instanceof Collection<?>) {
					q.setParameterList(key, (Collection<?>) obj);
				} else if (obj instanceof Object[]) {
					q.setParameterList(key, (Object[]) obj);
				} else {
					q.setParameter(key, obj);
				}
			}
		}
		return (Long) q.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int execute(String hql) {
		org.hibernate.query.Query q = getCurrentSession().createQuery(hql);
		return q.executeUpdate();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int execute(String hql, Map<String, Object> params) {
		org.hibernate.query.Query q = getCurrentSession().createQuery(hql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				Object obj = params.get(key);
				// 这里考虑传入的参数是什么类型，不同类型使用的方法不同
				if (obj instanceof Collection<?>) {
					q.setParameterList(key, (Collection<?>) obj);
				} else if (obj instanceof Object[]) {
					q.setParameterList(key, (Object[]) obj);
				} else {
					q.setParameter(key, obj);
				}
			}
		}
		return q.executeUpdate();
	}

	@Override
	public List findBySql(String sql) {
		NativeQuery createNativeQuery = getCurrentSession().createNativeQuery(sql);
		return createNativeQuery.getResultList();
	}

	@Override
	public List findBySql(String sql, PageHelper ph) {
		NativeQuery q = getCurrentSession().createNativeQuery(sql);
		return q.setFirstResult(ph.getOffset()).setMaxResults(ph.getLimit()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();
	}

	@Override
	public List findBySql(String sql, Map<String, Object> params) {
		NativeQuery q = getCurrentSession().createNativeQuery(sql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				Object obj = params.get(key);
				// 这里考虑传入的参数是什么类型，不同类型使用的方法不同
				if (obj instanceof Collection<?>) {
					q.setParameterList(key, (Collection<?>) obj);
				} else if (obj instanceof Object[]) {
					q.setParameterList(key, (Object[]) obj);
				} else {
					q.setParameter(key, obj);
				}
			}
		}
		return q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();
	}
	
	@Override
	public  <M> List<M> findBySql(Class<M> c,String sql, Map<String, Object> params) {
		NativeQuery q = getCurrentSession().createNativeQuery(sql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				Object obj = params.get(key);
				// 这里考虑传入的参数是什么类型，不同类型使用的方法不同
				if (obj instanceof Collection<?>) {
					q.setParameterList(key, (Collection<?>) obj);
				} else if (obj instanceof Object[]) {
					q.setParameterList(key, (Object[]) obj);
				} else {
					q.setParameter(key, obj);
				}
			}
		}
		return q.setResultTransformer(Transformers.aliasToBean(c)).getResultList();
	}

	@Override
	public List findBySql(String sql, Map<String, Object> params, PageHelper ph) {
		NativeQuery q = getCurrentSession().createNativeQuery(sql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				Object obj = params.get(key);
				// 这里考虑传入的参数是什么类型，不同类型使用的方法不同
				if (obj instanceof Collection<?>) {
					q.setParameterList(key, (Collection<?>) obj);
				} else if (obj instanceof Object[]) {
					q.setParameterList(key, (Object[]) obj);
				} else {
					q.setParameter(key, obj);
				}
			}
		}
		return q.setFirstResult(ph.getOffset()).setMaxResults(ph.getLimit()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();
	}

	@Override
	public int executeSql(String sql) {
		NativeQuery q = getCurrentSession().createNativeQuery(sql);
		return q.executeUpdate();
	}

	@Override
	public int executeSql(String sql, Map<String, Object> params) {
		NativeQuery q = getCurrentSession().createNativeQuery(sql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				Object obj = params.get(key);
				// 这里考虑传入的参数是什么类型，不同类型使用的方法不同
				if (obj instanceof Collection<?>) {
					q.setParameterList(key, (Collection<?>) obj);
				} else if (obj instanceof Object[]) {
					q.setParameterList(key, (Object[]) obj);
				} else {
					q.setParameter(key, obj);
				}
			}
		}
		return q.executeUpdate();
	}

	@Override
	public BigInteger countBySql(String sql) {
		NativeQuery q = getCurrentSession().createNativeQuery(sql);
		return (BigInteger) q.uniqueResult();
	}

	@Override
	public BigInteger countBySql(String sql, Map<String, Object> params) {
		NativeQuery q = getCurrentSession().createNativeQuery(sql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				Object obj = params.get(key);
				// 这里考虑传入的参数是什么类型，不同类型使用的方法不同
				if (obj instanceof Collection<?>) {
					q.setParameterList(key, (Collection<?>) obj);
				} else if (obj instanceof Object[]) {
					q.setParameterList(key, (Object[]) obj);
				} else {
					q.setParameter(key, obj);
				}
			}
		}
		return (BigInteger) q.uniqueResult();
	}

	@Override
	public Long count(String hql, Object... params) {
		org.hibernate.query.Query q = getCurrentSession().createQuery(hql);
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				q.setParameter(i, params[i]);
			}
		}
		return (Long) q.getSingleResult();
	}

	@Override
	public int execute(String hql, Object... params) {
		org.hibernate.query.Query q = getCurrentSession().createQuery(hql);
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				q.setParameter(i, params[i]);
			}
		}
		return q.executeUpdate();
	}

	@Override
	public List findBySql(String sql, List<Object> params) {
		NativeQuery q = getCurrentSession().createNativeQuery(sql);
		if (params != null && params.size() > 0) {
			for (int i = 0; i < params.size(); i++) {
				q.setParameter(i, params.get(i));
			}
		}
		return q.setFirstResult(0).setMaxResults(10).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();
	}

	@Override
	public <M> M get(Class<M> clazz, String hql, Map<String, Object> params) {
		org.hibernate.query.Query<M> q = getCurrentSession().createQuery(hql, clazz);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				Object obj = params.get(key);
				// 这里考虑传入的参数是什么类型，不同类型使用的方法不同
				if (obj instanceof Collection<?>) {
					q.setParameterList(key, (Collection<?>) obj);
				} else if (obj instanceof Object[]) {
					q.setParameterList(key, (Object[]) obj);
				} else {
					q.setParameter(key, obj);
				}
			}
		}
		try {
			return q.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public <M> M get(Class<M> clazz, String hql, Object... params) {
		org.hibernate.query.Query<M> q = getCurrentSession().createQuery(hql, clazz);
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				q.setParameter(i, params[i]);
			}
		}
		try {
			return q.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public <M> M get(Class<M> clazz, String hql) {
		Query<M> q = getCurrentSession().createQuery(hql, clazz);
		try {
			return q.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public <M> List<M> find(Class<M> resultType, String hql, Map<String, Object> params, PageHelper ph) {
		org.hibernate.query.Query<M> q = getCurrentSession().createQuery(hql, resultType);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				Object obj = params.get(key);
				// 这里考虑传入的参数是什么类型，不同类型使用的方法不同
				if (obj instanceof Collection<?>) {
					q.setParameterList(key, (Collection<?>) obj);
				} else if (obj instanceof Object[]) {
					q.setParameterList(key, (Object[]) obj);
				} else {
					q.setParameter(key, obj);
				}
			}
		}
		return q.setFirstResult(ph.getOffset()).setMaxResults(ph.getLimit()).getResultList();
	}

	@Override
	public <M> List<M> find(Class<M> resultType, String hql, Map<String, Object> params) {
		org.hibernate.query.Query<M> q = getCurrentSession().createQuery(hql, resultType);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				Object obj = params.get(key);
				// 这里考虑传入的参数是什么类型，不同类型使用的方法不同
				if (obj instanceof Collection<?>) {
					q.setParameterList(key, (Collection<?>) obj);
				} else if (obj instanceof Object[]) {
					q.setParameterList(key, (Object[]) obj);
				} else {
					q.setParameter(key, obj);
				}
			}
		}
		return q.getResultList();
	}

	@Override
	public <M> List<M> find(Class<M> resultType, String hql, PageHelper ph, Object... params) {
		org.hibernate.query.Query<M> q = getCurrentSession().createQuery(hql, resultType);
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				q.setParameter(i, params[i]);
			}
		}
		return q.setFirstResult(ph.getOffset()).setMaxResults(ph.getLimit()).getResultList();
	}

	@Override
	public <M> List<M> find(Class<M> resultType, String hql, Object... params) {
		org.hibernate.query.Query<M> q = getCurrentSession().createQuery(hql, resultType);
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				q.setParameter(i, params[i]);
			}
		}
		return q.getResultList();
	}

	@Override
	public <M> List<M> find(Class<M> resultType, String hql, PageHelper ph) {
		org.hibernate.query.Query<M> q = getCurrentSession().createQuery(hql, resultType);
		return q.setFirstResult(ph.getOffset()).setMaxResults(ph.getLimit()).getResultList();
	}

	@Override
	public <M> List<M> find(Class<M> resultType, String hql) {
		org.hibernate.query.Query<M> q = getCurrentSession().createQuery(hql, resultType);
		return q.getResultList();
	}

	@Override
	public void persist(BaseEntity o) {
		if (o != null) {
			o.setId(UUID.randomUUID().toString());
			o.setCreateDateTime(LocalDateTime.now());
			o.setModifyDateTime(o.getCreateDateTime());
			o.setDeleted(false);
		}
		getCurrentSession().persist(o);
	}
}
