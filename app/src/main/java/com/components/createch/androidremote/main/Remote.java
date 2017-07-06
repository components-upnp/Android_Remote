package com.components.createch.androidremote.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;

import com.components.createch.androidremote.R;
import com.components.createch.androidremote.upnp.Service;
import com.components.createch.androidremote.upnp.State;
import com.components.createch.androidremote.xml.GenerateurXml;

import org.fourthline.cling.android.AndroidUpnpServiceImpl;

import java.io.File;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import xdroid.toaster.Toaster;

import static com.components.createch.androidremote.R.id.seekBar;

public class Remote extends AppCompatActivity  {



    private ServiceConnection serviceConnection;
    private Service service;
    private Vibrator vib;
    private GenerateurXml gen;

    private static final int MY_PERMISSIONS_REQUEST_STORAGE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        //Vérification que l'autorisation d'accès au système de stockage est accrodée
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //Cela signifie que la permission à déjà était
                //demandé et l'utilisateur l'a refusé
                //Vous pouvez aussi expliquer à l'utilisateur pourquoi
                //cette permission est nécessaire et la redemander
                Toaster.toast("Vous avez refusé l'accés au Stockage, fermeture");
                finish();
            } else {
                //Sinon demander la permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_STORAGE);
            }
        }
        else {
            //Permission déjà accrodée
            init();
        }

    }

    public void init() {
        File dir;
        System.err.println(Build.BRAND);
        //Création dossier composant AndroidRemote
        if (Build.BRAND.toString().equals("htc_europe"))
            dir = new File("/mnt/emmc/AndroidRemote/");
        else
            dir = new File(Environment.getExternalStorageDirectory().getPath() + "/AndroidRemote/");

        while (!dir.exists()) {
            dir.mkdir();
            dir.setReadable(true);
            dir.setExecutable(true);
            dir.setWritable(true);
        }

        //Création dossier composant AndroidSlider
        if (Build.BRAND.toString().equals("htc_europe"))
            dir = new File("/mnt/emmc/AndroidSlider/");
        else
            dir = new File(Environment.getExternalStorageDirectory().getPath() + "/AndroidSlider/");

        while (!dir.exists()) {
            dir.mkdir();
            dir.setReadable(true);
            dir.setExecutable(true);
            dir.setWritable(true);
        }

        gen = new GenerateurXml();

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
                try {
                    String com = getXmlSlider(progress.toString());
                    service.getSliderControllerService().getManager().getImplementation().setTarget(com);
                } catch (TransformerException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }

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

    public void clicGauche(View view) throws TransformerException, ParserConfigurationException {
        service.getRemoteControllerService().getManager().getImplementation().setTarget(getXmlButton(State.GAUCHE.toString()));
        service.getRemoteControllerService().getManager().getImplementation().setTarget(getXmlButton(State.AUCUN.toString()));
        vibration();
    }

    public void clicDroit(View view) throws TransformerException, ParserConfigurationException {
        service.getRemoteControllerService().getManager().getImplementation().setTarget(getXmlButton(State.DROITE.toString()));
        service.getRemoteControllerService().getManager().getImplementation().setTarget(getXmlButton(State.AUCUN.toString()));
        vibration();
    }

    public void clicHaut(View view) throws TransformerException, ParserConfigurationException {
        service.getRemoteControllerService().getManager().getImplementation().setTarget(getXmlButton(State.HAUT.toString()));
        service.getRemoteControllerService().getManager().getImplementation().setTarget(getXmlButton(State.AUCUN.toString()));
        vibration();
    }

    public void clicBas(View view) throws TransformerException, ParserConfigurationException {
        service.getRemoteControllerService().getManager().getImplementation().setTarget(getXmlButton(State.BAS.toString()));
        service.getRemoteControllerService().getManager().getImplementation().setTarget(getXmlButton(State.AUCUN.toString()));
        vibration();
    }

    public void clicCentre(View view) throws TransformerException, ParserConfigurationException {
        service.getRemoteControllerService().getManager().getImplementation().setTarget(getXmlButton(State.CENTRE.toString()));
        service.getRemoteControllerService().getManager().getImplementation().setTarget(getXmlButton(State.AUCUN.toString()));
        vibration();
    }

    public String getXmlButton(String commande) throws TransformerException, ParserConfigurationException {
        return gen.getDocXml(service.getUdnButton().toString(), commande);
    }

    public String getXmlSlider(String commande) throws TransformerException, ParserConfigurationException {
        return gen.getDocXml(service.getUdnSlider().toString(), commande);
    }


    //Reçoit et traite la réponse de l'utilisateur à la demande d'accès au stockage
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // La permission est garantie on initialise les services et boutons
                    init();
                } else {
                    Toaster.toast("Permission refusée, fermeture");
                    finish();
                }
                return;
            }
        }
    }

}
