package akihyro.cloudprintconsole.service.model;

import javax.ws.rs.QueryParam;

import lombok.Data;

/**
 * ログインサービスリクエスト。
 */
@Data
public class LoginServiceReq {

    /** 認可コード */
    @QueryParam("code")
    private String authCode;

}
