package SharedModel;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 6;
    private String msg;

    public Message(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
