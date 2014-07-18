package akihyro.cloudprintconsole.api;

import java.io.Serializable;

import com.google.api.client.auth.oauth2.Credential;

import lombok.Data;

/**
 * 認証情報。
 */
@Data
public class CloudPrintCredential implements Serializable {

    /** シリアルバージョンUID */
    private static final long serialVersionUID = -3612672677686399852L;

    /** アクセストークン */
    private String accessToken;

    /**
     * コンストラクタ。
     */
    public CloudPrintCredential() {
    }

    /**
     * コンストラクタ。
     *
     * @param credential 認証情報。
     */
    public CloudPrintCredential(Credential credential) {
        accessToken = credential.getAccessToken();
    }

}
