package kz.mircella.blogserver.web;

import java.io.InputStream;
import java.io.SequenceInputStream;

class InputStreamCollector {
    private InputStream is;

    void collectInputStream(InputStream is) {
        if (this.is == null) this.is = is;
        this.is = new SequenceInputStream(this.is, is);
    }

    InputStream getInputStream() {
        return this.is;
    }
}
