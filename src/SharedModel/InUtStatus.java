package SharedModel;

import java.io.Serializable;
import java.util.Date;

public class InUtStatus implements Serializable {
    private static final long serialVersionUID = 5;
    private boolean incheckad;
    private String id;
    private Date tid;

    public InUtStatus(boolean incheckad, String id, Date tid) {
        this.incheckad = incheckad;
        this.id = id;
        this.tid = tid;
    }

    @Override
    public String toString() {
        return "InUtStatus{" +
                "incheckad=" + incheckad +
                ", id='" + id + '\'' +
                ", tid=" + tid +
                '}';
    }

    public boolean isIncheckad() {
        return incheckad;
    }

    public void setIncheckad(boolean incheckad) {
        this.incheckad = incheckad;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getTid() {
        return tid;
    }

    public void setTid(Date tid) {
        this.tid = tid;
    }
}
