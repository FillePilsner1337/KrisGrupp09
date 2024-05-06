package SharedModel;

import java.io.Serializable;





    public class RegReq implements Serializable {
        private static final long serialVersionUID = 604569492464863070L;

        private String userName;

        private String password;

        public RegReq(String userName, String password) {
            this.userName = userName;
            this.password = password;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }



        @Override
        public int hashCode() {
            return userName.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if(obj!=null && obj instanceof SharedModel.RegReq)
                return userName.equals(((SharedModel.RegReq)obj).getUserName());
            return false;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getUserName() {
            return userName;
        }
    }




