package com.components.createch.androidremote;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.controlpoint.SubscriptionCallback;
import org.fourthline.cling.model.VariableValue;
import org.fourthline.cling.model.gena.CancelReason;
import org.fourthline.cling.model.gena.GENASubscription;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.LocalService;
import org.fourthline.cling.model.meta.StateVariable;
import org.fourthline.cling.model.types.UDAServiceType;
import org.fourthline.cling.model.types.UDN;

import java.util.Map;
import java.util.UUID;

import static junit.framework.Assert.assertNull;

/**
 * Created by IDA on 28/02/2017.
 */

public class Service {

    private AndroidUpnpService upnpService;
    private UDN udnButton;  // TODO: Not stable! Sauvegarder l'UUID dans un fichier après la première génération
    private UDN udnSlider;
    private ServiceConnection serviceConnection;







        public Service() {
            serviceConnection = new ServiceConnection() {


                public void onServiceConnected(ComponentName className, IBinder service) {
                    upnpService = (AndroidUpnpService) service;


                    LocalService<RemoteController> remoteControllerService = getRemoteControllerService();

                    // Register the device when this activity binds to the service for the first time
                    if (remoteControllerService == null) {
                        try {
                            udnButton = new UDN(UUID.randomUUID());
                            LocalDevice remoteDevice = RemoteButtonsDevice.createDevice(udnButton);

                            upnpService.getRegistry().addDevice(remoteDevice);

                        } catch (Exception ex) {
                            System.err.println("Creating Android remote controller device failed !!!");
                            return;
                        }
                    }

                    udnSlider = new UDN(UUID.randomUUID());
                    LocalService<SliderController> sliderControllerService = getSliderControllerService();

                    if (sliderControllerService == null) {
                        try {


                            LocalDevice remoteDevice = RemoteSliderDevice.createDevice(udnSlider);

                            upnpService.getRegistry().addDevice(remoteDevice);


                        } catch (Exception ex) {
                            System.err.println("Creating Android remote controller device failed !!!");
                            return;
                        }
                    }
                    System.out.println("Creation device reussie...");

                }

                public void onServiceDisconnected(ComponentName className) {
                    upnpService = null;
                }
            };




        }

    public ServiceConnection getService() {
        return this.serviceConnection;
    }




    protected LocalService<RemoteController> getRemoteControllerService() {
        if (upnpService == null)
            return null;

        LocalDevice remoteDevice;
        if ((remoteDevice = upnpService.getRegistry().getLocalDevice(udnButton, true)) == null)
            return null;

        return (LocalService<RemoteController>)
                remoteDevice.findService(new UDAServiceType("RemoteController", 1));
    }

    protected LocalService<SliderController> getSliderControllerService() {
        if (upnpService == null)
            return null;

        LocalDevice remoteDevice;
        if ((remoteDevice = upnpService.getRegistry().getLocalDevice(udnSlider, true)) == null)
            return null;

        return (LocalService<SliderController>)
                remoteDevice.findService(new UDAServiceType("SliderController", 1));
    }

    protected AndroidUpnpService getUpnpService() {
        return this.upnpService;
    }

    public UDN getUdnSlider() {
        return udnSlider;
    }
}
