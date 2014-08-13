package akihyro.cloudprintconsole.actions.jobs.models;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataParam;

import lombok.Data;

/**
 * ジョブPOSTアクションリクエスト。
 */
@Data
public class JobPostActionRequest {

    /** プリンタID */
    @FormDataParam("printer-id")
    private String printerId;

    /** タイトル */
    @FormDataParam("title")
    private String title;

    /** コンテンツタイプ */
    @FormDataParam("content-type")
    private String contentType;

    @FormDataParam("content")
    private InputStream content;

}
