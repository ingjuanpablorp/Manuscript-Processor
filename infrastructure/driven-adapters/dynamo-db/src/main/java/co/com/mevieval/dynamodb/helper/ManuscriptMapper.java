package co.com.mevieval.dynamodb.helper;

import co.com.mevieval.dynamodb.ManuscriptEntity;
import co.com.mevieval.model.manuscript.Manuscript;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public class ManuscriptMapper {
    
    public ManuscriptEntity toEntity(Manuscript manuscript){
        ManuscriptEntity entity = new ManuscriptEntity();

        System.err.println(manuscript.toString());

        entity.setUniqueId(String.valueOf(String.join(",", manuscript.getManuscript()).hashCode()));
        entity.setContent(manuscript.getManuscript());
        entity.setCountClueFound(manuscript.getClue());
        entity.setCountNoClue(manuscript.getNoClue());

        double total = manuscript.getClue() + manuscript.getNoClue();
        double ratio = total > 0 ? (double) manuscript.getClue() / total : 0.0;

        entity.setRatio(ratio);
        return entity;
    }

    public Manuscript toDomain(ManuscriptEntity entity){
        return Manuscript.builder()
        .manuscript(entity.getContent())
                .clue(entity.getCountClueFound())
                .noClue(entity.getCountNoClue())
                .ratio(entity.getRatio())
        .build();
    }
}
