package flyrestaurant.ph03.ui;



import com.abby.android.orderapp.App;
import com.androidtest8.R;

import flyrestaurant.entity.User;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;


public abstract class BasicActivity extends Activity 
{
	AlertDialog.Builder dialogBuilder;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window window=getWindow();
		window.requestFeature(Window.FEATURE_LEFT_ICON);
		setContentView(getLayoutId());
		window.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_launcher);
		
		String title=getString(R.string.app_name);
		App app=(App) getApplication();
		User user=app.user;
		if (user!=null)
			title +=" "+user.code;
			title +=" "+getName();
			setTitle(title);
			dialogBuilder = new AlertDialog.Builder(this);
	}
	protected void showActivity(Context context,Class<? extends Context> contextClass)
	{
		Intent intent=new Intent(context,contextClass);
		startActivity(intent);
	}
	protected void showMessageDialog(String message,int iconId,DialogInterface.OnClickListener onClickListener)
	{
		dialogBuilder.setIcon(iconId);
		dialogBuilder.setTitle(message);
		dialogBuilder.setPositiveButton("È·¶¨", onClickListener);
		dialogBuilder.create().show();
	}
	protected abstract int getLayoutId();
	protected abstract String getName();
}
