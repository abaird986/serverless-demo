package main.java.shoppinglist;

import com.amazonaws.services.lambda.runtime.Context;

public class AddItemHandler {
	
	private static ShoppingListsTableClient client = new ShoppingListsTableClient();
	
	public static class AddItemRequest {
		
		private String listId;
		private String item;
		
		public String getListId() {
			return listId;
		}
		
		public void setListId(String listId) {
			this.listId = listId;
		}
		
		public String getItem() {
			return item;
		}
		
		public void setItem(String item) {
			this.item = item;
		}
		
	}

    public void addItem(AddItemRequest input, Context context) {
    	
        context.getLogger().log("Input: " + input);

        client.addItem(input.getListId(), input.getItem(), context.getLogger());
        
    }

}
