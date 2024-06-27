package com.styra.opa;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

class RecordingHandler extends Handler {
    private List<LogRecord> seenRecs = new ArrayList<>();

    @Override
    public void publish(LogRecord rec) {
        seenRecs.add(rec);
    }

    public List<LogRecord> getSeenRecs() {
        return this.seenRecs;
    }

    @Override
    public void close() {
    }

    @Override
    public void flush() {
    }
}
