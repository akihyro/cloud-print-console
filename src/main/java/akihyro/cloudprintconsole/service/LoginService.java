package akihyro.cloudprintconsole.service;


import java.net.URI;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import akihyro.cloudprintconsole.CloudPrintConsoleSession;
import akihyro.cloudprintconsole.api.CloudPrintApi;
import akihyro.cloudprintconsole.service.model.LoginServiceReq;

/**
 * ログインサービス。
 */
@RequestScoped
@Path("/login")
public class LoginService extends BaseService {

    /** URI情報 */
    @Context
    UriInfo uriInfo;

    /** API */
    @Inject
    CloudPrintApi api;

    /** セッション */
    @Inject
    CloudPrintConsoleSession session;

    /** リクエスト */
    @BeanParam
    LoginServiceReq req;

    /**
     * ログインする。
     *
     * @return プリンタリストへリダイレクト。
     * @throws Exception エラー。
     */
    @GET
    public Response login() throws Exception {

        // 未認証なら認証情報を取得/保存する
        if (!api.hasCredential(session.getId())) {
            api.storeCredential(session.getId(), req.getAuthCode());
        }

        // プリンタリストへリダイレクトする
        URI redirectURI = uriInfo.getBaseUriBuilder().path(PrintersService.class).build();
        return Response.seeOther(redirectURI).build();
    }

}
