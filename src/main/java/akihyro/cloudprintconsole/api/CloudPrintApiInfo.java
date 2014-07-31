package akihyro.cloudprintconsole.api;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.util.List;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.http.HttpHost;

import lombok.AccessLevel;
import lombok.Cleanup;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * Cloud Print API 情報。
 */
@XmlRootElement(name = "cloud-print-api")
@Data
@Setter(AccessLevel.PROTECTED)
@ToString(exclude = { "clientId", "clientSecret" })
@Slf4j
public class CloudPrintApiInfo {

    /** クライアントID */
    @Getter(onMethod = @__({
        @XmlElement(name = "client-id"),
    }) )
    private String clientId;

    /** クライアントシークレット */
    @Getter(onMethod = @__({
        @XmlElement(name = "client-secret"),
    }) )
    private String clientSecret;

    /** 認証URI */
    @Getter(onMethod = @__({
        @XmlElement(name = "auth-uri"),
    }) )
    private URI authUri;

    /** トークンURI */
    @Getter(onMethod = @__({
        @XmlElement(name = "token-uri"),
    }) )
    private URI tokenUri;

    /** スコープリスト */
    @Getter(onMethod = @__({
        @XmlElementWrapper(name = "scopes"),
        @XmlElement(name = "scope"),
    }) )
    private List<String> scopes;

    /** リダイレクトURI */
    @Getter(onMethod = @__({
        @XmlElement(name = "redirect-uri"),
    }) )
    private URI redirectUri;

    /**  API-URI */
    @Getter(onMethod = @__({
        @XmlElement(name = "api-uri"),
    }) )
    private URI apiUri;

    /** プロキシ情報 */
    @Getter(onMethod = @__({
        @XmlElement(name = "proxy"),
    }) )
    private ProxyInfo proxyInfo;

    /**
     * API-URIを取得する。
     *
     * @param name API名。
     * @return API-URI。
     */
    public URI getApiUri(String name) {
        return URI.create(getApiUri() + name);
    }

    /**
     * プロキシを取得する。
     *
     * @return プロキシ。
     */
    public Proxy getProxy() {
        if (proxyInfo == null) {
            return null;
        }
        return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyInfo.getHost(), proxyInfo.getPort()));
    }

    /**
     * プロキシHTTPホストを取得する。
     *
     * @return プロキシHTTPホスト。
     */
    public HttpHost getProxyHttpHost() {
        if (proxyInfo == null) {
            return null;
        }
        return new HttpHost(proxyInfo.getHost(), proxyInfo.getPort());
    }

    /**
     * ロードする。
     *
     * @return API情報。
     * @throws IOException IOエラー。
     */
    public static CloudPrintApiInfo load() throws IOException {
        log.info("Cloud Print API 情報をロードします。");
        @Cleanup val resource = CloudPrintApiInfo.class.getResourceAsStream("/cloud-print-api-info.xml");
        val apiInfo = JAXB.unmarshal(resource, CloudPrintApiInfo.class);
        log.info("Cloud Print API 情報をロードしました。 => {}", apiInfo);
        log.debug("Cloud Print API クライアントID: {}", apiInfo.getClientId());
        log.debug("Cloud Print API クライアントシークレット: {}", apiInfo.getClientSecret());
        return apiInfo;
    }

    /**
     * Cloud Print API プロキシ情報。
     */
    @XmlRootElement(name = "proxy")
    @Data
    @Setter(AccessLevel.PROTECTED)
    public static class ProxyInfo {

        /** ホスト */
        @Getter(onMethod = @__({
            @XmlElement(name = "host"),
        }) )
        private String host;

        /** ポート */
        @Getter(onMethod = @__({
            @XmlElement(name = "port"),
        }) )
        private Integer port;

    }

}
