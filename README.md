# Android_Remote
Télécommande Android avec une interface réseau UPnP

Description:
Application Android dont l'interface est composée d'une croix directionnelle, d'un bouton central et d'un curseur.
Cette application utilise le protocole UPnP afin d'envoyer un évènements sur le réseau, à d'autres composants.
Par exemple, on pourrait faire interagir la télécommande Android avec un lecteur audio et utiliser les booutons pour changer
de piste, le curseur pour le volume et le bouton central pour la lecture/pause.


Lancement de l'application:

L'application ne peut pas communiquer via UPnP lorsque lancée dans un émulateur, elle doit être lancée sur un terminal 
physique et appartenir au même réseau local que les autres composants.

Il faut donc installer l'apk sur le terminal, vérifier d'avoir autorisé les sources non vérifiées.

Après démarrage de l'application, il est possible d'ajouter le composantsur wcomp en suivant la méthode décrite sur le wiki oppocampus.

ATTENTION: 
  -redémarrer l'application fait changer l'UID du composant, il faut donc le rajouter sur wcomp à nouveau.(ce problème sera réglé utltérieurement).
  -

Messages d'évènements:
L'application envoie un message évenementiel sous forme de chaîne de charactère (String), de la forme suivante(selon le bouton
qui a été cliqué):

  -DROITE
  -GAUCHE
  -BAS
  -HAUT
  -CENTRE
  
  -Une valeur de 0 à 100 sous forme de String pour le Slider.
    
  
