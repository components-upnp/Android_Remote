package com.components.createch.androidremote;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.android.AndroidUpnpServiceImpl;
import org.fourthline.cling.binding.LocalServiceBindingException;
import org.fourthline.cling.binding.annotations.AnnotationLocalServiceBinder;
import org.fourthline.cling.model.DefaultServiceManager;
import org.fourthline.cling.model.ValidationException;
import org.fourthline.cling.model.meta.DeviceDetails;
import org.fourthline.cling.model.meta.DeviceIdentity;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.LocalService;
import org.fourthline.cling.model.meta.ManufacturerDetails;
import org.fourthline.cling.model.meta.ModelDetails;
import org.fourthline.cling.model.types.DeviceType;
import org.fourthline.cling.model.types.UDADeviceType;
import org.fourthline.cling.model.types.UDAServiceType;
import org.fourthline.cling.model.types.UDN;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.UUID;
import java.util.logging.Level;

public class Remote extends AppCompatActivity implements PropertyChangeListener {

    private AndroidUpnpService upnpService;

    private UDN udn = new UDN(UUID.randomUUID()); // TODO: Not stable! Sauvegarder l'UUID dans un fichier après la première génération

    private ServiceConnection serviceConnection = new ServiceConnection() {


        public void onServiceConnected(ComponentName className, IBinder service) {
            upnpService = (AndroidUpnpService) service;

            LocalService<RemoteController> remoteControllerService = getRemoteControllerService();

            // Register the device when this activity binds to the service for the first time
            if (remoteControllerService == null) {
                try {
                    LocalDevice remoteDevice = createDevice();

                    upnpService.getRegistry().addDevice(remoteDevice);

                    remoteControllerService = getRemoteControllerService();

                } catch (Exception ex) {
                    System.err.println("Creating Android remote controller device failed !!!");
                    return;
                }
            }

           /* // Obtain the state of the power switch and update the UI
            setLightbulb(switchPowerService.getManager().getImplementation().getStatus());

            // Start monitoring the power switch
            switchPowerService.getManager().getImplementation().getPropertyChangeSupport()
                    .addPropertyChangeListener(LightActivity.this);*/

        }

        public void onServiceDisconnected(ComponentName className) {
            upnpService = null;
        }
    };


    protected LocalDevice createDevice()
            throws ValidationException, LocalServiceBindingException {

        DeviceType type =
                new UDADeviceType("RemoteController", 1);

        DeviceDetails details =
                new DeviceDetails(
                        "Android Remote Controller",
                        new ManufacturerDetails("Creative Technology"),
                        new ModelDetails("AndroidController", "a UPnP Android based remote controller", "v1")
                );

        LocalService service =
                new AnnotationLocalServiceBinder().read(RemoteController.class);

        service.setManager(
                new DefaultServiceManager<>(service, RemoteController.class)
        );

        return new LocalDevice(
                new DeviceIdentity(udn),
                type,
                details,

                service
        );
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);

        getApplicationContext().bindService(
                new Intent(this, AndroidUpnpServiceImpl.class),
                serviceConnection,
                Context.BIND_AUTO_CREATE
        );
    }

    public void clicGauche(View view) {
        getRemoteControllerService().getManager().getImplementation().setTarget(State.GAUCHE.toString());
        getRemoteControllerService().getManager().getImplementation().setTarget(State.AUCUN.toString());

    }

    public void clicDroit(View view) {
        getRemoteControllerService().getManager().getImplementation().setTarget(State.DROITE.toString());
        getRemoteControllerService().getManager().getImplementation().setTarget(State.AUCUN.toString());

    }

    public void clicHaut(View view) {
        getRemoteControllerService().getManager().getImplementation().setTarget(State.HAUT.toString());
        getRemoteControllerService().getManager().getImplementation().setTarget(State.AUCUN.toString());

    }

    public void clicBas(View view) {
        getRemoteControllerService().getManager().getImplementation().setTarget(State.BAS.toString());
        getRemoteControllerService().getManager().getImplementation().setTarget(State.AUCUN.toString());

    }

    public void clicCentre(View view) {
        getRemoteControllerService().getManager().getImplementation().setTarget(State.CENTRE.toString());
        getRemoteControllerService().getManager().getImplementation().setTarget(State.AUCUN.toString());

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        //TODO!!!
    }

    protected LocalService<RemoteController> getRemoteControllerService() {
        if (upnpService == null)
            return null;

        LocalDevice binaryLightDevice;
        if ((binaryLightDevice = upnpService.getRegistry().getLocalDevice(udn, true)) == null)
            return null;

        return (LocalService<RemoteController>)
                binaryLightDevice.findService(new UDAServiceType("RemoteController", 1));
    }
}
