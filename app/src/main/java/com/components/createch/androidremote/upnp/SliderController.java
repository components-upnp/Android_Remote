package com.components.createch.androidremote.upnp;

import org.fourthline.cling.binding.annotations.UpnpAction;
import org.fourthline.cling.binding.annotations.UpnpInputArgument;
import org.fourthline.cling.binding.annotations.UpnpOutputArgument;
import org.fourthline.cling.binding.annotations.UpnpService;
import org.fourthline.cling.binding.annotations.UpnpServiceId;
import org.fourthline.cling.binding.annotations.UpnpServiceType;
import org.fourthline.cling.binding.annotations.UpnpStateVariable;

import java.beans.PropertyChangeSupport;

/**
 * Created by IDA on 28/02/2017.
 */

/*Classe contenant le service UPnP offert par l'application
* On y retrouve les différentes méthodes accessibles via le réseaux*/


//Définition du service UPnP, ID + Type

@UpnpService(
        serviceId = @UpnpServiceId("SliderController"),
        serviceType = @UpnpServiceType(value = "SliderController", version = 1)
)

public class SliderController {


        private final PropertyChangeSupport propertyChangeSupport;

        public SliderController() {
            this.propertyChangeSupport = new PropertyChangeSupport(this);
        }

        public PropertyChangeSupport getPropertyChangeSupport() {
            return propertyChangeSupport;
        }


        /**
         * Variable D'Etat, non �venemenc�e
         * Permet d'envoyer le message de l'�tat de la télécommande
         */
        @UpnpStateVariable(defaultValue = "0", sendEvents = false)
        private String target = "0";

        /**
         * Variable d'etat �venemmenc�e
         * Permet de v�rifier si la télécommande est bien dans le bon �tat.
         */
        @UpnpStateVariable(defaultValue = "0")
        private String status = "0";


        /**
         * Permet d'envoyer un message de changement d'etat de télécommande
         *
         * @param newTargetValue
         */
        @UpnpAction
        public void setTarget(@UpnpInputArgument(name = "NewTargetValue") String newTargetValue) {

            System.out.println("Changement");
            // [FACULTATIF] je garde la l'ancienne valeur pour emmettre l'evenenment
            String targetOldValue = target;
            target = newTargetValue;


            String statusOldValue = status;
            status = newTargetValue;

            // Envoie un �venement UPnP, c'est le nom de la variable d'etat qui lance l'�venement
            // COMMENCE PAR UNE MAJUSCULE. Ici "Status" pour la varialbe status
            getPropertyChangeSupport().firePropertyChange("Status", statusOldValue, status);



            // [FACULTATIF]
            // Ceci n'a pas d'effet pour le monitoring UPnP, mais fonctionne avec Javabean.
            // Ici on met le nom de la variable : status
            getPropertyChangeSupport().firePropertyChange("status", statusOldValue, status);



        }

        /**
         * Obtenir le target de la télécommande
         * Methode Upnp grace au syst�me d'annotation
         *
         * @return boolean
         */
        @UpnpAction(out = @UpnpOutputArgument(name = "RetTargetValue"))
        public String getTarget() {
            return target;
        }

        /**
         * Obtenir le status de la télécommande
         * Methode Upnp grace au syst�me d'annotation
         *
         * @return boolean
         */
        @UpnpAction(out = @UpnpOutputArgument(name = "ResultStatus"))
        public String getStatus() {
            // Pour ajouter des informations suppl�mentaires UPnP en cas d'erreur :
            // throw new ActionException(ErrorCode.ACTION_NOT_AUTHORIZED);
            return status;
        }

        /**
         * Print the version of the code
         * Ceci n'est pas une methode UPnP
         */
        public void printVersion() {
            System.out.println("Version : 1.0");
        }



}
