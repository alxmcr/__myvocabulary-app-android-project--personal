package bo.alxmcr.app.models;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

/**
 * Created by Jose Coca on 21/04/2015.
 */
public class Word {
    private String id;
    private Date creationDate;
    private Time creationTime;
    private Date modificationDate;
    private Time modificationTime;
    private String status;
    private String text;
    private List<Meaning> meanings;

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Time getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Time creationTime) {
        this.creationTime = creationTime;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Time getModificationTime() {
        return modificationTime;
    }

    public void setModificationTime(Time modificationTime) {
        this.modificationTime = modificationTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Meaning> getMeanings() {
        return meanings;
    }

    public void setMeanings(List<Meaning> meanings) {
        this.meanings = meanings;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Word{");
        sb.append("id='").append(id).append('\'');
        sb.append(", creationDate=").append(creationDate);
        sb.append(", creationTime=").append(creationTime);
        sb.append(", modificationDate=").append(modificationDate);
        sb.append(", modificationTime=").append(modificationTime);
        sb.append(", status='").append(status).append('\'');
        sb.append(", text='").append(text).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
