package akihyro.cloudprintconsole;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

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

    /** ID */
    private String id;

}
