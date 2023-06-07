package com.apps.dount_branch.model;

import java.io.Serializable;
import java.util.List;

public class OrderModel implements Serializable {
    private String id;
    private String branch_id;
    private String customer_id;
    private String total_qty;
    private String total_discount;
    private String total_tax;
    private String total_price;
    private String grand_total;
    private String item;
    private String payment_type;
    private String receive_type;
    private String is_delivary;
    private String delivery_id;
    private String delivery_status;
    private String status;
    private String notes;
    private String address;
    private String latitude;
    private String longitude;
    private String created_at;
    private String updated_at;
    private List<Detail> details;
private BranchModel branch;
private User customer;
    public String getId() {
        return id;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public String getTotal_qty() {
        return total_qty;
    }

    public String getTotal_discount() {
        return total_discount;
    }

    public String getTotal_tax() {
        return total_tax;
    }

    public String getTotal_price() {
        return total_price;
    }

    public String getGrand_total() {
        return grand_total;
    }

    public String getItem() {
        return item;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public String getReceive_type() {
        return receive_type;
    }

    public String getIs_delivary() {
        return is_delivary;
    }

    public String getDelivery_id() {
        return delivery_id;
    }

    public String getDelivery_status() {
        return delivery_status;
    }

    public String getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }

    public String getAddress() {
        return address;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public BranchModel getBranch() {
        return branch;
    }

    public User getCustomer() {
        return customer;
    }

    public class BranchModel implements Serializable {
        private String id;
        private String name;
        private String email;
        private String phone;
        private String address;
        private String company_id;
        private String is_active;
        private String created_at;
        private String updated_at;
        private String latitude;
        private String longitude;
        private boolean selected;

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return phone;
        }

        public String getAddress() {
            return address;
        }

        public String getCompany_id() {
            return company_id;
        }

        public String getIs_active() {
            return is_active;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }
    }


    public static class Product implements Serializable {
        private String id;
        private String name;
        private String image;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getImage() {
            return image;
        }
    }

    public static class Detail implements Serializable {
        private String id;
        private String order_id;
        private String product_id;
        private String qty;
        private String subtotal;
        private Product product;

        public String getId() {
            return id;
        }

        public String getOrder_id() {
            return order_id;
        }

        public String getProduct_id() {
            return product_id;
        }

        public String getQty() {
            return qty;
        }

        public String getSubtotal() {
            return subtotal;
        }

        public Product getProduct() {
            return product;
        }
    }

    public class User implements Serializable{
        private String id;
        private String customer_group_id;
        private String user_id;
        private String name;
        private String photo;
        private String company_name;
        private String email;
        private String phone_number;
        private String tax_no;
        private String address;
        private String city;
        private String state;
        private String postal_code;
        private String country;
        private String deposit;
        private String expense;
        private String code;
        private String purchase_gifts;
        private String register_by;
        private String share_gifts;
        private String is_active;
        private String created_at;
        private String updated_at;

        public String getId() {
            return id;
        }

        public String getCustomer_group_id() {
            return customer_group_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getName() {
            return name;
        }

        public String getPhoto() {
            return photo;
        }

        public String getCompany_name() {
            return company_name;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone_number() {
            return phone_number;
        }

        public String getTax_no() {
            return tax_no;
        }

        public String getAddress() {
            return address;
        }

        public String getCity() {
            return city;
        }

        public String getState() {
            return state;
        }

        public String getPostal_code() {
            return postal_code;
        }

        public String getCountry() {
            return country;
        }

        public String getDeposit() {
            return deposit;
        }

        public String getExpense() {
            return expense;
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

        public String getIs_active() {
            return is_active;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }
    }
}
