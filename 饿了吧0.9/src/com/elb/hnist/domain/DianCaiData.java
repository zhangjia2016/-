package com.elb.hnist.domain;

import java.util.ArrayList;

public class DianCaiData {
	public String retcode;
	public int id;
	public ArrayList<FanDian> data;
	
	
	public class FanDian
	{
		public String address;
		public int id;
		public String imageUrl;
		public int phone;
		public float price;
		public float score;
		public String title;
		public String url;
         
         @Override
        public String toString() {
        	// TODO Auto-generated method stub
        	return "address" + address + "imageUrl" + imageUrl;
        }
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "recode:" + retcode + "id" + id;
	}
}
