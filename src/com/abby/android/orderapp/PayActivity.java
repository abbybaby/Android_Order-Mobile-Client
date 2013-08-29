package com.abby.android.orderapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.androidtest8.R;

import flyrestaurant.ph03.service.OrderService;
import flyrestaurant.ph03.service.OrderService.OrderDto;
import flyrestaurant.ph03.ui.BasicActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class PayActivity extends BasicActivity {
	static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	EditText orderCodeEdt;
	Button queryBtn;
	Button payBtn;
	Button cancleBtn;
	TextView sumTxv;
	ListView orderedLtv;

	TextView orderCodeTxv;
	TextView tableCodeTxv;
	TextView waiterCodeTxv;
	TextView orderTimeTxv;
	TextView customersTxv;
	TextView payDescriptionTxv;

	List<Map<String, Object>> orderedList = new ArrayList<Map<String, Object>>();
	String[] orderedLtvKeys = new String[] { "no", "name", "description",
			"num", "price" };
	int[] orderedLtvIds = new int[] { R.id.noTxv, R.id.nameTxv,
			R.id.descriptionTxv, R.id.numTxv, R.id.priceTxv };

	int orderId;

	OrderService orderService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		orderService = new OrderService(this);
		//setContentView(R.layout.pay);
		
		orderCodeEdt = (EditText) findViewById(R.id.payOrderCodeEdt);
		queryBtn = (Button) findViewById(R.id.payQueryBtn);
		payBtn = (Button) findViewById(R.id.payBtn);
		cancleBtn = (Button) findViewById(R.id.payCancleBtn);
		sumTxv = (TextView) findViewById(R.id.paySumTxv);
		orderedLtv = (ListView) findViewById(R.id.payOrderedLtv);

		orderCodeTxv = (TextView) findViewById(R.id.payOrderCodeTxv);
		tableCodeTxv = (TextView) findViewById(R.id.payTableCodeTxv);
		waiterCodeTxv = (TextView) findViewById(R.id.payWaiterCodeTxv);
		orderTimeTxv = (TextView) findViewById(R.id.payOrderTimeTxv);
		customersTxv = (TextView) findViewById(R.id.payCustomersTxv);
		payDescriptionTxv = (TextView) findViewById(R.id.payDescriptionTxv);

		initOrderedLtv();
		
		
		queryBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				PayActivity.this.orderId = 0;
				String orderCode = orderCodeEdt.getText().toString();
				clearDisplay();
				if (orderCode.length() == 0) {
					showMessageDialog("请输入订单编号", R.drawable.warning, null);
					return;
				}
				OrderDto dto = null;
				try {
					dto = orderService.getOrder(orderCode);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (dto == null) {
					showMessageDialog("未查找到订单：" + orderCode,
							R.drawable.warning, null);
					return;
				}
				orderCodeTxv.setText("订单编号：" + dto.order.code);
				tableCodeTxv.setText("  餐桌ID:" + dto.order.tableId + "");
				waiterCodeTxv.setText("服务员ID：" + dto.order.waiterId + "");
				orderTimeTxv.setText("   时间:" + sdf.format(dto.order.orderTime));
				customersTxv.setText("顾客数量:" + dto.order.customers);
				payDescriptionTxv.setText(dto.order.description == null ? ""
						: ("   备注:" + dto.order.description));
				orderedList.addAll(dto.orderedList);
				SimpleAdapter sa = (SimpleAdapter) orderedLtv.getAdapter();
				sa.notifyDataSetChanged();
				orderedLtv.setVisibility(View.VISIBLE);
				if (dto.order.status == 1) {
					sumTxv.setText("此订单已结算，合计:￥" + dto.sum);
					PayActivity.this.orderId = -1;
				} else {
					sumTxv.setText("合计：￥" + dto.sum);
					PayActivity.this.orderId = dto.order.id;
				}

			}
		});
		payBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (orderId) {
				case 0: {
					showMessageDialog("请选择订单", R.drawable.warning, null);
				}
					break;

				case -1: {
					showMessageDialog("此订单已结算", R.drawable.warning, null);
				}
					break;

				default:
					break;
				}
				try {
					orderService.pay(orderId);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				orderId = -1;
				showMessageDialog("结账完成", R.drawable.info,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								finish();
							}
						});
			}
		});
		cancleBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				PayActivity.this.finish();
			}
		});
	}

	void clearDisplay() {
		// TODO Auto-generated method stub
		orderCodeEdt.setText(null);
		orderCodeTxv.setText(null);
		tableCodeTxv.setText(null);
		waiterCodeTxv.setText(null);
		orderTimeTxv.setText(null);
		customersTxv.setText(null);
		payDescriptionTxv.setText(null);
		sumTxv.setText(null);

		orderedList.clear();
		SimpleAdapter sa = (SimpleAdapter) orderedLtv.getAdapter();
		sa.notifyDataSetChanged();
		orderedLtv.setVisibility(View.GONE);

	}

	void initOrderedLtv() {
		// TODO Auto-generated method stub
		SimpleAdapter sa = new SimpleAdapter(this, orderedList,
				R.layout.ordered, orderedLtvKeys, orderedLtvIds) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				CheckBox checkCkb = (CheckBox) view.findViewById(R.id.checkCkb);
				checkCkb.setVisibility(View.GONE);
				return view;

			}
		};
		orderedLtv.setAdapter(sa);
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pay, menu);
		return true;
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.pay;
	}

	@Override
	protected String getName() {
		// TODO Auto-generated method stub
		return "结账";
	}

}
