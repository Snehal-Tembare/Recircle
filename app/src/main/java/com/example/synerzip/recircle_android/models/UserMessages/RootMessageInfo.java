package com.example.synerzip.recircle_android.models.UserMessages;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 2/6/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
@Getter
@Setter
public class RootMessageInfo {

    private ArrayList<OwnerProdRelatedMsg> ownerProdRelatedMsgs;

    private ArrayList<OwnerRequestMsg> ownerRequestMsgs;
}
