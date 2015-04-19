package com.synisys.reporting.test.model;

import java.util.*;

/**
 * Created by haykmartirosyan on 4/11/15.
 */
public class Project extends Entity{

    public Status status;
    public final Set<ProjectSector> projectSectors = new HashSet<>();
    public final Map<Sector, Set<ProjectSector>> projectSectorsIndex = new HashMap<>();

    public Project(int id, String name) {
        super(id, name);
    }
}
