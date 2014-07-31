package akihyro.cloudprintconsole.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * プリンタ検索APIレスポンス: プリンタ。
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CloudPrintApiSearchResPrinter {

    /** ID */
    @JsonProperty("id")
    private String id;

    /** 名前 */
    @JsonProperty("name")
    private String name;

    /** 表示名 */
    @JsonProperty("displayName")
    private String displayName;

    /** 説明 */
    @JsonProperty("description")
    private String description;

    /** 接続状態 */
    @JsonProperty("connectionStatus")
    private String connectionStatus;

}
