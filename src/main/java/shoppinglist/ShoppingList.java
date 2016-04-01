package main.java.shoppinglist;

import java.util.List;

public class ShoppingList {
	
	private String listId;
	private List<String> items;

	
	public String getListId() {
		return listId;
	}
	public void setListId(String listId) {
		this.listId = listId;
	}
	
	public List<String> getItems() {
		return items;
	}
	public void setItems(List<String> items) {
		this.items = items;
	}
	
	public void addItem(String item) {
		items.add(item);
	}
	

}
