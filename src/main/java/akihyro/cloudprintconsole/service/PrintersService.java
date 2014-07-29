package akihyro.cloudprintconsole.service;

import java.util.ArrayList;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import lombok.val;

import org.glassfish.jersey.server.mvc.Template;

import akihyro.cloudprintconsole.CloudPrintConsoleSession;
import akihyro.cloudprintconsole.api.CloudPrintApi;
import akihyro.cloudprintconsole.api.model.CloudPrintApiSearchReq;
import akihyro.cloudprintconsole.service.model.PrintersServiceRes;
import akihyro.cloudprintconsole.service.model.PrintersServiceResPrinter;

/**
 * プリンタリストサービス。
 */
@RequestScoped
@Path("/printers")
public class PrintersService extends BaseService {

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
    public PrintersServiceRes printers() throws Exception {
        val apiReq = new CloudPrintApiSearchReq();
        val apiRes = api.call(session.getId(), apiReq);
        val printers = new ArrayList<PrintersServiceResPrinter>(apiRes.getPrinters().size());
        for (val apiPrinter : apiRes.getPrinters()) {
            val printer = new PrintersServiceResPrinter();
            printer.setId(apiPrinter.getId());
            printer.setName(apiPrinter.getName());
            printer.setDisplayName(apiPrinter.getDisplayName());
            printer.setDescription(apiPrinter.getDescription());
            printer.setConnectionStatus(apiPrinter.getConnectionStatus());
            printers.add(printer);
        }
        val res = new PrintersServiceRes();
        res.setPrinters(printers);
        return res;
    }

}
