package main.java.test;

import main.java.shoppinglist.AddItemHandler;
import main.java.shoppinglist.AddItemHandler.AddItemRequest;

import com.amazonaws.services.lambda.AWSLambdaClient;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.simpleworkflow.flow.LambdaFunctionException;
import com.amazonaws.util.StringUtils;
import com.amazonaws.util.json.Jackson;

public class AddItemTestHandler {

    private static AWSLambdaClient lambdaClient = new AWSLambdaClient();

    public void addItemTest(final TestExecutionRequest input, final Context context) {

        final AddItemHandler.AddItemRequest successPayload = new AddItemRequest();
        successPayload.setItem("testItem");
        successPayload.setListId("testList");

        final InvokeRequest request = new InvokeRequest()
        .withFunctionName("ServerlessShoppingList-AddItem")
        .withQualifier(input.getFunctionVersionToTest())
        .withPayload(Jackson.toJsonString(successPayload));

        // success request attempt #1, should succeed and add an item to DDB
        final InvokeResult successResult = lambdaClient.invoke(request);

        if (successResult == null) {

            throw new LambdaFunctionException("successResponse object was null");

        } else if (successResult.getStatusCode() == null) {

            throw new LambdaFunctionException("successResponse status code was null");

        } else if (!StringUtils.isNullOrEmpty(successResult.getFunctionError()) || successResult.getStatusCode() < 200 || successResult.getStatusCode() > 299) {

            throw new LambdaFunctionException(
                    "ERROR response when should have succeeded. \nasuccessResponse object status code: "
                            + successResult.getStatusCode() + " error_message: "
                            + successResult.getFunctionError());
        } else {
            context.getLogger().log("Success test: PASSED!");
        }

        // do not set itemId, test should fail.
        final AddItemHandler.AddItemRequest errorPayload = new AddItemRequest();
        errorPayload.setItem("testItem2");
        request.setPayload(Jackson.toJsonString(errorPayload));

        final InvokeResult errorResult = lambdaClient.invoke(request);

        if (errorResult == null) {

            throw new LambdaFunctionException("errorResult object was null");

        } else if (errorResult.getStatusCode() == null) {

            throw new LambdaFunctionException("errorResult status code was null");

        } else if (StringUtils.isNullOrEmpty(errorResult.getFunctionError())
                && errorResult.getStatusCode() >= 200 && errorResult.getStatusCode() <= 299) {

            throw new LambdaFunctionException("OK response when should have failed. \n"
                    + "errorResult object status code: "
                    + errorResult.getStatusCode() + " error_message: "
                    + errorResult.getFunctionError());
        } else {
            context.getLogger().log("failure test: PASSED!");
        }

    }

}
