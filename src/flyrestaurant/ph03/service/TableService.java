package flyrestaurant.ph03.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.abby.android.orderapp.App;
import com.abby.android.orderapp.DbHelper;
import com.abby.android.orderapp.HttpUtils;

import flyrestaurant.entity.Table;


public class TableService {
	App app;
	DbHelper dbHelper;

	public TableService(Context context) {
		dbHelper = new DbHelper(context);
		app = (App) context.getApplicationContext();
	}

	public List<Table> query(int seats, boolean needEmpty) throws Exception {
		String url = app.serverUrl + "/?to=queryTable";
		Map<String, String> params = new HashMap<String, String>();
		params.put("seats", seats + "");
		if (needEmpty) {
			params.put("needEmpty", "true");
		}
		@SuppressWarnings("unchecked")
		List<Table> tables = (List<Table>) HttpUtils.post(url, params);
		return tables;
	}

	public List<Table> getTables() {
		List<Table> tables = new ArrayList<Table>();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.query("tables", null, null, null, null, null, null);

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			Table table = new Table();
			table.id = cursor.getInt(0);
			table.code = cursor.getString(1);
			table.seats = cursor.getInt(2);
			table.description = cursor.getString(3);
			tables.add(table);
		}
		cursor.close();
		db.close();
		return tables;
	}
}
