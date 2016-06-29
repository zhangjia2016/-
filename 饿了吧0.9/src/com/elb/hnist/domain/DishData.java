package com.elb.hnist.domain;

import java.io.Serializable;
import java.util.ArrayList;

public class DishData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int retcode;
	public int id;
	public ArrayList<Dish> data;
	
	
	public class Dish implements Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String foodName;
		public int id;
		public String imageUrl;
		public float price;
		public int saleQuantity;
		public float score;
		public int currentNumber = 0;
		
		@Override
		public String toString() {
		// TODO Auto-generated method stub
			return "foodName: " + foodName + ",float: " + price;
		}
		
	}
	
}
