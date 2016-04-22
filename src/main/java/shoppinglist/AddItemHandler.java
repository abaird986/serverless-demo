package main.java.shoppinglist;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.simpleworkflow.flow.LambdaFunctionException;
import com.amazonaws.util.StringUtils;
import com.amazonaws.util.json.Jackson;

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

        public void setListId(final String listId) {
            this.listId = listId;
        }

        public String getItem() {
            return item;
        }

        public void setItem(final String item) {
            this.item = item;
        }

    }

    /**
     * Response currently set to void, so response will be
     * null
     * Addition of a "success" object recommended for client retrying/validation.
     */
    public void addItem(final AddItemRequest input, final Context context) {

        validate(input);

        context.getLogger().log("Input: " + input);

        client.addItem(input.getListId(), input.getItem(), context.getLogger());

    }

    private void validate(final AddItemRequest input) {

        if (input == null || StringUtils.isNullOrEmpty(input.getItem())
                || StringUtils.isNullOrEmpty(input.getListId())) {
            throw new LambdaFunctionException("provided input not valid: "
                    + Jackson.toJsonString(input));
        }

    }

}
