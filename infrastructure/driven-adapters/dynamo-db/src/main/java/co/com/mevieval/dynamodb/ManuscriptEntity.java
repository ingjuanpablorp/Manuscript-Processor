package co.com.mevieval.dynamodb;

import java.util.List;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class ManuscriptEntity {

    private String uniqueId;
    private List<String> content;

    public ManuscriptEntity() {
    }

    public ManuscriptEntity(String uniqueId, List<String> content) {
        this.uniqueId = uniqueId;
        this.content = content;
    }

    @DynamoDbPartitionKey
    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    @DynamoDbAttribute("content")
    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }
}
