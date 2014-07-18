package akihyro.cloudprintconsole.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * プリンタ。
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Printer {

    /** ID */
    private String id;

    /** 名前 */
    private String name;

    /** 表示名 */
    private String displayName;

    /** 説明 */
    private String description;

    /** 接続状態 */
    private String connectionStatus;

}
