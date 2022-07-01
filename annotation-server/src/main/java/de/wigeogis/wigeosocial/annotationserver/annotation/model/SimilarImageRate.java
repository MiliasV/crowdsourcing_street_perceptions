package de.wigeogis.wigeosocial.annotationserver.annotation.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "similar_image_rate", schema = "public", catalog = "streetview")
public class SimilarImageRate {
    private long id;
    private String imageName;
    private String sessionId;
    private Long logId;
    private Long imageId;
    private Double similarity;
    private Double safety;
    private Double attractiveness;
    private Double business;
    private Timestamp creationDate;
    private Long imageRateId;

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
    @Column(name = "image_name")
    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @Basic
    @Column(name = "session_id")
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Basic
    @Column(name = "log_id")
    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    @Basic
    @Column(name = "image_id")
    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    @Basic
    @Column(name = "similarity")
    public Double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Double similarity) {
        this.similarity = similarity;
    }

    @Basic
    @Column(name = "safety")
    public Double getSafety() {
        return safety;
    }

    public void setSafety(Double safety) {
        this.safety = safety;
    }

    @Basic
    @Column(name = "attractiveness")
    public Double getAttractiveness() {
        return attractiveness;
    }

    public void setAttractiveness(Double attractiveness) {
        this.attractiveness = attractiveness;
    }

    @Basic
    @Column(name = "business")
    public Double getBusiness() {
        return business;
    }

    public void setBusiness(Double business) {
        this.business = business;
    }

    @Basic
    @Column(name = "creation_date")
    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }


    @Basic
    @Column(name = "image_rate_id")
    public Long getImageRateId() {
        return imageRateId;
    }

    public void setImageRateId(Long imageRateId) {
        this.imageRateId = imageRateId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimilarImageRate that = (SimilarImageRate) o;
        return id == that.id && Objects.equals(imageName, that.imageName) && Objects.equals(sessionId, that.sessionId) && Objects.equals(logId, that.logId) && Objects.equals(imageId, that.imageId) && Objects.equals(similarity, that.similarity) && Objects.equals(safety, that.safety) && Objects.equals(attractiveness, that.attractiveness) && Objects.equals(business, that.business) && Objects.equals(creationDate, that.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, imageName, sessionId, logId, imageId, similarity, safety, attractiveness, business, creationDate);
    }
}
