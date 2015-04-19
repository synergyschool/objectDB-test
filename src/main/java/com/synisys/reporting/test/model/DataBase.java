package com.synisys.reporting.test.model;

import java.util.*;

/**
 * Created by haykmartirosyan on 4/11/15.
 */
public class DataBase {
    public final List<Project> projects = new ArrayList<>();
    public final List<Sector> sectors = new ArrayList<>();
    public final List<Status> statuses = new ArrayList<>();


    public final List<Status> statusIndexByName = new ArrayList<>();
    public final List<Sector> sectorIndexByName = new ArrayList<>();
    public final Map<Project, Set<Sector>> projectSectorIndex = new HashMap<>();
}
