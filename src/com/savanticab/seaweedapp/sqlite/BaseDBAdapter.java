package com.savanticab.seaweedapp.sqlite;

import android.content.Context;

public abstract class BaseDBAdapter {

	protected MySQLiteHelper helper;
	protected Context mContext;
	public BaseDBAdapter(Context context){
		  helper = MySQLiteHelper.getInstance(context);
		  mContext = context;
	}

}
