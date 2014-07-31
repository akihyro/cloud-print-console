package akihyro.cloudprintconsole.actions.printers.models;

import java.util.List;

import lombok.Data;

/**
 * プリンタリストGETアクションレスポンス。
 */
@Data
public class PrintersGetActionRes {

    /** プリンタリスト */
    private List<PrintersGetActionResPrinter> printers;

}
