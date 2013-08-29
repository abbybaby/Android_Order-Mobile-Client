package flyrestaurant.entity;

import java.io.Serializable;

public class Table implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
public int id;
public String code;
public int seats;
public int customers;
public String description;

@Override
public String toString() {
	return code;
}
}
