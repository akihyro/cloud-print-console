package akihyro.cloudprintconsole.api.models;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import lombok.Data;
import lombok.val;

import akihyro.cloudprintconsole.api.CloudPrintApiInfo;

/**
 * サブミットAPIリクエスト。
 */
@Data
public class CloudPrintApiSubmitRequest implements CloudPrintApiRequest<CloudPrintApiSubmitResponse> {

    /** プリンタID */
    private String printerId;

    /** タイトル */
    private String title;

    /** コンテンツタイプ */
    private String contentType;

    /** コンテンツ */
    private InputStream content;

    /**
     * インターフェイス名を取得する。
     *
     * @return インターフェイス名。
     */
    @Override
    public String getInterfaceName() {
        return "/submit";
    }

    /**
     * レスポンスクラスを取得する。
     *
     * @return レスポンスクラス。
     */
    @Override
    public Class<CloudPrintApiSubmitResponse> getResponseType() {
        return CloudPrintApiSubmitResponse.class;
    }

    /**
     * HTTPリクエストに変換する。
     *
     * @param apiInfo API情報。
     * @return HTTPリクエスト。
     */
    @Override
    public HttpUriRequest toHttpRequest(CloudPrintApiInfo apiInfo) {
        val builder = RequestBuilder.post();
        builder.setUri(apiInfo.getApiUri(getInterfaceName()));
        builder.setEntity(toHttpEntity());
        return builder.build();
    }

    /**
     * HTTPエンティティに変換する。
     *
     * @return HTTPエンティティ。
     */
    protected HttpEntity toHttpEntity() {
        val builder = MultipartEntityBuilder.create();
        builder.addTextBody("printerid", printerId);
        builder.addTextBody("title", title);
        builder.addTextBody("contentType", contentType);
        builder.addBinaryBody("content", content);
        return builder.build();
    }

}
