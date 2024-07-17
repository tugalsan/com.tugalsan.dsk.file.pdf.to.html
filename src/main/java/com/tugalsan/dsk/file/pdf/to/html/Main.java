package com.tugalsan.dsk.file.pdf.to.html;

import com.tugalsan.api.desktop.server.TS_DesktopMainUtils;
import com.tugalsan.api.desktop.server.TS_DesktopWindowAndFrameUtils;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

public class Main {

    //cd C:\me\codes\com.tugalsan\trm\com.tugalsan.dsk.file.pdf.to.html
    //java --enable-preview --add-modules jdk.incubator.vector -jar target/com.tugalsan.dsk.file.pdf.to.html-1.0-SNAPSHOT-jar-with-dependencies.jar
    public static void main(String[] args) {
        TS_DesktopMainUtils.setThemeAndinvokeLaterAndFixTheme(() -> makeComponent(TS_ThreadSyncTrigger.of(), args));
    }

    public static Component makeComponent(TS_ThreadSyncTrigger kt, String[] args) {
        var frame = new JFrame();
        TS_DesktopWindowAndFrameUtils.exitOnClose(frame);
        TS_DesktopWindowAndFrameUtils.setBorder(frame);
//        TS_DesktopWindowAndFrameUtils.setLoc(frame, new (10, 10, 1000, 700));
//        TS_DesktopWindowAndFrameUtils.setfavIcon(frame, TS_DesktopResourceUtils.imageIcon("/images/favicons/screen.png").getImage());
        TS_DesktopWindowAndFrameUtils.setContent(frame, createPanel());
        TS_DesktopWindowAndFrameUtils.setTitleSizeCenterWithMenuBar(frame, Main.class.getPackageName(), createMenuBar());
        frame.setVisible(true);
        return frame;
    }

    public static JMenuBar createMenuBar() {
        var menuBar = new JMenuBar();

        return menuBar;
    }

    public static JPanel createPanel() {
        var panel = new JPanel();
        panel.setSize(300, 300);
        return panel;
    }

}
