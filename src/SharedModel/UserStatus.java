package SharedModel;

import java.io.Serializable;
import java.util.Date;

/**
 * Ett status objekt som håller info om var en user är incheckad. Varje user objekt håller '
 * ett status objekt
 */
public class UserStatus implements Serializable {
    private static final long serialVersionUID = 5;
    private boolean checkedIn;
    private String id;
    private Date time;

    public UserStatus(boolean checkedIn, String id, Date time) {
        this.checkedIn = checkedIn;
        this.id = id;
        this.time = time;
    }
    @Override
    public String toString() {
        return "incheckad=" + checkedIn +
                ", id='" + id + '\'' +
                ", tid=" + time;
    }
    public boolean isCheckedIn() {
        return checkedIn;
    }
    public void setCheckedIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
    }    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }public Date getTime() {
        return time;
    }public void setTime(Date time) {
        this.time = time;
    }
}
