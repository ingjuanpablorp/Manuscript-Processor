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
        .map(result -> result ? ResponseEntity.ok().<Void>build() : ResponseEntity.badRequest().<Void>build())
        .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<Void>build());
    }
}
