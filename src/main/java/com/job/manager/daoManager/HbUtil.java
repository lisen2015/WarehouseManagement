package com.job.manager.daoManager;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.metadata.ClassMetadata;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HbUtil
{
  public static int uniqueResultAsInt(Query qry)
  {
    List<?> results = qry.list();
    if ((results == null) || (results.size() == 0)) {
      return 0;
    }
    int result = 0;

    for (Iterator<?> iter = results.iterator(); iter.hasNext(); ) {
      Object obj = iter.next();

      if ((obj instanceof Number)) {
        result += ((Number)obj).intValue();
      }
    }
    return result;
  }

  public static long uniqueResultAsLong(Query qry)
  {
    List<?> results = qry.list();
    if ((results == null) || (results.size() == 0)) {
      return 0L;
    }
    long result = 0L;

    for (Iterator<?> iter = results.iterator(); iter.hasNext(); ) {
      Object obj = iter.next();
      if ((obj instanceof Number)) {
        result += ((Number)obj).longValue();
      }
    }
    return result;
  }

  public static Map<String, ClassMetadata> getAllEntityClassMetadata(SessionFactory sf)
  {
    return sf.getAllClassMetadata();
  }

  public static IdentifierGenerator getIdGenerator(SessionFactory sessionFactory, String rootEntityName)
  {
    if (!(sessionFactory instanceof SessionFactoryImplementor)) {
      throw new IllegalArgumentException("this sessionFactory [" + 
        sessionFactory + "] not a [" + 
        SessionFactoryImplementor.class.getName() + "] instance.");
    }
    SessionFactoryImplementor sfImpl = (SessionFactoryImplementor)sessionFactory;
    return sfImpl.getIdentifierGenerator(rootEntityName);
  }

}