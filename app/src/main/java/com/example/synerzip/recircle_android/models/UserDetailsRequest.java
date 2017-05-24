package com.example.synerzip.recircle_android.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by synerzip on 22/5/17.
 */
@Getter
@Setter
public class UserDetailsRequest {
    private String first_name;

    private String last_name;

    private String email;
    private String user_image_url;
    private UserAddress userAddress;
    private UserAccDetails user_acc_details;



}
/*
{
  "first_name": "nik",
  "last_name": "rathod",
  "email": "niksrathod009@gmail.com",
  "user_image_url": “http://cdsc.com”,
    "user_mob_no": 9028192413,
  “notification_flag” : true,
  "userAddress" : {
      "user_address_id": "c3ad9196-bee1-4f8b-853b-2c72c480a69a",
      "street" : "kothrud",
             "city": "pune",
            "state": "maha",
             "zip": 848484
  },
  "user_acc_details": {
    "user_acc_details_id": "csd-33-cs",
    "acc_number": 646743343,
    "acc_holder_name": "nik",
    "routing_number": 783723,
   “ssn” : 324332325,
   “DOB” : “2017-03-25T12:00:00.000Z”,
   “buisness_type” : “individual/buisness”,
  }
}

 */