package akihyro.cloudprintconsole.api.models;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;

import lombok.Data;
import lombok.val;

import akihyro.cloudprintconsole.api.CloudPrintApiInfo;

/**
 * プリンタ検索APIリクエスト。
 */
@Data
public class CloudPrintApiSearchReq implements CloudPrintApiReq<CloudPrintApiSearchRes> {

    /** クエリ */
    private String query;

    /**
     * インターフェイス名を取得する。
     *
     * @return インターフェイス名。
     */
    @Override
    public String getInterfaceName() {
        return "/search";
    }

    /**
     * レスポンスクラスを取得する。
     *
     * @return レスポンスクラス。
     */
    @Override
    public Class<CloudPrintApiSearchRes> getResType() {
        return CloudPrintApiSearchRes.class;
    }

    /**
     * HTTPリクエストに変換する。
     *
     * @param apiInfo API情報。
     * @return HTTPリクエスト。
     */
    @Override
    public HttpUriRequest toHttpReq(CloudPrintApiInfo apiInfo) {
        val builder = RequestBuilder.post();
        builder.setUri(apiInfo.getApiUri(getInterfaceName()));
        builder.addParameter("q", query);
        return builder.build();
    }

}
