package co.com.mevieval.api;

import lombok.AllArgsConstructor;

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
    public ResponseEntity<Void> findClues(@RequestBody Manuscript manuscript){
        useCase.findClues(manuscript);
        return ResponseEntity.ok().build();
    }
}
