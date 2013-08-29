package flyrestaurant.ph03.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.content.Context;

import com.abby.android.orderapp.App;
import com.abby.android.orderapp.HttpUtils;

import flyrestaurant.entity.Food;
import flyrestaurant.entity.Order;
import flyrestaurant.entity.OrderDetail;


public class OrderService {
	App app;
	FoodService foodService;
	public OrderService(Context context)
	{
		foodService=new FoodService(context);
		app=(App)context.getApplicationContext();
	}
	static List<Order> orders=new ArrayList<Order>();
	
	static {
		Order o=new Order();
		o.id=1;
		o.code="1";
		o.tableId=1;
		o.waiterId=1;
		o.orderTime=new Date();
		o.customers=4;
		o.status=1;
		o.orderDetails=new ArrayList<OrderDetail>();
		
		for (int i = 1; i <=5; i++) {
			OrderDetail od=new OrderDetail();
			od.id=i;
			od.orderId=1;
			od.foodId=i;
			od.num=1;
			o.orderDetails.add(od);
		}
		orders.add(o);
		
		o=new Order();
		o.id=2;
		o.code="2";
		o.tableId=2;
		o.waiterId=2;
		o.orderTime=new Date();
		o.customers=10;
		o.status=0;
		o.orderDetails=new ArrayList<OrderDetail>();
		for (int i = 6; i <=15; i++) 
		{
			OrderDetail od=new OrderDetail();
			od.id=i;
			od.orderId=2;
			od.foodId=i;
			od.num=1;
			o.orderDetails.add(od);
		}
		orders.add(o);
		
	}
	public void addOrder(Order order) throws Exception {
		//TODO: 添加订单到服务器
		String url = app.serverUrl + "/?to=addOrder";
		HttpUtils.sendObject(url, order);
	}
	
	public OrderDto getOrder(String code) throws Exception{
		String url = app.serverUrl + "/?to=getOrder";
		Map<String, String> params = new HashMap<String, String>();
		params.put("code", code);
		Order order = (Order) HttpUtils.post(url, params);

		if (order == null) {
			return null;
		}
		OrderDto dto=new OrderDto();
		dto.order=order;
	for (OrderDetail od:dto.order.orderDetails) {
		Food food=foodService.getFood(od.foodId);
		Map<String, Object> line=new HashMap<String, Object>();
		line.put("no", dto.orderedList.size()+1);
		line.put("name", food.name);
		line.put("description", od.description);
		line.put("num", od.num);
		line.put("price",food.price);
		dto.orderedList.add(line);
		dto.sum+=od.num*food.price;
	}
	return dto;
	}
	
	public void pay(int orderId) throws Exception{
		//TODO:连接服务器完成转账
		String url = app.serverUrl + "/?to=pay";
		Map<String, String> params = new HashMap<String, String>();
		params.put("orderId", orderId + "");
		HttpUtils.post(url, params);
	}
	public static class OrderDto {
		public Order order;
		public int sum;
		public List<Map<String, Object>> orderedList=new ArrayList<Map<String,Object>>();
	}
}

