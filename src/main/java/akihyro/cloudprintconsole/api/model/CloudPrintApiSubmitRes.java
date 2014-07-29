package akihyro.cloudprintconsole.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * サブミットAPIレスポンス。
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CloudPrintApiSubmitRes implements CloudPrintApiRes {

    /** 結果 */
    @JsonProperty("success")
    private boolean success;

    /** メッセージ */
    @JsonProperty("message")
    private String message;

}
