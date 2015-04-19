package com.synisys.reporting.test.generalmodel;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by haykmartirosyan on 4/12/15.
 */
public class Entity {
    Object[]fields;
    int systemId;

    /**
     * e.g status
     * array index is target entity system id(path)
     */
    Relation[] lookupRelation;


    //relations by system id as index
    Collection<Relation>[] functionalRelation;


    //indexes
    //arrays first index is relation system id
    //second index is relations entities system id

    private Map<Entity, Set<Relation>> [][] indexes;





    /**
     *
     * @param entity
     * @return functionals by target entity, for example return projectsectors from project instance  by particular sector, relationSystemId has same meaning as path
     */

    public Set<Relation> getRelationsByEntity(Entity entity, int relationSystemId){
      return indexes[relationSystemId][entity.systemId].get(entity);
    };


    public Set<Entity> getEntitiesFromRelations(int relationSystemId, int entitySystemId){
        return indexes[relationSystemId][entitySystemId].keySet();
    };
//    public Set<Relation> getRelationsByEntity(Entity entity, int relationSystemId);



}
