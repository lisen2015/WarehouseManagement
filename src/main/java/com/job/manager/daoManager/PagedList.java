package com.job.manager.daoManager;

import java.io.Serializable;
import java.util.List;

public class PagedList<T>
  implements Serializable
{
  private static final long serialVersionUID = -6670794331035414475L;
  private List<T> rows;
  private int pageIndex;
  private int pageSize;
  private int total;
  private int thisPageTotal;
  private int pageTotal;
  private int prevPage;
  private int nextPage;
  private int step;
  private int startPage;
  private int endPage;
  private List<T> footer;
  private GroupByResult groupByResult;

  public PagedList(List<T> pageItems, int totalItemCount)
  {
    this(pageItems, 0, 20, totalItemCount);
  }

  public PagedList(List<T> objects, int pageIndex, int pageSize, int totalItemCount)
  {
    this(pageIndex, pageSize, totalItemCount, objects, 10);
  }

  public PagedList()
  {
  }

  public PagedList(List<T> pageItems)
  {
    pageIndex = 0;
    pageSize = pageItems.size();
    total = pageItems.size();
    this.rows = pageItems;
    thisPageTotal = pageItems.size();
  }

  public PagedList(int pageIndex, int pageSize, int totalItemCount, List<T> pageItems, int step)
  {
    this.pageIndex = (pageIndex < 0 ? 0 : pageIndex);
    this.pageSize = (pageSize <= 0 ? 5 : pageSize);
    this.total = totalItemCount;
    this.rows = pageItems;
    thisPageTotal = (pageItems == null ? 0 : pageItems.size());

    computePageIndex(step);
  }

  public PagedList(int pageIndex, int pageSize, int totalItemCount, List<T> pageItems, int step, GroupByResult groupByResult)
  {
    this(pageIndex, pageSize, totalItemCount, pageItems, step);

    this.groupByResult = groupByResult;
  }

  public PagedList(int pageIndex, int pageSize, int totalItemCount, List<T> pageItems, GroupByResult groupByResult)
  {
    this(pageIndex, pageSize, totalItemCount, pageItems, 10);

    this.groupByResult = groupByResult;
  }

  private void computePageIndex(int stepValue)
  {
    if (total <= 0)
      pageTotal = 0;
    else {
      pageTotal = 
        (total / pageSize + (
        		total % pageSize == 0 ? 0 : 1));
    }
    prevPage = (pageIndex == 0 ? 0 : pageIndex - 1);
    nextPage = (pageIndex >= pageTotal - 1 ? pageTotal - 1 : pageIndex + 1);
    step = stepValue;
    startPage = (pageIndex / step * step);
    endPage = (startPage + step >= pageTotal ? pageTotal - 1 : startPage + 
      step);
  }

  public T get(int index)
  {
    return rows.get(index);
  }

  public List<T> getRows()
  {
    return rows;
  }

  public int getTotal()
  {
    return total;
  }

  public int getTotalPageCount()
  {
    return getPageTotal();
  }

  public int getPageTotal()
  {
    return pageTotal;
  }

  public int getFirstPageNo()
  {
    return 0;
  }

  public int getLastPageNo()
  {
    return pageTotal - 1;
  }

  public boolean isFirstPage()
  {
    return isFirstPage(getPageIndex());
  }

  public boolean isLastPage()
  {
    return isLastPage(getPageIndex());
  }

  public boolean isFirstPage(int page)
  {
    return page <= 0;
  }

  public boolean isLastPage(int page)
  {
    return page >= getTotalPageCount() - 1;
  }

  public int getPageIndex()
  {
    return pageIndex;
  }

  public int getPageSize()
  {
    return pageSize;
  }

  public int getThisPageTotal()
  {
    return thisPageTotal;
  }

  public int getStep()
  {
    return step;
  }

  public int getStartPage()
  {
    return startPage;
  }

  public int getEndPage()
  {
    return endPage;
  }

  public int getPrevPage()
  {
    return prevPage;
  }

  public int getNextPage()
  {
    return nextPage;
  }

  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    builder.append("PagedList [pageIndex=").append(pageIndex);
    builder.append(", total=").append(total);
    builder.append(", thisPageTotal=").append(thisPageTotal);
    if (rows != null) {
      builder.append("; pageItems=").append(rows);
    }
    builder.append("]");
    return builder.toString();
  }

  public GroupByResult getGroupByResult()
  {
    return groupByResult;
  }

  public void setGroupByResult(GroupByResult groupByResult)
  {
    this.groupByResult = groupByResult;
  }

  public int size()
  {
    return rows == null ? 0 : rows.size();
  }

  protected int getStartIndex()
  {
    return ((pageIndex > 1 ? pageIndex : 1) - 1) * pageSize + 1;
  }

  public void setRows(List<T> rows)
  {
    this.rows = rows;
  }

public List<T> getFooter() {
	return footer;
}

public void setFooter(List<T> footer) {
	this.footer = footer;
}
  
}