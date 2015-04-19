package com.synisys.reporting.test.reporting;

import com.synisys.reporting.test.model.*;

import java.math.BigDecimal;
import java.util.Iterator;

/**
 * Created by haykmartirosyan on 4/12/15.
 * <p>
 * report definition:
 * rows: status, project
 * column committed amount
 * <p>
 * order by status
 * <p>
 * filter by project.sectors contains sector1
 */
public class Report1 {

    private final Sector filterSector;

    public Report1(Sector filterSector) {
        this.filterSector = filterSector;
    }


    class Row {
        Status status;
        Project project;
        BigDecimal committedAmount;
    }

    class RowIterator implements Iterator<Row> {

        final Iterator<Status> statusIterator;
        Iterator<Project> projectIterator;

        Row row = new Row();

        boolean hasNext;


        RowIterator(Iterator<Status> statusIterator){
            this.statusIterator = statusIterator;
            if(statusIterator.hasNext()) {
                nextStatus();
            }
            else {
                projectIterator = new Iterator<Project>() {
                    @Override
                    public boolean hasNext() {
                        return false;
                    }

                    @Override
                    public Project next() {
                        return null;
                    }
                };
            }

        }

        @Override
        public boolean hasNext() {
            while (true) {

                if (projectIterator.hasNext()) {
                    row.project = projectIterator.next();
                    if (row.project.projectSectorsIndex.containsKey(filterSector)) {
                        calculateCommittedAmount(row.project, filterSector);
                        return true;
                    } else {
                        continue;
                    }

                } if(statusIterator.hasNext()) {
                    nextStatus();
                    continue;
                }
                else {
                    return hasNext = false;
                }
            }
        }

        private void calculateCommittedAmount(Project project, Sector filterSector) {
            row.committedAmount = new BigDecimal(0);
            for(ProjectSector projectSector : project.projectSectorsIndex.get(filterSector)){
                row.committedAmount.add(projectSector.committedAmount);
            }
        }

        private void nextStatus(){
            row.status = statusIterator.next();
            projectIterator = row.status.projects.iterator();
        }

        @Override
        public Row next() {
            return row;
        }
    }


    public void execute(DataBase dataBase) {
        RowIterator rowIterator = new RowIterator(dataBase.statusIndexByName.iterator());
        while(rowIterator.hasNext()){
            Row row = rowIterator.next();
        }

    }
}
