package com.abby.android.orderapp;

import java.io.IOException;
import java.util.Properties;

import com.androidtest8.R;


import flyrestaurant.ph03.service.ConfigService;
import flyrestaurant.ph03.ui.BasicActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ConfigActivity extends BasicActivity {
	
	EditText serverUrlEdt;
	Button okBtn;
	Button cancelBtn;
	
	ConfigService configService;
	Properties config;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		configService=new ConfigService(this);
		serverUrlEdt=(EditText)findViewById(R.id.serverUrlEdt);
		okBtn=(Button)findViewById(R.id.configOkBtn);
		cancelBtn=(Button)findViewById(R.id.configCancelBtn);
		
		try {
			config=configService.read();
			serverUrlEdt.setText(config.getProperty("serverUrl"));
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
			showMessageDialog("≈‰÷√Œƒº˛∂¡»° ß∞‹:"+e.getMessage(),R.drawable.not,null);
			config=new Properties();
		}
		okBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String url=serverUrlEdt.getText().toString();
				config.put("serverUrl", url);
				App app=(App) getApplicationContext();
				app.serverUrl=url;
				try {
					configService.write(config);
					finish();
				} catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
					showMessageDialog("≈‰÷√Œƒº˛∂¡»° ß∞‹:"+e.getMessage(),R.drawable.not,null);
				}
			}
		});
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			ConfigActivity.this.finish();	
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.config, menu);
		return true;
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.config;
	}

	@Override
	protected String getName() {
		// TODO Auto-generated method stub
		return "≈‰÷√";
	}

}
