package akihyro.cloudprintconsole;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;

import lombok.Data;

/**
 * セッション。
 */
@SessionScoped
@Data
public class CloudPrintConsoleSession implements Serializable {

    /** シリアルバージョンUID */
    private static final long serialVersionUID = 5626776089550232164L;

    /** ID */
    private String id;

}
