package SharedModel;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 604569492464863070L;
    private String msg;

    public Message(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
