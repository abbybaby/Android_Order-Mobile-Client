package flyrestaurant.ph03.service;

import java.util.ArrayList;
import java.util.List;

import com.abby.android.orderapp.App;
import com.abby.android.orderapp.DbHelper;
import com.abby.android.orderapp.HttpUtils;

import flyrestaurant.entity.Food;
import flyrestaurant.entity.FoodType;
import flyrestaurant.entity.Table;

import android.app.Service;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;

public class UpdateDataService extends Service{
	public static final String OK="OK";
	public static final String EXCEPTION="EXCEPTION";

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		new Thread(){
			@Override
			public void run(){
				try {
					updateData();
					sendBroadcast(new Intent(OK));
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					Intent intent=new Intent(EXCEPTION);
					intent.putExtra(EXCEPTION, e.getMessage());
					sendBroadcast(intent);
				}
				stopSelf();
			}
		}.start();
	}
	
	@SuppressWarnings({"rawtypes","unchecked"})
	void updateData() throws Exception
	{
		App app=(App)getApplicationContext();
		DbHelper dbHelper=new  DbHelper(getApplicationContext());
		dbHelper.deleteDb();
		
		String url=app.serverUrl+"/?to=update";
		List[] data=(List[])HttpUtils.post(url, null);
		
		List<Food> foods=data[0];
		List<FoodType> foodTypes=data[1];
		List<Table> tables=data[2];
		
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		//开始事务
		db.beginTransaction();
		
		String sql1="insert into food(id,code,type_id,name,price,description) values(?,?,?,?,?,?)";
		String sql2="insert into food_type(id,name) values(?,?)";
		String sql3="insert into tables(id,code,seats,description) values(?,?,?,?)";
		
		for (Food f:foods)
			db.execSQL(sql1,new Object[]{f.id,f.code,f.typeId,f.name,f.price,f.description});
		for (FoodType ft:foodTypes)
			db.execSQL(sql2,new Object[]{ft.id,ft.name});
		for (Table t:tables)
			db.execSQL(sql3,new Object[]{t.id,t.code,t.seats,t.description});
		
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
		
	}
	@SuppressWarnings("rawtypes")
	 ArrayList[] downloadFromServer() {
		// TODO Auto-generated method stub
		ArrayList[] data=new ArrayList[3];
		ArrayList<FoodType> types=new ArrayList<FoodType>();
		ArrayList<Food> foods=new ArrayList<Food>();
		ArrayList<Table> tables=new ArrayList<Table>();
		
		data[0]=foods;
		data[1]=types;
		data[2]=tables;
		
		FoodType ft=new FoodType();
		ft.id=1;
		ft.name="热菜";
		types.add(ft);
		ft=new FoodType();
		ft.id=2;
		ft.name="凉菜";
		types.add(ft);
		ft=new FoodType();
		ft.id=3;
		ft.name="烧烤";
		types.add(ft);
		ft=new FoodType();
		ft.id=4;
		ft.name="酒水";
		types.add(ft);
		ft=new FoodType();
		ft.id=5;
		ft.name="主食";
		types.add(ft);
		
		for (int i = 1; i <= 40; i++) {
			Food f=new Food();
			f.id=i;
			f.code="FOOD" + i;
			f.typeId=i%5+1;
			f.name="餐品"+i;
			f.price=(i%7+1)*10;
			foods.add(f);
		}
		for (int i = 1; i <=20; i++) {
			Table t= new Table();
			t.id=i;
			t.code="TABLE"+i;
			t.seats=i%5*2+2;
			t.customers=i%3==0?t.seats:0;
			t.description=i%4==0?"靠窗":"";
			tables.add(t);
		}
		return data;
	}
	
}
