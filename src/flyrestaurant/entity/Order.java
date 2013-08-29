package flyrestaurant.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Order implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public  int id;
	public  String code;
	public  int tableId;
	public  int waiterId;
	public  Date orderTime;
	public  int customers;
	public  int status;
	public  String description;
	
	public List<OrderDetail> orderDetails;  

}
