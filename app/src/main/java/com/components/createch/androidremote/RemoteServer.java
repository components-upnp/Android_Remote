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

import java.io.IOException;

/**
 * Created by IDA on 22/02/2017.
 */

public class RemoteServer {
    public static LocalDevice createDevice()
            throws ValidationException, LocalServiceBindingException, IOException {

        /**
         * Description du Device
         */
        DeviceIdentity identity =
                new DeviceIdentity(
                        UDN.uniqueSystemIdentifier("Remote")
                );

        DeviceType type =
                new UDADeviceType("TypeRemote", 1);

        DeviceDetails details =
                new DeviceDetails(
                        "Telecommande Android",					// Friendly Name
                        new ManufacturerDetails(
                                "CreaTech",								// Manufacturer
                                ""),								// Manufacturer URL
                        new ModelDetails(
                                "AndroidRemote",						// Model Name
                                "Telecommande Android UPnP.",	// Model Description
                                "v1" 								// Model Number
                        )
                );


        // Ajout du service


        LocalService<RemoteController> remoteService =
                new AnnotationLocalServiceBinder().read(RemoteController.class);

        remoteService.setManager(
                new DefaultServiceManager(remoteService, RemoteController.class)
        );




        // retour en cas de 1 service
        return new LocalDevice(identity, type, details, remoteService);


		/* Si jamais plusieurs services pour un device (adapter code)
	    return new LocalDevice(
	            identity, type, details,
	            new LocalService[] {switchPowerService, myOtherService}
	    );
	    */
    }
}
