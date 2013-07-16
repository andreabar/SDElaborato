package model;

import java.util.ArrayList;
import java.util.Date;

public class Search {

	private ArrayList<Integer> recordsId;
	private Date date;
	private String keyword;
	
	
	public Search(ArrayList<Integer> rid, Date d, String k){
		this.setRecordsId(rid);
		this.setDate(d);
		this.setKeyword(k);
	}
	
	
	public ArrayList<Integer> getRecordsId() {
		return recordsId;
	}


	public void setRecordsId(ArrayList<Integer> recordsId) {
		this.recordsId = recordsId;
	}


	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getKeyword() {
		return keyword;
	}
	
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	
}
