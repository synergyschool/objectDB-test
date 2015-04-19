package com.synisys.reporting.test.model;

import java.math.BigDecimal;

/**
 * Created by haykmartirosyan on 4/11/15.
 */
public class ProjectSector {
    public final Project project;
    public final Sector sector;
    public final BigDecimal committedAmount;

    public ProjectSector(Project project, Sector sector, BigDecimal committedAmount) {
        this.project = project;
        this.sector = sector;
        this.committedAmount = committedAmount;
    }
}
