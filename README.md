# Android_Remote
Télécommande Android avec une interface réseau UPnP

<strong><u>Description:</u></strong>
Application Android dont l'interface est composée d'une croix directionnelle, d'un bouton central et d'un curseur.
Cette application utilise le protocole UPnP afin d'envoyer un évènements sur le réseau, à d'autres composants.
Par exemple, on pourrait faire interagir la télécommande Android avec un lecteur audio et utiliser les booutons pour changer
de piste, le curseur pour le volume et le bouton central pour la lecture/pause.


<strong><u>Lancement de l'application:</u></strong>

L'application ne peut pas communiquer via UPnP lorsque lancée dans un émulateur, elle doit être lancée sur un terminal 
physique et appartenir au même réseau local que les autres composants.

Il faut donc installer l'apk sur le terminal, vérifier d'avoir autorisé les sources non vérifiées.

Après démarrage de l'application, il est possible d'ajouter le composantsur wcomp en suivant la méthode décrite sur le wiki oppocampus.

Voici un exemple d'exécution de l'application:

![alt tag](https://github.com/components-upnp/Android_Remote/blob/master/CaptureTelecommande.png)

ATTENTION: 
  -redémarrer l'application fait changer l'UID du composant, il faut donc le rajouter sur wcomp à nouveau.(ce problème sera réglé utltérieurement).
  
  
<strong><u>Spécification UPnP:</u></strong>

La télécommande est composée de deux composants UPnP, Android Remote Controller(pour les boutons) et Android Slider Controller (pour le slider). Ces composants offrent chacun un seul service, respectivement RemoteController et SliderController, dont voici les decriptions:

RemoteController:

  1) GetStatus(): retourne le statut courant

Ce composant envoie un événement UPnP StatusEvent avec une chaîne de caractère correspondat au bouton qui a été cliqué, à savoir:
  -DROITE
  -GAUCHE
  -BAS
  -HAUT
  -CENTRE


SliderController:

  1) GeetStatus(): retourne la valeur courante du slider.
  
Ce composant envoie un événement UPnP StatusEvent, avec un entier, allant de 0 à 100 correspondant à la valeur du slider.

Voici le schéma correspondant à la télécommande, composée des composants Android Slider Controller et Android Remote Controller:

![alt tag](https://github.com/components-upnp/Android_Remote/blob/master/telecommande(1).png)

  
<strong><u>Maintenance:</u></strong>

Le projet de l'application est projet gradle.
  
