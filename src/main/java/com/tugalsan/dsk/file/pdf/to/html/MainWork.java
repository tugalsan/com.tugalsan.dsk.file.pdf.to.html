package com.tugalsan.dsk.file.pdf.to.html;

import com.tugalsan.api.file.server.TS_FileUtils;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import org.apache.pdfbox.Loader;
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
            try (var pdf = Loader.loadPDF(srcPDF.toFile()); var output = new PrintWriter(dstHTM.toFile(), StandardCharsets.UTF_8);) {
            //try (var pdf = PDDocument.load(srcPDF.toFile()); var output = new PrintWriter(dstHTM.toFile(), StandardCharsets.UTF_8);) {
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

