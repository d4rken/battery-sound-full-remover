package eu.thedarken.BFSR;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

public class Main extends Activity {
    Context mContext;
    File file_charged_true;
    File file_charged_back;
    File file_usbconnection_true;
    File file_usbconnection_back;
    File file_shutdown_true;
    File file_shutdown_back;
    File file_lowbattery_true;
    File file_lowbattery_back;
    File file_dialerbutton_true;
    File file_dialerbutton_back;
    Boolean hasBatterySound = false;
    Boolean hasUsbConnectionSound = false;
    Boolean hasShutdownSound = false;
    Boolean hasBatteryLowSound = false;
    Boolean hasDialerButtonSound = false;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mContext = this;
        if (android.os.Build.MODEL.equalsIgnoreCase("GT-I9000")) {
            file_charged_true = new File("/system/media/audio/ui/TW_Battery_caution.ogg");
            file_charged_back = new File("/system/media/audio/ui/TW_Battery_caution.bak");
            file_usbconnection_true = new File("/system/media/audio/ui/Charger_Connection.ogg");
            file_usbconnection_back = new File("/system/media/audio/ui/Charger_Connection.bak");
            file_shutdown_true = new File("/system/media/audio/ui/shutdown.ogg");
            file_shutdown_back = new File("/system/media/audio/ui/shutdown.bak");
            file_lowbattery_true = new File("/system/media/audio/ui/TW_Low_Battery.ogg");
            file_lowbattery_back = new File("/system/media/audio/ui/TW_Low_Battery.bak");
            file_dialerbutton_true = new File("/system/media/audio/ui/TW_Waterdrop.ogg");
            file_dialerbutton_back = new File("/system/media/audio/ui/TW_Waterdrop.bak");
        } else if (android.os.Build.MODEL.equalsIgnoreCase("SCH-I500")) {
            Toast.makeText(this, "Found DEVICE: " + android.os.Build.MODEL + " Using Mesmerize config.", Toast.LENGTH_LONG).show();
            file_charged_true = new File("/system/media/audio/ui/TW_Battery_caution.ogg");
            file_charged_back = new File("/system/media/audio/ui/TW_Battery_caution.bak");
            file_usbconnection_true = new File("/system/media/audio/ui/Charger_Connection.ogg");
            file_usbconnection_back = new File("/system/media/audio/ui/Charger_Connection.bak");
            file_shutdown_true = new File("/system/media/audio/ui/0405_PowerOff.mp3");
            file_shutdown_back = new File("/system/media/audio/ui/0405_PowerOff.bak");
        } else {
            Toast.makeText(this, "Unknown DEVICE: " + android.os.Build.MODEL + " Using defaults.", Toast.LENGTH_LONG).show();
            file_charged_true = new File("/system/media/audio/ui/TW_Battery_caution.ogg");
            file_charged_back = new File("/system/media/audio/ui/TW_Battery_caution.bak");
            file_usbconnection_true = new File("/system/media/audio/ui/Charger_Connection.ogg");
            file_usbconnection_back = new File("/system/media/audio/ui/Charger_Connection.bak");
            file_shutdown_true = new File("/system/media/audio/ui/shutdown.ogg");
            file_shutdown_back = new File("/system/media/audio/ui/shutdown.bak");
            file_lowbattery_true = new File("/system/media/audio/ui/TW_Low_Battery.ogg");
            file_lowbattery_back = new File("/system/media/audio/ui/TW_Low_Battery.bak");
            file_dialerbutton_true = new File("/system/media/audio/ui/TW_Waterdrop.ogg");
            file_dialerbutton_back = new File("/system/media/audio/ui/TW_Waterdrop.bak");
        }


        hasBatterySound = true;
        hasUsbConnectionSound = true;
        hasShutdownSound = true;
        hasBatteryLowSound = true;
        hasDialerButtonSound = true;
        CheckBox cbBattery = (CheckBox) findViewById(R.id.cb_batterysound);
        CheckBox cbUsbConnection = (CheckBox) findViewById(R.id.cb_usbconnection);
        CheckBox cbShutdown = (CheckBox) findViewById(R.id.cb_shutdown);
        CheckBox cbBatteryLow = (CheckBox) findViewById(R.id.cb_batterylow);
        CheckBox cbDialerButton = (CheckBox) findViewById(R.id.cb_dialerbutton);

        if (file_dialerbutton_true.exists()) {
            cbDialerButton.setChecked(false);
        } else if (file_dialerbutton_back.exists()) {
            cbDialerButton.setChecked(true);
        } else {
            Toast.makeText(this, "No dialer button sound found.", Toast.LENGTH_LONG).show();
            hasDialerButtonSound = false;
        }
        if (file_charged_true.exists()) {
            cbBattery.setChecked(false);
        } else if (file_charged_back.exists()) {
            cbBattery.setChecked(true);
        } else {
            Toast.makeText(this, "No battery full sound found.", Toast.LENGTH_LONG).show();
            hasBatterySound = false;
        }
        if (file_lowbattery_true.exists()) {
            cbBatteryLow.setChecked(false);
        } else if (file_lowbattery_back.exists()) {
            cbBatteryLow.setChecked(true);
        } else {
            Toast.makeText(this, "No battery low sound found.", Toast.LENGTH_LONG).show();
            hasBatteryLowSound = false;
        }
        if (file_usbconnection_true.exists()) {
            cbUsbConnection.setChecked(false);
        } else if (file_usbconnection_back.exists()) {
            cbUsbConnection.setChecked(true);
        } else {
            Toast.makeText(this, "No usb/charger connection sound found.", Toast.LENGTH_LONG).show();
            hasUsbConnectionSound = false;
        }
        if (file_shutdown_true.exists()) {
            cbShutdown.setChecked(false);
        } else if (file_shutdown_back.exists()) {
            cbShutdown.setChecked(true);
        } else {
            Toast.makeText(this, "No shutdown sound found.", Toast.LENGTH_LONG).show();
            hasShutdownSound = false;
        }

        cbDialerButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Process q;
                if (!hasDialerButtonSound) {
                    showDialog(0);
                    return;
                }
                try {
                    q = Runtime.getRuntime().exec("su");
                    DataOutputStream os = new DataOutputStream(q.getOutputStream());
                    os.writeBytes("mount -w -o remount /system /system\n");
                    if (((CheckBox) v).isChecked()) {
                        os.writeBytes("mv " + file_dialerbutton_true + " " + file_dialerbutton_back + "\n");
                    } else {
                        os.writeBytes("mv " + file_dialerbutton_back + " " + file_dialerbutton_true + "\n");
                    }
                    os.writeBytes("exit\n");
                    os.flush();
                } catch (IOException e) {
                    Toast.makeText(mContext, "Something didn't work.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        cbBattery.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Process q;
                if (!hasBatterySound) {
                    showDialog(0);
                    return;
                }
                try {
                    q = Runtime.getRuntime().exec("su");
                    DataOutputStream os = new DataOutputStream(q.getOutputStream());
                    os.writeBytes("mount -w -o remount /system /system\n");
                    if (((CheckBox) v).isChecked()) {
                        os.writeBytes("mv " + file_charged_true + " " + file_charged_back + "\n");
                    } else {
                        os.writeBytes("mv " + file_charged_back + " " + file_charged_true + "\n");
                    }
                    os.writeBytes("exit\n");
                    os.flush();
                } catch (IOException e) {
                    Toast.makeText(mContext, "Something didn't work.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
        cbBatteryLow.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Process q;
                if (!hasBatteryLowSound) {
                    showDialog(0);
                    return;
                }
                try {
                    q = Runtime.getRuntime().exec("su");
                    DataOutputStream os = new DataOutputStream(q.getOutputStream());
                    os.writeBytes("mount -w -o remount /system /system\n");
                    if (((CheckBox) v).isChecked()) {
                        os.writeBytes("mv " + file_lowbattery_true + " " + file_lowbattery_back + "\n");
                    } else {
                        os.writeBytes("mv " + file_lowbattery_back + " " + file_lowbattery_true + "\n");
                    }
                    os.writeBytes("exit\n");
                    os.flush();
                } catch (IOException e) {
                    Toast.makeText(mContext, "Something didn't work.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
        cbUsbConnection.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Process q;
                if (!hasUsbConnectionSound) {
                    showDialog(0);
                    return;
                }
                try {
                    q = Runtime.getRuntime().exec("su");
                    DataOutputStream os = new DataOutputStream(q.getOutputStream());
                    os.writeBytes("mount -w -o remount /system /system\n");
                    if (((CheckBox) v).isChecked()) {
                        os.writeBytes("mv " + file_usbconnection_true + " " + file_usbconnection_back + "\n");
                    } else {
                        os.writeBytes("mv " + file_usbconnection_back + " " + file_usbconnection_true + "\n");
                    }
                    os.writeBytes("exit\n");
                    os.flush();
                } catch (IOException e) {
                    Toast.makeText(mContext, "Something didn't work.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
        cbShutdown.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Process q;
                if (!hasShutdownSound) {
                    showDialog(0);
                    return;
                }
                try {
                    q = Runtime.getRuntime().exec("su");
                    DataOutputStream os = new DataOutputStream(q.getOutputStream());
                    os.writeBytes("mount -w -o remount /system /system\n");
                    if (((CheckBox) v).isChecked()) {
                        os.writeBytes("mv " + file_shutdown_true + " " + file_shutdown_back + "\n");
                    } else {
                        os.writeBytes("mv " + file_shutdown_back + " " + file_shutdown_true + "\n");
                    }
                    os.writeBytes("exit\n");
                    os.flush();
                } catch (IOException e) {
                    Toast.makeText(mContext, "Something didn't work.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }

    public void hasRoot(View view) {
        try {
            Process p = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            os.writeBytes("echo \"Do I have root?\" >/system/temporary.txt\n");
            os.writeBytes("exit\n");
            os.flush();
            try {
                p.waitFor();
                if (p.exitValue() != 255) {
                    Toast.makeText(mContext, "Root gained successfully.", Toast.LENGTH_LONG).show();
                } else {
                    showDialog(1);
                }
            } catch (InterruptedException e) {
                showDialog(1);
            }
        } catch (IOException e) {
            showDialog(1);
        }
    }

    public void debugInfo(View view) {
        showDialog(2);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 0:
                return new AlertDialog.Builder(this)
                        .setTitle("Battery Full Sound Remover")
                        .setCancelable(false)
                        .setMessage(
                                "Sorry, but there were no valid files found.\nYour android devices is incompatible with this mod at the moment.\nYou could contact the developer dark3n on xda-developers with your android version and phone modell and maybe it can get included in the next version.")
                        .setNeutralButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        //finish();
                                    }
                                }).create();
            case 1:
                return new AlertDialog.Builder(this)
                        .setTitle("Debug Infos")
                        .setCancelable(false)
                        .setMessage(
                                "Could not gain root access. Is this phone rooted?")
                        .setNeutralButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        //finish();
                                    }
                                }).create();

            case 2:
                return new AlertDialog.Builder(this)
                        .setTitle("Battery Full Sound Remover")
                        .setCancelable(false)
                        .setMessage(
                                "DEVICE: " + android.os.Build.DEVICE + "\n"
                                        + "MODEL: " + android.os.Build.MODEL + "\n"
                                        + "PRODUCT: " + android.os.Build.PRODUCT + "\n"
                                        + "ID: " + android.os.Build.ID + "\n"
                                        + "FINGERPRINT: " + android.os.Build.FINGERPRINT + "\n"
                                        + "BRAND: " + android.os.Build.BRAND + "\n")
                        .setNeutralButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        //finish();
                                    }
                                }).create();
        }
        return null;
    }
}