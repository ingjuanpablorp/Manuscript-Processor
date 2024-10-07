package co.com.mevieval.dynamodb.helper;

import co.com.mevieval.dynamodb.ManuscriptEntity;
import co.com.mevieval.model.manuscript.Manuscript;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public class ManuscriptMapper {
    
    public ManuscriptEntity toEntity(Manuscript manuscript){
        ManuscriptEntity entity = new ManuscriptEntity();

        entity.setUniqueId(String.valueOf(String.join(",", manuscript.getManuscript()).hashCode()));
        entity.setContent(manuscript.getManuscript());
        return entity;
    }

    public Manuscript toDomain(ManuscriptEntity entity){
        return Manuscript.builder()
        .manuscript(entity.getContent())
        .build();
    }
}
