package akihyro.cloudprintconsole.api.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * プリンタ検索APIレスポンス。
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CloudPrintApiSearchResponse implements CloudPrintApiResponse {

    /** プリンタリスト */
    @JsonProperty("printers")
    private List<CloudPrintApiSearchResponsePrinter> printers;

}
