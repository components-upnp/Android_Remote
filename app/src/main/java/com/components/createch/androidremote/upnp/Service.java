package com.components.createch.androidremote.upnp;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.LocalService;
import org.fourthline.cling.model.types.UDAServiceType;
import org.fourthline.cling.model.types.UDN;

import java.io.IOException;
import java.util.UUID;

import xdroid.toaster.Toaster;

/**
 * Created by IDA on 28/02/2017.
 */

public class Service {

    private AndroidUpnpService upnpService;
    private UDN udnButton;
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
                            udnButton = new SaveUDN().getUdn("AndroidRemote");
                            LocalDevice remoteDevice = RemoteButtonsDevice.createDevice(udnButton);

                            upnpService.getRegistry().addDevice(remoteDevice);

                        } catch (Exception ex) {
                            System.err.println("Creating Android remote controller device failed !!!");
                            Toaster.toast("ERREUR CREATION");
                            return;
                        }
                    }

                    try {
                        udnSlider = new SaveUDN().getUdn("AndroidSlider");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    LocalService<SliderController> sliderControllerService = getSliderControllerService();

                    if (sliderControllerService == null) {
                        try {


                            LocalDevice remoteDevice = RemoteSliderDevice.createDevice(udnSlider);

                            upnpService.getRegistry().addDevice(remoteDevice);

                        } catch (Exception ex) {
                            System.err.println("Creating Android remote controller device failed !!!");
                            Toaster.toast("ERREUR CREATION");
                            return;
                        }
                    }

                }

                public void onServiceDisconnected(ComponentName className) {
                    upnpService = null;
                }
            };


        }

    public ServiceConnection getService() {
        return this.serviceConnection;
    }


    public UDN getUdnButton() {
        return udnButton;
    }

    public UDN getUdnSlider() {
        return udnSlider;
    }

    public LocalService<RemoteController> getRemoteControllerService() {
        if (upnpService == null)
            return null;

        LocalDevice remoteDevice;
        if ((remoteDevice = upnpService.getRegistry().getLocalDevice(udnButton, true)) == null)
            return null;

        return (LocalService<RemoteController>)
                remoteDevice.findService(new UDAServiceType("RemoteController", 1));
    }

    public LocalService<SliderController> getSliderControllerService() {
        if (upnpService == null)
            return null;

        LocalDevice remoteDevice;
        if ((remoteDevice = upnpService.getRegistry().getLocalDevice(udnSlider, true)) == null)
            return null;

        return (LocalService<SliderController>)
                remoteDevice.findService(new UDAServiceType("SliderController", 1));
    }
}
