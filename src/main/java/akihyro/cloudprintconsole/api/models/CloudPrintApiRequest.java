package akihyro.cloudprintconsole.api.models;

import org.apache.http.client.methods.HttpUriRequest;

import akihyro.cloudprintconsole.api.CloudPrintApiInfo;

/**
 * Cloud Print API リクエスト。
 *
 * @param <ResponseType> レスポンスクラス。
 */
public interface CloudPrintApiRequest<ResponseType extends CloudPrintApiResponse> {

    /**
     * インターフェイス名を取得する。
     *
     * @return インターフェイス名。
     */
    public String getInterfaceName();

    /**
     * レスポンスクラスを取得する。
     *
     * @return レスポンスクラス。
     */
    public Class<ResponseType> getResponseType();

    /**
     * HTTPリクエストに変換する。
     *
     * @param apiInfo API情報。
     * @return HTTPリクエスト。
     */
    public HttpUriRequest toHttpRequest(CloudPrintApiInfo apiInfo);

}
