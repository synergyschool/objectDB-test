package com.synisys.reporting.odb.model;


import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by haykmartirosyan on 4/11/15.
 */
@Entity
public class ProjectSector implements Serializable {
    @Id
    @GeneratedValue
    long id;
    @ManyToOne
    public Project project;

    @ManyToOne
    public Sector sector;

    public BigDecimal committedAmount;

    public ProjectSector(){

    }
    public ProjectSector(Project project, Sector sector, BigDecimal committedAmount) {
        this.project = project;
        this.sector = sector;
        this.committedAmount = committedAmount;
    }


}
