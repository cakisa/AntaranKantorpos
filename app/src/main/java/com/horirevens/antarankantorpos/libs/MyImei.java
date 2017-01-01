package com.horirevens.antarankantorpos.libs;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by horirevens on 11/25/16.
 */
public class MyImei {
    private String imeiNumber;

    public String getImeiNumber(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        imeiNumber = telephonyManager.getDeviceId();
        return imeiNumber;
    }
}
