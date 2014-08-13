package akihyro.cloudprintconsole.filters;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import lombok.extern.slf4j.Slf4j;

import akihyro.cloudprintconsole.models.UriInfo;

/**
 * URI情報捕捉フィルタ。
 */
@Provider
@Priority(Priorities.USER)
@ApplicationScoped
@Slf4j
public class UriInfoGrabFilter implements ContainerRequestFilter {

    /** URI情報 */
    @Inject
    protected UriInfo uriInfo;

    /**
     * 初期化する。
     */
    @PostConstruct
    public void init() {
        log.info("URI情報捕捉フィルタを初期化します。 => {}", this);
    }

    /**
     * 終了する。
     */
    @PreDestroy
    public void dispose() {
        log.info("URI情報捕捉フィルタを終了します。 => {}", this);
    }

    /**
     * リクエストをフィルタリングする。
     * URI情報をセットする。
     *
     * @param context リクエストコンテキスト。
     * @throws IOException IOエラー。
     */
    @Override
    public void filter(ContainerRequestContext context) throws IOException {
        uriInfo.setUriInfo(context.getUriInfo());
        log.info("URI情報をセットしました。 => {}", uriInfo);
    }

}
