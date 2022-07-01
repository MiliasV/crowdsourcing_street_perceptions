package de.wigeogis.wigeosocial.annotationserver.annotation.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity(name = "users")
public class User {

    private Long id;
    private String username;
    private String password;
    private Timestamp lastLogin;
    private Boolean showInstruction;
    private Boolean prolific;
    private Integer tasks = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id" , updatable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "username" , updatable = false)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "password" , updatable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "last_login")
    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }


    @Basic
    @Column(name = "show_instruction")
    public Boolean getShowInstruction() {
        return showInstruction;
    }

    public void setShowInstruction(Boolean showInstruction) {
        this.showInstruction = showInstruction;
    }


    @Basic
    @Column(name = "prolific")
    public Boolean getProlific() {
        return prolific;
    }

    public void setProlific(Boolean prolific) {
        this.prolific = prolific;
    }


    @Basic
    @Column(name = "tasks")
    public Integer getTasks() {
        return tasks;
    }

    public void setTasks(Integer tasks) {
        this.tasks = tasks;
    }
}
