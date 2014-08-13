package akihyro.cloudprintconsole.models;

import java.net.URI;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import akihyro.cloudprintconsole.filters.UriInfoGrabFilter;

/**
 * URI情報。
 * {@link javax.ws.rs.core.UriInfo} をラップしたもの。
 * {@link javax.ws.rs.core.UriInfo} は {@link UriInfoGrabFilter} で捕捉する。
 * URI情報の参照をJSPからも可能にする為、リクエストスコープに保存する。
 */
@Named
@RequestScoped
@Data
@Slf4j
public class UriInfo {

    /** URI情報 */
    private javax.ws.rs.core.UriInfo uriInfo;

    /**
     * 初期化する。
     */
    @PostConstruct
    public void init() {
        log.info("URI情報を初期化します。 => {}", this);
    }

    /**
     * 終了する。
     */
    @PreDestroy
    public void dispose() {
        log.info("URI情報を終了します。 => {}", this);
    }

    /**
     * 文字列に変換する。
     *
     * @return 文字列。
     */
    @Override
    public String toString() {
        if (uriInfo == null) {
            return "(uninitialized)";
        }
        return uriInfo.getRequestUri().toString();
    }

    /**
     * アプリケーションベースで相対URIを解決する。
     *
     * @param uri 相対URI。
     * @return URI。
     */
    public URI resolve(String uri) {
        return resolveUri(URI.create(uri));
    }

    /**
     * アプリケーションベースで相対URIを解決する。
     *
     * @param uri 相対URI。
     * @return URI。
     */
    public URI resolveUri(URI uri) {
        return uriInfo.resolve(uri);
    }

    /**
     * リソースクラスのURIを解決する。
     *
     * @param resourceClass リソースクラス。
     * @return URI。
     */
    public URI resolveResourceClass(Class<?> resourceClass) {
        return uriInfo.getBaseUriBuilder().path(resourceClass).build();
    }

}
