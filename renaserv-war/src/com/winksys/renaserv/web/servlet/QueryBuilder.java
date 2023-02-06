package com.winksys.renaserv.web.servlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Utiltário para criação de query de consulta HQL
 * 
 * @author winksys
 *
 */
public class QueryBuilder {

	private ArrayList<String> where = new ArrayList<String>();
	private ArrayList<String> projection = new ArrayList<String>();
	private ArrayList<String> groupBy = new ArrayList<String>();
	private ArrayList<String> order = new ArrayList<String>();
	private ArrayList<String> from = new ArrayList<String>();
	private HashMap<String, Object> parameters = new HashMap<String, Object>();
	
	public QueryBuilder addFrom(String fromClause) {
		from.add(fromClause);
		return this;
	}
	public QueryBuilder addWhere(String whereClause) {
		where.add(whereClause);
		return this;
	}
	public QueryBuilder addOrder(String orderClause) {
		order.add(orderClause);
		return this;
	}
	public QueryBuilder addProjection(String projectionClause) {
		projection.add(projectionClause);
		return this;
	}
	public QueryBuilder addGroup(String groupBy) {
		this.groupBy.add(groupBy);
		return this;
	}
	public QueryBuilder setParameter(String name, Object value) {
		parameters.put(name,value);
		return this;
	}
	
	public Query build(EntityManager em) {
		
		if (from.isEmpty()) {
			throw new IllegalStateException("Clause from is empty");
		}
		
		StringBuilder sb = new StringBuilder();
		
		int i = 0;
		for(String proj : projection) {
			if (i == 0) {
				sb.append("select ");
				sb.append(proj);
			} else {
				sb.append(",").append(proj);
			}
			i++;
		}
		
		i = 0;
		for(String f : from) {
			if (i == 0) {
				sb.append(" from ").append(f).append(" ");
			} else {
				sb.append(f).append(" ");
			}
			i++;
		}
		
		i = 0;
		for(String f : where) {
			if (i == 0) {
				sb.append(" where ").append(f);
			} else {
				sb.append(" and ").append(f);
			}
			i++;
		}
		
		i = 0;
		for(String f : groupBy) {
			if (i == 0) {
				sb.append(" group by ").append(f);
			} else {
				sb.append(",").append(f);
			}
			i++;
		}
		
		i = 0;
		for(String f : order) {
			if (i == 0) {
				sb.append(" order by ").append(f);
			} else {
				sb.append(",").append(f);
			}
			i++;
		}
		
		Query query = em.createQuery(sb.toString());
		
		for(Entry<String,Object> e : parameters.entrySet()) {
			query.setParameter(e.getKey(), e.getValue());
		}
		
		return query;
	}
	
	public void clearProjection() {
		projection.clear();
	}
	
	public static QueryBuilder init() {
		return new QueryBuilder();
	}
	
}
