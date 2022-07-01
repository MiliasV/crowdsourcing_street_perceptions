package de.wigeogis.wigeosocial.annotationserver.annotation.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class Log {

    private long id;
    private String sessionId;
    private Long imageId;
    private long pathId;
    private Double lng;
    private Double lat;
    private Double yaw;
    private Double pitch;
    private Double pov;
    private Timestamp recordedAt;

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
    @Column(name = "path_id")
    public long getPathId() {
        return pathId;
    }

    public void setPathId(long pathId) {
        this.pathId = pathId;
    }

    @Basic
    @Column(name = "lng")
    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    @Basic
    @Column(name = "lat")
    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
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
    @Column(name = "yaw")
    public Double getYaw() {
        return yaw;
    }

    public void setYaw(Double yaw) {
        this.yaw = yaw;
    }

    @Basic
    @Column(name = "pitch")
    public Double getPitch() {
        return pitch;
    }

    public void setPitch(Double pitch) {
        this.pitch = pitch;
    }

    @Basic
    @Column(name = "pov")
    public Double getPov() {
        return pov;
    }

    public void setPov(Double pov) {
        this.pov = pov;
    }

    @Basic
    @Column(name = "recorded_at")
    public Timestamp getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(Timestamp recordedAt) {
        this.recordedAt = recordedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Log log = (Log) o;
        return id == log.id && sessionId == log.sessionId && pathId == log.pathId && Objects.equals(lng, log.lng) && Objects.equals(lat, log.lat) && Objects.equals(imageId, log.imageId) && Objects.equals(yaw, log.yaw) && Objects.equals(pitch, log.pitch) && Objects.equals(pov, log.pov) && Objects.equals(recordedAt, log.recordedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sessionId, pathId, lng, lat, imageId, yaw, pitch, pov, recordedAt);
    }
}
