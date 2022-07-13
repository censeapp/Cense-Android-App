package com.app.cense.ui.testActivity;

import static android.app.admin.DevicePolicyManager.EXTRA_PROVISIONING_DEVICE_ADMIN_PACKAGE_NAME;

import android.app.Activity;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyDeviceAdminReceiver extends DeviceAdminReceiver {
    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        Toast.makeText(context, "Device admin permission received", Toast.LENGTH_SHORT).show();

        /*createAndroidProfile();
        onProfileProvisioningComplete(context, intent);*/

        //DevicePolicyManager devicePolicyManager = getManager(context);
        //devicePolicyManager.setUninstallBlocked(getWho(context), "com.app.cense", true);
        //System.out.println("права администратора получены, удаление запрещено.");

  }
    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        return "are you sure?";
    }
    // Provision an Android Profile Owner


    @Override
    public void onProfileProvisioningComplete(Context context, Intent intent) {
        Toast.makeText(context, "Profile Provisioning Complete", Toast.LENGTH_SHORT).show();
        DevicePolicyManager myDevicePolicyMgr = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName mDeviceAdmin = new ComponentName(context.getApplicationContext(), MyDeviceAdminReceiver.class);
        myDevicePolicyMgr.setProfileName(mDeviceAdmin, "My New Work Profile");
        myDevicePolicyMgr.setProfileEnabled(mDeviceAdmin);
        myDevicePolicyMgr.enableSystemApp(mDeviceAdmin,"com.app.cense");
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        Toast.makeText(context, "Device admin permission revoked", Toast.LENGTH_SHORT).show();
    }

    // Provision an Android Profile Owner
    private void createAndroidProfile() {


        // Set up the provisioning intent
        Intent provisioningIntent = new Intent("android.app.action.PROVISION_MANAGED_PROFILE");
        provisioningIntent.putExtra(EXTRA_PROVISIONING_DEVICE_ADMIN_PACKAGE_NAME, "com.app.cense");
        /*if (provisioningIntent.resolveActivity(provisioningActivity.getPackageManager()) == null) {
            // No handler for intent! Can't provision this device.
            // Show an error message and cancel.
        } else {
            // REQUEST_PROVISION_MANAGED_PROFILE is defined
            startActivityForResult(provisioningIntent, 1);
            provisioningActivity.finish();
        }*/
    }

    @Override
    public void onLockTaskModeExiting(Context context, Intent intent) {
        // here you must re-lock your app. make your activity know of this event and make it call startLockTask again!
    }
}