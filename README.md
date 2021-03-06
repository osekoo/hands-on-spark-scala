# Hands-on Spark and Scala

L'objectif de cette session pratique est de réaliser la compilation et le packaging des applications Spark développées en Scala en utilisant SBT sous IntelliJ.  

1. Installer docker
Nous allons utiliser des dockers (conteneurs d'applications) pour faire tourner nos application.  
Un docker permet d'exécuter une application sans soucier de l'OS ni des dépendances.  
Les liens ci-dessous permettent d'installer Docker sur votre machine en fonction de l'OS:  
a. Windows: https://docs.docker.com/docker-for-windows/install/  
b. Ubuntu: https://docs.docker.com/engine/install/ubuntu/  
c. Mac: https://docs.docker.com/docker-for-mac/install/  

2. Installer docker-compose
Linux: https://www.digitalocean.com/community/tutorials/how-to-install-and-use-docker-compose-on-ubuntu-20-04  
Windows: inclus dans Docker Desktop  
Mac: inclus dans Docker Desktop  

3. Lancer le cluster spark avec le fichier docker-compose de spark (dans ce repo)
Télécharger le fichier docker-compose.yaml (https://raw.githubusercontent.com/PersonalDSAI/scala-hands-on/main/spark/docker-compose.yaml). Il se trouve également dans le repo /spark  
Sauvegarder ce fichier dans un répertoire sur lequel vous avez les droits (par exemple myworkspace/spark)  
Aller dans ce répertoire et exécuter la commande
```
docker-compose up
```
Une fois le script lancé, aller à l'adresse http://localhost:8080/ pour l'état de votre cluster spark. Vous devez voir le master et un worker.  
Pour augmenter le nombre de worker, il suffit de lancer la commande suivante:
```
docker-compose up --scale spark-worker=5
```
où 5 correspond on nombre de workers que vous souhaitez lancer. Vous pouvez varier ce nombre et voir l'impact sur http://localhost:8080/  

4.C
