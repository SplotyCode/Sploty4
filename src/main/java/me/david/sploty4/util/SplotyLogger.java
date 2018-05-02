package me.david.sploty4.util;

import me.david.sploty4.Sploty;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class SplotyLogger {

    private Logger logger;
    private Level level;

    public SplotyLogger(final Level level) {
        this.level = level;
        LogManager.getLogManager().reset();
        Logger root = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        this.logger = root;
        FileHandler txt = null;
        try {
            String filename = new SimpleDateFormat("dd MM yyyy").format(new Date()) + "-1";
            File logdir = new File(Sploty.getDirectory(), "Logs/");
            if(!logdir.exists()) {
                logdir.mkdirs();
            }
            while(new File(logdir, filename + ".log").exists()) {
                int next = Integer.valueOf(filename.split("-")[1]);
                next++;
                filename = filename.substring(0, filename.split("-")[0].length()) + "-" + next;
            }
            txt = new FileHandler(new File(logdir, filename + ".log").getAbsolutePath());
            root.setLevel(level);
            txt.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                    String ret = "";
                    ret += "[" + record.getLevel().getName() + "] ";
                    ret += new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(record.getMillis());
                    ret += ": " + record.getMessage();
                    ret += "\r\n";
                    return ret;
                }
            });
            root.addHandler(txt);
        } catch (Exception e) {
            exception(e, "Failed to create Logger");
        }
    }

    public SplotyLogger info(String info) {
        logger.info(info);
        System.out.println(info);
        return this;
    }

    public SplotyLogger warn(String warning){
        logger.warning(warning);
        System.out.println("WARN: " + warning);
        return this;
    }

    public SplotyLogger printBanner(){
        info("  _________      .__          __            _____ ").
        info(" /   _____/_____ |  |   _____/  |_ ___.__. /  |  |").
        info(" \\_____  \\\\____ \\|  |  /  _ \\   __<   |  |/   |  |_").
        info(" /        \\  |_> >  |_(  <_> )  |  \\___  /    ^   /").
        info("/_______  /   __/|____/\\____/|__|  / ____\\____   | ").
        info("        \\/|__|                     \\/         |__| ");

        return this;
    }

    public SplotyLogger infoConsole(String info, boolean in) {
        logger.info("Console " + (in ? "<-" : "->") + " " + info);
        System.out.println(info);
        return this;
    }


    //TODO debug mode?
    public SplotyLogger debug(String debug){
        info(debug);
        return this;
    }

    public SplotyLogger debugWarn(String debug){
        warn(debug);
        return this;
    }

    public SplotyLogger exception(Throwable throwable, String message){
        debugWarn("Exception Was Thrown: " + message);
        for(String line : StringUtil.fromException(throwable).split(System.lineSeparator()))
            debugWarn(line);
        return this;
    }

    public SplotyLogger infoLog(String message) {
        logger.info(message);
        return this;
    }

    public Logger getLogger() {
        return logger;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
}
