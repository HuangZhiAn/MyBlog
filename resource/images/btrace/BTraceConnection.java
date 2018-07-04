package aisino.ws.controllers;

import com.sun.btrace.annotations.*;
import com.sun.btrace.annotations.Export;

import static com.sun.btrace.BTraceUtils.*;

/**
 * Created by JoeHuang on 2018/6/2.
 */
@BTrace
public class BTraceConnection {
    @Export
    private static long openedCount;

    @Export
    private static long closedCount;

    @OnMethod(clazz="/.*PoolingDataSource/", method="getConnection", location=@Location(Kind.RETURN))
    public static void m(@Return Object obj) {
        openedCount++;
        println("One connection is opened!");
        println(obj);
        Threads.jstack();
    }

    @OnMethod(clazz="/.*PoolableConnection/", method="close")
    public static void d(@Self Object obj) {
        closedCount++;
        println("One connection is closed!");
        println(obj);
        Threads.jstack();
    }

    @OnExit
    public static void onexit(int code) {
        print("Total opened connection:");
        println(openedCount);
        print("Total closed connection:");
        println(closedCount);
    }
}

