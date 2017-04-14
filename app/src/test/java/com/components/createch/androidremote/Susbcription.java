package com.components.createch.androidremote;


import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.controlpoint.ActionCallback;
import org.fourthline.cling.controlpoint.SubscriptionCallback;
import org.fourthline.cling.model.UnsupportedDataException;
import org.fourthline.cling.model.action.ActionArgumentValue;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.gena.CancelReason;
import org.fourthline.cling.model.gena.GENASubscription;
import org.fourthline.cling.model.gena.RemoteGENASubscription;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.message.header.STAllHeader;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.state.StateVariableValue;
import org.fourthline.cling.model.types.InvalidValueException;
import org.fourthline.cling.model.types.ServiceId;
import org.fourthline.cling.model.types.UDAServiceId;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.registry.RegistryListener;

import java.util.ArrayList;
import java.util.Map;


/**
 * Created by comkostiuk on 14/04/2017.
 */


/*Afin de lancer cette suite de tests, il faut avoir préalablement lancé l'application sur le terminal Android
* Le terminal et la machine exécutant les tests doivent être sur le même réseau
* */
public class Susbcription  {

    private String serviceName;
    private String status = new String("lol");
    private final UpnpService upnpService = new UpnpServiceImpl();
    private org.fourthline.cling.model.meta.RemoteService switchPower;


    public Susbcription(String name) {
        this.serviceName = name;
    }

    //Permet de lancer un service UPnP et de chercher le device sur le réseau

    public void run() {
        try {

            upnpService.getRegistry().addListener(
                    createRegistryListener(upnpService)
            );

            // Broadcast a search message for all devices
            upnpService.getControlPoint().search(
                    new STAllHeader()
            );
        } catch( Exception e) {
            System.err.println("Exception Occured: " + e);
            System.exit(1);
        }
    }

    //Lance un évèenement lorsque le device est découvert
    private RegistryListener createRegistryListener(final UpnpService upnpService) {
        return new DefaultRegistryListener() {

            ServiceId serviceId = new UDAServiceId(serviceName);

            @Override
            public void remoteDeviceAdded(Registry registry, RemoteDevice device) {


                if ((switchPower = device.findService(serviceId)) != null) {

                    System.out.println("Service discovered: " + switchPower);

                    //callback permet de s'inscrire à un device et de traiter les évènements en
                    //provenance de ce dernier
                    SubscriptionCallback callback = new SubscriptionCallback(switchPower, 600) {

                        @Override
                        public void established(GENASubscription sub) {
                            System.out.println("Established: " + sub.getSubscriptionId());
                        }

                        @Override
                        protected void failed(GENASubscription subscription,
                                              UpnpResponse responseStatus,
                                              Exception exception,
                                              String defaultMsg) {
                            System.err.println(defaultMsg);
                        }

                        @Override
                        public void ended(GENASubscription sub,
                                          CancelReason reason,
                                          UpnpResponse response) {
                        }

                        @Override
                        public synchronized void eventReceived(GENASubscription sub) {

                            System.out.println("Event: " + sub.getCurrentSequence().getValue());

                            Map<String, StateVariableValue> values = sub.getCurrentValues();
                            status = values.get("Status").toString();



                            System.out.println("Status is: " + status.toString());

                        }

                        @Override
                        public void eventsMissed(GENASubscription sub, int numberOfMissedEvents) {
                            System.out.println("Missed events: " + numberOfMissedEvents);
                        }

                        @Override
                        protected void invalidMessage(RemoteGENASubscription sub,
                                                      UnsupportedDataException ex) {
                            // Log/send an error report?
                        }
                    };

                    upnpService.getControlPoint().execute(callback);



                }

            }

            @Override
            public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
                org.fourthline.cling.model.meta.RemoteService switchPower;
                if ((switchPower = device.findService(serviceId)) != null) {
                    System.out.println("Service disappeared: " + switchPower);
                }
            }

        };
    }

    //Permet d'exécuter une action sur le device distant
    // et de recuperer la reponse du serveur
    ArrayList<Object> executeAction(String action,
                            String valueName,
                            Object value) {

        final ArrayList<Object>  reponse = new ArrayList<>();
        ActionInvocation setTargetInvocation =
                new SetTargetActionInvocation(action, valueName, value);
        // Executes asynchronous in the background
        upnpService.getControlPoint().execute(
                new ActionCallback(setTargetInvocation) {

                    @Override
                    public void success(ActionInvocation invocation) {
                        //assert invocation.getOutput().length == 0;

                        if (invocation.getOutput().length > 0) {
                            for (ActionArgumentValue<?> object : invocation.getOutput())
                                reponse.add(object);
                        }
                        System.out.println("Successfully called action!");
                    }

                    @Override
                    public void failure(ActionInvocation invocation,
                                        UpnpResponse operation,
                                        String defaultMsg) {
                        System.err.println(defaultMsg);
                    }
                }
        );

        return reponse;

    }

    class SetTargetActionInvocation extends ActionInvocation {

        SetTargetActionInvocation(String action,
                                  String valueName,
                                  Object value) {

            super(switchPower.getAction(action));
            try {

                // Throws InvalidValueException if the value is of wrong type
                if (valueName != null)
                    setInput(valueName,value);

            } catch (InvalidValueException ex) {
                System.err.println(ex.getMessage());
                System.exit(1);
            }
        }
    }

    public String getStatus() {
        return status;
    }

    public UpnpService getUpnpService() {
        return upnpService;
    }


}
