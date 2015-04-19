package com.synisys.reporting.test;

/**
 * Created by haykmartirosyan on 4/12/15.
 */
public class Profiler {

    public static void execute(String message, Runnable action){
        long start = System.currentTimeMillis();
        action.run();

        long duration = System.currentTimeMillis()-start;
        System.out.println(String.format(message, duration/10 / 100.0));

    }
}
