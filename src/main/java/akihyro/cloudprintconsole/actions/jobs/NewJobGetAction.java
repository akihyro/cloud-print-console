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

import akihyro.cloudprintconsole.actions.jobs.models.NewJobGetActionRequest;
import akihyro.cloudprintconsole.actions.jobs.models.NewJobGetActionResponse;

/**
 * ジョブ登録情報GETアクション。
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
        log.info("ジョブ登録情報GETアクションを初期化します。 => {}", this);
    }

    /**
     * 終了する。
     */
    @PreDestroy
    public void dispose() {
        log.info("ジョブ登録情報GETアクションを終了します。 => {}", this);
    }

    /**
     * ジョブ登録ページを取得する。
     *
     * @param request リクエスト。
     * @return ジョブ登録ページ。
     * @throws Exception エラー。
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Template(name = "/jobs/new.html.jsp")
    public NewJobGetActionResponse getAsHtml(@BeanParam NewJobGetActionRequest request) throws Exception {
        val response = new NewJobGetActionResponse();
        response.setPrinterId(request.getPrinterId());
        return response;
    }

}
