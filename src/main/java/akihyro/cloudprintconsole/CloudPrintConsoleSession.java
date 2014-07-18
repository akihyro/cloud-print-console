package akihyro.cloudprintconsole;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import akihyro.cloudprintconsole.api.CloudPrintCredential;
import lombok.Data;

/**
 * セッション。
 */
@Named
@SessionScoped
@Data
public class CloudPrintConsoleSession implements Serializable {

    /** シリアルバージョンUID */
    private static final long serialVersionUID = 5626776089550232164L;

    /** 認証情報 */
    private CloudPrintCredential credential;

    /**
     * 認証済かどうか判定する。
     *
     * @return 認証済かどうか。
     */
    public boolean hasCredential() {
        return credential != null;
    }

}
