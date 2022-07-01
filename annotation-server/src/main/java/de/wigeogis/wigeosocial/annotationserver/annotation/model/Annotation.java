package de.wigeogis.wigeosocial.annotationserver.annotation.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class Annotation {


    private Long id;
    private Integer imageId;
    private Integer categoryId;
    private Double top;
    private Double left;
    private Double bottom;
    private Double right;
    private Double xscale;
    private Double yscale;
    private Integer sessionId;
    private Boolean valid;
    private Timestamp creationDate;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "image_id")
    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    @Basic
    @Column(name = "category_id")
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    @Basic
    @Column(name = "\"top\"")
    public Double getTop() {
        return top;
    }

    public void setTop(Double top) {
        this.top = top;
    }

    @Basic
    @Column(name = "\"left\"")
    public Double getLeft() {
        return left;
    }

    public void setLeft(Double left) {
        this.left = left;
    }

    @Basic
    @Column(name = "\"bottom\"")
    public Double getBottom() {
        return bottom;
    }

    public void setBottom(Double bottom) {
        this.bottom = bottom;
    }

    @Basic
    @Column(name = "\"right\"")
    public Double getRight() {
        return right;
    }

    public void setRight(Double right) {
        this.right = right;
    }

    @Basic
    @Column(name = "xscale")
    public Double getXscale() {
        return xscale;
    }

    public void setXscale(Double xscale) {
        this.xscale = xscale;
    }

    @Basic
    @Column(name = "yscale")
    public Double getYscale() {
        return yscale;
    }

    public void setYscale(Double yscale) {
        this.yscale = yscale;
    }

    @Basic
    @Column(name = "session_id")
    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    @Basic
    @Column(name = "valid")
    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    @Basic
    @Column(name = "creation_date")
    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Annotation that = (Annotation) o;
        return id == that.id && Objects.equals(imageId, that.imageId) && Objects.equals(categoryId, that.categoryId) && Objects.equals(top, that.top) && Objects.equals(left, that.left) && Objects.equals(bottom, that.bottom) && Objects.equals(right, that.right) && Objects.equals(xscale, that.xscale) && Objects.equals(yscale, that.yscale) && Objects.equals(sessionId, that.sessionId) && Objects.equals(valid, that.valid) && Objects.equals(creationDate, that.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, imageId, categoryId, top, left, bottom, right, xscale, yscale, sessionId, valid, creationDate);
    }
}
