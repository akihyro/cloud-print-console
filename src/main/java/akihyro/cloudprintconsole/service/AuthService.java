package akihyro.cloudprintconsole.service;

import java.net.URI;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import akihyro.cloudprintconsole.CloudPrintConsoleSession;
import akihyro.cloudprintconsole.api.CloudPrintApi;

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

    /** API */
    @Inject
    CloudPrintApi api;

    /** セッション */
    @Inject
    CloudPrintConsoleSession session;

    /** セッションID */
    @CookieParam("JSESSIONID")
    private String sessionId;

    /**
     * 認証する。
     *
     * @return 認証ページへリダイレクト。
     * @throws Exception エラー。
     */
    @GET
    public Response auth() throws Exception {

        // セッションIDを保持する
        session.setId(sessionId);

        // リダイレクト先を決定する
        URI redirectURI = null;
        if (api.hasCredential(session.getId())) {
            // セッションが認証済ならログインする
            redirectURI = uriInfo.getBaseUriBuilder().path(LoginService.class).build();
        } else {
            // 未認証なら認可コードをリクエストする
            redirectURI = api.newAuthCodeReqURI();
        }

        return Response.seeOther(redirectURI).build();
    }

}
