package com.canzius.handler;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.canzius.ApiGatewayResponse;
import com.canzius.Response;
import com.canzius.persistence.DynamoDbFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Map;

public class GetPathIdHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = LogManager.getLogger(GetPathIdHandler.class);

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

		LOG.info("received: {}", input);

		Map<String,String> pathParameters =  (Map<String,String>)input.get("pathParameters");
		String todoId = pathParameters.get("id");

		String tableName = System.getenv("DYNAMODB_TABLE");
		LOG.info("Getting item with id: from  table: {}",todoId,tableName);

		DynamoDB dynamoDB = DynamoDbFactory.createDynamoDbClient();
		Table table = dynamoDB.getTable(tableName);

		Item todoItem =
				table.getItem(new GetItemSpec()
						.withPrimaryKey("id", todoId)
				);

		Response responseBody = new Response("Retrieved item", todoItem.get("todo"));
		return ApiGatewayResponse.builder()
				.setStatusCode(200)
				.setObjectBody(responseBody)
				.setHeaders(Collections.singletonMap("Access-Control-Allow-Origin", "*"))
				.build();
	}
}
