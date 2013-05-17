package model;

import java.util.ArrayList;


public class SearchResponse {
	private ArrayList<Item> items;
	private Integer itemsCount;
	private Integer totalResults;
	
	public SearchResponse(ArrayList<Item> items, Integer itemsCount, Integer totalResults){
		setItems(items);
		setItemsCount(itemsCount);
		setTotalResults(totalResults);
	}

	public ArrayList<Item> getItems() {
		return items;
	}

	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}

	public Integer getItemsCount() {
		return itemsCount;
	}

	public void setItemsCount(Integer itemsCount) {
		this.itemsCount = itemsCount;
	}

	public Integer getTotalResults() {
		return totalResults;
	}

	public void setTotalResults(Integer totalResults) {
		this.totalResults = totalResults;
	}

}
