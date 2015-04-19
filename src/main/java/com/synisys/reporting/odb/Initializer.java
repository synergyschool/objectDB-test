package com.synisys.reporting.odb;





import com.synisys.reporting.odb.model.Project;
import com.synisys.reporting.odb.model.ProjectSector;
import com.synisys.reporting.odb.model.Sector;
import com.synisys.reporting.odb.model.Status;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by haykmartirosyan on 4/13/15.
 */
public class Initializer {

    private static final Random random = new Random(10);
    public void init(EntityManager em){



        // Store 1000 Point objects in the database:
        em.getTransaction().begin();


        List<Project> projects = new ArrayList<>();
        List<Sector> sectors = new ArrayList<>();
        List<Status> statuses = new ArrayList<>();

        final int projectCount = 20_000;
        final int statusCount = 100;
        final int sectorCount = 1000;
        final int avgSectorPerProject = 30;
        final double statusDistributionMedian = 0.5;//0.5 is normal distribution

        assert avgSectorPerProject<sectorCount;


        List<Integer> sectorIndexes = new ArrayList<>(sectorCount);


        try {
            initEntities(sectors, sectorCount, Sector.class.getConstructor(String.class), "Sector");
            initEntities(statuses, statusCount, Status.class.getConstructor(String.class), "Status");
            initEntities(projects, projectCount, Project.class.getConstructor(String.class), "Project");
            initProjectStatus(projects, statuses, statusDistributionMedian);
            initProjectsWithSectors(projects, sectors, avgSectorPerProject);





        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }



        for (Project project : projects) {
            em.persist(project);
            for (ProjectSector projectSector: project.projectSectors) {
                em.persist(projectSector);
            }
        }
        for (Status status: statuses) {
            em.persist(status);
        }
        for (Sector sector: sectors) {
            em.persist(sector);
        }

        em.getTransaction().commit();
    }






    private <T> void initEntities(Collection<T> classifiers, int count, Constructor<T> constructor, String name){
        try {
            for(int i=0; i<count; i++){
                classifiers.add(constructor.newInstance(name + " " + i));

            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    //private <E1 extends Entity, E2 extends Entity> void initSubEntities(Collection<E1> entities1, Collection<E2> entities2, )
    //private <E1 extends Entity, E2 extends Entity> void initSubEntities(E1 entity, Collection<> Collection<E2> entities2, int numberOfEntities2ToFill)


    private void initProjectStatus(List<Project> projects, List<Status> statuses, double statusDistributionMedian){
        for(Project project : projects){
            int statusIndex = (int) (random(statusDistributionMedian)*statuses.size());
            Status status = statuses.get(statusIndex);
            project.status = status;
            status.projects.add(project);
        }
    }
    private void initProjectsWithSectors(List<Project> projects, List<Sector> sectors, int avgSectorsPerProject){
        List<Integer> sectorShuffledIndexes = new ArrayList<>(sectors.size());
        for(int i=0; i<sectors.size(); i++){
            sectorShuffledIndexes.add(i);
        }

        int i=0;
        for(Project project : projects){
            if(i%10==0){
                Collections.shuffle(sectorShuffledIndexes);
            }
            i++;
            int numberOfSectors2ToFill = (int) (random((double)avgSectorsPerProject/sectors.size())* sectors.size());

            initProjectSectors(project, sectors, numberOfSectors2ToFill, sectorShuffledIndexes);
        }

    }

    private void initProjectSectors(Project project, List<Sector> sectors, int numberOfSectors2ToFill, List<Integer> sectorShuffledIndexes){
        Iterator<Integer> sectorIndexIterator = sectorShuffledIndexes.iterator();
        for(int i=0; i<numberOfSectors2ToFill; i++){
            Sector sector = sectors.get(sectorIndexIterator.next());
            BigDecimal decimal = new BigDecimal(random.nextDouble()*10000000.00);
            decimal = decimal.setScale(4, BigDecimal.ROUND_HALF_DOWN);
            ProjectSector projectSector = new ProjectSector(project, sector, decimal);
            project.projectSectors.add(projectSector);
            sector.projectSectors.add(projectSector);


        }
    }

    public double random(double median){
        double r = random.nextDouble();
        double associatedPoint = 1-median;



        double leftSideCoeficient = median / associatedPoint;
        double rightSideCoeficient = (1-median) / (1-associatedPoint);

        return r<associatedPoint? r* leftSideCoeficient : (r-associatedPoint)*rightSideCoeficient+median;
    }
}
