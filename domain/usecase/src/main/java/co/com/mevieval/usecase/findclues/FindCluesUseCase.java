package co.com.mevieval.usecase.findclues;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

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
    public Mono<Boolean> findClues(Manuscript m) {
        return Flux.fromIterable(m.getManuscript())
        .index()
                .flatMap(indexAndRow -> {
                    return Flux.fromArray(indexAndRow.getT2().chars()
                            .mapToObj(c -> (char) c)
                            .toArray(Character[]::new))
                            .index()
                            .flatMap(indexAndColumn -> {
                                //Reglas para disminuir las interaciones
                                Map<String, Boolean> rules = calculateRules(indexAndRow.getT1().intValue(),
                                        indexAndColumn.getT1().intValue(),
                                        m.getManuscript());
                                int i = indexAndRow.getT1().intValue();
                                int j = indexAndColumn.getT1().intValue();

                                char searchCriteria = m.getManuscript().get(i).charAt(j);

                                //Busquedas de Clue
                                return Flux.zip(triggerBottomRightOrientation(m, rules, i, j, searchCriteria),
                                        triggerRightOrientation(m, rules, i, j, searchCriteria),
                                        triggerBottomOrientation(m, rules, i, j, searchCriteria),
                                        triggerBottomLeftOrientation(m, rules, i, j, searchCriteria));
                            });
                })
                .doOnNext(zipOfSearching -> {
                    if(zipOfSearching.getT1() != 3 && zipOfSearching.getT2() != 3
                            && zipOfSearching.getT3() != 3 && zipOfSearching.getT4() != 3)
                        m.setNoClue(m.getNoClue() + 1);
                })
                .last()
                .flatMap(lastExec -> manuscriptRepository.save(m))
                .map(result -> m.getClue() != 0);
    }

    /**
     * Método para calcular estadisticas por manuscrito
     */

    public Mono<Object> calculateStats(Manuscript m){
        return manuscriptRepository.findManuscript(m);
    }

    private static Mono<Integer> triggerBottomRightOrientation(Manuscript m, Map<String, Boolean> rules, int i, int j, char searchCriteria) {

        List<Character> charactersToCheck = new ArrayList<>();

        if (Boolean.TRUE.equals(rules.get(BOTTOM_RIGHT_ORIENTATION))) {
            charactersToCheck = Arrays.asList(
                    m.getManuscript().get(i + 1).charAt(j + 1),
                    m.getManuscript().get(i + 2).charAt(j + 2),
                    m.getManuscript().get(i + 3).charAt(j + 3)
            );
        }

        return validateCharacters(m, searchCriteria, charactersToCheck);
    }

    private static Mono<Integer> triggerRightOrientation(Manuscript m, Map<String, Boolean> rules, int i, int j, char searchCriteria) {

        List<Character> charactersToCheck = new ArrayList<>();

        if (Boolean.TRUE.equals(rules.get(RIGHT_ORIENTATION))) {
            charactersToCheck = Arrays.asList(
                    m.getManuscript().get(i).charAt(j + 1),
                    m.getManuscript().get(i).charAt(j + 2),
                    m.getManuscript().get(i).charAt(j + 3)
            );
        }

        return validateCharacters(m, searchCriteria, charactersToCheck);
    }

    private static Mono<Integer> validateCharacters(Manuscript m, char searchCriteria, List<Character> charactersToCheck) {
        return Flux.fromIterable(charactersToCheck)
                .filter(elementPivote -> searchCriteria == elementPivote)
                .count()
                .flatMap(count -> {
                    if (count.intValue() == 3) {
                        m.setClue(m.getClue() + 1);
                    }
                    return Mono.just(count.intValue());
                })
                .switchIfEmpty(Mono.just(0));
    }

    private static Mono<Integer> triggerBottomOrientation(Manuscript m, Map<String, Boolean> rules, int i, int j, char searchCriteria) {
        List<Character> charactersToCheck = new ArrayList<>();

        if (Boolean.TRUE.equals(rules.get(BOTTOM_ORIENTATION))) {
            charactersToCheck = Arrays.asList(
                    m.getManuscript().get(i + 1).charAt(j),
                    m.getManuscript().get(i + 2).charAt(j),
                    m.getManuscript().get(i + 3).charAt(j)
            );
        }

        return validateCharacters(m, searchCriteria, charactersToCheck);
    }

    private static Mono<Integer> triggerBottomLeftOrientation(Manuscript m, Map<String, Boolean> rules, int i, int j, char searchCriteria) {
        List<Character> charactersToCheck = new ArrayList<>();

        if (Boolean.TRUE.equals(rules.get(BOTTOM_LEFT_ORIENTATION))) {
            charactersToCheck = Arrays.asList(
                    m.getManuscript().get(i + 1).charAt(j - 1),
                    m.getManuscript().get(i + 2).charAt(j - 2),
                    m.getManuscript().get(i + 3).charAt(j - 3)
            );
        }

        return validateCharacters(m, searchCriteria, charactersToCheck);
    }

    private Map<String, Boolean> calculateRules(int i, int j, List<String> manuscript) {
        Map<String, Boolean> rules = new LinkedHashMap<>();

        rules.put(BOTTOM_RIGHT_ORIENTATION, true);
        rules.put(BOTTOM_LEFT_ORIENTATION, true);
        rules.put(BOTTOM_ORIENTATION, true);
        rules.put(RIGHT_ORIENTATION, true);

        if (j < 3)
            rules.replace(BOTTOM_LEFT_ORIENTATION, false);

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
