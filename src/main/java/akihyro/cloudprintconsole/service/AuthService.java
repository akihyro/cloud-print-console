package akihyro.cloudprintconsole.service;

import java.net.URI;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import akihyro.cloudprintconsole.CloudPrintConsoleSession;
import akihyro.cloudprintconsole.api.CloudPrintFacade;

/**
 * 認証サービス。
 */
@Named
@RequestScoped
@Path("/auth")
public class AuthService {

    /** URI情報 */
    @Context
    UriInfo uriInfo;

    /** ファサード */
    @Inject
    CloudPrintFacade facade;

    /** セッション */
    @Inject
    CloudPrintConsoleSession session;

    /**
     * 認証する。
     *
     * @return 認証ページへリダイレクト。
     * @throws Exception エラー。
     */
    @GET
    public Response auth() throws Exception {

        // リダイレクト先を決定する
        URI redirectURI = null;
        if (session.hasCredential()) {
            // セッションが認証済ならログインする
            redirectURI = uriInfo.getBaseUriBuilder().path(LoginService.class).build();
        } else {
            // 未認証なら認可コードをリクエストする
            redirectURI = facade.newAuthCodeReqURI();
        }

        return Response.seeOther(redirectURI).build();
    }

}