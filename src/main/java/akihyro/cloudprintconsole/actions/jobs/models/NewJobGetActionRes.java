package akihyro.cloudprintconsole.actions.jobs.models;

import lombok.Data;

/**
 * ジョブ登録GETアクションレスポンス。
 */
@Data
public class NewJobGetActionRes {

    /** プリンタID */
    private String printerId;

}