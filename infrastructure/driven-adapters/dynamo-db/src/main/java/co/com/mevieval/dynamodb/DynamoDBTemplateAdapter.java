package co.com.mevieval.dynamodb;

import co.com.mevieval.dynamodb.helper.ManuscriptMapper;
import co.com.mevieval.model.manuscript.Manuscript;
import co.com.mevieval.model.manuscript.gateways.ManuscriptRepository;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
public class DynamoDBTemplateAdapter implements ManuscriptRepository{

    private final DynamoDbEnhancedAsyncClient enhancedAsyncClient;
    private final DynamoDbAsyncTable<ManuscriptEntity> table;
    private final ManuscriptMapper mapper;

    public DynamoDBTemplateAdapter(DynamoDbEnhancedAsyncClient enhancedAsyncClient, ManuscriptMapper mapper){
        this.enhancedAsyncClient = enhancedAsyncClient;
        this.mapper = mapper;
        this.table = this.enhancedAsyncClient.table("manuscripts", TableSchema.fromBean(ManuscriptEntity.class));
    }

    @Override
    public Mono<Void> save(Manuscript manuscript) {
        return Mono.fromFuture(this.table
        .putItem(mapper.toEntity(manuscript)))
        .onErrorResume(e -> {
            System.err.println("Error saving Manuscript " + e.getMessage());
            return Mono.empty();
        });
    }
}
