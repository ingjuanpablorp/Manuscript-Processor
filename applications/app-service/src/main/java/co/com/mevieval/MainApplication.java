package co.com.mevieval;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;

@SpringBootApplication
public class MainApplication {

    /**
     * Orientation of ways in loop
     */
    static final String BOTTOM_RIGHT_ORIENTATION = "BR";
    static final String BOTTOM_LEFT_ORIENTATION = "BL";
    static final String BOTTOM_ORIENTATION = "BOTTOM";
    static final String RIGHT_ORIENTATION = "RIGHT";

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);

        findClues();
    }

    /**
     * Método para recorrer el flux de arreglos
     */
    public static void findClues() {
        // TODO: Iniciar a recorrer cada fila

        String[] manuscript = { "RTHGQW", "XRLOWE", "NARWRE", "REWRAE", "XXXXLE" };

        Flux.just(manuscript)
                //.delayElements(Duration.ofMillis(2000))
                .index()
                .flatMap(strRow -> {
                    // String to char array
                    return Flux.fromArray(strRow.getT2().chars()
                            .mapToObj(c -> (char) c)
                            .toArray(Character[]::new))
                            .index()
                            .map(searchCriteria -> search(manuscript, strRow, searchCriteria));
                            //.delayElements(Duration.ofMillis(1000));
                })
                .blockLast();
    }

    /**
     * Método para orquestar las búsquedas por posición.
     * Acá se debe validar las reglas.
     * 
     * @param manuscript
     * @param strRow
     * @param searchCriteria
     * @return
     */
    private static String search(String[] manuscript, Tuple2<Long, String> strRow,
            Tuple2<Long, Character> searchCriteria) {

        Map<String, Boolean> pathsForSearch = new LinkedHashMap<>();

        int i = strRow.getT1().intValue();
        int j = searchCriteria.getT1().intValue();

        buildRules(manuscript, strRow, searchCriteria, pathsForSearch);

        // Init search by char
        Flux.just(1, 2, 3)
                //.doOnNext(s -> System.err.println(s))
                .map(iteration -> {                    
                    //triggerSearchBottomRight(manuscript, searchCriteria, pathsForSearch, i, j, countBROccurrences, iteration);
                    Flux<Character> bottomLeftSearch = triggerSearchBottomLeft(manuscript, searchCriteria, pathsForSearch, i, j, iteration);

                    return bottomLeftSearch;
                    //.count()
                    //.filter(s -> s > 0)
                    //.doOnNext(s -> System.err.println(s))
                    //.filter(counter -> counter == 3)
                    //.map(isClue -> true);
                    //.subscribe(s -> System.err.println(s));
                    //triggerSearchBottom(manuscript, searchCriteria, pathsForSearch, i, j, countBccurrences, iteration);
                    //triggerSearchRight(manuscript, searchCriteria, pathsForSearch, i, j, countROccurrences, iteration);

                    //return "Search Succesfull";
                })
                .doOnNext(s -> s
                .collectList()
                .map(list -> {
                    return list; 
                }
                )
                .doOnNext(e -> System.err.println(e.size()))
                )
                .map(m -> true)
                .subscribe();

                return "";
    }

    private static void triggerSearchRight(String[] manuscript, Tuple2<Long, Character> searchCriteria,
            Map<String, Boolean> pathsForSearch, int i, int j, AtomicInteger countROccurrences, Integer iteration) {
        Flux<String> rightSearch = Flux.empty();
        if (pathsForSearch.get(RIGHT_ORIENTATION)) {
            rightSearch = Flux.just(manuscript[i].charAt(j + iteration))
                    .filter(element -> element == searchCriteria.getT2())
                    .doOnNext(value -> countROccurrences.incrementAndGet())
                    .filter(isAClue -> countROccurrences.get() == 3)
                    .map(a -> "(" + i + ", " + j + ") exist manuscript in " + RIGHT_ORIENTATION
                            + " orientation with character: " + searchCriteria.getT2() + Thread.currentThread().getName());
        }
        rightSearch.subscribeOn(Schedulers.parallel())
        .subscribe(result -> System.err.println(result));
    }

    private static void triggerSearchBottom(String[] manuscript, Tuple2<Long, Character> searchCriteria,
            Map<String, Boolean> pathsForSearch, int i, int j, AtomicInteger countBccurrences, Integer iteration) {

        Flux<String> bottomSearch = Flux.empty();

        if(pathsForSearch.get(BOTTOM_ORIENTATION)){
            bottomSearch = Flux.just(manuscript[i + iteration].charAt(j))
            .filter(element -> element == searchCriteria.getT2())
            .doOnNext(value -> countBccurrences.getAndIncrement())
            .filter(isAClue -> countBccurrences.get() == 3)
            .map(a -> "(" + i + ", " + j + ") exist manuscript in " + BOTTOM_ORIENTATION
            + " orientation with character: " + searchCriteria.getT2() + Thread.currentThread().getName());
        }
        bottomSearch.subscribeOn(Schedulers.parallel())
        .subscribe();
    }

    private static Flux<Character> triggerSearchBottomLeft(String[] manuscript, Tuple2<Long, Character> searchCriteria,
            Map<String, Boolean> pathsForSearch, int i, int j, Integer iteration) {

        return pathsForSearch.get(BOTTOM_LEFT_ORIENTATION) 
        ? Flux.just(manuscript[i + iteration].charAt(j - iteration))
        .filter(element -> element == searchCriteria.getT2())
        .doOnNext(a -> System.err.print(" \n Hay "+a+" en la posición: "+ (i + iteration) + "," + (j-iteration) + " Estoy buscando: " + searchCriteria.getT2() + " desde la posición "+ i + ", " + j))
        : Flux.empty();

        //bottomLeftSearch
        //.doOnNext(a -> System.err.print(" Y llevo " + a + " encuentros."))
        //.filter(isAClue -> isAClue == 3)
        //.map(a -> "(" + i + ", " + j + ") exist manuscript in " + BOTTOM_LEFT_ORIENTATION
        //+ " orientation with character: " + searchCriteria.getT2() + Thread.currentThread().getName())
        //.subscribeOn(Schedulers.parallel())
        //.subscribe();
    }

    private static void triggerSearchBottomRight(String[] manuscript, Tuple2<Long, Character> searchCriteria,
            Map<String, Boolean> pathsForSearch, int i, int j, AtomicInteger countBROccurrences, Integer iteration) {
        Flux<String> bottomRightSearch = Flux.empty();

        if (pathsForSearch.get(BOTTOM_RIGHT_ORIENTATION)) {
            bottomRightSearch = Flux.just(manuscript[i + iteration].charAt(j + iteration))
                    .filter(element -> element == searchCriteria.getT2())
                    .doOnNext(value -> countBROccurrences.incrementAndGet())
                    .filter(isAClue -> countBROccurrences.get() == 3)
                    .map(a ->  "(" + i + ", " + j + ") exist manuscript in " + BOTTOM_RIGHT_ORIENTATION
                                + " orientation with character: " + searchCriteria.getT2() + Thread.currentThread().getName());
        }

        bottomRightSearch.subscribeOn(Schedulers.parallel())
        .subscribe(result -> System.err.println(result));
    }

    private static void buildRules(String[] manuscript, Tuple2<Long, String> strRow,
            Tuple2<Long, Character> searchCriteria, Map<String, Boolean> pathsForSearch) {

        pathsForSearch.put(BOTTOM_RIGHT_ORIENTATION, false);
        pathsForSearch.put(BOTTOM_LEFT_ORIENTATION, true);
        pathsForSearch.put(BOTTOM_ORIENTATION, false);
        pathsForSearch.put(RIGHT_ORIENTATION, false);

        // Rule 1
        if(searchCriteria.getT1() < 3){
            pathsForSearch.replace(BOTTOM_LEFT_ORIENTATION, false);
        }

        // Rule 2
        if (searchCriteria.getT1() + 3 > strRow.getT2().length() - 1) {
            pathsForSearch.replace(RIGHT_ORIENTATION, false);
            pathsForSearch.replace(BOTTOM_RIGHT_ORIENTATION, false);
        }

        // Rule 4
        if (strRow.getT1() + 3 > manuscript.length - 1) {
            pathsForSearch.replace(BOTTOM_LEFT_ORIENTATION, false);
            pathsForSearch.replace(BOTTOM_RIGHT_ORIENTATION, false);
            pathsForSearch.replace(BOTTOM_ORIENTATION, false);
        }

        //System.err.println("For " + searchCriteria.getT2() + " have this rules: " + pathsForSearch.toString());
    }

}
