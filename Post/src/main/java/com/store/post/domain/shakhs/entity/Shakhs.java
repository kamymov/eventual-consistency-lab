package com.store.post.domain.shakhs.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "SHAKHS")
public class Shakhs {

    @Id
    @Column(name = "ID")
    long id;

    @Column(name = "NAAM")
    String naam;

    @Column(name = "NAAMKHANEVADEGI")
    String naamKhanevadegi;

    public Shakhs() {}

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNaamKhanevadegi() {
        return naamKhanevadegi;
    }

    public void setNaamKhanevadegi(String naamKhanevadegi) {
        this.naamKhanevadegi = naamKhanevadegi;
    }

    public Shakhs(long id, String naam, String naamKhanevadegi) {
        this.id = id;
        this.naam = naam;
        this.naamKhanevadegi = naamKhanevadegi;
    }
}
