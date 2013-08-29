package flyrestaurant.ph03.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.androidtest8.R;
import com.androidtest8.R.layout;
import com.androidtest8.R.menu;

import flyrestaurant.entity.Table;
import flyrestaurant.ph03.service.TableService;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class TableActivity extends BasicActivity {

	EditText seatsEdt;
	CheckBox needEmptyCkb;
	ListView tablesLtv;
	Button queryBtn;
	Button tableCancelBtn;

	List<Map<String, Object>> tableList = new ArrayList<Map<String, Object>>();
	String[] tablesLtvKeys = new String[] { "no", "code", "seats", "customers",
			"description" };
	int[] tablesLtvIds = new int[] { R.id.noTxv, R.id.codeTxv, R.id.seatsTxv,
			R.id.customersTxv, R.id.descriptionTxv };

	TableService tableService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tableService = new TableService(this);
		seatsEdt = (EditText) findViewById(R.id.seatsEdt);
		needEmptyCkb = (CheckBox) findViewById(R.id.needEmptyCkb);
		tablesLtv = (ListView) findViewById(R.id.tablesLtv);
		queryBtn=(Button)findViewById(R.id.tableQueryBtn);
		tableCancelBtn=(Button)findViewById(R.id.tableCancelBtn);
		queryBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int seats = 0;
				String s = seatsEdt.getText().toString();
				if (s.length() > 0) {
					seats = Integer.parseInt(s);
				}
				boolean needEmpty = needEmptyCkb.isChecked();
				List<Table> tables;
				try {
					tables = tableService.query(seats, needEmpty);
				} catch (Exception e) {
					e.printStackTrace();
					showMessageDialog(e.getMessage(), R.drawable.not, null);
					return;
				}
				tableList.clear();
				for (Table table : tables) {
					Map<String, Object> line = new HashMap<String, Object>();
					line.put("tableId", table.id);
					line.put("no", tableList.size() + 1);
					line.put("code", table.code);
					line.put("seats", table.seats + "×ù");
					if (table.customers == 0) {
						line.put("customers", "");
					} else {
						line.put("customers", table.customers + "Î»¾Í²Í");
					}
					line.put("description", table.description);
					tableList.add(line);
				}
				((SimpleAdapter) tablesLtv.getAdapter()).notifyDataSetChanged();
				if (tables.size() == 0) {
					tablesLtv.setVisibility(View.GONE);
				} else {
					tablesLtv.setVisibility(View.VISIBLE);
				}
			}
		});
		tableCancelBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TableActivity.this.finish();
			}
		});
		initTablesLtv();
	}

	public void initTablesLtv() {
		SimpleAdapter sa = new SimpleAdapter(this, tableList, R.layout.tables,
				tablesLtvKeys, tablesLtvIds);
		tablesLtv.setAdapter(sa);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.table;
	}

	@Override
	protected String getName() {
		return "²é×À";
	}


}
