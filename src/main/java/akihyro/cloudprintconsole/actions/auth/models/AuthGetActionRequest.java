package akihyro.cloudprintconsole.actions.auth.models;

import javax.ws.rs.QueryParam;

import lombok.Data;

/**
 * 認証GETアクションリクエスト。
 */
@Data
public class AuthGetActionRequest {

    /** 認可コード */
    @QueryParam("code")
    private String authCode;

}
