package main.java.shoppinglist;

import com.amazonaws.services.lambda.runtime.Context;

public class GetShoppingListHandler {
	
	private static ShoppingListsTableClient client = new ShoppingListsTableClient();
	
	public static class GetShoppingListRequest {
		
		private String listId;

		public String getListId() {
			return listId;
		}

		public void setListId(String listId) {
			this.listId = listId;
		}
		
	}

    public ShoppingList getShoppingList(GetShoppingListRequest input, Context context) {
        context.getLogger().log("Input: " + input);

        return client.getShoppingList(input.getListId(), context.getLogger());
        
    }

}
