package com.tugalsan.dsk.file.pdf.to.html;

import com.tugalsan.api.desktop.server.TS_DesktopDialogInfoUtils;
import com.tugalsan.api.desktop.server.TS_DesktopJMenuButton;
import com.tugalsan.api.desktop.server.TS_DesktopJMenuButtonBar;
import com.tugalsan.api.desktop.server.TS_DesktopMainUtils;
import com.tugalsan.api.desktop.server.TS_DesktopPathUtils;
import com.tugalsan.api.desktop.server.TS_DesktopWindowAndFrameUtils;
import com.tugalsan.api.file.properties.server.TS_FilePropertiesUtils;
import com.tugalsan.api.file.server.TS_FileUtils;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import com.tugalsan.api.function.client.maythrowexceptions.checked.TGS_FuncMTCUtils;
import com.tugalsan.lib.file.pdf.to.html.server.TS_LibFilePdfToHtmlUtils;
import java.awt.Component;
import java.nio.file.Path;
import java.util.Properties;
import java.util.stream.IntStream;
import javax.swing.WindowConstants;

public class Main {

    final private static TS_Log d = TS_Log.of(Main.class);

    final public static Path pathCurDir = TS_DesktopPathUtils.currentFolder();
    public static Path pathConfig = pathCurDir.resolve(Main.class.getPackageName() + ".properties");

    public static Properties props;
    public static MainFrame mainFrame;

    //cd C:\git\dsk\com.tugalsan.dsk.file.pdf.to.html
    //java --enable-preview --add-modules jdk.incubator.vector -jar target/com.tugalsan.dsk.file.pdf.to.html-1.0-SNAPSHOT-jar-with-dependencies.jar
    public static void main(String[] args) {
        TGS_FuncMTCUtils.run(() -> {
            var kt = TS_ThreadSyncTrigger.of("main");
            var configLoadCmdFlagIdx = IntStream.range(0, args.length)
                    .filter(i -> args[i].equals(TS_LibFilePdfToHtmlUtils.EXECUTE_PARAM_LOAD_CONFIG_FILE)).findAny().orElse(-1);
            if (configLoadCmdFlagIdx == -1) {
                gui(kt);
            } else {
                console(kt, args, configLoadCmdFlagIdx);
            }
        }, e -> d.ct("main", e));
    }

    private static void console(TS_ThreadSyncTrigger kt, String[] args, int configLoadCmdFlagIdx) {
        d.cr("console", "hello");
        if (args.length <= configLoadCmdFlagIdx + 1) {
            d.ce("main", "ERROR: config file is not given!");
            System.exit(1);
        }
        pathConfig = Path.of(args[configLoadCmdFlagIdx + 1]);
        if (!TS_FileUtils.isExistFile(pathConfig)) {
            d.ce("main", "ERROR: config file is not found!", pathConfig);
            System.exit(1);
        }
        Path pathInput = null;
        { //READ PROPERTIES
            d.cr("console", "props.reading...");
            var u_props = TS_FilePropertiesUtils.createPropertyReader(pathConfig);
            if (u_props.isExcuse()) {
                d.ce("console", "ERROR: " + u_props.excuse().getMessage());
                props = TS_FilePropertiesUtils.createNewInstance();
                System.exit(1);
            }
            d.cr("console", "props.read: OK");
            props = u_props.value();
            {
                var op = TS_FilePropertiesUtils.getValue(props, TS_LibFilePdfToHtmlUtils.CONFIG_PARAM_PATH_INPUT);
                if (op.isEmpty()) {
                    d.ce("console", "ERROR: " + TS_LibFilePdfToHtmlUtils.CONFIG_PARAM_PATH_INPUT + " param not present in config file");
                    System.exit(1);
                }
                var str = op.orElseThrow();
                pathInput = Path.of(str);
            }
        }
        d.cr("console", "pathInput", pathInput);
        if (!TS_FileUtils.isExistFile(pathInput)) {
            d.ce("console", "ERROR: pathInput is not found!", pathInput);
            System.exit(1);
        }
        MainWork.work(true, pathInput, TS_LibFilePdfToHtmlUtils.pathOutput(pathInput));
    }

    private static void gui(TS_ThreadSyncTrigger kt) {
        d.cr("gui", "hello");
        TGS_FuncMTCUtils.run(() -> {
            TS_DesktopMainUtils.setThemeAndinvokeLaterAndFixTheme(() -> {
                return gui_component(kt);
            });
        }, e -> TS_DesktopDialogInfoUtils.show("error", e.toString()));
    }

    private static Component gui_component(TS_ThreadSyncTrigger kt) {
        d.cr("gui_component", "hello");
        {//CREATE FRAME
            mainFrame = new MainFrame();
            mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            TS_DesktopWindowAndFrameUtils.showAlwaysInTop(mainFrame, false);
        }

        {//LOG LOCATION
            MainLog.add("curDir: " + pathCurDir);
            MainLog.add("pathProperties: " + pathConfig);
        }

        { //READ PROPERTIES
            MainLog.add("props.reading...");
            var u_props = TS_FilePropertiesUtils.createPropertyReader(pathConfig);
            if (u_props.isExcuse()) {
                MainLog.add("ERROR: " + u_props.excuse().getMessage());
                props = TS_FilePropertiesUtils.createNewInstance();
            } else {
                MainLog.add("props.read: OK");
                props = u_props.value();
                {
                    var u = TS_FilePropertiesUtils.getValue(props, TS_LibFilePdfToHtmlUtils.CONFIG_PARAM_PATH_INPUT);
                    if (u.isPresent()) {
                        mainFrame.tfPdfInput.setText(u.orElseThrow());
                        mainFrame.tfHtmlOutput.setText(TS_LibFilePdfToHtmlUtils.pathOutput(
                                        Path.of(
                                                mainFrame.tfPdfInput.getText()
                                        )
                                ).toAbsolutePath().toString()
                        );
                    }
                }
            }
        }

        //INIT FRAME
        MainPopup.selectAll_copy_togather(mainFrame.tfPdfInput);
        MainPopup.selectAll_copy_togather(mainFrame.tfHtmlOutput);

        TS_DesktopWindowAndFrameUtils.setTitleSizeCenterWithMenuBar(mainFrame, Main.class.getPackageName(), TS_DesktopJMenuButtonBar.of(
                TS_DesktopJMenuButton.of("Exit", mx -> {
                    System.exit(0);
                }),
                TS_DesktopJMenuButton.of("About", mx -> {
                    MainAsync.run(() -> {
                        TS_DesktopDialogInfoUtils.show("About", "Written by Tuğasan Karabacak (tugalsan@gmail.com)\nFor mesametal.cloud and mebosa.cloud");
                    });
                })
        ));
        mainFrame.setSize(1000, 690);
        return mainFrame;
    }
}
