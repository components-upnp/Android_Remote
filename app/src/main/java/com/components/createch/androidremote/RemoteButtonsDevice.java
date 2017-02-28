package com.components.createch.androidremote;

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
import org.fourthline.cling.model.types.UDN;

/**
 * Created by IDA on 28/02/2017.
 */

public class RemoteButtonsDevice {

    /*Fonction permettant la création du device UPnP, on définit son type,
   * lui ajoute des détails et lui donne une classe qui contient le service
   * UPnP
   * */
    static LocalDevice createDevice(UDN udn)
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
}
