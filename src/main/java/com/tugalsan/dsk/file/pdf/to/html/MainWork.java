package com.tugalsan.dsk.file.pdf.to.html;

import com.tugalsan.api.file.server.TS_FileUtils;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.fit.pdfdom.PDFDomTree;

public class MainWork {

    final private static TS_Log d = TS_Log.of(MainWork.class);

    public static void work(boolean isConsole, Path srcPDF, Path dstHTM) {
        TGS_UnSafe.run(() -> {
            TS_FileUtils.deleteFileIfExists(dstHTM);
            if (TS_FileUtils.isExistFile(dstHTM)) {
                d.cr("work", "ERROR canot delete outputFile", dstHTM);
                System.exit(1);
            }
            d.cr("work", "init", srcPDF, dstHTM);
            try (var pdf = PDDocument.load(srcPDF.toFile()); var output = new PrintWriter(dstHTM.toFile(), StandardCharsets.UTF_8);) {
                new PDFDomTree().writeText(pdf, output);
            }
            if (isConsole) {
                d.cr("work", "success");
                System.exit(0);
            }
        }, e -> {
            var errMsg = "ERROR:" + e.getMessage();
            if (isConsole) {
                System.err.println(errMsg);
                System.exit(1);
            }
            MainLog.add(errMsg);
        });
    }
}
//    @Deprecated //NOT WORKING ERROR: org.apache.pdfbox version incompatible
//    public static Path castFromPDFtoHTM(Path srcPDF, Path dstHTM, CharSequence optionalTitle, CharSequence optionalHeaderContent, CharSequence optional_iframe_video, boolean addLoader) {
//        d.ci("castFromPDFtoHTM", srcPDF, dstHTM);
//        TGS_UnSafe.run(() -> {
//            castFromPDFtoHTM_do(srcPDF, dstHTM);
//        }, e -> {
//            TS_FileTxtUtils.toFile("""
//            <html><head><title>ERROR</title></head><body>
//            PDF'den HTM ön izlene dosyası oluşturuken bir hata oluştu. Lütfen orjinal pdf dosyayı indiriniz.<br>
//            An error occured creating HTM preview file from PDF. Please download the original pdf file.<br>
//            <br>
//            %s
//            </body></html>
//            """.formatted("ERROR: org.apache.pdfbox version incompatible; disabled until further notice!"), dstHTM, false);
//            e.printStackTrace();
//        });
//
//        var strHtm = TS_FileTxtUtils.toString(dstHTM);
//        if (addLoader) {
//            strHtm = TS_FileHtmlUtils.addLoader(strHtm);
//        }
//        if (optional_iframe_video
//                != null) {
//            strHtm = TS_FileHtmlUtils.appendResponsiveVideo(strHtm, optional_iframe_video);
//        }
//        if (optionalHeaderContent
//                != null) {
//            strHtm = TS_FileHtmlUtils.appendToBodyStartAfter(strHtm, optionalHeaderContent);
//        }
//        if (optionalTitle
//                != null) {
//            strHtm = TS_FileHtmlUtils.updateTitleContent(strHtm, optionalTitle);
//        }
//
//        return TS_FileTxtUtils.toFile(strHtm, dstHTM, false);
//    }

