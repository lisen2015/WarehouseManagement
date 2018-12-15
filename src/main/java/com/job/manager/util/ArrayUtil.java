package com.job.manager.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ArrayUtil
{
  public static Integer[] toIntegerArray(int[] values)
  {
    if (values == null) {
      return null;
    }
    Integer[] result = new Integer[values.length];
    for (int i = 0; i < values.length; i++) {
      result[i] = new Integer(values[i]);
    }
    return result;
  }
 
  public static Object[] toBooleanArray(boolean[] values)
  {
    if (values == null) {
      return null;
    }
    Boolean[] result = new Boolean[values.length];
    for (int i = 0; i < result.length; i++) {
      result[i] = new Boolean(values[i]);
    }
    return result;
  }

  public static int[] toIntArray(Integer[] values)
  {
    if (values == null) {
      return null;
    }
    int[] result = new int[values.length];
    for (int i = 0; i < values.length; i++) {
      result[i] = values[i].intValue();
    }
    return result;
  }

@SuppressWarnings({ "rawtypes", "unchecked" })
public static int[] toIntArray(String[] values)
  {
    if (values == null) {
      return null;
    }

    List temps = new ArrayList();
    for (int i = 0; i < values.length; i++)
      try {
        temps.add(Integer.valueOf(values[i]));
      }
      catch (Exception localException) {
      }
    return toIntArray((Integer[])temps.toArray(new Integer[0]));
  }
public static Long[] toLongArray(String[] values)
{
	Long [] returnArray=new Long[values.length];
	for (int i = 0; i < values.length; i++) {
		returnArray[i]=Long.parseLong(values[i]);
	}
  return returnArray;
}
  public static String[] keysToString(Map<String, ? extends Object> map)
  {
    return map.keySet().toArray(new String[0]);
  }

  public static String[] listToString(List<String> list)
  {
    return list.toArray(new String[0]);
  }

  public static int[] toIntArray(List<Integer> list)
  {
    int[] intArray = new int[list.size()];
    for (int i = 0; i < list.size(); i++) {
      intArray[i] = list.get(i).intValue();
    }
    return intArray;
  }

  public static boolean compareArrayValue(int[] a, int[] b)
  {
    if ((a.length == b.length) && (a.length != 0)) {
      for (int i = 0; i < a.length; i++) {
        boolean flag = false;
        for (int j = 0; j < b.length; j++) {
          if (a[i] == b[j])
            flag = true;
        }
        if (!flag)
          return false;
      }
      return true;
    }

    return false;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
public static int[] getArrayValuesExceeded(int[] a, int[] b)
  {
    List arrayValuesExceededIntegerList = new ArrayList();
    for (int i = 0; i < a.length; i++) {
      boolean flag = false;
      for (int j = 0; j < b.length; j++) {
        if (a[i] == b[j])
          flag = true;
      }
      if (!flag)
        arrayValuesExceededIntegerList.add(Integer.valueOf(a[i]));
    }
    return toIntArray(arrayValuesExceededIntegerList);
  }

  public static <T> boolean contain(T[] anArray, T anElement)
  {
    if (anArray == null) {
      return false;
    }
    for (int i = 0; i < anArray.length; i++) {
      if (anArray[i].equals(anElement)) {
        return true;
      }
    }
    return false;
  }

  public static boolean prefixMatch(String[] anArray, String anElement)
  {
    if (anArray == null) {
      return false;
    }
    for (int i = 0; i < anArray.length; i++)
    {
      if ((anArray[i] != null) && (anArray[i].length() != 0))
      {
        if (anElement.startsWith(anArray[i]))
          return true;
      }
    }
    return false;
  }

  public static String toCommaSplitedString(long[] values, boolean showTotal)
  {
    if (values == null) {
      return "null";
    }
    int maxIndex = values.length - 1;
    if (maxIndex < 0) {
      return "[]";
    }
    StringBuilder sb = new StringBuilder();
    if (showTotal) {
      sb.append(maxIndex + 1).append(" elements: ");
    }
    sb.append("[");
    String comma = ", ";
    for (int i = 0; i < maxIndex; i++) {
      sb.append(values[i]);
      sb.append(comma);
    }
    sb.append(values[maxIndex]);
    sb.append("]");
    return sb.toString();
  }


  public static int[] createSequencedPositiveIntArray(int length)
  {
    int[] result = new int[length];
    for (int i = 0; i < result.length; i++) {
      result[i] = (i + 1);
    }
    return result;
  }

  

  public static int[] expand(int[] srcArray, int[] deltaArray)
  {
    AssertUtil.notNull(srcArray, "srcArray is null.");
    AssertUtil.notNull(deltaArray, "deltaArray is null.");
    int originLength = srcArray.length;
    Object destArray = Array.newInstance(srcArray.getClass()
      .getComponentType(), originLength + deltaArray.length);
    System.arraycopy(srcArray, 0, destArray, 0, originLength);
    System.arraycopy(deltaArray, 0, destArray, originLength, 
      deltaArray.length);
    return (int[])destArray;
  }

  public static <T> boolean equals(T[] oneArray, T[] anotherArray)
  {
    return Arrays.equals(oneArray, anotherArray);
  }

  public static boolean isEmpty(Object[] objArray)
  {
    return (objArray == null) || (objArray.length == 0);
  }

  public static boolean isEmpty(int[] array)
  {
    return (array == null) || (array.length == 0);
  }

  public static boolean isEmpty(long[] array)
  {
    return (array == null) || (array.length == 0);
  }

  public static boolean isEmpty(boolean[] array)
  {
    return (array == null) || (array.length == 0);
  }

  public static boolean isEmpty(float[] array)
  {
    return (array == null) || (array.length == 0);
  }

  public static boolean isEmpty(double[] array)
  {
    return (array == null) || (array.length == 0);
  }

  public static String toString(List<String> list, String token)
  {
    if ((list == null) || (list.size() == 0)) {
      return "";
    }
    if (token == null) {
      token = "";
    }
    StringBuffer returnStr = new StringBuffer();
    boolean isBegin = true;
    for (String str : list)
      if (!StringHelper.isEmpty(str))
      {
        if (isBegin) {
          returnStr.append(str);
          isBegin = false;
        }
        else {
          returnStr = returnStr.append(token).append(str);
        }
      }
    return returnStr.toString();
  }
}