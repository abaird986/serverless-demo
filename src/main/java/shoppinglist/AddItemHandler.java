package main.java.shoppinglist;

import com.amazonaws.services.lambda.runtime.Context;

/**
 * Class to serve as handler for an AWS Lambda function. Expects JSON payload in the form of:
 * {
 *   "listId": "this-is-a-list-id",
 *   "item": "this-is-an-ite"
 * }
 */
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

	/**
	 * Response currently set to void, so response will be
	 * null
	 * Addition of a "success" object recommended for client retrying/validation.	 
	 */
    public void addItem(AddItemRequest input, Context context) {
    	
        context.getLogger().log("Input: " + input);

        client.addItem(input.getListId(), input.getItem(), context.getLogger());
        
    }

}
