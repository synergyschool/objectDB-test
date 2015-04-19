package com.synisys.reporting.odb.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by haykmartirosyan on 4/11/15.
 */
@Entity
public class Sector implements Serializable {
    @Id
    @GeneratedValue
    long id;
    public String name;
    public Sector(String name) {
        this.name = name;
    }

    @OneToMany(targetEntity = ProjectSector.class, mappedBy = "sector", fetch = FetchType.LAZY)
    public Set<ProjectSector> projectSectors = new HashSet<>();

    @Override
    public String toString() {
        return "Sector{" +
                "name='" + name + '\'' +
                '}';
    }
}
