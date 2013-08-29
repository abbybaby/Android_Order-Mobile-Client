package flyrestaurant.ph03.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.abby.android.orderapp.ConfigActivity;
import com.abby.android.orderapp.OrderActivity;
import com.abby.android.orderapp.PayActivity;
import com.androidtest8.R;
import com.androidtest8.R.drawable;

import flyrestaurant.ph03.service.UpdateDataService;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.NoCopySpan.Concrete;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

public class MainActivity extends BasicActivity {
	int[] icons = { R.drawable.icon1, R.drawable.icon2,
			R.drawable.icon3, R.drawable.icon4,
			R.drawable.icon5 };

	String[] iconTexts = { "点餐", "结账", "查桌", "更新数据", "设置" };
	GridView gdv;
	
	ProgressDialog updateDialog;
	BroadcastReceiver boradcastReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gdv = (GridView) findViewById(R.id.gdv);
		List<Map<String, Object>> iconList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < icons.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("imageView", icons[i]);
			map.put("imageTitle", iconTexts[i]);
			iconList.add(map);
		}
		gdv.setAdapter(new SimpleAdapter(this, iconList, R.layout.mainitem,
				new String[] { "imageView", "imageTitle" }, new int[] {
						R.id.imageView, R.id.imageTitle }));
		gdv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				switch (arg2) {
				case 0:
					//System.out.println("页面将要转换");
					showActivity(MainActivity.this, OrderActivity.class);
					//showMessageDialog("点餐", R.drawable.ic_launcher	, null);
					break;
				case 1:
					showActivity(MainActivity.this, PayActivity.class);
					break;

				case 2:
					showActivity(MainActivity.this, TableActivity.class);
					break;

				case 3:
					updateData();
					break;

				case 4:
					showActivity(MainActivity.this, ConfigActivity.class);
					break;
				}

			}
		});
		updateDialog=new ProgressDialog(MainActivity.this);
		updateDialog.setMessage("正在更新数据，请稍后");
		updateDialog.setCancelable(false);
		IntentFilter f=new IntentFilter();
		f.addAction(UpdateDataService.OK);
		f.addAction(UpdateDataService.EXCEPTION);
		boradcastReceiver=new BroadcastReceiver(){
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				updateDialog.hide();
				String action=intent.getAction();
				if (UpdateDataService.OK.equals(action)) 
					showMessageDialog("更新完成", R.drawable.info, null);
				else if (UpdateDataService.EXCEPTION.equals(action)) 
					showMessageDialog("更新失败:"+intent.getStringExtra(UpdateDataService.EXCEPTION),R.drawable.not,null);
			}
		};
		registerReceiver(boradcastReceiver, f);
		//setContentView(R.layout.activity_main);
	}

	protected void updateData() {
		// TODO Auto-generated method stub
		AlertDialog.Builder b=new AlertDialog.Builder(MainActivity.this);
		b.setIcon(R.drawable.warning);
		b.setTitle("更新需要时间,您确定要更新吗?");
		b.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				updateDialog.show();
				startService(new Intent(MainActivity.this,UpdateDataService.class));
			}
		});
		b.setNegativeButton("取消", null);
		b.create().show();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_main;
	}

	@Override
	protected String getName() {
		// TODO Auto-generated method stub
		return "主菜单";
	}
	protected void onDestory(){
		super.onDestroy();
		unregisterReceiver(boradcastReceiver);
	}

}
