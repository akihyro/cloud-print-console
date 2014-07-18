package akihyro.cloudprintconsole.service;


import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import akihyro.cloudprintconsole.CloudPrintConsoleSession;
import akihyro.cloudprintconsole.api.CloudPrintFacade;
import lombok.Getter;
import lombok.Setter;

/**
 * ログインサービス。
 */
@Named
@RequestScoped
@Path("/login")
public class LoginService {

    /** ファサード */
    @Inject
    CloudPrintFacade facade;

    /** セッション */
    @Inject
    CloudPrintConsoleSession session;

    /** 認可コード */
    @QueryParam("code")
    @Getter @Setter
    private String authCode;

    /**
     * ログインする。
     *
     * @return レスポンス。
     * @throws Exception エラー。
     */
    @GET
    public String login() throws Exception {

        // 未認証なら認証情報を得る
        if (!session.hasCredential()) {
            session.setCredential(facade.takeCredential(authCode));
        }

        // TODO: 取り敢えず認証情報を返しとく
        return session.getCredential().toString();
    }

}
