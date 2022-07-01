package de.wigeogis.wigeosocial.annotationserver.annotation.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Path {
    private int id;
    private String polygon;
    private String line;
    private Integer counter = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "polygon")
    public String getPolygon() {
        return polygon;
    }

    public void setPolygon(String polygon) {
        this.polygon = polygon;
    }

    @Basic
    @Column(name = "line")
    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }


    @Basic
    @Column(name = "counter")
    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Path path = (Path) o;
        return id == path.id && Objects.equals(polygon, path.polygon) && Objects.equals(line, path.line);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, polygon, line);
    }
}
