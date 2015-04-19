package com.synisys.reporting.odb.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by haykmartirosyan on 4/13/15.
 */
@Entity
public class Project implements Serializable {
    @Id @GeneratedValue long id;
    public String name;

    public Project(String name) {
        this.name = name;
    }

    @ManyToOne
    public Status status;

    @OneToMany (targetEntity = ProjectSector.class, mappedBy = "project", fetch = FetchType.EAGER)
    public final Set<ProjectSector> projectSectors = new HashSet<>();

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
