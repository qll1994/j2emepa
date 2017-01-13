# j2emepa
projet j2e

====================================================================
                       Projet Chaos Monkey
====================================================================
Vous trouverez ci-dessous les procédures de création et d'accès à la
base de données MySQL utilisée pour l'application, ainsi que la
procédure de déployement de l'application sur un serveur Tomcat.

Dans la suite, la variable d'environnement CATALINA_BASE spécifie 
l'emplacement du répertoire racine de la "configuration active" du
serveur Tomcat utilisé.


====================================================================
          Procédure de création de la base de données
====================================================================
(1) Démarrer le serveur MySQL.
(2) Lancer la console MySQL.
(3) Importer la base de données "pannes" (dans la console, utiliser
la commande : source [répertoire]/pannes.sql; où [répertoire] désigne
le chemin du répertoire contenant le fichier pannes.sql).


====================================================================
       Procédure de modification d'accès à la base de données
====================================================================
Pour modifier l'accès à la base de données (ie utiliser la base de
donéée "pannes" d'un autre utilisateur), il faut modifier le fichier
config.properties du projet.

Le fichier se trouve dans le répertoire suivant :
$CATALINA_BASE/webapps/projet/WEB-INF/classes/

Editer le fichier config.properties en remplacer la valeur des
variables DB_LOGIN et DB_PASSWORD par le nom d'utilisateur et le mot
de passe d'accès à la nouvelle console.

Note : c'est également ce fichier qu'il faut éditer si vous souhaitez
utiliser une base de données Oracle.


====================================================================
  Procédure d'installation de l'application sur un serveur Tomcat
====================================================================
Si l'accès à l'application manager est configuré :
(1) Démarrer le serveur Tomcat.
(2) Ouvrir l'url http://localhost:8080/ dans un navigateur.
(3) Ouvrir l'application manager à l'aide du bouton Manager App (il
faut s'identifier pour accèder à l'application manager).
(4) Sélectionner le fichier projet.war à déployer (dans la partie
Deployer de l'application).
(5) Le projet apparait dans la liste des applications, il suffit de
cliquer sur le chemin pour le lancer.

Si l'accès à l'application manager n'est pas configuré :
(1) Copier/coller le fichier projet.war dans le répertoire 
suivant : $CATALINA_BASE/webapps/
(2) Démarrer le serveur Tomcat.
(3) Ouvrir l'url http://localhost:8080/projet dans un navigateur
pour lancer l'application (Tomcat déploie l'archive web
automatiquement).

