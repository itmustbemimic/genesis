package io.neond.genesis.domain.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Data;
import lombok.Setter;

@Data
@DynamoDBTable(tableName = "genesis-user")
public class MemberGameResult {
    @DynamoDBHashKey(attributeName = "user_id")
    private String user_id;

    @DynamoDBRangeKey(attributeName = "date")
    private String date;

    @DynamoDBAttribute
    private String game_id;

    @DynamoDBAttribute
    private int point;

    @DynamoDBAttribute
    private int prize_amount;

    @DynamoDBAttribute
    private String prize_type;

}
