package com.tugalsan.dsk.file.pdf.to.html;

public class MainLog {

    public static void add(CharSequence cs) {
        Main.mainFrame.taConsole.append("\n");
        Main.mainFrame.taConsole.append(cs.toString());
        Main.mainFrame.taConsole.setCaretPosition(Main.mainFrame.taConsole.getDocument().getLength());
    }
}
