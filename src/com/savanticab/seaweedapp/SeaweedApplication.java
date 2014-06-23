package com.savanticab.seaweedapp;

import android.app.Application;
import android.content.Context;

import com.dropbox.sync.android.DbxAccount;
import com.dropbox.sync.android.DbxAccountManager;

public class SeaweedApplication extends Application {
	
	public static final String appKey = "r4za7fsvw23g6s3";
    public static final String appSecret = "ze5bnkmzy6famn7";

    private static Context applicationContext;
    
    public static DbxAccountManager getAccountManager(Context context)
    {
        return DbxAccountManager.getInstance(context.getApplicationContext(), appKey, appSecret);
    }
    
    public static DbxAccount getDefaultAccount() {
    	return DbxAccountManager.getInstance(applicationContext, appKey, appSecret).getLinkedAccount();
    }
	public void onCreate() {
		applicationContext = this.getApplicationContext();
		  
	}

}
