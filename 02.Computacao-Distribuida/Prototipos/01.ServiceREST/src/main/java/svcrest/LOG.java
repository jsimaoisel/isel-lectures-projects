package svcrest;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LOG {

    private static PrintWriter fout=null;
    //private static boolean LogOutput=true;

    public static void InitLogs(String logFileName, boolean noLog ) throws IOException {
        if (noLog) {
             fout=new PrintWriter(System.out);
        } else {
//            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
//            LocalDateTime now = LocalDateTime.now();
//            FileWriter fw = new FileWriter(logFileName+"+"+dtf.format(now), true);
            FileWriter fw = new FileWriter(logFileName, true);
            BufferedWriter bw = new BufferedWriter(fw);
            fout = new PrintWriter(bw);
        }
    }
    private static final Object lock=new Object();
    public static void INFO(String logInfo)  {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss:SS:n");
        LocalDateTime now = LocalDateTime.now();
        synchronized(lock) {
            fout.println(dtf.format(now)+" : "+logInfo); fout.flush();
        }
    }
}
