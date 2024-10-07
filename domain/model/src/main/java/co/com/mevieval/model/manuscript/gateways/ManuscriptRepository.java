package co.com.mevieval.model.manuscript.gateways;

import co.com.mevieval.model.manuscript.Manuscript;
import reactor.core.publisher.Mono;

public interface ManuscriptRepository {

    Mono<Void> save(Manuscript manuscript);

}
