package com.canzius.persistence;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;


public class DynamoDbFactory {
    public static DynamoDB createDynamoDbClient(){
        DynamoDB dynamoDB = null;
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        dynamoDB = new DynamoDB(client);
        return dynamoDB;
    }
}
