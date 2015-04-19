package com.synisys.reporting.test;

import com.synisys.reporting.test.init.Initializer;
import com.synisys.reporting.test.model.DataBase;
import com.synisys.reporting.test.reporting.Report1;

import java.io.IOException;

public class Main {



    public static void main(String[] args) throws IOException {
        final DataBase dataBase = new DataBase();
        final Initializer initializer = new Initializer();
        Profiler.execute("Initialized in %s seconds", new Runnable() {
            @Override
            public void run() {
                initializer.initialiseDatabase(dataBase);
            }
        });


        Profiler.execute("Report1 executed in %s seconds", new Runnable(){
            @Override
            public void run() {
                new Report1(dataBase.sectors.get(2)).execute(dataBase);
            }
        });

//        System.out.println("ok");
//        System.in.read();
    }
}
