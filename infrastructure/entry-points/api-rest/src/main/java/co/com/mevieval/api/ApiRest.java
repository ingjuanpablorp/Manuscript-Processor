package co.com.mevieval.api;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.mevieval.model.manuscript.Manuscript;
import co.com.mevieval.usecase.findclues.FindCluesUseCase;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ApiRest {
    private final FindCluesUseCase useCase;

    @PostMapping("/clue")
    public Mono<ResponseEntity<Void>> findClues(@RequestBody Manuscript manuscript){
        return useCase.findClues(manuscript)
        .map(result -> result ? ResponseEntity.ok().<Void>build() : ResponseEntity.status(HttpStatus.FORBIDDEN).<Void>body(null))
        .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<Void>build());
    }

    @PostMapping("/stats")
    public Mono<ResponseEntity<?>> getStatsManuscript(@RequestBody Manuscript manuscript){
        return useCase.calculateStats(manuscript)
                .map(result -> result != null ? ResponseEntity.ok().body(result) : ResponseEntity.noContent().<Void>build())
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<Void>build());
    }

    /**
     * TODO: crear un endpoint para acceder a unas estadisticas generales de
     * las búsquedas.
     */
}
