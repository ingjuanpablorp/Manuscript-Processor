package co.com.mevieval.model.manuscript.gateways;

import co.com.mevieval.model.manuscript.Manuscript;
import reactor.core.publisher.Mono;

public interface ManuscriptRepository {

    Mono<Boolean> save(Manuscript manuscript);

}
