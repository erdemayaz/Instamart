package com.ayaz.instamart.sdk;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.util.Locale;
import java.util.UUID;

/**
 * Created by Ayaz on 19.11.2016.
 */

public class DeviceInfo {
    public static final String OS_NAME = "android";
    private CachedInfo cachedInfo;
    private Context context;

    private class CachedInfo {
        private String brand;
        private String carrier;
        private String language;
        private String manufacturer;
        private String model;
        private String osName;
        private String osVersion;
        private String versionName;
        private String versionRelease;
        private String product;
        private String id;
        private String hardware;
        private String serial;

        public CachedInfo() {
            this.brand = getBrand();
            this.carrier = getCarrier();
            this.language = getLanguage();
            this.manufacturer = getManufacturer();
            this.model = getModel();
            this.osName = getOsName();
            this.osVersion = getOsVersion();
            this.versionName = getVersionName();
            this.versionRelease = getVersionRelease();
            this.product = getProduct();
            this.id = getId();
            this.hardware = getHardware();
            this.serial = getSerial();
        }

        private String getOsName() {
            return DeviceInfo.OS_NAME;
        }

        private String getOsVersion() {
            return Build.VERSION.RELEASE;
        }

        private String getBrand() {
            return Build.BRAND;
        }

        private String getManufacturer() {
            return Build.MANUFACTURER;
        }

        private String getModel() {
            return Build.MODEL;
        }

        private String getVersionRelease(){return android.os.Build.VERSION.RELEASE;}

        private String getProduct(){return Build.PRODUCT;}

        private String getId(){return Build.ID;}

        private String getHardware(){return Build.HARDWARE;}

        private String getSerial(){return Build.SERIAL;}

        private String getIMEI(){
            TelephonyManager mngr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return mngr.getDeviceId();
        }

        private String getDeviceUniqueID(){
            return Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }

        private String getVersionName() {
            try {
                return DeviceInfo.this.context.getPackageManager().getPackageInfo(DeviceInfo.this.context.getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                return null;
            }
        }

        private String getCarrier() {
            return ((TelephonyManager) DeviceInfo.this.context.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName();
        }

        private String getLanguage() {
            return Locale.getDefault().getLanguage();
        }

    }

    public DeviceInfo(Context context) {
        this.context = context;
    }

    private CachedInfo getCachedInfo() {
        if (this.cachedInfo == null) {
            this.cachedInfo = new CachedInfo();
        }
        return this.cachedInfo;
    }

    public void prefetch() {
        getCachedInfo();
    }

    public String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public String getVersionName() {
        return getCachedInfo().versionName;
    }

    public String getOsName() {
        return getCachedInfo().osName;
    }

    public String getOsVersion() {
        return getCachedInfo().osVersion;
    }

    public String getBrand() {
        return getCachedInfo().brand;
    }

    public String getManufacturer() {
        return getCachedInfo().manufacturer;
    }

    public String getModel() {
        return getCachedInfo().model;
    }

    public String getCarrier() {
        return getCachedInfo().carrier;
    }

    public String getLanguage() {
        return getCachedInfo().language;
    }

    public String getVersionRelease(){return getCachedInfo().getVersionRelease();}

    public String getProduct(){return getCachedInfo().getProduct();}

    public String getId(){return getCachedInfo().getId();}

    public String getHardware(){return getCachedInfo().getHardware();}

    public String getSerial(){return getCachedInfo().getSerial();}

    public String getIMEI(){return getCachedInfo().getIMEI();}

    public String getDeviceUniqueID(){return getCachedInfo().getDeviceUniqueID();}

}
