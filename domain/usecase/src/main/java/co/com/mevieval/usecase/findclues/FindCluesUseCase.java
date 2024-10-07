package co.com.mevieval.usecase.findclues;

import lombok.RequiredArgsConstructor;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import co.com.mevieval.model.manuscript.Manuscript;
import co.com.mevieval.model.manuscript.gateways.ManuscriptRepository;

@RequiredArgsConstructor
public class FindCluesUseCase {

    private final ManuscriptRepository manuscriptRepository;

    /**
     * Orientation of ways in loop
     */
    static final String BOTTOM_RIGHT_ORIENTATION = "BR";
    static final String BOTTOM_LEFT_ORIENTATION = "BL";
    static final String BOTTOM_ORIENTATION = "BOTTOM";
    static final String RIGHT_ORIENTATION = "RIGHT";

    /**
     * Método para recorrer el flux de arreglos
     */
    public void findClues(Manuscript m) {

        /**
         * TODO: Cambia cuando migre la impl imperativa a reactiva
         */
        manuscriptRepository.save(m).subscribe();

        var manuscript = m.getManuscript();
        
        boolean existManuscripts = false;

        for (int i = 0; i < manuscript.size(); i++) {
            for (int j = 0; j < manuscript.get(i).length(); j++) {
                if (search(i, j, manuscript, manuscript.get(i).charAt(j))) {
                    existManuscripts = true;
                }
            }
        }

        if (existManuscripts)
            System.err.println("Clues Found");
    }

    private boolean search(int i, int j, List<String> manuscript, char character) {

        Map<String, Boolean> rules = calculateRules(i, j, manuscript);
        boolean existBottomRight = false;
        boolean existBottomLeft = false;
        boolean existBottom = false;
        boolean existRight = false;

        if (rules.get(BOTTOM_RIGHT_ORIENTATION)) {
            existBottomRight = existManuscriptInBottomRight(i, j, manuscript, character);
        }

        if (rules.get(BOTTOM_LEFT_ORIENTATION)) {
            existBottomLeft = existManuscriptInBottomLeft(i, j, manuscript, character);
        }

        if (rules.get(BOTTOM_ORIENTATION)) {
            existBottom = existManuscriptInBottom(i, j, manuscript, character);
        }

        if (rules.get(RIGHT_ORIENTATION)) {
            existRight = existManuscriptInRight(i, j, manuscript, character);
        }

        if (existBottomLeft)
            System.err.println("existe manuscripto en el recorrido abajo izquierda con el caracter: " + character);
        if (existBottomRight)
            System.err.println("existe manuscrito en el recorrido abajo derecha con el caracter: " + character);
        if (existBottom)
            System.err.println("existe manuscrito en el recorrido abajo con el caracter: " + character);
        if (existRight)
            System.err.println("existe manuscrito en el recorrido derecha con el caracter: " + character);

        return existBottomLeft || existBottomRight || existBottom || existRight;
        // Si la ocurrencia es 3 retorne true y guarde en base de datos.
    }

    private boolean existManuscriptInRight(int i, int j, List<String> manuscript, char character) {
        int counterOfOcurrences = 0;

        for (int k = 1; k <= 3; k++) {
            if (manuscript.get(i).charAt(j + k) == character) {
                counterOfOcurrences++;
                if (k == 3)
                    return counterOfOcurrences == 3;
            } else {
                return false;
            }
        }
        return false;
    }

    private boolean existManuscriptInBottom(int i, int j, List<String> manuscript, char character) {
        int counterOfOcurrences = 0;

        for (int k = 1; k <= 3; k++) {
            if (manuscript.get(i + k).charAt(j) == character) {
                counterOfOcurrences++;
                if (k == 3)
                    return counterOfOcurrences == 3;
            } else {
                return false;
            }
        }
        return false;
    }

    private boolean existManuscriptInBottomLeft(int i, int j, List<String> manuscript, char character) {
        int counterOfOcurrences = 0;

        for (int k = 1; k <= 3; k++) {
            if (manuscript.get(i + k).charAt(j - k) == character) {
                counterOfOcurrences++;
                if (k == 3)
                    return counterOfOcurrences == 3;
            } else {
                return false;
            }
        }
        return false;
    }

    private boolean existManuscriptInBottomRight(int i, int j, List<String> manuscript, char character) {
        int counterOfOcurrences = 0;

        for (int k = 1; k <= 3; k++) {
            if (manuscript.get(i + k).charAt(j + k) == character) {
                counterOfOcurrences++;
                if (k == 3)
                    return counterOfOcurrences == 3;
            } else {
                return false;
            }
        }
        return false;
    }

    private Map<String, Boolean> calculateRules(int i, int j, List<String> manuscript) {
        Map<String, Boolean> rules = new LinkedHashMap<>();

        rules.put(BOTTOM_RIGHT_ORIENTATION, true);
        rules.put(BOTTOM_LEFT_ORIENTATION, true);
        rules.put(BOTTOM_ORIENTATION, true);
        rules.put(RIGHT_ORIENTATION, true);

        if (j < 3) {
            rules.replace(BOTTOM_LEFT_ORIENTATION, false);
        }
        if (j + 3 > manuscript.get(i).length() - 1) {
            rules.replace(RIGHT_ORIENTATION, false);
            rules.replace(BOTTOM_RIGHT_ORIENTATION, false);
        }
        if (i + 3 > manuscript.size() - 1) {
            rules.replace(BOTTOM_LEFT_ORIENTATION, false);
            rules.replace(BOTTOM_RIGHT_ORIENTATION, false);
            rules.replace(BOTTOM_ORIENTATION, false);
        }
        return rules;
    }
}
