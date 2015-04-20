package com.synisys.reporting.neo4j;

import com.synisys.reporting.test.Profiler;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.helpers.collection.IteratorUtil;
import org.neo4j.kernel.api.exceptions.Status;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by haykmartirosyan on 4/13/15.
 */
public class Main {

    private static final String DB_PATH = System.getProperty("user.dir") + "/neo4j-db1";
    private static final String CONFIG_FILE = System.getProperty("user.dir") + "/neo4j-db/neo4j.properties";
    public static void main(String[] args) throws IOException {

        final GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder( DB_PATH )
                //.loadPropertiesFromFile( CONFIG_FILE)
                .newGraphDatabase();
        registerShutdownHook( graphDb );

        Profiler.execute("Initialized in %s seconds", new Runnable() {
            @Override
            public void run() {
//                new Initializer().init(graphDb);
//                new Initializer().createIndexes(graphDb);
            }
        });

        Profiler.execute("Warm-up report executed in %s seconds", new Runnable() {
            @Override
            public void run() {
                try ( Transaction ignored = graphDb.beginTx()){
                    for(Node node : graphDb.getAllNodes()) {
                        IteratorUtil.count(node.getRelationships());
                    }
                }


                final String query1 = "MATCH (status:status {id:1}) RETURN status.name";
                try ( Transaction ignored = graphDb.beginTx();
                      Result result = graphDb.execute( query1 ) ) {
                    while ( result.hasNext() )
                    {
                        Map<String,Object> row = result.next();
                        for ( Map.Entry<String,Object> column : row.entrySet() ) {

                        }
                        //rows += "\n";
                    }
                }
            }
        });
        Profiler.execute("Number of projects calculated in %s seconds", new Runnable() {
            @Override
            public void run() {
                final String query1 = "MATCH (project:project) RETURN count(project)";
                try ( Transaction ignored = graphDb.beginTx();
                      Result result = graphDb.execute( query1 ) ) {
                    while ( result.hasNext() )
                    {
                        Map<String,Object> row = result.next();
                        for ( Map.Entry<String,Object> column : row.entrySet() ) {
                            System.out.println(column.getKey() + ": " + column.getValue() + "; ");
                        }
                        //rows += "\n";
                    }
                }
            }
        });

//        Profiler.execute("ReportX executed in %s seconds", new Runnable() {
//            @Override
//            public void run() {
//                final String query1 = "MATCH (project:project)-[]->(status:status) RETURN status.name";
//                try ( Transaction ignored = graphDb.beginTx();
//                      Result result = graphDb.execute( query1 ) ) {
//                    while ( result.hasNext() )
//                    {
//                        Map<String,Object> row = result.next();
//                        for ( Map.Entry<String,Object> column : row.entrySet() ) {
//                            //Object value = column.getValue();
//                            System.out.println(column.getKey() + ": " + column.getValue() + "; ");
//                        }
//                        //rows += "\n";
//                    }
//                }
//            }
//        });

        Profiler.execute("Report1 executed in %s seconds", new Runnable() {
            @Override
            public void run() {
                final String query1 = "MATCH (status:status)-[]->(project:project)-[projectSector]->(sector:sector) RETURN status.name, sum(projectSector.committedAmount)";
                try ( Transaction ignored = graphDb.beginTx();
                      Result result = graphDb.execute( query1 ) ) {
                    while ( result.hasNext() )
                    {
                        Map<String,Object> row = result.next();
                        for ( Map.Entry<String,Object> column : row.entrySet() ) {
                            Object value = column.getValue();
                            //System.out.println(column.getKey() + ": " + column.getValue() + "; ");
                        }
                        //rows += "\n";
                    }
                }
            }
        });


        Profiler.execute("Report2 executed in %s seconds", new Runnable() {
            @Override
            public void run() {
                final String query1 = "MATCH (status:status)-[]->(project:project)-[projectSector]->(sector:sector) RETURN sector.name, status.name, count(project)";
                try ( Transaction ignored = graphDb.beginTx();
                      Result result = graphDb.execute( query1 ) ) {
                    while ( result.hasNext() )
                    {
                        Map<String,Object> row = result.next();
                        for ( Map.Entry<String,Object> column : row.entrySet() ) {
//                            System.out.println(column.getKey() + ": " + column.getValue() + "; ");
                        }
                        //rows += "\n";
                    }
                }
            }
        });

        Profiler.execute("Report4 executed in %s seconds", new Runnable() {
            @Override
            public void run() {
                final String query1 = "MATCH (sector:sector)-[]->(project:project) RETURN sector ORDER BY sector.name";
                try ( Transaction ignored = graphDb.beginTx();
                      Result result = graphDb.execute( query1 ) ) {
                    while ( result.hasNext() )
                    {
                        Map<String,Object> row = result.next();
                        for ( Map.Entry<String,Object> column : row.entrySet() ) {
//                            System.out.println(column.getKey() + ": " + column.getValue() + "; ");
                        }
                        //rows += "\n";
                    }
                }
            }
        });
    }



     private static void registerShutdownHook(final GraphDatabaseService graphDb) {
         // Registers a shutdown hook for the Neo4j instance so that it
         // shuts down nicely when the VM exits (even if you "Ctrl-C" the
         // running application).
         Runtime.getRuntime().addShutdownHook(new Thread() {
             @Override
             public void run() {
                 graphDb.shutdown();
             }
         });
     }

 }
