package com.components.createch.androidremote;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;

import org.fourthline.cling.android.AndroidUpnpServiceImpl;

import static com.components.createch.androidremote.R.id.seekBar;

public class Remote extends AppCompatActivity  {



    private ServiceConnection serviceConnection;
    private Service service;
    Vibrator vib;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);

        this.service = new Service();
        this.serviceConnection = this.service.getService();

        getApplicationContext().bindService(
                new Intent(this, AndroidUpnpServiceImpl.class),
                serviceConnection,
                Context.BIND_AUTO_CREATE
        );


        final SeekBar slider = (SeekBar) findViewById(seekBar);

        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                progressChanged = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Integer progress = slider.getProgress();
                service.getSliderControllerService().getManager().getImplementation().setTarget(progress.toString());
            }
        });

        vib = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
    }


    public void vibration(){
        vib.vibrate(19);
    }


    //Dans les fonctions qui suivent, on envoie un premier message pour avertir du changement d'état
    //On envoie ensuite un autre message pour revenir sur l'état AUCUN
    //Cela permet de faire deux fois la même actions (deux fois DROITE par exemple)

    public void clicGauche(View view) {
        service.getRemoteControllerService().getManager().getImplementation().setTarget(State.GAUCHE.toString());
        service.getRemoteControllerService().getManager().getImplementation().setTarget(State.AUCUN.toString());
        vibration();
    }

    public void clicDroit(View view) {
        service.getRemoteControllerService().getManager().getImplementation().setTarget(State.DROITE.toString());
        service.getRemoteControllerService().getManager().getImplementation().setTarget(State.AUCUN.toString());
        vibration();
    }

    public void clicHaut(View view) {
        service.getRemoteControllerService().getManager().getImplementation().setTarget(State.HAUT.toString());
        service.getRemoteControllerService().getManager().getImplementation().setTarget(State.AUCUN.toString());
        vibration();
    }

    public void clicBas(View view) {
        service.getRemoteControllerService().getManager().getImplementation().setTarget(State.BAS.toString());
        service.getRemoteControllerService().getManager().getImplementation().setTarget(State.AUCUN.toString());
        vibration();
    }

    public void clicCentre(View view) {
        service.getRemoteControllerService().getManager().getImplementation().setTarget(State.CENTRE.toString());
        service.getRemoteControllerService().getManager().getImplementation().setTarget(State.AUCUN.toString());
        vibration();
    }

}
