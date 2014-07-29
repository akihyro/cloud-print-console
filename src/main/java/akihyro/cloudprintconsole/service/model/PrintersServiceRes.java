package akihyro.cloudprintconsole.service.model;

import java.util.List;

import lombok.Data;

/**
 * プリンタリストサービスレスポンス。
 */
@Data
public class PrintersServiceRes {

    /** プリンタリスト */
    private List<PrintersServiceResPrinter> printers;

}
