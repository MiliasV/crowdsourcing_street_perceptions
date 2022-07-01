package de.wigeogis.wigeosocial.annotationserver.annotation.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Image {
    private int id;
    private String name;
    private double x;
    private double y;
    private Integer pitch;
    private Integer heading;
    private Integer height;
    private Double fov;
    private Integer annotations;
    private String country;
    private Double direction;

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
    @Column(name = "name", nullable = false, length = -1)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "x", nullable = false, precision = 0)
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    @Basic
    @Column(name = "y", nullable = false, precision = 0)
    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Basic
    @Column(name = "pitch", nullable = true)
    public Integer getPitch() {
        return pitch;
    }

    public void setPitch(Integer pitch) {
        this.pitch = pitch;
    }

    @Basic
    @Column(name = "heading", nullable = true)
    public Integer getHeading() {
        return heading;
    }

    public void setHeading(Integer heading) {
        this.heading = heading;
    }

    @Basic
    @Column(name = "height", nullable = true)
    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    @Basic
    @Column(name = "fov", nullable = true, precision = 0)
    public Double getFov() {
        return fov;
    }

    public void setFov(Double fov) {
        this.fov = fov;
    }

    @Basic
    @Column(name = "annotations", nullable = true)
    public Integer getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Integer annotations) {
        this.annotations = annotations;
    }


    @Basic
    @Column(name = "country", nullable = true)
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Basic
    @Column(name = "direction", nullable = true)
    public Double getDirection() {
        return direction;
    }

    public void setDirection(Double direction) {
        this.direction = direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return id == image.id &&
                Double.compare(image.x, x) == 0 &&
                Double.compare(image.y, y) == 0 &&
                Objects.equals(name, image.name) &&
                Objects.equals(pitch, image.pitch) &&
                Objects.equals(heading, image.heading) &&
                Objects.equals(height, image.height) &&
                Objects.equals(fov, image.fov) &&
                Objects.equals(annotations, image.annotations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, x, y, pitch, heading, height, fov, annotations);
    }
}
