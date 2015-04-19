package com.synisys.reporting.odb.model;


import javax.jdo.annotations.Index;
import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by haykmartirosyan on 4/11/15.
 */
@Entity
public class Status implements Serializable{
    @Id
    @GeneratedValue
    long id;
    @Index(unique = "true")
    public String name;
    public Status(String name) {
        Objects.requireNonNull(name);
        this.name = name;
    }


    @OneToMany(targetEntity = Project.class, mappedBy = "status", fetch = FetchType.LAZY)
    public final Set<Project> projects = new HashSet<>();

    @Override
    public String toString() {
        return "Status{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
