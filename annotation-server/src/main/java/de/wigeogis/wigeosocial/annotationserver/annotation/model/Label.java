package de.wigeogis.wigeosocial.annotationserver.annotation.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Label {
    private long id;
    private String name_en;
    private String name_de;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name_en", nullable = true, length = -1)
    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    @Basic
    @Column(name = "name_de", nullable = true, length = -1)
    public String getName_de() {
        return name_de;
    }

    public void setName_de(String name_de) {
        this.name_de = name_de;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Label label = (Label) o;
        return id == label.id && Objects.equals(name_en, label.name_en) && Objects.equals(name_de, label.name_de);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name_en, name_de);
    }
}
