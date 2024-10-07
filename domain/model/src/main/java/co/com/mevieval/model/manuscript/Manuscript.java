package co.com.mevieval.model.manuscript;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Manuscript implements Serializable{
    private List<String> manuscript;
}