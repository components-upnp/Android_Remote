# Android_Remote
Télécommande Android avec une interface réseau UpNp

Description:
Application Android dont l'interface est composée d'une croix directionnelle, d'un bouton central et d'un curseur.
Cette application utilise le protocole UPnP afin d'envoyer un évènements sur le réseau, à d'autres composants.
Par exemple, on pourrait faire interagir la télécommande Android avec un lecteur audio et utiliser les booutons pour changer
de piste, le curseur pour le volume et le bouton central pour la lecture/pause.


Spécificités techniques:

L'application ne peut pas communiquer via UPnP lorsque lancée dans un émulateur, elle doit être lancée sur un terminal 
physique et appartenir au même réseau local que les autres composants.

L'application envoie un message évenementiel sous forme de chaîne de charactère (String), de la forme suivante(selon le bouton
qui a été cliqué):

  -DROITE
  -GAUCHE
  -BAS
  -HAUT
  -CENTRE
  
  Interface UPnP:
  
  
  
  @UpnpService(
        serviceId = @UpnpServiceId("Selection"),                    // ID du service
        serviceType = @UpnpServiceType(value = "Selection", version = 1)  //Type du service
)



  @UpnpStateVariable(defaultValue = "AUCUNE", sendEvents = false) //Variable d'état non évenementielle de la selection, pour envoyer le message
    private Action target = Action.AUCUNE;

    
    @UpnpStateVariable(defaultValue = "AUCUNE")   //variable d'état évenementielle, permet de vérifier l'état de la séléction
    private Action status = Action.AUCUNE;

    //Cette fonction permet de changer la variable d'état et de lancer un évènement à destination de l'interface graphique

@UpnpAction
    public void setTarget(@UpnpInputArgument(name = "NewTargetValue") String newTargetValue);
    
    
    
     //retourne le statut de la sélection

    
@UpnpAction(out = @UpnpOutputArgument(name = "ResultStatus"))
    public Action getStatus() {
        // Pour ajouter des informations supplï¿½mentaires UPnP en cas d'erreur :
        // throw new ActionException(ErrorCode.ACTION_NOT_AUTHORIZED);
        return status;
    }
    
  
