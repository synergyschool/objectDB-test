package com.synisys.reporting.odb;

import com.synisys.reporting.test.Profiler;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by haykmartirosyan on 4/13/15.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        System.setProperty("objectdb.home", System.getProperty("user.dir") + "/odb");
        System.setProperty("objectdb.conf", System.getProperty("user.dir") + "/odb/config/objectdb.xml");
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("$objectdb/db/projects.odb");
        final EntityManager entityManager = emf.createEntityManager();

        Profiler.execute("Initialized in %s seconds", new Runnable() {
            @Override
            public void run() {
//                new Initializer().init(entityManager);
            }
        });

        final List result = new ArrayList<>(1000000);

//        Profiler.execute("Report0 executed in %s seconds", new Runnable() {
//                    @Override
//                    public void run() {
//                        String query1 = "SELECT project from Project project";
//                        Query q1 = entityManager.createQuery(query1);
//                        List<?> list = q1.getResultList();
//                    }
//                });

        Profiler.execute("Report0 executed in %s seconds", new Runnable() {
            @Override
            public void run() {
                //Query q1 = entityManager.createQuery("SELECT status, sum(status.projects.projectSectors.commitedAmount) from Status status group by status");
                //String query1 = "SELECT status, count(project) from Status status inner join status.projects project group by status";
                //String query1 = "SELECT count(projectSector) from ProjectSector projectSector";
                //String query1 = "SELECT distinct project.status from Project project";
                String query1 = "SELECT status, min(projectSector.committedAmount) from Status status join status.projects project  join project.projectSectors projectSector group by status";

                Query q1 = entityManager.createQuery(query1);
                List<?> list = q1.getResultList();
            }
        });

        Profiler.execute("Report1 executed in %s seconds", new Runnable() {
            @Override
            public void run() {
                //Query q1 = entityManager.createQuery("SELECT status, sum(status.projects.projectSectors.commitedAmount) from Status status group by status");
                //String query1 = "SELECT status, count(project) from Status status inner join status.projects project group by status";
                //String query1 = "SELECT count(projectSector) from ProjectSector projectSector";
                //String query1 = "SELECT distinct project.status from Project project";
                String query1 = "SELECT status, sum(projectSector.committedAmount) from Status status join status.projects project  join project.projectSectors projectSector  group by status order by status.name";

                Query q1 = entityManager.createQuery(query1);
                List<?> list = q1.getResultList();
                result.addAll(list);

            }
        });

        Profiler.execute("Report2 executed in %s seconds", new Runnable() {
            @Override
            public void run() {
                String query1 = "SELECT sector, projectSector.project.status, count(projectSector.project) from Sector sector join sector.projectSectors projectSector group by sector, projectSector.project.status order by sector.name, projectSector.project.status.name";

                Query q1 = entityManager.createQuery(query1);
                List<?> list = q1.getResultList();

            }
        });

        Profiler.execute("Report3 executed in %s seconds", new Runnable() {
            @Override
            public void run() {
                String query1 = "SELECT sector from Sector sector join sector.projectSectors projectSector group by sector, projectSector.project.status ";

                Query q1 = entityManager.createQuery(query1);
                List<?> list = q1.getResultList();

            }
        });

        Profiler.execute("Report4 executed in %s seconds", new Runnable() {
            @Override
            public void run() {
                String query1 = "SELECT  sector from Sector sector join sector.projectSectors projectSector  order by sector.name";

                Query q1 = entityManager.createQuery(query1);
                List<?> list = q1.getResultList();

            }
        });

//        for(Object item : result){
//            if(item instanceof Object[]){
//                Object[]row = (Object[])item;
//                for(Object columnValue : row){
//                    System.out.print(columnValue+ "\t");
//                }
//                System.out.println();
//            }
//            else {
//                System.out.println(item);
//            }
//
//        }
        System.out.println(result.size());
        System.in.read();

    }
}
