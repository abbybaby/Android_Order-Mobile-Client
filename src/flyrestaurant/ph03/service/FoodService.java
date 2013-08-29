package flyrestaurant.ph03.service;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.abby.android.orderapp.DbHelper;

import flyrestaurant.entity.Food;
import flyrestaurant.entity.FoodType;

public class FoodService {
	DbHelper dbHelper;
	public FoodService(Context context)
	{
		dbHelper=new DbHelper(context);
	}
	
public List<FoodType> getFoodTypes() {
	List<FoodType> list=new ArrayList<FoodType>();
	SQLiteDatabase db=dbHelper.getWritableDatabase();
	Cursor cursor=db.query("food_type",null,null,null,null,null,null);
	for (cursor.moveToFirst();
			!cursor.isAfterLast();
			cursor.moveToNext()) 
	{
		FoodType t=new FoodType();
		t.id=cursor.getInt(0);
		t.name=cursor.getString(1);
		list.add(t);
	}
	cursor.close();
	db.close();
	return list;
}
public List<Food> getFoods(int foodTypeId) {
	List<Food> list=new ArrayList<Food>();
	SQLiteDatabase db=dbHelper.getWritableDatabase();
	String where=null;
	if (foodTypeId!=0) 
		where="type_id="+foodTypeId;
	Cursor cursor=db.query("food",null,where,null,null,null,null);
	for (cursor.moveToFirst();
			!cursor.isAfterLast();
			cursor.moveToNext())
	{
		Food f=new Food();
		f.id=cursor.getInt(0);
		f.code=cursor.getString(1);
		f.typeId=cursor.getInt(2);
		f.name=cursor.getString(3);
		f.price=cursor.getInt(4);
		f.description=cursor.getString(5);
		list.add(f);
	}
	cursor.close();
	db.close();
	return list;
}
public Food getFood(int foodId) {
	Food f=null;
	SQLiteDatabase db=dbHelper.getWritableDatabase();
	Cursor cursor=db.query("food",null,"id="+foodId,null,null,null,null);
	if (cursor.moveToFirst()) {
		f=new Food();
		f.id=cursor.getInt(0);
		f.code=cursor.getString(1);
		f.typeId=cursor.getInt(2);
		f.name=cursor.getString(3);
		f.price=cursor.getInt(4);
		f.description=cursor.getString(5);
	}
	cursor.close();
	db.close();
	return f;
}
}
