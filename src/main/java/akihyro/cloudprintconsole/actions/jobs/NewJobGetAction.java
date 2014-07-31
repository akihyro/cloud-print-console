package akihyro.cloudprintconsole.actions.jobs;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.mvc.Template;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

import akihyro.cloudprintconsole.actions.jobs.models.NewJobGetActionReq;
import akihyro.cloudprintconsole.actions.jobs.models.NewJobGetActionRes;

/**
 * ジョブ登録GETアクション。
 */
@ApplicationScoped
@Path("jobs/new")
@Slf4j
public class NewJobGetAction {

    /**
     * 初期化する。
     */
    @PostConstruct
    public void init() {
        log.info("ジョブ登録GETアクションインスタンスを初期化します。 => {}", this);
    }

    /**
     * 終了する。
     */
    @PreDestroy
    public void dispose() {
        log.info("ジョブ登録GETアクションインスタンスを終了します。 => {}", this);
    }

    /**
     * ジョブ登録ページを取得する。
     *
     * @param req リクエスト。
     * @return ジョブ登録ページ。
     * @throws Exception エラー。
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Template(name = "/jobs/new.html.jsp")
    public NewJobGetActionRes getAsHtml(@BeanParam NewJobGetActionReq req) throws Exception {
        val res = new NewJobGetActionRes();
        res.setPrinterId(req.getPrinterId());
        return res;
    }

}
