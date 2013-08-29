package com.abby.android.orderapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.androidtest8.R;


import flyrestaurant.entity.Food;
import flyrestaurant.entity.FoodType;
import flyrestaurant.entity.Order;
import flyrestaurant.entity.OrderDetail;
import flyrestaurant.entity.Table;
import flyrestaurant.entity.User;
import flyrestaurant.ph03.service.FoodService;
import flyrestaurant.ph03.service.OrderService;
import flyrestaurant.ph03.service.TableService;
import flyrestaurant.ph03.ui.BasicActivity;


import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class OrderActivity extends BasicActivity {

	Spinner tableSpn;
	EditText customersEdt;
	EditText descriptionEdt;
	TextView sumTxv;
	Button addFoodBtn;
	Button orderBtn;
	Button cancleBtn;
	ListView orderedLtv;
	// 已点餐品
	List<Map<String, Object>> orderedList = new ArrayList<Map<String, Object>>();
	String[] orderedLtvKeys = new String[] { "no", "name", "description",
			"num", "price" };
	int[] orderedLtvIds = new int[] { R.id.noTxv, R.id.nameTxv,
			R.id.descriptionTxv, R.id.numTxv, R.id.priceTxv };
	// 点菜dialog
	Spinner foodSpn;
	EditText numEdt;
	EditText foodDescriptionTxt;

	TableService tableService;
	FoodService foodService;
	OrderService orderService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tableService = new TableService(this);
		foodService = new FoodService(this);
		orderService = new OrderService(this);
		//使用findViewById()方法初始化各控件
		tableSpn=(Spinner)findViewById(R.id.orderTableSpn);
		customersEdt=(EditText)findViewById(R.id.orderCustomersEdt);
		descriptionEdt=(EditText)findViewById(R.id.orderDescriptionEdt);
		sumTxv=(TextView)findViewById(R.id.orderSumTxv);
		addFoodBtn=(Button)findViewById(R.id.addFoodBtn);
		orderBtn=(Button)findViewById(R.id.orderBtn);
		cancleBtn=(Button)findViewById(R.id.orderCancleBtn);
		orderedLtv=(ListView)findViewById(R.id.orderOrderedLtv);
		

		initTableSpn();
		initOrderedLTV();
		// 用findViewById()方法初始化各控件
		LayoutInflater li = LayoutInflater.from(this);
		View foodView = li.inflate(R.layout.food, null);
		foodSpn = (Spinner) foodView.findViewById(R.id.foodSpn);
		numEdt = (EditText) foodView.findViewById(R.id.numEdt);
		foodDescriptionTxt = (EditText) foodView
				.findViewById(R.id.foodDescriptionEdt);
		GridView foodTypeGdv = (GridView) foodView
				.findViewById(R.id.foodTypeGdv);
		initFoodTypeGV(foodTypeGdv);

		numEdt.setText("1");
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setTitle("选择酒菜").setIcon(R.drawable.dingcan)
				.setView(foodView).setCancelable(true)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						Food food = (Food) foodSpn.getSelectedItem();
						int num = 1;
						String n = numEdt.getText().toString();
						if (n.length() > 0)
							num = Integer.parseInt(n);
						String description = foodDescriptionTxt.getText()
								.toString();
						Map<String, Object> line = new HashMap<String, Object>();
						line.put("foodId", food.id);
						line.put("no", orderedList.size() + 1);
						line.put("name", food.name);
						line.put("description", description);
						line.put("num", num);
						line.put("price", food.price);
						line.put("checked", true);
						orderedList.add(line);
						((SimpleAdapter) orderedLtv.getAdapter())
								.notifyDataSetChanged();
						orderedLtv.setVisibility(View.VISIBLE);
						refreshSum(num * food.price);
						foodDescriptionTxt.setText(null);

					}
				});
		dialogBuilder.setNegativeButton("取消", null);

		final AlertDialog dialog = dialogBuilder.create();
		addFoodBtn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				numEdt.setText("1");
				dialog.show();
				
			}
		});
		orderBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				int customers = 0;
				String c = customersEdt.getText().toString();
				if (c.length() > 0)
					customers = Integer.parseInt(c);
				if (customers<=0) {
					showMessageDialog("请输入顾客数量", R.drawable.shezhi, null);
					return;
				}
				int sum = Integer.parseInt(sumTxv.getText().toString());
				if (sum <= 0) {
					showMessageDialog("尚未选择任何餐品", R.drawable.shezhi, null);
					return;
				}
				Table table = (Table) tableSpn.getSelectedItem();

				Order order = new Order();
				User waiter = ((App) getApplication()).user;
				order.tableId = table.id;
				order.waiterId = waiter.id;
				order.customers = Integer.parseInt(customersEdt.getText()
						.toString());
				order.description = descriptionEdt.getText().toString();
				order.orderDetails = new ArrayList<OrderDetail>();

				for (Map<String, Object> line : orderedList) {
					boolean checked = (Boolean) line.get("checked");
					if (!checked)
						continue;
					OrderDetail od = new OrderDetail();
					od.foodId = (Integer) line.get("foodId");
					od.num = (Integer) line.get("num");
					od.description = (String) line.get("description");
					order.orderDetails.add(od);
				}
				try {
					orderService.addOrder(order);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finish();
			}
		});
		cancleBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				OrderActivity.this.finish();
			}
		});
	}

	// 填充餐桌Spinner
	void initTableSpn() {
		List<Table> tables = tableService.getTables();
		tableSpn.setAdapter(new ArrayAdapter<Table>(this,
				android.R.layout.simple_spinner_item, tables));

	}

	// 填充餐品Spinner
	void initFoodSpn(int foodTypeId) {
		List<Food> foods = foodService.getFoods(foodTypeId);
		foodSpn.setAdapter(new ArrayAdapter<Food>(this,
				android.R.layout.simple_spinner_item, foods));
	}

	// 填充已点餐品列表ListView
	void initOrderedLTV() {
		SimpleAdapter sa = new SimpleAdapter(this, orderedList,
				R.layout.ordered, orderedLtvKeys, orderedLtvIds) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				final int lineNo = position;
				View view = super.getView(position, convertView, parent);
				CheckBox checkCkb = (CheckBox) view.findViewById(R.id.checkCkb);
				checkCkb.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						@SuppressWarnings("unchecked")
						Map<String, Object> line = (Map<String, Object>) getItem(lineNo);
						int num = (Integer) line.get("num");
						int price = (Integer) line.get("price");
						if (isChecked)
							refreshSum(num * price);
						else
							refreshSum(-num * price);
						line.put("checked", isChecked);
					}
				});

				return view;

			}
		};
		orderedLtv.setAdapter(sa);
	}

	// 填充餐品类型GridView
	void initFoodTypeGV(GridView foodTypeGdv) {
		final List<FoodType> foodTypes = foodService.getFoodTypes();
		final List<RadioButton> rbs = new ArrayList<RadioButton>();

		for (FoodType ft : foodTypes) {
			RadioButton rb = new RadioButton(OrderActivity.this);
			rb.setText(ft.name);
			rb.setTag(ft.id);
			rb.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {

				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if (!isChecked)
						return;
					for (RadioButton b : rbs) {
						if (b != buttonView)
							b.setChecked(false);
					}
					int foodTypeId = Integer.parseInt(buttonView.getTag()
							.toString());
					initFoodSpn(foodTypeId);
				}
			});
			rbs.add(rb);
		}
		rbs.get(0).setChecked(true);
		initFoodSpn(0);
		foodTypeGdv.setAdapter(new BaseAdapter() {

			public View getView(int position, View convertView, ViewGroup parent) {

				return rbs.get(position);
			}

			public long getItemId(int position) {

				return 0;
			}

			public Object getItem(int position) {
				return null;
			}

			public int getCount() {
				return foodTypes.size();
			}
		});

	}

	void refreshSum(int newAmount) {
		int sum = Integer.parseInt(sumTxv.getText().toString());
		sum += newAmount;
		sumTxv.setText(sum + "");

	}
@Override
	protected int getLayoutId() {
		return R.layout.order;
	}
@Override
	protected String getName() {
		return "点餐";
	}
	

}

