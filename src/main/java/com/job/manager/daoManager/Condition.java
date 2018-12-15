package com.job.manager.daoManager;
/**
 * HQL条件构造器
 * @author CC
 *
 */
public class Condition
{
  /**
   * 判断条件（=、>、<、like 等）
   */
  private String op;
  /**
   * 检索字段
   */
  private String field;
  /**
   * 参数绑定名称
   */
  private String bindName;
  /**
   * 检索值1
   */
  private Object value;
  /**
   * 检索值2
   */
  private Object value2;
  /**
   * 是否字段对比
   */
  private boolean noNeedBindParam;
  /**
   * 判断条件：like
   */
  static final String OP_LIKE = "like";
  /**
   * 判断条件：is null
   */
  static final String OP_ISNULL = "is null";
  /**
   * 判断条件：is not null
   */
  static final String OP_ISNOTNULL = "is not null";
  /**
   * 判断条件：>
   */
  static final String OP_GT = ">";
  /**
   * 判断条件：>=
   */
  static final String OP_GTE = ">=";
  /**
   * 判断条件：<
   */
  static final String OP_LT = "<";
  /**
   * 判断条件：<=
   */
  static final String OP_LTE = "<=";
  /**
   * 判断条件：=
   */
  static final String OP_EQUAL = "=";
  /**
   * 判断条件：!=
   */
  static final String OP_NOTEQUAL = "!=";
  /**
   * 判断条件：in
   */
  static final String OP_IN = "in";
  /**
   * 判断条件：not in
   */
  static final String OP_NOTIN = "not in";
  /**
   * 判断条件：between
   */
  static final String BETWEEN = "between";
  /**
   * 判断条件数组,包含{ "=", "like", ">=", "<=", ">", "<", "in", "not in", "!=", "is null" }
   */
  private static final String[] VALID_OPS = { "=", "like", ">=", "<=", ">", "<", "in", "not in", "!=", "is null", "is not null" };
  /**
   * 
   * @param op 判断条件（=、>、<、like 等）
   * @param prop 检索字段
   * @param value 检索值
   */
  private Condition(String op, String prop, Object value)
  {
    this.op = op;
    field = prop;
    this.value = value;
  }
  /**
   * 
   * @param op 判断条件（=、>、<、like 等）
   * @param prop 查询字段
   * @param value1 检索值1
   * @param value2 检索值2
   */
  private Condition(String op, String prop, Object value1, Object value2)
  {
    this.op = op;
    field = prop;
    value = value1;
    this.value2 = value2;
  }
  /**
   * 判断是否需要绑定参数
   * @return
   * @version
   * 	2014-11-26		chenchen	create
   */
  public boolean noNeedToBindParam()
  {
    return (value == null) && (value2 == null);
  }
  /**
   * 判断是否需要绑定1个参数
   * @return
   * @version
   * 	2014-11-26		chenchen	create
   */
  public boolean needBindOneParam()
  {
    return (value != null) && (value2 == null);
  }
  /**
   * 判断是否需要绑定2个参数
   * @return
   * @version
   * 	2014-11-26		chenchen	create
   */
  public boolean needBindTwoParams()
  {
    return (value != null) && (value2 != null);
  }
  /**
   * 判断是否需要绑定集合参数
   * @return
   * @version
   * 	2014-11-26		chenchen	create
   */
  public boolean needBindCollectionParam()
  {
    return ("in".equals(op)) || ("not in".equals(op));
  }
  /**
   * 判断是否between条件
   * @return
   * @version
   * 	2014-11-26		chenchen	create
   */
  public boolean isBetweenCondition()
  {
    return "between".equals(op);
  }
  /**
   * 判断是否like条件
   * @return
   * @version
   * 	2014-11-26		chenchen	create
   */
  public boolean isLikeCondition()
  {
    return "like".equals(op);
  }

  public String getOp()
  {
    return op;
  }

  public String getField()
  {
    return field;
  }

  public Object getValue()
  {
    return value;
  }

  public Object getValue2()
  {
    return value2;
  }

  public String getBindName()
  {
    return bindName;
  }

  void setBindName(String bindName)
  {
    this.bindName = bindName;
  }
  public Boolean isNoNeedBindParam()
  {
    return noNeedBindParam;
  }

  void setNoNeedBindParam(Boolean noNeedBindParam)
  {
    this.noNeedBindParam = noNeedBindParam;
  }
  
  /**
   * 判断条件是否正确
   * @param op 判断条件
   * @return
   * @version
   * 	2014-11-26		chenchen	create
   */
  static boolean isValidOp(String op)
  {
    for (int i = 0; i < VALID_OPS.length; i++) {
      if (VALID_OPS[i].equals(op)) {
        return true;
      }
    }
    return false;
  }

  /**
   * 绑定筛选条件
   * @param op 判断条件
   * @param field 检索字段
   * @param value 检索值
   * @return
   * @version
   * 	2014-11-26		chenchen	create
   */
  static Condition buildCondition(String op, String field, Object value) {
	/*
	 * true ,当前传入的判断条件输入错误，不是一个正确的判断条件
	 */
    if (!isValidOp(op)) {
      return null;
    }
    /*
     * true,判断条件=is null 或者 is not null
     */
    if (("is null".equals(op)) || ("is not null".equals(op))) {
      return new Condition(op, field, value);
    }
    /*
     * true,检索值为空
     */
    if (value == null) {
      return null;
    }
    return new Condition(op, field, value);
  }
  /**
   * 构造between筛选条件
   * @param field 检索字段
   * @param value 检索值1
   * @param value2 检索值2
   * @return
   * @version
   * 	2014-11-26		chenchen	create
   */
  public static Condition buildBetweenCondition(String field, Object value, Object value2)
  {
    if ((value == null) || (value2 == null)) {
      return null;
    }
    return new Condition("between", field, value, value2);
  }
}