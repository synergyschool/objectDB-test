package com.synisys.reporting.test.generalmodel;


import javax.xml.crypto.Data;
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
public class GeneralReport {

    private int[]rowEntitySystemIds;
    private int measuresEntitySystemId;
    private int measuresFieldSystemId;

    class Path{
        //from one entity to another
        //int []systemIds;
        //e.g from sector to status will be
        // sector systemid, projectSector system id, project system id, someway status field id(pathid), Status systemid
        Iterator<Entity> createIterator(Entity rootEntity){
            return null;
        }
    }



    class Row {
        Entity[]entities;
        Object value;
    }

    class RowIterator implements Iterator<Row> {

        Iterator<Entity>[] iterators = new Iterator[DataBase.maxEntitySystemId];
        Path[] paths;

        Row row = new Row();




        @Override
        public boolean hasNext() {
            boolean hasNext;
            while(hasNext = moveIterators() && !checkFiltering()){

            }
            
            return hasNext;
            
        }

        private boolean checkFiltering() {
            return true;
        }

        //first iterator should be initialised separately from db

        /**
         *
         * @param startIndex
         * @return false if no data available, in that case first level iterator should be moved
         */
        private boolean initIterators(int startIndex) {

            assert startIndex>0;
            int index = startIndex;
            while(index<iterators.length-1){
//incomplete
                Iterator<Entity> iterator = iterators[index] = paths[index].createIterator(row.entities[index-1]);
                if(iterator.hasNext()){
                    row.entities[index] = iterator.next();
                    index++;
                }
                else if(index>0){
                    index --;
                }
                else {
                    return false;
                }

            }
            return true;
        }

        private boolean moveIterators() {
            int index = iterators.length  -1;
            while(index>=0){
                if(iterators[index].hasNext()){

                    row.entities[index] = iterators[index].next();
                    if(index < iterators.length-1){//reinit sub iterators
                        initIterators(index+1);
                    }
                    return true;
                }
                else {
                    index --;
                }
            }
            return false;
        }


        @Override
        public Row next() {
            initRow();
            return row;
        }

        private void initRow() {
            //calculate aggregations
        }
    }


    public void execute(DataBase dataBase) {
//        RowIterator rowIterator = new RowIterator(dataBase.statusIndexByName.iterator());
//        while(rowIterator.hasNext()){
//            Row row = rowIterator.next();
//        }

    }
}
