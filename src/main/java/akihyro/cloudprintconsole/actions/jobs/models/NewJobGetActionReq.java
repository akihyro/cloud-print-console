package akihyro.cloudprintconsole.actions.jobs.models;

import javax.ws.rs.QueryParam;

import lombok.Data;

/**
 * ジョブ登録情報GETアクションリクエスト。
 */
@Data
public class NewJobGetActionReq {

    /** プリンタID */
    @QueryParam("printer-id")
    private String printerId;

}
