package ch.heigvd.iict.sym.sym_labo4;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

/*  L'interface  DataClient.OnDataChangedListener permet de traiter les events des data item.
 *  Elle mets à disposition un listner d'event */
public class WearSynchronizedActivity extends AppCompatActivity implements DataClient.OnDataChangedListener {

    private static final String TAG = WearSynchronizedActivity.class.getSimpleName();

    /* 3 clés différentes pour représenter une coloueur (RGB)
    *  Valeurs échangées avec l'application mobile */
    private static final String R_KEY = "r";
    private static final String G_KEY = "g";
    private static final String B_KEY = "b";

    @Override
    protected void onResume() {
        super.onResume();
        /* Ajout du listener lorsque l'application est utilisé */
        Wearable.getDataClient(this).addListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wearsynchronized);
    }

    @Override
    protected void onPause() {
        super.onPause();
        /* Lors que l'on quitte l'activité, on retire le listenr */
        Wearable.getDataClient(this).removeListener(this);
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // DataItem changed
                DataItem item = event.getDataItem();
                /* Est-ce que c'est un objet "color" (contenant les composantes RGB) */
                if (item.getUri().getPath().compareTo("/color") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    /* Mise à jour des couleurs avec les valeurs reçus du wearable */
                    updateColor(dataMap.getInt(R_KEY),dataMap.getInt(G_KEY),dataMap.getInt(B_KEY));
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                // DataItem deleted
            }
        }
    }


    /*
     *  Code utilitaire fourni
     */

    /**
     * Method used to update the background color of the activity
     * @param r The red composant (0...255)
     * @param g The green composant (0...255)
     * @param b The blue composant (0...255)
     */
    private void updateColor(int r, int g, int b) {
        View rootView = findViewById(android.R.id.content);
        rootView.setBackgroundColor(Color.argb(255, r,g,b));
    }

}
