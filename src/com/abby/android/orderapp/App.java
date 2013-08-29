package com.abby.android.orderapp;

import java.io.IOException;


import flyrestaurant.entity.User;
import flyrestaurant.ph03.service.ConfigService;

import android.R.string;
import android.app.Application;

public class App extends Application
{
	public User user;
	public String serverUrl;;
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		ConfigService configService=new ConfigService(this);
		try {
			serverUrl=configService.read().getProperty("serverUrl");
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
