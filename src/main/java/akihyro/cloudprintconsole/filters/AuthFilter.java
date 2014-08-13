package akihyro.cloudprintconsole.filters;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

import akihyro.cloudprintconsole.actions.auth.AuthGetAction;
import akihyro.cloudprintconsole.actions.root.RootGetAction;
import akihyro.cloudprintconsole.api.CloudPrintApi;
import akihyro.cloudprintconsole.models.UserInfo;

/**
 * 認証フィルタ。
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
@ApplicationScoped
@Slf4j
public class AuthFilter implements ContainerRequestFilter {

    /** 認証不要リソースクラス */
    private static final Class<?>[] UNPROTECTED_RESOURCE_CLASSES = {
        RootGetAction.class,
        AuthGetAction.class,
    };

    /** リソース情報 */
    @Context
    private ResourceInfo resourceInfo;

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
        log.info("認証フィルタを初期化します。 => {}", this);
    }

    /**
     * 終了する。
     */
    @PreDestroy
    public void dispose() {
        log.info("認証フィルタを終了します。 => {}", this);
    }

    /**
     * リクエストをフィルタリングする。
     * 未認証の場合は認証用URIへリダイレクトする。
     *
     * @param context リクエストコンテキスト。
     * @throws IOException IOエラー。
     */
    @Override
    public void filter(ContainerRequestContext context) throws IOException {

        // 認証不要ならNOP
        val resourceClass = resourceInfo.getResourceClass();
        if (ArrayUtils.contains(UNPROTECTED_RESOURCE_CLASSES, resourceClass)) {
            return;
        }

        // 認証済みならNOP
        if (api.hasCredential(userInfo.getId())) {
            return;
        }

        // 認証後のリダイレクトURIを保存する
        //   => 基本は認証前のリクエストURI。
        //   => 認証アクションリクエスト時はリダイレクトループに陥るので除く。
        //   => GETメソッド以外はHTTPエンティティを中継しにくいので除く。
        val httpMethod = context.getMethod();
        val uriInfo = context.getUriInfo();
        if (!AuthGetAction.class.isAssignableFrom(resourceClass)
                && StringUtils.equalsIgnoreCase(httpMethod, HttpMethod.GET)) {
            userInfo.setRedirectUriForAuth(uriInfo.getRequestUri());
        } else {
            userInfo.setRedirectUriForAuth(uriInfo.getBaseUriBuilder().path(RootGetAction.class).build());
        }
        log.info("認証後のリダイレクトURIを保存しました。 => {}", userInfo.getRedirectUriForAuth());

        // 認証用URIへリダイレクトする
        val uri = api.newAuthCodeRequestUri();
        log.info("認証用URIへリダイレクトします。 => {}", uri);
        val response = Response.seeOther(uri).build();
        context.abortWith(response);

    }

}
