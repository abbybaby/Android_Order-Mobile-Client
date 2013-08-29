package flyrestaurant.ph03.service;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.abby.android.orderapp.App;
import com.abby.android.orderapp.HttpUtils;

import flyrestaurant.entity.User;

public class UserService 
{
	App app;
	public UserService (Context context)
	{
		app=(App)context.getApplicationContext();
	}
public User login(String code,String password) throws Exception
{
	String url = app.serverUrl + "/?to=login";
	Map<String, String> params = new HashMap<String, String>();
	params.put("code", code);
	params.put("password", password);
	User user = (User) HttpUtils.post(url, params);
	return user;
}
}
