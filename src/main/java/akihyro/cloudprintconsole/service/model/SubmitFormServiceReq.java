package akihyro.cloudprintconsole.service.model;

import javax.ws.rs.QueryParam;

import lombok.Data;

/**
 * サブミットフォームサービスリクエスト。
 */
@Data
public class SubmitFormServiceReq {

    /** プリンタID */
    @QueryParam("printer-id")
    private String printerId;

}
