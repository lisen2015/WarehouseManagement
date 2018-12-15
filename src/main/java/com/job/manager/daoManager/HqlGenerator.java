package com.job.manager.daoManager;

import org.hibernate.Query;

import java.util.Collection;

/**
 * HQL语句构造器
 * @author CC
 *
 */
public class HqlGenerator
{
  private SearchFilter sf;

  public HqlGenerator(SearchFilter sf)
  {
    this.sf = sf;
  }

  public SearchFilter getSearchFilter()
  {
    return sf;
  }
  /**
   * 构造where条件
   * @return
   * @version
   * 	2014-11-26		chenchen	create
   */
  public String buildWhere()
  {
	//总共有多少条件
    int totalConditions = sf.getTotalConditions();
    /*
     * true,当前hql无需检索条件，返回“”
     */
    if (totalConditions <= 0) {
      return "";
    }
    //用于构建返回where语句
    StringBuilder sb = new StringBuilder();
    sb.append(" where ");
    /*
     * 循环判断条件，设置where语句
     */
    for (int i = 0; i < totalConditions; i++) {
      Condition condition = sf.getCondition(i);
      sb.append(condition.getField()).append(' ')
        .append(condition.getOp());
      /*
       * 判断绑定类型,构建不同的条件语句
       * 如果检索值是一个集合 ，  eg xxx=(:parm)
       * 如果判断条件是between ,eg xxx between :paramlo and :paramhi
       * 如果需要绑定一个参数，eg xxx=:param
       */
      if (condition.isNoNeedBindParam()) {
    	  sb.append(condition.getBindName());
      }else if (condition.needBindCollectionParam()) {
        sb.append(" (:").append(condition.getBindName()).append(')');
      } else if (condition.isBetweenCondition()) {
        sb.append(" :").append(condition.getBindName()).append("lo");
        sb.append(" and :").append(condition.getBindName()).append("hi");
      } else if (condition.needBindOneParam()) {
        sb.append(" :").append(condition.getBindName());
      }
      /*
       * true,不是最后一个判断条件， 需要and连接符
       */
      if (i < totalConditions - 1)
      {
        sb.append(" and ");
      }
    }
    return sb.toString();
  }
  /**
   * 绑定参数
   * @param query Hibernate Query对象
   * @version
   * 	2014-11-26		chenchen	create
   */
  public void bindParameters(Query query)
  {
    bindParameters(query, sf);
  }
	/**
	 * 绑定参数
	 * @param query
	 * @param sf 
	 * @version
	 * 	2014-11-26		chenchen	create
	 */
	 @SuppressWarnings("rawtypes")
	static void bindParameters(Query query, SearchFilter sf){
		int totalConditions = sf.getTotalConditions();//获取当前条件数
	    /*
	     * 循环条件数，依次绑定参数
	     */
	    for (int i = 0; i < totalConditions; i++) {
	    	Condition condition = sf.getCondition(i);//获取hql条件构造器
	    	Object value = condition.getValue();//获取检索值
	    		/*
	    		 * true,字段对比不需要绑定参数
	    		 */
	    		if (condition.isNoNeedBindParam() || condition.getValue()==null) {
					continue;
				}
	    		if (condition.isLikeCondition()) {
		    		//绑定like条件的检索值
		    		query.setParameter(condition.getBindName(), '%' + value.toString() + '%');
		    	} else if (condition.needBindCollectionParam()) {
		    		if (value.getClass().isArray()){
		    			//绑定检索值是数组
		    			query.setParameterList(condition.getBindName(), (Object[])value);
		    		}else{
		    			//绑定检索值是集合
		    			query.setParameterList(condition.getBindName(), (Collection)value);
		    		}
		    	}else if (condition.isBetweenCondition()) {
		    		//绑定between
		    		query.setParameter(condition.getBindName() + "lo", value);
		    		query.setParameter(condition.getBindName() + "hi", condition.getValue2());
		    	} else if (condition.needBindOneParam()) {
		    		//其他
		    		query.setParameter(condition.getBindName(), value);
		    	}
		}
	}
}