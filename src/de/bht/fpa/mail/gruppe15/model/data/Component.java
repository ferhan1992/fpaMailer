package de.bht.fpa.mail.gruppe15.model.data;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;

/*
 * This is the component part of a composite pattern.
 * 
 * @author Simone Strippgen
 */
@Entity
@Inheritance
public abstract class Component implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    // absolute directory path to this component
    private String path;
    // name of the component (without path)
    private String name;

    public Component() {
    }

    public Component(final File path) {
        this.path = path.getAbsolutePath();
        this.name = path.getName();
    }

    public void addComponent(final Component comp) {
        throw new UnsupportedOperationException("Not supported."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<Component> getComponents() {
        throw new UnsupportedOperationException("Not supported."); //To change body of generated methods, choose Tools | Templates.
    }

    // @return is the component expandable
    public abstract boolean isExpandable();

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setPath(final String p) {
        path = p;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return name;
    }
}
