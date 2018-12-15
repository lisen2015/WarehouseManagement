package com.job.manager.daoManager;

public class ExtendSearchFilter extends SearchFilter {

	public ExtendSearchFilter(int pageNo, int pageSize) {
		super(pageNo, pageSize);
	}
	public static ExtendSearchFilter getDefault(){
		ExtendSearchFilter esf=new ExtendSearchFilter(1, -1);
		esf.addEqCondition("isValidate", 1);
		return esf;
		
	}
	public static ExtendSearchFilter getPageDefault(int pageNo, int pageSize){
		ExtendSearchFilter esf=new ExtendSearchFilter(pageNo, pageSize);
		esf.addEqCondition("isValidate", 1);
		return esf;
		
	}
	
}
