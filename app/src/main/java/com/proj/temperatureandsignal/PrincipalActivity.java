package com.proj.temperatureandsignal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.widget.TextView;

public class PrincipalActivity extends AppCompatActivity {



    TextView TempShow;
    TextView SignalShow;
    float batteryTemp;
    IntentFilter intentfilter;
    TelephonyManager mTelephonyManager;
    int mSignalStrength = 0;
    private LocationManager lm;
    private Location location;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        intentfilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

        this.registerReceiver(broadcastreceiver,intentfilter);

        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mTelephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

        TempShow = (TextView) findViewById(R.id.bateria_value);
        SignalShow = (TextView) findViewById(R.id.itensidade_value);










    }


    private BroadcastReceiver broadcastreceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            batteryTemp = (float)(intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0))/10;

            TempShow.setText(batteryTemp +" "+ (char) 0x00B0 +"C");
        }



    };

    private PhoneStateListener phoneStateListener = new PhoneStateListener(){
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            mSignalStrength = signalStrength.getGsmSignalStrength();
            mSignalStrength = (2 * mSignalStrength) - 113;
            SignalShow.setText(mSignalStrength+" dBm");
        }
    };
}

