package de.wigeogis.wigeosocial.annotationserver.annotation.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "image_recordings", schema = "public", catalog = "streetview")
public class ImageRecordings {
    private long id;
    private String imageId;
    private int pathId;
    private Integer orderId;
    private double direction;
    private double lng;
    private double lat;
    private Boolean start;
    private Boolean end;
    private Double height;
    private Double groundOffset;

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
    @Column(name = "image_id")
    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    @Basic
    @Column(name = "path_id")
    public int getPathId() {
        return pathId;
    }

    public void setPathId(int pathId) {
        this.pathId = pathId;
    }

    @Basic
    @Column(name = "order_id")
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    @Basic
    @Column(name = "direction")
    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    @Basic
    @Column(name = "lng")
    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Basic
    @Column(name = "lat")
    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    @Basic
    @Column(name = "\"start\"")
    public Boolean getStart() {
        return start;
    }

    public void setStart(Boolean start) {
        this.start = start;
    }

    @Basic
    @Column(name = "\"end\"")
    public Boolean getEnd() {
        return end;
    }

    public void setEnd(Boolean end) {
        this.end = end;
    }

    @Basic
    @Column(name = "height")
    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    @Basic
    @Column(name = "ground_offset")
    public Double getGroundOffset() {
        return groundOffset;
    }

    public void setGroundOffset(Double groundOffset) {
        this.groundOffset = groundOffset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageRecordings that = (ImageRecordings) o;
        return id == that.id && pathId == that.pathId && Double.compare(that.direction, direction) == 0 && Double.compare(that.lng, lng) == 0 && Double.compare(that.lat, lat) == 0 && Objects.equals(imageId, that.imageId) && Objects.equals(orderId, that.orderId) && Objects.equals(start, that.start) && Objects.equals(end, that.end) && Objects.equals(height, that.height) && Objects.equals(groundOffset, that.groundOffset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, imageId, pathId, orderId, direction, lng, lat, start, end, height, groundOffset);
    }
}
