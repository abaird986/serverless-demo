package main.java.shoppinglist;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.util.StringUtils;

public class ShoppingListsTableClient {
	

	private static AmazonDynamoDBClient client;
	private static Table shoppingListTable;
	
	private void initialize(LambdaLogger logger) {
		client = new AmazonDynamoDBClient();
		shoppingListTable = new Table(client, "ShoppingLists");
	}
	
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
