package co.com.mevieval.dynamodb;

import java.util.List;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@Data
@DynamoDbBean
public class ManuscriptEntity {

    private String uniqueId;
    private List<String> content;
    private int countClueFound;
    private int countNoClue;
    private double ratio;

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

    @DynamoDbAttribute("count_clue_found")
    public int getCountClueFound(){
        return countClueFound;
    }

    public void setCountClueFound(int countClueFound) {
        this.countClueFound = countClueFound;
    }

    @DynamoDbAttribute("count_no_clue")
    public int getCountNoClue(){
        return countNoClue;
    }

    public void setCountNoClue(int countNoClue) {
        this.countNoClue = countNoClue;
    }

    @DynamoDbAttribute("ratio")
    public double getRatio(){
        return ratio;
    }

    public void setRatio(double ratio){
        this.ratio = ratio;
    }

}
