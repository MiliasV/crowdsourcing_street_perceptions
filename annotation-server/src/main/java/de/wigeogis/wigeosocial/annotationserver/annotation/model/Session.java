package de.wigeogis.wigeosocial.annotationserver.annotation.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class Session {

    private long id;
    private String browserName;
    private String browserVersion;
    private String operatingSystem;
    private String ipAddress;
    private Timestamp creationDate;
    private Integer width;
    private Integer height;
    private String pid;
    private String sessionId;
    private String studyId;
    private Double timeElapsed;
    private Double instructionTimeEffort;
    private Boolean complete;
    private Timestamp finishDate;

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
    @Column(name = "browser_name")
    public String getBrowserName() {
        return browserName;
    }

    public void setBrowserName(String browserName) {
        this.browserName = browserName;
    }

    @Basic
    @Column(name = "browser_version")
    public String getBrowserVersion() {
        return browserVersion;
    }

    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
    }

    @Basic
    @Column(name = "operating_system")
    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    @Basic
    @Column(name = "ip_address")
    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
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
    @Column(name = "width")
    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    @Basic
    @Column(name = "height")
    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    @Basic
    @Column(name = "pid")
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
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
    @Column(name = "study_id")
    public String getStudyId() {
        return studyId;
    }

    public void setStudyId(String studyId) {
        this.studyId = studyId;
    }

    @Basic
    @Column(name = "time_elapsed")
    public Double getTimeElapsed() {
        return timeElapsed;
    }

    public void setTimeElapsed(Double timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    @Basic
    @Column(name = "instruction_time_effort")
    public Double getInstructionTimeEffort() {
        return instructionTimeEffort;
    }

    public void setInstructionTimeEffort(Double instructionTimeEffort) {
        this.instructionTimeEffort = instructionTimeEffort;
    }

    @Basic
    @Column(name = "complete")
    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }



    @Basic
    @Column(name = "finish_date")
    public Timestamp getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Timestamp finishDate) {
        this.finishDate = finishDate;
    }
}
