package com.job.manager.daoManager;

import java.util.Collections;
import java.util.List;

public class GroupByResult
{
  private String fieldName;
  private List<GroupByRecord> records;

  public GroupByResult(String fieldName, List<GroupByRecord> records)
  {
    this.fieldName = fieldName;
    this.records = records;
  }

  public String getFieldName()
  {
    return fieldName;
  }

  public List<GroupByRecord> getRecords()
  {
    if (records == null) {
      return Collections.emptyList();
    }
    return records;
  }

  public static class GroupByRecord
  {
    private Object fieldValue;
    private int recordCount;

    public GroupByRecord(Object fieldValue, int recordCount)
    {
      this.fieldValue = fieldValue;
      this.recordCount = recordCount;
    }

    public Object getFieldValue()
    {
      return fieldValue;
    }

    public int getRecordCount()
    {
      return recordCount;
    }
  }
}