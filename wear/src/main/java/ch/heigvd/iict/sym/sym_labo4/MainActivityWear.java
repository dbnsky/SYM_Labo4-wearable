package ch.heigvd.iict.sym.sym_labo4;

import android.os.Bundle;
import android.support.wear.widget.BoxInsetLayout;
import android.support.wearable.activity.WearableActivity;

import com.bozapro.circularsliderrange.CircularSliderRange;
import com.bozapro.circularsliderrange.ThumbEvent;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import ch.heigvd.iict.sym.sym_labo4.widgets.CircularSliderRangeFixed;

public class MainActivityWear extends WearableActivity {

    /* 3 clés différentes pour représenter une coloueur (RGB)
    *  Valeurs échangées avec l'application mobile */
    private static final String R_KEY = "r";
    private static final String G_KEY = "g";
    private static final String B_KEY = "b";

    private DataClient mDataClient;

    private static final String TAG = MainActivityWear.class.getSimpleName();

    private BoxInsetLayout mContainerView           = null;

    private CircularSliderRangeFixed redSlider      = null;
    private CircularSliderRangeFixed greenSlider    = null;
    private CircularSliderRangeFixed blueSlider     = null;

    private double startAngleRed    =     0;
    private double startAngleGreen  =     0;
    private double startAngleBlue   =     0;
    private double endAngleRed      = 90+30;
    private double endAngleGreen    = 90+60;
    private double endAngleBlue     = 90+90;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_wear);
        setAmbientEnabled();

        //link to GUI
        this.mContainerView = findViewById(R.id.container);
        this.redSlider      = findViewById(R.id.circular_red);
        this.greenSlider    = findViewById(R.id.circular_green);
        this.blueSlider     = findViewById(R.id.circular_blue);

        //events
        this.redSlider.setOnSliderRangeMovedListener(new CircularSliderRange.OnSliderRangeMovedListener() {
            @Override public void onStartSliderMoved(double pos) { /* fixed */ }

            @Override public void onEndSliderMoved(double pos) {
                endAngleRed = 90 + pos;
            }

            @Override public void onStartSliderEvent(ThumbEvent event) { }

            @Override
            public void onEndSliderEvent(ThumbEvent event) {
                updateColor();
            }
        });

        this.greenSlider.setOnSliderRangeMovedListener(new CircularSliderRange.OnSliderRangeMovedListener() {
            @Override public void onStartSliderMoved(double pos) { /* fixed */ }

            @Override public void onEndSliderMoved(double pos) {
                endAngleGreen = 90 + pos;
            }

            @Override public void onStartSliderEvent(ThumbEvent event) { }

            @Override
            public void onEndSliderEvent(ThumbEvent event) {
                updateColor();
            }
        });

        this.blueSlider.setOnSliderRangeMovedListener(new CircularSliderRange.OnSliderRangeMovedListener() {
            @Override public void onStartSliderMoved(double pos) { /* fixed */ }

            @Override public void onEndSliderMoved(double pos) {
                endAngleBlue = 90 + pos;
            }

            @Override public void onStartSliderEvent(ThumbEvent event) { }

            @Override
            public void onEndSliderEvent(ThumbEvent event) {
                updateColor();
            }
        });

        updateColor();

    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {
        if (isAmbient()) {
            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));
        } else {
            mContainerView.setBackground(null);
        }
    }

    /**
     *  Method called when a slider stops moving
     *  Color RGB composants are computed
     *  You need to send them to the smartphone application using DataLayer API
     */
    private void updateColor() {

        int r = (int) Math.round(255 * ((endAngleRed   - startAngleRed)   % 360) / 360.0);
        int g = (int) Math.round(255 * ((endAngleGreen - startAngleGreen) % 360) / 360.0);
        int b = (int) Math.round(255 * ((endAngleBlue  - startAngleBlue)  % 360) / 360.0);

        /*  Utilisation des Data items pour partager les changements de couleurs depuis le wearable. */
        // Création de l'objet (=RGB) PutDataMapRequest en spécifiants
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/color");
        /* On ajoute les 3 compostantes de la couleur */
        putDataMapReq.getDataMap().putInt(R_KEY, r);
        putDataMapReq.getDataMap().putInt(G_KEY, g);
        putDataMapReq.getDataMap().putInt(B_KEY, b);
        /* Obtenir un PutDataRequest */
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        Task<DataItem> putDataTask = Wearable.getDataClient(this).putDataItem(putDataReq);

    }

}
