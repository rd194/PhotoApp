package in.terek.photoapp.db;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

/**
 * Entity mapped to table "IMAGE_FILES".
 */
@Entity
public class ImageFiles {

    @Id
    private Long id;
    private Long fileName;
    private String localUriPath;
    private String serverUriPath;
    private java.util.Date creationDate;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    @Generated
    public ImageFiles() {
    }

    public ImageFiles(Long id) {
        this.id = id;
    }

    @Generated
    public ImageFiles(Long id, Long fileName, String localUriPath, String serverUriPath, java.util.Date creationDate) {
        this.id = id;
        this.fileName = fileName;
        this.localUriPath = localUriPath;
        this.serverUriPath = serverUriPath;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFileName() {
        return fileName;
    }

    public void setFileName(Long fileName) {
        this.fileName = fileName;
    }

    public String getLocalUriPath() {
        return localUriPath;
    }

    public void setLocalUriPath(String localUriPath) {
        this.localUriPath = localUriPath;
    }

    public String getServerUriPath() {
        return serverUriPath;
    }

    public void setServerUriPath(String serverUriPath) {
        this.serverUriPath = serverUriPath;
    }

    public java.util.Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(java.util.Date creationDate) {
        this.creationDate = creationDate;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
