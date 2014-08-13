package akihyro.cloudprintconsole.actions.printers;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.mvc.Template;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

import akihyro.cloudprintconsole.actions.printers.models.PrintersGetActionResponse;
import akihyro.cloudprintconsole.actions.printers.models.PrintersGetActionResponsePrinter;
import akihyro.cloudprintconsole.api.CloudPrintApi;
import akihyro.cloudprintconsole.api.models.CloudPrintApiSearchRequest;
import akihyro.cloudprintconsole.models.UserInfo;

/**
 * プリンタリストGETアクション。
 */
@ApplicationScoped
@Path("printers")
@Slf4j
public class PrintersGetAction {

    /** API */
    @Inject
    protected CloudPrintApi api;

    /** ユーザ情報 */
    @Inject
    protected UserInfo userInfo;

    /**
     * 初期化する。
     */
    @PostConstruct
    public void init() {
        log.info("プリンタリストGETアクションを初期化します。 => {}", this);
    }

    /**
     * 終了する。
     */
    @PreDestroy
    public void dispose() {
        log.info("プリンタリストGETアクションを終了します。 => {}", this);
    }

    /**
     * プリンタリストページを取得する。
     *
     * @return プリンタリストページ。
     * @throws Exception エラー。
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Template(name = "/printers/printers.html.jsp")
    public PrintersGetActionResponse getAsHtml() throws Exception {

        // プリンタリストを取得する
        log.info("プリンタリストを取得します。");
        val apiRequest = new CloudPrintApiSearchRequest();
        val apiResponse = api.call(userInfo.getId(), apiRequest);
        val printers = new ArrayList<PrintersGetActionResponsePrinter>(apiResponse.getPrinters().size());
        for (val apiPrinter : apiResponse.getPrinters()) {
            val printer = new PrintersGetActionResponsePrinter();
            printer.setId(apiPrinter.getId());
            printer.setName(apiPrinter.getName());
            printer.setDisplayName(apiPrinter.getDisplayName());
            printer.setDescription(apiPrinter.getDescription());
            printer.setConnectionStatus(apiPrinter.getConnectionStatus());
            printers.add(printer);
        }
        val response = new PrintersGetActionResponse();
        response.setPrinters(printers);

        return response;
    }

}
