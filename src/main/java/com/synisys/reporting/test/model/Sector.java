package com.synisys.reporting.test.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by haykmartirosyan on 4/11/15.
 */
public class Sector extends Entity {
    public Sector(int id, String name) {
        super(id, name);
    }
    public final Set<ProjectSector> sectorProjects = new HashSet<>();
}
