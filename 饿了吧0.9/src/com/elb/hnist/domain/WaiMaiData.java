package com.elb.hnist.domain;

import java.util.ArrayList;

public class WaiMaiData {
	public String retcode;
	public int id;
	public ArrayList<CanGuan> data;
	
	
	public class CanGuan
	{
		public String address;
		public int id;
		public String imageUrl;
		public int phone;
		public float price;
		public float score;
		public String title;
		public String url;
		public String peiSongFei;
		public String peiSongShiJian;
         
         @Override
        public String toString() {
        	// TODO Auto-generated method stub
        	return "title" + title + "imageUrl" + imageUrl;
        }
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "recode:" + retcode + "id" + id;
	}
}
