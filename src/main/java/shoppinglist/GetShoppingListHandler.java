package main.java.shoppinglist;

import com.amazonaws.services.lambda.runtime.Context;

/**
 * Class to serve as handler for an AWS Lambda function. Expects JSON payload in the form of:
 * {
 *   "listId": "this-is-a-list-id"
 *   
 *   Adding a test comment here. Another comment. Another one.
 * }
 */
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
	
	/**
	 * 
	 * @return - serialized JSON object in the form of:
	 * {
	 *   "listId": "this-is-a-list-id",
	 *   "itemms": ["item1", "item2"]
	 * }
	 */
    public ShoppingList getShoppingList(GetShoppingListRequest input, Context context) {
        context.getLogger().log("Input: " + input);

        return client.getShoppingList(input.getListId(), context.getLogger());
        
    }

}
