package akihyro.cloudprintconsole.actions.auth;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

import akihyro.cloudprintconsole.actions.auth.models.AuthGetActionRequest;
import akihyro.cloudprintconsole.api.CloudPrintApi;
import akihyro.cloudprintconsole.models.UserInfo;

/**
 * 認証GETアクション。
 */
@ApplicationScoped
@Path("auth")
@Slf4j
public class AuthGetAction {

    /** API */
    @Inject
    private CloudPrintApi api;

    /** ユーザ情報 */
    @Inject
    private UserInfo userInfo;

    /**
     * 初期化する。
     */
    @PostConstruct
    public void init() {
        log.info("認証GETアクションを初期化します。 => {}", this);
    }

    /**
     * 終了する。
     */
    @PreDestroy
    public void dispose() {
        log.info("認証GETアクションを終了します。 => {}", this);
    }

    /**
     * 認証する。
     *
     * @param request リクエスト。
     * @return リダイレクト。
     * @throws Exception エラー。
     */
    @GET
    public Response get(@BeanParam AuthGetActionRequest request) throws Exception {

        // 未認証なら認証情報を取得/保存する
        if (!api.hasCredential(userInfo.getId())) {
            api.storeCredential(userInfo.getId(), request.getAuthCode());
        }

        // リダイレクトする
        val redirectUri = userInfo.getRedirectUriForAuth();
        log.info("リダイレクトします。 => {}", redirectUri);
        return Response.seeOther(redirectUri).build();
    }

}
