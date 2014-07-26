package akihyro.cloudprintconsole.api.model;

import org.apache.http.client.methods.HttpUriRequest;

import akihyro.cloudprintconsole.api.CloudPrintApiInfo;

/**
 * APIリクエスト。
 *
 * @param <ResType> レスポンスクラス。
 */
public interface CloudPrintApiReq<ResType extends CloudPrintApiRes> {

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
    public Class<ResType> getResType();

    /**
     * HTTPリクエストに変換する。
     *
     * @param apiInfo API情報。
     * @return HTTPリクエスト。
     */
    public HttpUriRequest toHttpReq(CloudPrintApiInfo apiInfo);

}
