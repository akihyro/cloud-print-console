package akihyro.cloudprintconsole;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * セッション。
 */
@SessionScoped
@Data
@Slf4j
public class CloudPrintConsoleSession implements Serializable {

    /** シリアルバージョンUID */
    private static final long serialVersionUID = 5626776089550232164L;

    /** ID */
    private String id;

    /**
     * 初期化する。
     */
    @PostConstruct
    public void init() {
        log.debug("セッションを初期化しました。 => {}", this);
    }

    /**
     * 解放する。
     */
    @PreDestroy
    public void release() {
        log.debug("セッションを解放しました。 => {}", this);
    }

}
