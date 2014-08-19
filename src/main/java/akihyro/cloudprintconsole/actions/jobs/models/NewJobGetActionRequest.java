package akihyro.cloudprintconsole.actions.jobs.models;

import javax.ws.rs.QueryParam;

import lombok.Data;

import akihyro.cloudprintconsole.validation.constraints.PrinterId;

/**
 * ジョブ登録情報GETアクションリクエスト。
 */
@Data
public class NewJobGetActionRequest {

    /** プリンタID */
    @PrinterId
    @QueryParam("printer-id")
    private String printerId;

}
