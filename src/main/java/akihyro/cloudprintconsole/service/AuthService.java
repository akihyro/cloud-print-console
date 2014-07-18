package akihyro.cloudprintconsole.service;

import java.net.URI;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import akihyro.cloudprintconsole.api.CloudPrintFacade;

/**
 * 認証サービス。
 */
@Named
@RequestScoped
@Path("/auth")
public class AuthService {

    /** ファサード */
    @Inject
    CloudPrintFacade facade;

    /**
     * 認証する。
     *
     * @return 認証ページへリダイレクト。
     * @throws Exception エラー。
     */
    @GET
    public Response auth() throws Exception {
        URI redirectURI = facade.newAuthCodeReqURI();
        return Response.seeOther(redirectURI).build();
    }

}
