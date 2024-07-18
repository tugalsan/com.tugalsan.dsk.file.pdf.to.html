package com.tugalsan.dsk.file.pdf.to.html;

import com.tugalsan.api.charset.client.TGS_CharSetCast;
import com.tugalsan.api.file.properties.server.TS_FilePropertiesUtils;
import static com.tugalsan.dsk.file.pdf.to.html.Main.mainFrame;
import com.tugalsan.lib.file.pdf.to.html.server.TS_LibFilePdfToHtmlUtils;
import java.awt.FileDialog;
import java.nio.file.Path;
import javax.swing.text.JTextComponent;

public class MainRecordFile {

    public static Path pathLast = Main.pathCurDir;

    private static void thing(String param, String filter, JTextComponent tf) {
        MainAsync.run(() -> {
            Main.mainFrame.taConsole.setText("MainRecordFile.thing." + param + "." + tf.getText());
            var fd = new FileDialog(Main.mainFrame, TGS_CharSetCast.turkish().toUpperCase(param), FileDialog.LOAD);
            fd.setDirectory(pathLast.toAbsolutePath().toString());
            fd.setFile(filter);
            fd.setVisible(true);
            var selectedFile = fd.getFile();
            if (selectedFile == null) {
                MainLog.add(param + ".cancelled");
                return;
            }
            selectedFile = fd.getDirectory() + selectedFile;
            MainLog.add(param + ".selected_as:" + selectedFile);
            tf.setText(selectedFile);
            TS_FilePropertiesUtils.setValue(Main.props, param, selectedFile);
            TS_FilePropertiesUtils.write(Main.props, Main.pathConfig);
        });
    }

    public static void pdfInput() {
        thing(TS_LibFilePdfToHtmlUtils.CONFIG_PARAM_PATH_INPUT, "*.pdf", Main.mainFrame.tfPdfInput);
        mainFrame.tfHtmlOutput.setText(TS_LibFilePdfToHtmlUtils.pathOutput(
                        Path.of(
                                mainFrame.tfPdfInput.getText()
                        )
                ).toAbsolutePath().toString()
        );
    }
}
