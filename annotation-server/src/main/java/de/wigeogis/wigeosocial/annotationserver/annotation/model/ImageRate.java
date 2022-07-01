package de.wigeogis.wigeosocial.annotationserver.annotation.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "image_rate", schema = "public", catalog = "streetview")
public class ImageRate {
    private long id;
    private String sessionId;
    private Long logId;
    private Long imageId;
    private Double safety;
    private Double attractiveness;
    private Double business;
    private Timestamp creationDate;
    private String attractivenessReason;
    private String safetyReason;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageRate imageRate = (ImageRate) o;
        return id == imageRate.id && Objects.equals(sessionId, imageRate.sessionId) && Objects.equals(logId, imageRate.logId) && Objects.equals(imageId, imageRate.imageId) && Objects.equals(safety, imageRate.safety) && Objects.equals(attractiveness, imageRate.attractiveness) && Objects.equals(business, imageRate.business) && Objects.equals(creationDate, imageRate.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sessionId, logId, imageId, safety, attractiveness, business, creationDate);
    }

    @Basic
    @Column(name = "attractiveness_reason")
    public String getAttractivenessReason() {
        return attractivenessReason;
    }

    public void setAttractivenessReason(String attractivenessReason) {
        this.attractivenessReason = attractivenessReason;
    }

    @Basic
    @Column(name = "safety_reason")
    public String getSafetyReason() {
        return safetyReason;
    }

    public void setSafetyReason(String safetyReason) {
        this.safetyReason = safetyReason;
    }
}
