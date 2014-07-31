package akihyro.cloudprintconsole.actions.root;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import lombok.extern.slf4j.Slf4j;

import org.glassfish.jersey.server.mvc.Template;

/**
 * ルートGETアクション。
 */
@ApplicationScoped
@Path("")
@Slf4j
public class RootGetAction {

    /**
     * 初期化する。
     */
    @PostConstruct
    public void init() {
        log.info("ルートGETアクションインスタンスを初期化します。 => {}", this);
    }

    /**
     * 終了する。
     */
    @PreDestroy
    public void dispose() {
        log.info("ルートGETアクションインスタンスを終了します。 => {}", this);
    }

    /**
     * ルートページを取得する。
     *
     * @return ルートページ。
     * @throws Exception エラー。
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Template(name = "/root/root.html.jsp")
    public Object getAsHtml() throws Exception {
        return this;
    }

}
