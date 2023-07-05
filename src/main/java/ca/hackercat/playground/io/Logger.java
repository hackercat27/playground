package ca.hackercat.playground.io;

import ca.hackercat.playground.utils.ANSI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Simple lightweight logger that will output to a {@code PrintStream} instance.
 *
 * @author Tobin Brenner
 */
public class Logger {
    
    private PrintStream out;
    private FileOutputStream fileOutputStream;

    private final PrintStream outWrapper = new PrintStream(new OutputStream() {
        @Override
        public void write(int b) throws IOException {
            if (out != null)
                out.write(b);
            if (fileOutputStream != null && writeLogFiles)
                fileOutputStream.write(b);
        }
    });

    boolean supportsANSI = true;
    boolean printNewLines = true;
    boolean writeLogFiles = false;
    
    public Logger() {
        this(System.out);
    }
    public Logger(PrintStream out) {
        this.out = out;
        initFileStream();
    }

    private void initFileStream() {
        if (!writeLogFiles)
            return;
        try {
            new File("logs/").mkdirs();
            File file = new File("logs/" + getLogTime() + ".log");
            fileOutputStream = new FileOutputStream(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setPrintNewLines(boolean printNewLines) {
        this.printNewLines = printNewLines;
    }
    public void setSupportsANSI(boolean supportsANSI) {
        this.supportsANSI = supportsANSI;
    }
    
    public void log(String msg) {
        outWrapper.print("[" + getTime() + "] [INFO/" + Thread.currentThread().getName() + "] " + msg);
        if (printNewLines) outWrapper.println();
    }
    public void warn(String msg) {
        if (supportsANSI) outWrapper.print(ANSI.FOREGROUND_YELLOW);
        outWrapper.print("[" + getTime() + "] [WARN/" + Thread.currentThread().getName() + "] " + msg);
        if (supportsANSI) outWrapper.print(ANSI.RESET);
        if (printNewLines) outWrapper.println();
    }
    public void error(Exception e) {
        error(e.toString());
    }
    public void error(String msg) {
        if (supportsANSI) outWrapper.print(ANSI.FOREGROUND_RED);
        outWrapper.print("[" + getTime() + "] [ERROR/" + Thread.currentThread().getName() + "] " + msg);
        if (supportsANSI) outWrapper.print(ANSI.RESET);
        if (printNewLines) outWrapper.println();
    }
    public void fatal(String msg) {
        if (supportsANSI) outWrapper.print(ANSI.FOREGROUND_RED + ANSI.BOLD);
        outWrapper.print("[" + getTime() + "] [FATAL/" + Thread.currentThread().getName() + "] " + msg);
        if (supportsANSI) outWrapper.print(ANSI.RESET);
        if (printNewLines) outWrapper.println();
    }
    
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy_MM_dd HH-mm-ss");
    private String getTime() {
        return dtf.format(LocalDateTime.now());
    }
    private String getLogTime() {
        return dtf2.format(LocalDateTime.now());
    }
}
