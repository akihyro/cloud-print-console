package akihyro.cloudprintconsole.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * プリンタリスト。
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Printers {

    /** プリンタリスト */
    private List<Printer> printers;

}
