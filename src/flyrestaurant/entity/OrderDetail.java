package flyrestaurant.entity;

import java.io.Serializable;



public class OrderDetail implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int id;
	public int orderId;
	public int foodId;
	public int num;
	public String description;

}
