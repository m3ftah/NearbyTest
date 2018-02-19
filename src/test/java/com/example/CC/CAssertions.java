package com.example.CC;

import com.example.CC.CController;

import junit.framework.Assert;

/**
 * Created by guema on 2/19/2018.
 */

public class CAssertions {

    private CController cController;

    public CAssertions(CController cController) {
        this.cController = cController;
    }

/*    public void assertStopAdvertising(){
        Assert.assertTrue("Must call startAdvertising() before calling stopAdvertising()",cController.isAdvertising());
    }

    public void assertStartAdvertising() {
        Assert.assertFalse("Already Advertising",cController.isAdvertising());
    }*/
}
