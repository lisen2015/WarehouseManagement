package com.job.manager.util;

import java.util.Collection;


public class AssertUtil
{
  public static void notNull(Object obj, String message)
  {
    if (obj == null)
      throw new IllegalArgumentException(
        StringHelper.isEmpty(message) ? "the object is null!" : 
        message);
  }

  public static void notNullOrEmpty(String str, String message)
  {
    if ((str == null) || (str.trim().length() == 0))
      throw new IllegalArgumentException(
        StringHelper.isEmpty(message) ? "the string is empty!" : 
        message);
  }

  public static void notNullOrEmpty(Collection<?> objs, String message)
  {
    notNull(objs, StringHelper.isEmpty(message) ? "the collection is null!" : 
      message);
    if (objs.size() == 0)
      throw new IllegalArgumentException(
        StringHelper.isEmpty(message) ? "the collection is empty!" : 
        message);
  }
}