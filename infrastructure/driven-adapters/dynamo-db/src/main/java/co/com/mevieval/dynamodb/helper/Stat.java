package co.com.mevieval.dynamodb.helper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Stat {
    int count_clue_found;
    int count_no_clue;
    double ratio;
}
