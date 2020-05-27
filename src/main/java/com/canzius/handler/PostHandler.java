package com.canzius.handler;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.canzius.ApiGatewayResponse;
import com.canzius.Response;
import com.canzius.persistence.DynamoDbFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class PostHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = LogManager.getLogger(PostHandler.class);

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		LOG.info("received: {}", input);

		try {


			String tableName = System.getenv("DYNAMODB_TABLE");
			LOG.info("Listing all items in table: {}",tableName);
			DynamoDB dynamoDB = DynamoDbFactory.createDynamoDbClient();
			Table table = dynamoDB.getTable(tableName);
			ObjectMapper mapper = new ObjectMapper();
			Map payload = mapper.readValue(
					input.get("body").toString(),
					Map.class);

			payload.put("todo",truncateIfNecessary(payload.get("todo").toString()));

			Item item = new Item().withPrimaryKey("id", UUID.randomUUID().toString()).withMap("todo",payload);
			table.putItem(item);

		} catch (IOException e) {
			LOG.error("Error in saving todo item : ", e);

			// send the error response back
			Response responseBody = new Response("Error in saving todo item: ", input);
			return ApiGatewayResponse.builder()
					.setStatusCode(500)
					.setObjectBody(responseBody)
					.setHeaders(Collections.singletonMap("Access-Control-Allow-Origin", "*"))
					.build();
		}

		Response responseBody = new Response("Todo item saved", input);
		return ApiGatewayResponse.builder()
				.setStatusCode(200)
				.setObjectBody(responseBody)
				.setHeaders(Collections.singletonMap("Access-Control-Allow-Origin","*"))
				.build();
	}

	private String truncateIfNecessary(String str){
		if(str.length() > 255){
			return str.substring(0,255);
		}
		return str;
	}
}
