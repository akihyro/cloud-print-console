package akihyro.cloudprintconsole.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * プリンタ検索APIレスポンス。
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CloudPrintApiSearchRes implements CloudPrintApiRes {

    /** プリンタリスト */
    @JsonProperty("printers")
    private List<CloudPrintApiSearchResPrinter> printers;

}
