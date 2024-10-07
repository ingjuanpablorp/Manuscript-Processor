package co.com.mevieval.dynamodb;

import co.com.mevieval.dynamodb.helper.ManuscriptMapper;
import co.com.mevieval.model.manuscript.Manuscript;
import co.com.mevieval.model.manuscript.gateways.ManuscriptRepository;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;

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
    public Mono<Boolean> save(Manuscript manuscript) {
        ManuscriptEntity manuscriptEntity = mapper.toEntity(manuscript);

        findManuscript(manuscriptEntity.getUniqueId()).subscribe();

        return findManuscript(manuscriptEntity.getUniqueId())
        .flatMap(exists -> {
            if(exists){
                return Mono.just(false);
            } else{
                return Mono.fromFuture(this.table.putItem(mapper.toEntity(manuscript)))
                .then(Mono.just(true))
                .onErrorResume(e -> {
                    System.err.println("Error saving Manuscript: " + e.getMessage());
                    return Mono.just(false);
                });
            }
        });
    }

    private Mono<Boolean> findManuscript(String manuscriptId){        

        return Mono.fromFuture(this.table.getItem(GetItemEnhancedRequest.builder()
        .key(Key.builder()
        .partitionValue(manuscriptId).build())
        .build()))
        .map(item -> item != null)
        .defaultIfEmpty(false)
        .onErrorResume(e -> {
            System.err.println("Error checking existence of Manuscript: " + e.getMessage());
            return Mono.just(false);
        });
    }
}
