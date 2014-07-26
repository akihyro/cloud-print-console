package akihyro.cloudprintconsole.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import lombok.val;

import org.glassfish.jersey.server.mvc.Template;

import akihyro.cloudprintconsole.CloudPrintConsoleSession;
import akihyro.cloudprintconsole.api.CloudPrintApi;
import akihyro.cloudprintconsole.api.model.CloudPrintApiSearchReq;
import akihyro.cloudprintconsole.api.model.CloudPrintApiSearchRes;

/**
 * プリンタリストサービス。
 */
@RequestScoped
@Path("/printers")
public class PrintersService {

    /** API */
    @Inject
    CloudPrintApi api;

    /** セッション */
    @Inject
    CloudPrintConsoleSession session;

    /**
     * プリンタをリストアップする。
     *
     * @return プリンタリスト。
     * @throws Exception エラー。
     */
    @GET
    @Template(name = "/printers")
    public CloudPrintApiSearchRes printers() throws Exception {
        val req = new CloudPrintApiSearchReq();
        return api.call(session.getId(), req);
    }

}
