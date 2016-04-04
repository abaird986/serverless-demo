package main.java.shoppinglist;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.util.StringUtils;

/**
 * Integrates with DynamoDB to get/put items which represent
 * user shopping lists.
 */
public class ShoppingListsTableClient {
	

	private static AmazonDynamoDBClient client;
	private static Table shoppingListTable;
	
	private void initialize(LambdaLogger logger) {
		client = new AmazonDynamoDBClient();
		//DyanmoDB table is hardcoded below to "ShoppingLists"
		shoppingListTable = new Table(client, "ShoppingLists");
	}
	
	/**
	 * retrieve an existing DynamoDB list based on listIt, if one exists, then append
	 * the provided item to the list and put into DDB table.
	 * @param listId - id of the shopping list we're adding to.
	 * @param item - string representation of the shopping list item to be added.
	 * @param logger - LambdaLogger object for logging to CloudWatch Logs.
	 */
	public void addItem(String listId, String item, LambdaLogger logger){
		
		if (shoppingListTable == null) {
			initialize(logger);
		}
		
		ShoppingList shoppingList;
		if (!StringUtils.isNullOrEmpty(listId)) {
			
			shoppingList = getShoppingList(listId, logger);
			
			if (!StringUtils.isNullOrEmpty(item)) {
			
				shoppingList.addItem(item);
				
				Item ddbItem = new Item()
						.withString("listId", listId)
						.withList("items", shoppingList.getItems());
				
				shoppingListTable.putItem(ddbItem);
			}
		}
	}
	
	/**
	 * Retrieve a shopping list, if one exists, from DDB. Return an empty list if one
	 * does not yet exist.
	 * @param listId - id of the list to be retrieved.
	 * @param logger - LambdaLogger object to be used for logging to CloudWatch Logs.
	 * @return
	 */
	public ShoppingList getShoppingList(String listId, LambdaLogger logger) {
		
		if (shoppingListTable == null) {
			initialize(logger);
		}
		
		ShoppingList list = new ShoppingList();
		List<String> items;
		
		logger.log("your list id" + listId);
		Item ddbItem = shoppingListTable.getItem("listId", listId);
		
		if (ddbItem != null) {
			items = ddbItem.getList("items");
		} else {
			items = new ArrayList<String>();
		}
		
		list.setItems(items);
		list.setListId(listId);
		
		return list;
		
	}	
	
}
