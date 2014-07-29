package akihyro.cloudprintconsole.service.model;

import lombok.Data;

/**
 * プリンタリストサービスレスポンス：プリンタ。
 */
@Data
public class PrintersServiceResPrinter {

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
