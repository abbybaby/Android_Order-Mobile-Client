package flyrestaurant.entity;

import java.io.Serializable;

public class Food implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int id;
	public String code;
	public int typeId;
	public String name;
	public int price;
	public String description;
	
	@Override
	public String toString()
	{
		return code+" "+name+"гд"+price;
	}
}
