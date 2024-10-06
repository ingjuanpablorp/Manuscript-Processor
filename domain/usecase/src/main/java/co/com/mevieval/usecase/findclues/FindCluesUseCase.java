package co.com.mevieval.usecase.findclues;

import java.util.Arrays;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FindCluesUseCase {

    /**
     * Orientation of ways in loop
     */
    static final String BOTTOM_RIGHT_ORIENTATION = "BR";
    static final String BOTTOM_LEFT_ORIENTATION = "BL";
    static final String BOTTOM_ORIENTATION = "BOTTOM";
    static final String TOP_RIGHT_ORIENTATION = "TR";
    static final String RIGHT_ORIENTATION = "RIGHT";
    static final String TOP_LEFT_ORIENTATION = "TL";
    static final String LEFT_ORIENTATION = "LEFT";
    static final String TOP_ORIENTATION = "UP";

    /**
     * Método para recorrer el flux de arreglos
     */
    public void findClues(){
        //TODO: Iniciar a recorrer cada fila

        String[] manuscript = {"RTHGQW", "XRLORE", "NARURR", "REVRAL", "EGSILE"};

        var a = Mono.just(manuscript);

        System.err.println(a.subscribe());
    }

}
