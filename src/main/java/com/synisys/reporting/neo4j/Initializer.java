package com.synisys.reporting.neo4j;





import org.neo4j.graphdb.*;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.neo4j.graphdb.schema.Schema;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by haykmartirosyan on 4/13/15.
 */
public class Initializer {

    private static final Random random = new Random(10);

    public static final Label PROJECT_LABEL = DynamicLabel.label("project");
    public static final Label STATUS_LABEL = DynamicLabel.label("status");
    public static final Label SECTOR_LABEL = DynamicLabel.label("sector");

    public void init(final GraphDatabaseService graphDatabaseService){






        List<Node> projects = new ArrayList<>();
        List<Node> sectors = new ArrayList<>();
        List<Node> statuses = new ArrayList<>();

        final int projectCount = 20_000;
        final int statusCount = 100;
        final int sectorCount = 1000;
        final int avgSectorPerProject = 30;
        final double statusDistributionMedian = 0.5;//0.5 is normal distribution

        assert avgSectorPerProject<sectorCount;


        List<Integer> sectorIndexes = new ArrayList<>(sectorCount);


        NodeCreator nodeCreator = new NodeCreator() {
            @Override
            public Node newInstance(Label label, int id, String name) {
                Node node = graphDatabaseService.createNode(label);

                node.setProperty("id", id);
                node.setProperty("name", name);
                node.addLabel(DynamicLabel.label(name));
                return node;
            }
        };








        try ( Transaction tx = graphDatabaseService.beginTx() ) {

            initNodes(sectors, sectorCount, nodeCreator,  "Sector", SECTOR_LABEL);
            initNodes(statuses, statusCount, nodeCreator, "Status", STATUS_LABEL);
            initNodes(projects, projectCount, nodeCreator, "Project", PROJECT_LABEL);
            initProjectStatus(projects, statuses, statusDistributionMedian);
            initProjectsWithSectors(projects, sectors, avgSectorPerProject);

            tx.success();
        }




    }



    public void createIndexes(final GraphDatabaseService graphDatabaseService){
        IndexDefinition indexDefinition;
        try ( Transaction tx = graphDatabaseService.beginTx() )
        {
            Schema schema = graphDatabaseService.schema();
            indexDefinition = schema.indexFor(PROJECT_LABEL)
                    .on("id")
                    .create();
            indexDefinition = schema.indexFor(PROJECT_LABEL)
                    .on("name")
                    .create();
            indexDefinition = schema.indexFor(STATUS_LABEL)
                    .on("id")
                    .create();
            indexDefinition = schema.indexFor( STATUS_LABEL)
                    .on( "name" )
                    .create();
            indexDefinition = schema.indexFor( SECTOR_LABEL )
                    .on( "id" )
                    .create();
            indexDefinition = schema.indexFor( SECTOR_LABEL )
                    .on( "name" )
                    .create();
            tx.success();
        }
    }


    private <T> void initNodes(Collection<Node> classifiers, int count, NodeCreator nodeCreator, String entity, Label label){
            for(int i=0; i<count; i++){
                classifiers.add(nodeCreator.newInstance(label, i, entity + i));

            }

    }

    //private <E1 extends Entity, E2 extends Entity> void initSubEntities(Collection<E1> entities1, Collection<E2> entities2, )
    //private <E1 extends Entity, E2 extends Entity> void initSubEntities(E1 entity, Collection<> Collection<E2> entities2, int numberOfEntities2ToFill)


    private void initProjectStatus(List<Node> projects, List<Node> statuses, double statusDistributionMedian){
        for(Node project : projects){
            int statusIndex = (int) (random(statusDistributionMedian)*statuses.size());
            Node status = statuses.get(statusIndex);
            project.createRelationshipTo(status, RelationshipTypes.KNOWS);
            status.createRelationshipTo(project, RelationshipTypes.KNOWS);
        }
    }
    private void initProjectsWithSectors(List<Node> projects, List<Node> sectors, int avgSectorsPerProject){
        List<Integer> sectorShuffledIndexes = new ArrayList<>(sectors.size());
        for(int i=0; i<sectors.size(); i++){
            sectorShuffledIndexes.add(i);
        }

        int i=0;
        for(Node project : projects){
            if(i%10==0){
                Collections.shuffle(sectorShuffledIndexes);
            }
            i++;
            int numberOfSectors2ToFill = (int) (random((double)avgSectorsPerProject/sectors.size())* sectors.size());

            initProjectSectors(project, sectors, numberOfSectors2ToFill, sectorShuffledIndexes);
        }

    }

    private void initProjectSectors(Node project, List<Node> sectors, int numberOfSectors2ToFill, List<Integer> sectorShuffledIndexes){
        Iterator<Integer> sectorIndexIterator = sectorShuffledIndexes.iterator();
        for(int i=0; i<numberOfSectors2ToFill; i++){
            Node sector = sectors.get(sectorIndexIterator.next());
            BigDecimal decimal = new BigDecimal(random.nextDouble()*10000000.00);
            decimal = decimal.setScale(4, BigDecimal.ROUND_HALF_DOWN);
            Relationship relationship = project.createRelationshipTo(sector, RelationshipTypes.KNOWS);
            relationship.setProperty("committedAmount", decimal.doubleValue());

            Relationship relationship2 = sector.createRelationshipTo(project, RelationshipTypes.KNOWS);
            relationship2.setProperty("committedAmount", decimal.doubleValue());



        }
    }

    public double random(double median){
        double r = random.nextDouble();
        double associatedPoint = 1-median;



        double leftSideCoeficient = median / associatedPoint;
        double rightSideCoeficient = (1-median) / (1-associatedPoint);

        return r<associatedPoint? r* leftSideCoeficient : (r-associatedPoint)*rightSideCoeficient+median;
    }

    interface NodeCreator {
        Node newInstance(Label label, int id, String name);
    }
}
