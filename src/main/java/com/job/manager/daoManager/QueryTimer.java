package com.job.manager.daoManager;

import org.apache.log4j.Logger;

public class QueryTimer {
	private static final Logger logger = Logger.getLogger(QueryTimer.class);

	private long beginTimeInMillis = System.currentTimeMillis();
	private Class<?> classType;

	public QueryTimer(Class<?> classType) {
		this.classType = classType;
	}

	public void startWatch() {
		beginTimeInMillis = System.currentTimeMillis();
	}

	public void stopWatch(Object id) {
		long duration = getDuration();
		if (isHighRiskSlow(duration)) {
			logger.warn("(" + classType + ")'s Get id=(" + id + ") consumed ("
					+ duration + ") ms!!", new Exception("Thread-dump"));
			return;
		}
		if (isSlow(duration))
			logger.debug("(" + classType + ")'s Get id=(" + id + ") consumed ("
					+ duration + ") ms");
	}
	 public void stopWatch(SearchFilter searchFilter)
	  {
	    stopWatch2(searchFilter, null);
	  }

	  public void stopWatch2(SearchFilter searchFilter, Throwable exception)
	  {
	    long duration = getDuration();
	    if (isHighRiskSlow(duration)) {
	      if (exception != null)
	        logger.warn("(" + classType + ")'s SearchFilter(" + searchFilter + ") consumed (" + duration + ") ms!!", exception);
	      else {
	        logger.warn("(" + classType + ")'s SearchFilter(" + searchFilter + ") consumed (" + duration + ") ms!!", new Exception("Thread-dump"));
	      }
	      return;
	    }
	    if ((isSlow(duration)) && 
	      (logger.isDebugEnabled()))
	      logger.debug("(" + classType + ")'s SearchFilter(" + 
	        searchFilter.toString() + ") consumed (" + duration + 
	        ") ms");
	  }
	public void stopWatch() {
		long duration = getDuration();
		if (isHighRiskSlow(duration)) {
			logger.warn("(" + classType + ")'s listObjects consumed ("
					+ duration + ") ms!!", new Exception("Thread-dump"));
			return;
		}
		if (isSlow(duration))
			logger.debug("(" + classType + ")'s listObjects consumed ("
					+ duration + ") ms");
	}

	public void stopWatch(Object entity, String action) {
		long duration = getDuration();
		if (isHighRiskSlow(duration)) {
			logger.warn("(" + entity.toString() + ")'s (" + action
					+ ") consumed (" + duration + ") ms!!", new Exception(
					"Thread-dump"));
			return;
		}
		if (isSlow(duration))
			logger.debug("(" + entity.toString() + ")'s (" + action
					+ ") consumed (" + duration + ") ms");
	}

	public void stopWatch(String statement) {
		long duration = getDuration();
		if (isHighRiskSlow(duration)) {
			logger.warn("(" + classType + ")'s execute command(" + statement
					+ ") consumed (" + duration + ") ms!!", new Exception(
					"Thread-dump"));
			return;
		}
		if (isSlow(duration))
			logger.debug("(" + classType + ")'s execute command(" + statement
					+ ") consumed (" + duration + ") ms");
	}
	

	 
	private boolean isSlow(long duration) {
		return duration > 1000L;
	}

	private boolean isHighRiskSlow(long duration) {
		return duration >= 5000L;
	}

	private long getDuration() {
		return System.currentTimeMillis() - beginTimeInMillis;
	}
}