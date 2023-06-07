package com.apps.dount_branch.model;

import java.io.Serializable;

public class PreviousOrderModel implements Serializable {
    private String id;
    private String user_id;
    private String created_at;
    private String day;
    private String time;
    private OrderModel.BranchModel branch;
    private User user;

    public String getId() {
        return id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getDay() {
        return day;
    }

    public String getTime() {
        return time;
    }

//    public OrderModel.Setting getSetting() {
//        return setting;
//    }

    public User getUser() {
        return user;
    }

    public static class User implements Serializable {
        private String id;
        private String first_name;
        private String last_name;
        private String photo;
        private String phone;
        private String code;
        private String purchase_gifts;
        private String register_by;
        private String share_gifts;
        private String total;
        private String created_at;
        private String updated_at;

        public String getId() {
            return id;
        }

        public String getFirst_name() {
            return first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public String getPhoto() {
            return photo;
        }

        public String getPhone() {
            return phone;
        }

        public String getCode() {
            return code;
        }

        public String getPurchase_gifts() {
            return purchase_gifts;
        }

        public String getRegister_by() {
            return register_by;
        }

        public String getShare_gifts() {
            return share_gifts;
        }

        public String getTotal() {
            return total;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }
    }


}
