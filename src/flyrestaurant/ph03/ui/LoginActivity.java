package flyrestaurant.ph03.ui;

import java.io.IOException;
import java.util.Properties;


import com.abby.android.orderapp.*;
import com.androidtest8.R;


import flyrestaurant.entity.User;
import flyrestaurant.ph03.service.ConfigService;
import flyrestaurant.ph03.service.UserService;
import flyrestaurant.ph03.ui.LoginActivity;
import flyrestaurant.ph03.ui.MainActivity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends BasicActivity 
{
	EditText serverUrlEdt;
	EditText codEditText;
	EditText passwordEditText;
	Button loginButton;
	Button cancleButton;
	
	UserService userService;
	ConfigService configService;
	Properties config;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		String title=getString(R.string.app_name)+" "+getName();
		setTitle(title);
		
		userService=new UserService(this);
		configService=new ConfigService(this);
		
		serverUrlEdt=(EditText)findViewById(R.id.serverUrlEdt);
		codEditText=(EditText)findViewById(R.id.codeEdt);
		passwordEditText=(EditText)findViewById(R.id.passwordEdt);
		loginButton=(Button)findViewById(R.id.loginBtn);
		cancleButton=(Button)findViewById(R.id.cancelBtn);
		
		try{
			config=configService.read();
			serverUrlEdt.setText(config.getProperty("serverUrl"));
			//serverUrlEdt.setText("http://180.84.33.236:8080/Android_Order");
			
		}catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
			showMessageDialog("读取配置文件失败"+e.getMessage(), R.drawable.not, null);
			config=new Properties();
		}
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
        .detectDiskReads()
        .detectDiskWrites()
        .detectNetwork()   // or .detectAll() for all detectable problems
        .penaltyLog()
        .build());
StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
.detectLeakedSqlLiteObjects() //探测SQLite数据库操作
.penaltyLog() //打印logcat
.penaltyDeath()
.build());
		loginButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String serverUrl = serverUrlEdt.getText().toString();
				String code=codEditText.getText().toString();
				String password=passwordEditText.getText().toString();
				if (serverUrl.length() == 0) {
					showMessageDialog("请输入服务器地址", R.drawable.warning, null);
					return;
				}
				if (code.length() == 0 || password.length() == 0) {
					showMessageDialog("请输入登录名和密码", R.drawable.warning, null);
					return;
				}
				config.put("serverUrl", serverUrl);
				App app=(App)getApplicationContext();
				app.serverUrl = serverUrl;
				try {
					User user = userService.login(code, password);
					if (user == null) {
						showMessageDialog("您的登录名或密码错误", R.drawable.warning, null);
						return;
					}
					try {
						configService.write(config);
					} catch (IOException e) {
						e.printStackTrace();
						showMessageDialog("配置文件保存失败：" + e.getMessage(), R.drawable.not,
								null);
					}
					app.user = user;
					showActivity(LoginActivity.this, MainActivity.class);
				} catch (Exception e) {
					e.printStackTrace();
					showMessageDialog("登陆失败：" + e.getMessage(), R.drawable.not, null);
				}
			}
		});
		cancleButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				LoginActivity.this.finish();
			}
		});
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.login;
	}

	@Override
	protected String getName() {
		// TODO Auto-generated method stub
		return "登录";
	}

}
