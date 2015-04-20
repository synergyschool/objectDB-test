package com.synisys.reporting.neo4j;

import org.neo4j.graphdb.RelationshipType;

/**
 * Created by haykmartirosyan on 4/20/15.
 */
public enum RelationshipTypes implements RelationshipType{
    CONTAINED_IN, KNOWS
}
