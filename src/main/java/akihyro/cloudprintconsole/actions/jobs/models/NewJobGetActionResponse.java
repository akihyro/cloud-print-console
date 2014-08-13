package akihyro.cloudprintconsole.actions.jobs.models;

import lombok.Data;

/**
 * ジョブ登録情報GETアクションレスポンス。
 */
@Data
public class NewJobGetActionResponse {

    /** プリンタID */
    private String printerId;

}
