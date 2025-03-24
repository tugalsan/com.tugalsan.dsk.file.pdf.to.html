package com.tugalsan.dsk.file.pdf.to.html;

import com.tugalsan.api.thread.server.async.run.TS_ThreadAsyncRun;
import .client.TGS_FuncMTU;
import com.tugalsan.api.function.client.maythrowexceptions.unchecked.TGS_FuncMTU;
import com.tugalsan.api.function.client.maythrowexceptions.checked.TGS_FuncMTCUtils;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import java.time.Duration;

public class MainAsync {

    final public static Duration RUN_SECONDS = Duration.ofSeconds(10);
    final private static TS_ThreadSyncTrigger killTrigger  = TS_ThreadSyncTrigger.of(MainAsync.class.getSimpleName());

    public static void run(TGS_FuncMTU exe) {
        run(exe, RUN_SECONDS);
    }

    public static void run(TGS_FuncMTU exe, Duration until) {
        TS_ThreadAsyncRun.now(killTrigger, kt -> {
//            var u = TS_ThreadAsyncAwait.runUntil(kt, until, kt2 -> {
//                MainLog.add("AWAIT.BEGIN...");
                TGS_FuncMTCUtils.run(exe, e -> MainLog.add("ERROR: " + e.getMessage()));
//            });
//            if (u.timeout()) {
//                MainLog.add("AWAIT.ERROR: TIMEOUT");
//            } else if (u.hasError()) {
//                MainLog.add("AWAIT.ERROR: " + u.exceptionIfFailed.orElseThrow().getMessage());
//            } else {
//                MainLog.add("AWAIT.END");
//            }
        });
    }
}
