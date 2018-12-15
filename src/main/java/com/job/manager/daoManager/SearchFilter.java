package com.job.manager.daoManager;

import com.job.manager.util.ArrayUtil;
import com.job.manager.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 检索过滤器，提供service调用。构建hql检索语句<br/>
 * 借鉴于：<br/>
 * trsdev4_infra_jdk16.src.zip\com\trs\dev4\jdk16\dao.SearchFilter * 
 * 版 本 号： v1.0
 */
public class SearchFilter {
	public static final int DEFAULT_PAGE_NO = 0;
	public static final int DEFAULT_PAGE_SIZE = 20;
	public static final int MAX_PAGE_SIZE = 1000;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<Condition> conditions = new ArrayList();//所有条件集合
	private int startPos = 0;//开始查询的记录索引	
	private int maxResults = -1;//获取的最大记录数
	private String orderBy;//排序条件
	private String groupBy;//分组条件
	private String defaultFields = "*";//查询的字段，默认所有
	private String selectFields;
	private boolean cacheable = false;
	private String defaultKeyword;
	
	private boolean debugMode;
	private Class<?> viewClassType;
	/**
	 * 创 建 人： 陈晨 
	 * 日 期： 2013-10-24 
	 * 描 述：构造函数，用于分页
	 * 版 本 号： v1.0
	 * 参数：
	 * pageNo 当前页
	 * pageSize 记录数
	 */
	public SearchFilter(int pageNo, int pageSize) {
		if (pageSize > 1000) {
			pageSize = 1000;
		}
		setMaxResults(pageSize);
		setStartPos(pageSize * (pageNo-1));
		setCacheable(true);
	}
	
	/**
	 * 获取检索条件总数
	 * @return
	 * @version
	 * 	2014-11-26		chenchen	create
	 */
	public int getTotalConditions() {
		return conditions.size();
	}
	/**
	 * 创 建 人： 陈晨 
	 * 日 期： 2013-10-24 
	 * 描 述：返回条件的字段
	 * 版 本 号： v1.0
	 * 参数 index 条件的索引
	 */
	public String getPropertyName(int index) {
		return getCondition(index).getField();
	}
	/**
	 * 创 建 人： 陈晨 
	 * 日 期： 2013-10-24 
	 * 描 述：返回条件的内容
	 * 版 本 号： v1.0
	 * 参数 index 条件的索引
	 */
	public Object getValue(int index) {
		return getCondition(index).getValue();
	}
	/**
	 * 创 建 人： 陈晨 
	 * 日 期： 2013-10-24 
	 * 描 述：返回条件的内容2，用于 between 条件
	 * 版 本 号： v1.0
	 * 参数 index 条件的索引
	 */
	public Object getBetweenValue2(int index) {
		return getCondition(index).getValue2();
	}
	/**
	 * 创 建 人： 陈晨 
	 * 日 期： 2013-10-24 
	 * 描 述：返回条件的查询类型 例如 = > < !=
	 * 版 本 号： v1.0
	 * 参数 index 条件的索引
	 */
	public String getRelationOp(int index) {
		return getCondition(index).getOp();
	}
	/**
	 * 创 建 人： 陈晨 
	 * 日 期： 2013-10-24 
	 * 描 述：等同于 where xxx=xxxx
	 * 版 本 号： v1.0
	 * 参数 
	 * prop: 字段
	 * value： 内容
	 */
	public void addEqCondition(String prop, Object value) {
		addCondition("=", prop, value);
	}
	/**
	 * 创 建 人： 陈晨 
	 * 日 期： 2013-10-24 
	 * 描 述：等同于 where xxx=xxxx,用于字段本身比较
	 * 版 本 号： v1.0
	 * 参数 
	 * prop: 字段
	 * value： 内容
	 */
	public void addEqField(String prop, String value) {
		addField("=", prop, value);
	}
	/**
	 * 描 述：等同于 where xxx<>xxxx
	 * @param prop 检索字段
	 * @param value 检索值
	 * @version
	 * 	2014-11-26		chenchen	create
	 */
	public void addNotEqCondition(String prop, Object value) {
		addCondition("!=", prop, value);
	}
	/**
	 * 描 述：等同于 where xxx<>xxxx,用于字段比较
	 * @param prop 检索字段1
	 * @param value 检索字段2
	 * @version
	 * 	2014-11-26		chenchen	create
	 */
	public void addNotEqConditionField(String prop, Object value) {
		addField("!=", prop, value);
	}
	/**
	 * 创 建 人： 陈晨 
	 * 日 期： 2013-10-24 
	 * 描 述：等同于 where xxx is null
	 * 版 本 号： v1.0
	 * 参数 
	 * prop: 字段
	 */
	public void addIsNullCondition(String prop) {
		addCondition("is null", prop, null);
	}
	/**
	 * 创 建 人： 陈晨 
	 * 日 期： 2013-10-24 
	 * 描 述：等同于 where xxx is not null 
	 * 版 本 号： v1.0
	 * 参数 
	 * prop: 字段
	 */
	public void addNotNullCondition(String prop) {
		addCondition("is not null", prop, null);
	}
	/**
	 * 创 建 人： 陈晨 
	 * 日 期： 2013-10-24 
	 * 描 述：等同于 where xxx in(xxxx,xxxx,...)
	 * 版 本 号： v1.0
	 * 参数 
	 * prop: 字段
	 * value： 内容数组
	 */
	public void addInCondition(String prop, Object[] value) {
		if ((value == null) || (value.length == 0)) {
			return;
		}

		addCondition("in", prop, value);
	}
	/**
	 * 创 建 人： 陈晨 
	 * 日 期： 2013-10-24 
	 * 描 述：等同于 where xxx notin(xxxx,xxxx,...)
	 * 版 本 号： v1.0
	 * 参数 
	 * prop: 字段
	 * value： 内容数组
	 */
	public void addNotInCondition(String prop, Object[] value) {
		if ((value == null) || (value.length == 0)) {
			return;
		}

		addCondition("not in", prop, value);
	}
	/**
	 * 创 建 人： 陈晨 
	 * 日 期： 2013-10-24 
	 * 描 述：等同于 where xxx in(xxxx,xxxx,...) 
	 * 版 本 号： v1.0
	 * 参数 
	 * prop: 字段
	 * value： 内容数组
	 */
	public void addInCondition(String prop, int[] value) {
		if ((value == null) || (value.length == 0)) {
			return;
		}

		addCondition("in", prop, ArrayUtil.toIntegerArray(value));
	}
	
	/**
	 * 增加一个判断条件
	 * eg: addCondition(">",a,2) ,where a>2
	 * @param op 条件
	 * @param prop 字段
	 * @param value 内容
	 * @version
	 * 	2013-10-24		chenchen	create
	 * 	2014-11-27		chenchen	将方法从public改为private
	 */
	private void addCondition(String op, String prop, Object value) {
		Condition condition = Condition.buildCondition(op, prop, value);
		addCondition(condition);
	}
	/**
	 * 等同于 where prop op value,用于字段本身比较<br />
	 * eg: addCondition(">",a,b) ,where a>b
	 * @param op 条件
	 * @param prop 字段
	 * @param value 内容
	 */
	private void addField(String op, String prop, Object value) {
		Condition condition = Condition.buildCondition(op, prop, value);
		condition.setNoNeedBindParam(true);
		addCondition(condition);
	}
	/**
	 * 等同于 where xxx >= xxx 
	 * @param prop 检索字段
	 * @param value 检索值
	 * @version
	 * 	2014-11-26		chenchen	create
	 */
	public void addGreaterThanEquals(String prop, Object value) {
		addCondition(">=", prop, value);
	}
	/**
	 * 等同于 where xxx >=xxx,用于字段比较
	 * @param prop 检索字段1
	 * @param value 检索字段2
	 * @version
	 * 	2014-11-26		chenchen	create
	 */
	public void addGreaterThanEqualsField(String prop, Object value) {
		addField(">=", prop, value);
	}
	/**
	 * 等同于 where xxx > xxx 
	 * @param prop 检索字段
	 * @param value 检索值
	 * @version
	 * 	2014-11-26		chenchen	create
	 */
	public void addGreaterThan(String prop, Object value) {
		addCondition(">", prop, value);
	}
	/**
	 * 等同于 where xxx > xxx ,用于字段比较
	 * @param prop 检索字段1
	 * @param value 检索字段2
	 * @version
	 * 	2014-11-26		chenchen	create
	 */
	public void addGreaterThanField(String prop, Object value) {
		addField(">", prop, value);
	}
	/**
	 * 等同于 where xxx < xxxx
	 * @param prop 检索字段
	 * @param value 检索值
	 * @version
	 * 	2014-11-26 chenchen	create
	 */
	public void addLesserThan(String prop, Object value) {
		addCondition("<", prop, value);
	}
	/**
	 * 等同于 where xxx < xxxx,用于字段比较
	 * @param prop 检索字段1
	 * @param value 检索字段2
	 * @version
	 * 	2014-11-26 chenchen	create
	 */
	public void addLesserThanField(String prop, Object value) {
		addField("<", prop, value);
	}
	/**
	 * 等同于 where xxx <=xxxx
	 * @param prop 检索字段
	 * @param value 检索值
	 * @version
	 * 	2014-11-26		chenchen	create
	 */
	public void addLesserThanEquals(String prop, Object value) {
		addCondition("<=", prop, value);
	}
	/**
	 * 等同于 where xxx <=xxxx,用于字段比较
	 * @param prop 检索字段1
	 * @param value 检索字段2
	 * @version
	 * 	2014-11-26		chenchen	create
	 */
	public void addLesserThanEqualsField(String prop, Object value) {
		addField("<=", prop, value);
	}
	/**
	 * 创 建 人： 陈晨 
	 * 日 期： 2013-10-24 
	 * 描 述：等同于 where xxx like xxxx
	 * 版 本 号： v1.0
	 * 参数 
	 * prop: 字段
	 * value： 内容
	 */
	public void addLike(String prop, Object value) {
		addCondition("like", prop, value);
	}
	/**
	 * 为conditions（hql条件构建器）增加一个条件
	 * @param condition
	 * @version
	 * 	2014-11-26		chenchen	create
	 */
	private void addCondition(Condition condition) {
		if (condition != null) {
			conditions.add(condition);
			if (condition.isNoNeedBindParam()) {
				condition.setBindName(condition.getValue().toString());
			}else{
				condition.setBindName( "param_"+ conditions.size());
			}
		}
	}
	
	
	/**
	 * 等同于 where xxx between(xxxx，xxxx)
	 * @param prop  检索字段
	 * @param value1 检索值1
	 * @param value2  检索值2
	 */
	public void addBetweenCondition(String prop, Object value1, Object value2) {
		addCondition(Condition.buildBetweenCondition(prop, value1, value2));
	}
	/**
	 * 等同于 where xxx between(xxxx，xxxx),用于字段检索
	 * @param prop 检索字段1
	 * @param value1 检索字段2
	 * @param value2 检索字段3
	 * @version
	 * 	2014-11-26		chenchen	create
	 */
	public void addBetweenField(String prop, Object value1, Object value2) {
		Condition condition=Condition.buildBetweenCondition(prop, value1, value2);
		condition.setNoNeedBindParam(true);
		addCondition(condition);
	}
	public int getStartPos() {
		return startPos;
	}
	public void setStartPos(int startPos) {
		if (startPos > 0)
			this.startPos = startPos;
	}
	public int getMaxResults() {
		return maxResults;
	}
	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	public Condition getCondition(int index) {
		if ((index < 0) || (index >= conditions.size())) {
			throw new IndexOutOfBoundsException("index should in [0, "
					+ conditions.size() + "), but it's " + index + "!");
		}
		return conditions.get(index);
	}
	/**
	 * 判断是否没有设置排序条件
	 * @return
	 * @version
	 * 	2014-11-26		chenchen	create
	 */
	public boolean hasNotSetOrderBy() {
		return StringHelper.isEmpty(orderBy);
	}
	/**
	 * 判断是否设置排序条件
	 * @return
	 * @version
	 * 	2014-11-26		chenchen	create
	 */
	public boolean hasSetOrderBy() {
		return StringHelper.isNotEmpty(orderBy);
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	/**
	 * 构建where条件
	 * @return
	 * @version
	 * 	2014-11-26		chenchen	create
	 */
	public String buildWhere() {
		return new HqlGenerator(this).buildWhere();
	}
	/**
	 * 设置分页
	 * @param 第几页
	 * @param 显示多少条数据
	 * @return
	 * @version
	 * 	2014-11-26		chenchen	create
	 */
	public void setPageNo(int pageNo,int maxResults) {
		this.maxResults=maxResults;
		startPos = ((pageNo-1) * maxResults);
	}

	public String toString() {
		StringBuilder strBuf = new StringBuilder();
		strBuf.append("{[where: ");
		strBuf.append(buildWhere()).append("]");
		if (getOrderBy() != null) {
			strBuf.append("[order:").append(getOrderBy()).append("]");
		}
		if (getGroupBy() != null) {
			strBuf.append("[group:").append(getGroupBy()).append("]");
		}
		strBuf.append("[parameters:");
		int totalConditions = getTotalConditions();
		for (int i = 0; i < totalConditions; i++) {
			Condition condition = getCondition(i);
			if (condition.isNoNeedBindParam()) {
				continue;
			}
			strBuf.append(condition.getBindName()).append(" = ");
			if (condition.needBindCollectionParam()) {
				strBuf.append("(").append(condition.getValue()).append(')');
			} else if (condition.isBetweenCondition()) {
				strBuf.append('(').append(condition.getValue());
				strBuf.append(" and ").append(condition.getValue2())
						.append(')');
			} else if (condition.needBindOneParam()) {
				strBuf.append(condition.getValue());
			}
			if (i < totalConditions - 1) {
				strBuf.append(";");
			}
		}
		strBuf.append("]}");
		if (isDebugMode()) {
			strBuf.append("(debugMode)");
		}
		return strBuf.toString();
	}
	/**
	 * 获取无分页的SearchFilter
	 * @return
	 * @version
	 * 	2014-11-26		chenchen	create
	 */
	public static SearchFilter getDefault() {
		return new SearchFilter(1, -1);
	}
	/**
	 * 获取无分页的SearchFilter
	 * @return
	 * @version
	 * 	2014-11-26		chenchen	create
	 */
	public static SearchFilter getNoPagedFilter() {
		return new SearchFilter(1, -1);
	}
	/**
	 * 获取SearchFilter
	 * @param pageNo 第几页
	 * @param pageSize 显示多少条数据
	 * @return
	 * @version
	 * 	2014-11-26		chenchen	create
	 */
	public static SearchFilter getSearchFilter(int pageNo, int pageSize) {
		return new SearchFilter(pageNo, pageSize);
	}

	public String getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	public void setCacheable(boolean cacheable) {
		this.cacheable = cacheable;
	}

	public String getDefaultFields() {
		return defaultFields;
	}

	public void addDefaultFields(String defaultFields) {
		this.defaultFields = defaultFields;
	}

	public String getDefaultKeyword() {
		return defaultKeyword;
	}

	public void setDefaultKeyword(String defaultKeyword) {
		this.defaultKeyword = defaultKeyword;
	}

	public boolean isCacheable() {
		if (!cacheable)
			return false;
		int totalConditions = getTotalConditions();
		for (int i = 0; i < totalConditions; i++) {
			Condition condition = getCondition(i);
			if (condition.needBindCollectionParam()) {
				return false;
			}
		}
		return cacheable;
	}

	public boolean isDebugMode() {
		return debugMode;
	}

	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}
	public String getSelectFields() {
		return selectFields;
	}
	/**
	 * 设置HQL查询条件<br />
	 * eg:sf.setSelectFields("name,loginName,password");<br/>
	 * 备注：使用这个方法，必须要在model类中定义相应的构造器，eg：new User(name,loginName,password)
	 * @param selectFields 查询条件 
	 */
	public void setSelectFields(String selectFields) {
		this.selectFields = selectFields;
	}
	public Class<?> getViewClassType() {
		return viewClassType;
	}

	public void setViewClassType(Class<?> viewClassType) {
		this.viewClassType = viewClassType;
	}
}