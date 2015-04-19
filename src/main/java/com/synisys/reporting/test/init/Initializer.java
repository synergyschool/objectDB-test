package com.synisys.reporting.test.init;

import com.synisys.reporting.test.model.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.IntStream;

/**
 * Created by haykmartirosyan on 4/11/15.
 */
public class Initializer {




    public void initialiseDatabase(DataBase dataBase){

        final int projectCount = 100000;
        final int statusCount = 100;
        final int sectorCount = 10000;
        final int avgSectorPerProject = 1000;
        final double statusDistributionMedian = 0.5;//0.5 is normal distribution

        assert avgSectorPerProject<sectorCount;


        List<Integer> sectorIndexes = new ArrayList<>(sectorCount);


        try {
            initEntities(dataBase.sectors, sectorCount, Sector.class.getConstructor(int.class, String.class), "Sector");
            initIndexByName(dataBase.sectors, dataBase.sectorIndexByName);
            initEntities(dataBase.statuses, statusCount, Status.class.getConstructor(int.class, String.class), "Status");
            initIndexByName(dataBase.statuses, dataBase.statusIndexByName);
            initEntities(dataBase.projects, projectCount, Project.class.getConstructor(int.class, String.class), "Project");
            initProjectStatus(dataBase.projects, dataBase.statuses, statusDistributionMedian);
            initProjectsWithSectors(dataBase.projects, dataBase.sectors, avgSectorPerProject);

            initProjectSectorIndex(dataBase.projects);




        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }


    }

    private void initProjectSectorIndex(List<Project> projects) {
        for(Project project : projects){
            for(ProjectSector projectSector : project.projectSectors){
                Set<ProjectSector> projectSectorsBySector = project.projectSectorsIndex.get(projectSector.sector);
                if(projectSectorsBySector==null){
                    project.projectSectorsIndex.put(projectSector.sector, projectSectorsBySector = new HashSet<>());
                }
                projectSectorsBySector.add(projectSector);
            }
        }
    }

    private <T extends Entity> void initIndexByName(List<T> entities, List<T> entityIndexByName) {
        entityIndexByName.addAll(entities);
        entityIndexByName.sort(new Comparator<Entity>() {
            @Override
            public int compare(Entity o1, Entity o2) {
                return o1.name.compareTo(o2.name);
            }
        });
    }

    private <T extends Entity> void initEntities(Collection<T> classifiers, int count, Constructor<T> constructor, String name){
        try {
        for(int i=0; i<count; i++){
            classifiers.add(constructor.newInstance(i, name + " " + i));

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


        for(Project project : projects){
            Collections.shuffle(sectorShuffledIndexes);
            int numberOfSectors2ToFill = (int) (random((double)avgSectorsPerProject/sectors.size())* sectors.size());
            
            initProjectSectors(project, sectors, numberOfSectors2ToFill, sectorShuffledIndexes);
        }

    }

    private void initProjectSectors(Project project, List<Sector> sectors, int numberOfSectors2ToFill, List<Integer> sectorShuffledIndexes){
        Iterator<Integer> sectorIndexIterator = sectorShuffledIndexes.iterator();
        for(int i=0; i<numberOfSectors2ToFill; i++){
            Sector sector = sectors.get(sectorIndexIterator.next());
            ProjectSector projectSector = new ProjectSector(project, sector, new BigDecimal(Math.random()*10000000.00));
            project.projectSectors.add(projectSector);
            sector.sectorProjects.add(projectSector);

        }
    }

    public double random(double median){
        double r = Math.random();
        double associatedPoint = 1-median;



        double leftSideCoeficient = median / associatedPoint;
        double rightSideCoeficient = (1-median) / (1-associatedPoint);

        return r<associatedPoint? r* leftSideCoeficient : (r-associatedPoint)*rightSideCoeficient+median;
    }
}
