package com.fx.model.table;

import java.util.Comparator;

public class FormattableInteger implements Comparator<FormattableInteger> {

	private int size;

	public FormattableInteger(int size) {
		this.size = size;
	}

	@Override
	public String toString() {
		String returnValue;
		if(size<= 0){
			returnValue =  "0";}
		
		else if(size<1024){
			returnValue = size + " B";
		}
		else if(size < 1048576){
			returnValue = size/1024 + " kB";
		}else{
			returnValue = size/1048576 + " MB";
		}
		return returnValue;
	}

	@Override
	public int compare(FormattableInteger arg0, FormattableInteger arg1) {
		Integer int1, int2;		
		int1 = arg0.size ;
		int2 = arg1.size;
		return int1.compareTo(int2);
	}

}
