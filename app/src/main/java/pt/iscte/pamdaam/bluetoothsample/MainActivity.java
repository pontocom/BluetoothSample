package pt.iscte.pamdaam.bluetoothsample;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    protected BluetoothAdapter mBTAdpater;

    protected static int REQ_ENABLE_BT = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // verificar se o dispositivo suporta BT
        mBTAdpater = BluetoothAdapter.getDefaultAdapter();
        if(mBTAdpater==null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Bluetooth não suportado.");
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            // verificar se o BT está activo ou não
            if(!mBTAdpater.isEnabled()) {
                Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBT, REQ_ENABLE_BT);
            }
        }
    }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //discovery starts, we can show progress dialog or perform other tasks
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //discovery finishes, dismis progress dialog
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //bluetooth device found
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                Log.i("BTAPP", "Device Name = " + device.getName() + "/ " + device.getAddress());


                Toast.makeText(getApplicationContext(), "Found device " + device.getName(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    protected void procuraBTdevices(View v) {
        Toast.makeText(this, "Inicio da procura de dispositivos", Toast.LENGTH_LONG).show();

        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(mReceiver, filter);
        mBTAdpater.startDiscovery();

    }

    protected void paraProcuraBTDevices(View v) {
        Toast.makeText(this, "Parar a procura de dispositivos BT!", Toast.LENGTH_LONG).show();
        mBTAdpater.cancelDiscovery();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Bluetooth ligado com sucesso");
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        if (resultCode==RESULT_CANCELED) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Não foi ligado o Bluetooth!");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
