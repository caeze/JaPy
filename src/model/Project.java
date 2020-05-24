package model;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Data class for a project.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class Project {

    private List<String> titles;
    private List<String> files;
    private String jaPyVersion;
    private Date modificationDate;

    public Project(List<String> titles, List<String> files, String jaPyVersion, Date modificationDate) {
        this.titles = titles;
        this.files = files;
        this.jaPyVersion = jaPyVersion;
        this.modificationDate = modificationDate;
    }

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public String getJaPyVersion() {
        return jaPyVersion;
    }

    public void setJaPyVersion(String jaPyVersion) {
        this.jaPyVersion = jaPyVersion;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.titles);
        hash = 47 * hash + Objects.hashCode(this.files);
        hash = 47 * hash + Objects.hashCode(this.jaPyVersion);
        hash = 47 * hash + Objects.hashCode(this.modificationDate);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Project other = (Project) obj;
        if (!Objects.equals(this.jaPyVersion, other.jaPyVersion)) {
            return false;
        }
        if (!Objects.equals(this.titles, other.titles)) {
            return false;
        }
        if (!Objects.equals(this.files, other.files)) {
            return false;
        }
        if (!Objects.equals(this.modificationDate, other.modificationDate)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Project{" + "titles=" + titles + "files=" + files + ", jaPyVersion=" + jaPyVersion + ", modificationDate=" + modificationDate + '}';
    }
}
