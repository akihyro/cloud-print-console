package akihyro.cloudprintconsole.actions.printers.models;

import java.util.List;

import lombok.Data;

/**
 * プリンタリストGETアクションレスポンス。
 */
@Data
public class PrintersGetActionResponse {

    /** プリンタリスト */
    private List<PrintersGetActionResponsePrinter> printers;

}
