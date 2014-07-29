package akihyro.cloudprintconsole.service;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import lombok.val;

import org.glassfish.jersey.server.mvc.Template;

import akihyro.cloudprintconsole.service.model.SubmitFormServiceReq;
import akihyro.cloudprintconsole.service.model.SubmitFormServiceRes;

/**
 * サブミットフォームサービス。
 */
@RequestScoped
@Path("/submit-form")
public class SubmitFormService extends BaseService {

    /** リクエスト */
    @BeanParam
    SubmitFormServiceReq req;

    /**
     * サブミットフォームを表示する。
     *
     * @return プリントジョブ結果。
     * @throws Exception エラー。
     */
    @GET
    @Template(name = "/submit-form")
    public SubmitFormServiceRes get() throws Exception {
        val res = new SubmitFormServiceRes();
        res.setPrinterId(req.getPrinterId());
        return res;
    }

}
