package com.canzius.handler;

import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.canzius.ApiGatewayResponse;
import com.canzius.Response;
import com.canzius.persistence.DynamoDbFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class GetHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = LogManager.getLogger(GetHandler.class);

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		//Return all todo's

		LOG.info("received: {}", input);
		String tableName = System.getenv("DYNAMODB_TABLE");
		LOG.info("Listing all items in table: {}",tableName);

		DynamoDB dynamoDB = DynamoDbFactory.createDynamoDbClient();
		Table table = dynamoDB.getTable(tableName);
		ItemCollection<ScanOutcome> items = table.scan();
		Iterator<Item> iterator = items.iterator();
		List itemList = new ArrayList();
		while(iterator.hasNext()){
			itemList.add(iterator.next().asMap());
		}

		Response responseBody = new Response(String.format("%s items retrieved",itemList.size()),itemList);
		return ApiGatewayResponse.builder()
				.setStatusCode(200)
				.setObjectBody(responseBody)
				.setHeaders(Collections.singletonMap("Access-Control-Allow-Origin", "*"))
				.build();
	}
}
