package com.canzius.handler;

import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.canzius.ApiGatewayResponse;
import com.canzius.Response;
import com.canzius.persistence.DynamoDbFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Map;

public class DeletePathIdHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = LogManager.getLogger(DeletePathIdHandler.class);

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

		LOG.info("received: {}", input);

		Map<String,String> pathParameters =  (Map<String,String>)input.get("pathParameters");
		String todoId = pathParameters.get("id");

		String tableName = System.getenv("DYNAMODB_TABLE");
		LOG.info("Deleting item with id:{} from  table: {}",todoId,tableName);

		DynamoDB dynamoDB = DynamoDbFactory.createDynamoDbClient();
		Table table = dynamoDB.getTable(tableName);

		DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
				.withPrimaryKey(new PrimaryKey("id", todoId));

		DeleteItemOutcome deleteItemOutcome = table.deleteItem(deleteItemSpec);

		Response responseBody = new Response("Item deleted successfully!", todoId);
		return ApiGatewayResponse.builder()
				.setStatusCode(200)
				.setObjectBody(responseBody)
				.setHeaders(Collections.singletonMap("Access-Control-Allow-Origin", "*"))
				.build();
	}
}
