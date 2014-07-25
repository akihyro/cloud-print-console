package akihyro.cloudprintconsole.api;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Cleanup;
import lombok.Data;
import lombok.Getter;
import lombok.val;

/**
 * API情報。
 */
@Data
@XmlRootElement(name = "cloud-print-api")
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

    /**
     * API-URIを取得する。
     *
     * @param name API名。
     * @return API-URI。
     */
    public URI getApiUri(String name) {
        return URI.create(getApiUri() + "/" + name);
    }

    /**
     * ロードする。
     *
     * @return API情報。
     * @throws IOException IOエラー。
     */
    public static CloudPrintApiInfo load() throws IOException {
        @Cleanup val resource = CloudPrintApiInfo.class.getResourceAsStream("/cloud-print-api-info.xml");
        return JAXB.unmarshal(resource, CloudPrintApiInfo.class);
    }

}
