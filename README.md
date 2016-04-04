# serverless-demo

This project can be used to create the Lambda functions necessary for running the serverless shopping list demo application, as shown in the AWS Webinar: Getting Started with Serverless Architectures.

# Recreating the Demo
## 1. Create a DynamoDB table called `ShoppingLists`.
This table will be where all of the shopping lists are persisted. The only configuration you are required to set while creating the table is to define the hash key 'listId' of type string.  All other values may remain default.

##2. Create an IAM Role to be used by your Lambda functions.
Best practices dictate that you should create separate IAM Roles for each one of your Lambda functions.  But for this demo, you can use the same IAM Role for both Lambda functions used by the app.  Here is a sample policy that can be used:


         {
    	    "Version": "2012-10-17",
            "Statement": [
              {
            	"Sid": "Stmt1458242582000",
            	"Effect": "Allow",
            	"Action": [
           	   	  "logs:CreateLogGroup",
              	  "logs:CreateLogStream",
                  "logs:PutLogEvents"
                ],
                "Resource": [
                  "arn:aws:logs:*:*:*"
                ]
              },
              {
                "Sid": "Stmt1458250681000",
           	    "Effect": "Allow",
                "Action": [
                  "cloudformation:DescribeStacks"
                ],
                "Resource": [
                  "*"
                ]
        	  },
              {
                "Sid": "Stmt1458242658000",
                "Effect": "Allow",
                "Action": [
                  "dynamodb:GetItem",
                  "dynamodb:PutItem"
                 ],
                "Resource": [
                  "arn:aws:dynamodb:*:*:table/*ShoppingLists*"
                ]
              }
            ]
          }
## Build the provided source code and upload .zip to S3.
Included is a build.gradle file that can be used to directly build the source code provided using Gradle.  Output .zip will be added to the \build\distributions subdirectory.

## Create two Lambda functions
Create the necessary Lambda functions to run the application, pointing to the S3 object location where you've uploaded the build .zip to S3.
#### GetShoppingList
The handler for this Lambda function from the provided source code is:
`main.java.shoppinglist.GetShoppingListHandler::getShoppingList`
#### AddItem
The handler for this Lambda function is:
`main.java.shoppinglist.AddItemHandler::addItem`

## Create new API
The methods/resources you'll need to create methods on are:
`GET /lists/{listId}` - integrates with GetShoppingList LambdaFunction.
and
`POST /lists/{listId}/items` - integrates with AddItem above.

Each of the above API methods will require a mapping template, see \config directory for the mapping templates you can use - configured via 'Integration Request' console inside the Amazon API Gateway console.

Enable CORS for both resources that have methods associated: http://docs.aws.amazon.com/apigateway/latest/developerguide/how-to-cors.html 

Deploy this API to a new stage.

## Modify provided index.html
index.html is located inside the \html directory, and you'll need to update it to use your new API Gateway endpoint and stage.

## Upload index.html to S3, configure as public website.
Create a new S3 bucket and upload index.html to it. Follow these instructions to expose this as a public website:
http://docs.aws.amazon.com/AmazonS3/latest/dev/HostingWebsiteOnS3Setup.html