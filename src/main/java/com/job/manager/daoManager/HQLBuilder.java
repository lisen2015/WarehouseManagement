/*
 * FileName：HQLBuilder.java
 *
 * Description：hql 语句
 *
 * History：
 * 版本号 		作者 			日期       			简介
 *  1.0   	    chenchen	    2013-10-24  		Create
 *  2.0   	    chenchen	    2016-07-05  		update 新增逻辑删除
 */
package com.job.manager.daoManager;

import com.job.manager.util.ArrayUtil;
import com.job.manager.util.ProjectContext;
import com.job.manager.util.StringHelper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class HQLBuilder {
	private static final String HQL_SEPARATOR_COMMA = ",";
	private static final String HQL_LEFTBRANKET = "(";
	private static final String HQL_RIGHTBRANKET = ")";
	private static final String HQL_DESC = "DESC";
	private static final String HQL_ASC = "ASC";
	private static final String HQL_OR = "OR";
	private static final String HQL_AND = "AND";
	private static final String HQL_SET = "SET";
	private static final String HQL_FROM = "FROM";
	private static final String HQL_ORDERBY = "ORDER BY";
	private static final String HQL_WHERE = "WHERE";
	private static final String HQL_SEPARATOR = " ";
	private static final String PREFIX_UPDATE = "UPDATE";
	private static final String PREFIX_DELETE = "DELETE";
	private List<Condition> parameterValues = new ArrayList();
	private List<Expression> whereExpressions = new ArrayList();
	private Expression currentExpression;
	private Class<? extends Object> objectClass;
	private String prefix;
	private List<OrderBy> orderBys = new ArrayList();

	private List<Condition> assignExpressions = new ArrayList();

	protected HQLBuilder(String prefix, Class<? extends Object> objectClass) {
		this.prefix = prefix;
		this.objectClass = objectClass;
	}

	public static HQLBuilder DELETE(Class<? extends Object> objectClass) {
		HQLBuilder builder = new HQLBuilder("DELETE", objectClass);
		return builder;
	}
    public static HQLBuilder LOGICDELLETE(Class<? extends Object> objectClass) {
        HQLBuilder builder = new HQLBuilder("UPDATE", objectClass);
        builder.setNewValue("isValidate",0);
        builder.setNewValue("deleteTime",new Timestamp(System.currentTimeMillis()));
        if (ProjectContext.getUser()!=null){
            builder.setNewValue("lastUpdateUser",ProjectContext.getUser().getId());
        }else {
            builder.setNewValue("lastUpdateUser",0);
        }
        return builder;
    }
	public static HQLBuilder UPDATE(Class<? extends Object> objectClass) {
		HQLBuilder builder = new HQLBuilder("UPDATE", objectClass);
		return builder;
	}

	/**
	 * 增加一个判断条件<br />
	 * eg: addCondition(">",a,2) ,where a>2
	 * @param operator 条件
	 * @param field 字段
	 * @param value 内容
	 * @version 
	 * 	2013-10-24 	chenchen 	create <br />
	 *  2014-11-27 	chenchen	将方法从public改为private <br />
	 */
	private HQLBuilder addCondition(String field, String operator, Object value) {
		if ((isUpdateHQL()) && (assignExpressions.size() == 0)) {
			throw new IllegalArgumentException("请先设置要更新的数据！！！");
		}
		currentExpression = new Condition(field, operator, value, false);
		whereExpressions.add(currentExpression);
		return this;
	}

	/**
	 * 增加一个判断条件,用于字段对比 <br />
	 * eg: addCondition(">",a,b)=where a>b
	 * @param operator 条件
	 * @param field 字段
	 * @param value 内容
	 * @version 
	 * 	2013-10-24 	chenchen	create	<br />
	 * 	2014-11-27 	chenchen	将方法从public改为private	<br />
	 */
	private HQLBuilder addField(String field, String operator, Object value) {
		if ((isUpdateHQL()) && (assignExpressions.size() == 0)) {
			throw new IllegalArgumentException("请先设置要更新的数据！！！");
		}
		currentExpression = new Condition(field, operator, value, true);
		whereExpressions.add(currentExpression);
		return this;
	}
	/**
	 * 增加一个in条件<br />
	 * eg: addInCondition(a,[1,2])=where a in (1,2)
	 * @param field 检索字段
	 * @param value 检索内容
	 * @version 
	 * 	2013-10-24 	chenchen	create	<br />
	 */
	public HQLBuilder addInCondition(String field, Object[] value) {
		if ((value == null) || (value.length == 0)) {
			return this;
		}
		currentExpression = new InCondition(field, value);
		whereExpressions.add(currentExpression);
		return this;
	}
	/**
	 * 增加一个in条件<br />
	 * eg: addInCondition(a,[1,2])=where a in (1,2)
	 * @param field 检索字段
	 * @param value 检索内容
	 * @version 
	 * 	2013-10-24 	chenchen	create	<br />
	 */
	public HQLBuilder addInCondition(String field, int[] value) {
		return addInCondition(field, ArrayUtil.toIntegerArray(value));
	}
	/**
	 * 增加一个not in条件<br />
	 * eg: addNotInCondition(a,[1,2])=where a not in (1,2)
	 * @param field 检索字段
	 * @param value 检索内容
	 * @version 
	 * 	2013-10-24 	chenchen	create	<br />
	 */
	public HQLBuilder addNotInCondition(String field, Object[] value) {
		if ((value == null) || (value.length == 0)) {
			return this;
		}

		currentExpression = new NotInCondition(field, value);
		whereExpressions.add(currentExpression);
		return this;
	}
	/**
	 * 增加一个not in条件<br />
	 * eg: addNotInCondition(a,[1,2])=where a not in (1,2)
	 * @param field 检索字段
	 * @param value 检索内容
	 * @version 
	 * 	2013-10-24 	chenchen	create	<br />
	 */
	public HQLBuilder addNotInCondition(String field, int[] value) {
		return addNotInCondition(field, ArrayUtil.toIntegerArray(value));
	}
	/**
	 * 增加一个>条件<br />
	 * eg: addGreaterThan(a,3)>where a>3
	 * @param field 检索字段
	 * @param value 检索内容
	 * @version 
	 * 	2013-10-24 	chenchen	create	<br />
	 */
	public HQLBuilder addGreaterThan(String field, Object value) {
		return addCondition(field, ">", value);
	}
	/**
	 * 增加一个>条件，用于字段对比<br />
	 * eg: addGreaterThanField(a,3)>where a>3
	 * @param field 检索字段1
	 * @param value 检索字段2
	 * @version 
	 * 	2013-10-24 	chenchen	create	<br />
	 */
	public HQLBuilder addGreaterThanField(String field, Object value) {
		return addField(field, ">", value);
	}
	/**
	 * 增加一个>=条件<br />
	 * eg: addGreaterThanEquals(a,3)=where a>=3
	 * @param field 检索字段
	 * @param value 检索内容
	 * @version 
	 * 	2013-10-24 	chenchen	create	<br />
	 */
	public HQLBuilder addGreaterThanEquals(String field, Object value) {
		return addCondition(field, ">=", value);
	}
	/**
	 * 增加一个>=条件，用于字段对比<br />
	 * eg: addGreaterThanEqualsField(a,3)=where a>=3
	 * @param field 检索字段1
	 * @param value 检索字段2
	 * @version 
	 * 	2013-10-24 	chenchen	create	<br />
	 */
	public HQLBuilder addGreaterThanEqualsField(String field, Object value) {
		return addField(field, ">=", value);
	}
	/**
	 * 增加一个< 条件<br />
	 * eg: addLesserThan(a,3)=where a<3
	 * @param field 检索字段
	 * @param value 检索内容
	 * @version 
	 * 	2013-10-24 	chenchen	create	<br />
	 */
	public HQLBuilder addLesserThan(String field, Object value) {
		return addCondition(field, "<", value);
	}
	/**
	 * 增加一个< 条件,用于字段对比<br />
	 * eg: addLesserThanField(a,3)=where a<3
	 * @param field 检索字段1
	 * @param value 检索字段2
	 * @version 
	 * 	2013-10-24 	chenchen	create	<br />
	 */
	public HQLBuilder addLesserThanField(String field, Object value) {
		return addField(field, "<", value);
	}
	/**
	 * 增加一个<=条件<br />
	 * eg: addLesserThanEquals(a,3)=where a<=3
	 * @param field 检索字段
	 * @param value 检索内容
	 * @version 
	 * 	2013-10-24 	chenchen	create	<br />
	 */
	public HQLBuilder addLesserThanEquals(String field, Object value) {
		return addCondition(field, "<=", value);
	}
	/**
	 * 增加一个<=条件，用于字段对比<br />
	 * eg: addLesserThanEqualsField(a,3)=where <=3
	 * @param field 检索字段1
	 * @param value 检索字段2
	 * @version 
	 * 	2013-10-24 	chenchen	create	<br />
	 */
	public HQLBuilder addLesserThanEqualsField(String field, Object value) {
		return addField(field, "<=", value);
	}
	
	/**
	 * 增加一个=条件<br />
	 * eg: addEqCondition(a,3)=where =3
	 * @param field 检索字段
	 * @param value 检索值
	 * @version 
	 * 	2013-10-24 	chenchen	create	<br />
	 */
	public HQLBuilder addEqCondition(String field, Object value) {
		return addCondition(field, "=", value);
	}
	/**
	 * 增加一个=条件，用于字段对比<br />
	 * eg: addEqConditionField(a,3)=where =3
	 * @param field 检索字段1
	 * @param value 检索字段2
	 * @version 
	 * 	2013-10-24 	chenchen	create	<br />
	 */
	public HQLBuilder addEqConditionField(String field, Object value) {
		return addField(field, "=", value);
	}
	/**
	 * 增加一个<>条件<br />
	 * eg: addNotEqCondition(a,3)=where <>3
	 * @param field 检索字段
	 * @param value 检索值
	 * @version 
	 * 	2013-10-24 	chenchen	create	<br />
	 */
	public HQLBuilder addNotEqCondition(String field, Object value) {
		return addCondition(field, "<>", value);
	}
	/**
	 * 增加一个<>条件，用于字段对比<br />
	 * eg: addNotEqConditionField(a,b)=where <>b
	 * @param field 检索字段1
	 * @param value 检索字段2
	 * @version 
	 * 	2013-10-24 	chenchen	create	<br />
	 */
	public HQLBuilder addNotEqConditionField(String field, Object value) {
		return addField(field, "<>", value);
	}
	

	private HQLBuilder addAND() {
		return addRelation("AND");
	}

	
	/**
	 * 设置更新字段字段值 
	 * @param field 字段
	 * @param value	值
	 * @return
	 * @version
	 * 	2014-11-27		chenchen	create
	 */
	public HQLBuilder setNewValue(String field, Object value) {
		if (!isUpdateHQL()) {
			throw new IllegalArgumentException(
					"Can't assign new Value,only update HQL supported.");
		}
		assignExpressions.add(new ExpressionCondition(field, "=", value,false));
		return this;
	}
	/**
	 * 设置更新字段字段值 ,字段更新字段
	 * @param field 字段1
	 * @param value	字段2
	 * @return
	 * @version
	 * 	2014-11-27		chenchen	create
	 */
	public HQLBuilder setNewValueField(String field, Object value) {
		if (!isUpdateHQL()) {
			throw new IllegalArgumentException(
					"Can't assign new Value,only update HQL supported.");
		}
		assignExpressions.add(new ExpressionCondition(field, "=", value,true));
		return this;
	}
	public boolean isUpdateHQL() {
		return prefix.equalsIgnoreCase("UPDATE");
	}
	/**
	 * 设置更新字段字段值（特殊）
	 * <p>
	 * isParam=false,代表非参数模式，可传入数据库字段<br />
	 * eg：setNewValue("id", "id+1",false); update a set a.id=a.id+1
	 * 
	 * @cUser 陈晨
	 * @cDate 2014-7-24
	 * @mUser 陈晨
	 * @mDate 2014-7-24
	 * @param field
	 * @param value
	 * @param isParam
	 * @return
	 */
	public HQLBuilder setNewValue(String field, Object value, Boolean isParam) {
		if (!isUpdateHQL()) {
			throw new IllegalArgumentException(
					"Can't assign new Value,only update HQL supported.");
		}
		assignExpressions.add(new ExpressionCondition(field, "=", value,
				isParam));
		return this;
	}

	private HQLBuilder addRelation(String relation) {
		if ((currentExpression instanceof Relation)) {
			throw new IllegalArgumentException("");
		}
		currentExpression = new Relation(relation);
		whereExpressions.add(currentExpression);
		return this;
	}

	public HQLBuilder addOR() {
		return addRelation("OR");
	}

	public HQLBuilder addAscOrder(String field) {
		return addOrderBy(field, "ASC");
	}

	public HQLBuilder addDescOrder(String field) {
		return addOrderBy(field, "DESC");
	}

	public String getPreparedHQL() {
		parameterValues.clear();
		if (isUpdateHQL()) {
			return getUpdatePreparedHQL();
		}
		return getDeletePreparedHQL();
	}

	private String getDeletePreparedHQL() {
		StringBuffer preparedHQL = new StringBuffer();
		preparedHQL.append(prefix).append(" ").append("FROM").append(" ");
		preparedHQL.append(objectClass.getSimpleName()).append(" ")
				.append(buildWhereHQL());
		return preparedHQL.toString();
	}

	private String buildWhereHQL() {
		StringBuilder searchHQL = new StringBuilder();
		if (whereExpressions.size() > 0) {
			searchHQL.append("WHERE").append(" ")
					.append(buildWhereExpression()).append(" ");
		}
		if (orderBys.size() > 0) {
			searchHQL.append("ORDER BY").append(" ").append(buildOrderBy());
		}
		return searchHQL.toString().trim();
	}

	private String buildOrderBy() {
		StringBuilder orderBy = new StringBuilder();
		if (orderBys.size() > 0) {
			orderBy.append(orderBys.get(0).toHQL());
			for (int i = 0; i < orderBys.size(); i++) {
				orderBy.append(",").append(orderBys.get(i).toHQL());
			}
		}
		return orderBy.toString();
	}

	private String buildWhereExpression() {
		StringBuffer where = new StringBuffer();
		if (whereExpressions.size() > 0) {			
			for (int i = 0; i < whereExpressions.size(); i++) {
				where.append(whereExpressions.get(i).toHQL("_W"+i));
				parameterValues.add((Condition) whereExpressions.get(i));
				/*
				 * true,不是最后一个条件 需要增加and
				 */
				if (i<whereExpressions.size()-1) {
					where.append(" and ");
				}
			}
		}
		return where.toString();
	}

	private String getUpdatePreparedHQL() {
		StringBuilder preparedHQL = new StringBuilder();
		preparedHQL.append(prefix).append(" ")
				.append(objectClass.getSimpleName()).append(" ");
		preparedHQL.append(buildSetClause());
		preparedHQL.append(buildWhereHQL());
		return preparedHQL.toString().trim();
	}

	private Object buildSetClause() {
		StringBuilder setClause = new StringBuilder();
		if (assignExpressions.size() > 0) {
			setClause.append("SET ").append(" ");
			for (int i = 0; i < assignExpressions.size(); i++) {
				buildSetClause(setClause, assignExpressions.get(i),i);
				if (i<assignExpressions.size()-1) {
					setClause.append(",");
				}
			}
		}
		return setClause.toString();
	}

	private void buildSetClause(StringBuilder setClause,
			Condition assignmentExpression,int num) {
		parameterValues.add(assignmentExpression);
		setClause.append(assignmentExpression.toHQL("_V"+num));
		setClause.append(" ");
	}

	public List<Condition> getParameterValues() {
		return parameterValues;
	}

	private HQLBuilder addOrderBy(String field, String order) {
		orderBys.add(new OrderBy(field, order));
		return this;
	}

	public class Condition implements Expression {
		protected static final String PARAMETER_PREFIX = ":";
		protected String field;//检索字段
		protected String operator;//检索条件
		protected Object value;//检索值或检索字段2
		protected Boolean noNeedBindParam;//是否字段比较标志
		protected String parameterName;//绑定参数名
		public Condition(String field, String operator, Object value,
				Boolean noNeedBindParam) {
			this.field = field;
			this.operator = operator;
			this.value = value;
			this.noNeedBindParam = noNeedBindParam;
		}

		public String toHQL(String num) {	
			//设置参数名称
			setParameterName(num);
			StringBuilder sb = new StringBuilder();
			if (isNoNeedBindParam()) {
				sb.append(field).append(" ").append(operator).append(" ")
				.append(value);
			} else {
				sb.append(field).append(" ").append(operator).append(" ")
				.append(":").append(getParameterName());
				
			}
			return sb.toString();
		}

		public Object getValue() {
			return value;
		}

		public Boolean isNoNeedBindParam() {
			return noNeedBindParam;
		}

		public void setNoNeedBindParam(Boolean noNeedBindParam) {
			this.noNeedBindParam = noNeedBindParam;
		}

		public String getParameterName() {
			return parameterName;
		}
		public void setParameterName(String num) {
			if (field==null || field.length()==0) {
				parameterName= null;
			}else if(isNoNeedBindParam()){
				parameterName= null;
			}else {
				int beginIndex=field.indexOf(".");
				if (beginIndex>0) {
					parameterName=field.substring(beginIndex+1)+num;
				}else{
					parameterName=field+num;
				}
			}
			
		}
		public boolean isValueType() {
			return true;
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("(").append(field).append(operator).append(value)
					.append(", parameterName: " + getParameterName() + ")");
			sb.append(super.toString());
			return sb.toString();
		}

		public String getField() {
			return field;
		}

		public String getOperator() {
			return operator;
		}
	}

	/**
	 * 表达式类
	 * 
	 * @author CC
	 * 
	 */
	interface Expression {
		String toHQL(String num);
	}

	/**
	 * 
	 * @author CC
	 * 
	 */
	class ExpressionCondition extends Condition {
		private static final String PARAMETER_ASSGIN = "new";
		public ExpressionCondition(String field, String operator, Object value,
				Boolean noNeedBindParam) {
			super(field, operator, value, noNeedBindParam);
		}
		public boolean isValueType() {
			return false;
		}

		public String getParameterName() {
			StringBuilder sb = new StringBuilder();
			sb.append("new_").append(parameterName);
			return sb.toString();
		}
	}

	public class InCondition extends Condition {
		public InCondition(String field, Object[] value) {
			super(field, "in", value,false);
		}

		public String toHQL(String num) {
			//设置参数名称
			setParameterName(num);
			StringBuilder sb = new StringBuilder();
			sb.append(field).append(" ").append(operator).append(" ")
					.append("(").append(":").append(getParameterName())
					.append(")");
			return sb.toString();
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("(").append(field).append(operator)
					.append(StringHelper.join((Object[]) value))
					.append(", parameterName: " + getParameterName() + ")");
			sb.append(super.toString());
			return sb.toString();
		}
	}

	public class NotInCondition extends Condition {
		public NotInCondition(String field, Object[] value) {
			super(field, "not in", value,false);
		}

		public String toHQL(String num) {
			//设置参数名称
			setParameterName(num);
			StringBuilder sb = new StringBuilder();
			sb.append(field).append(" ").append(operator).append(" ")
					.append("(").append(":").append(getParameterName())
					.append(")");
			return sb.toString();
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("(").append(field).append(operator)
					.append(StringHelper.join((Object[]) value))
					.append(", parameterName: " + getParameterName() + ")");
			sb.append(super.toString());
			return sb.toString();
		}
	}

	class OrderBy {
		private String field;
		private String order;

		public OrderBy(String field, String order) {
			this.field = field;
			this.order = order;
		}

		public String toHQL() {
			StringBuffer sb = new StringBuffer();
			sb.append(field).append(" ").append(order);
			return sb.toString();
		}
	}

	class Relation implements Expression {
		private String name;

		public Relation(String name) {
			this.name = name;
		}

		public String toHQL(String num) {
			return name+num;
		}
	}
}