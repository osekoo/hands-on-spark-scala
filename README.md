# Spark and Scala

## Objectifs
L'objectif de cette session pratique est:
- d'implémenter un mini-projet Spark en [Scala](https://www.scala-lang.org/) en utilisant [IntelliJ](https://www.jetbrains.com/idea/),
- de tester et débugger l'application en utilisant l'utilitaire [SBT](https://www.scala-sbt.org/),
- de lancer un cluster [Spark](https://spark.apache.org/) sur une machine locale,
- de packager l'application et l'exécuter sur le cluster local Spark.

## Les prériquis (à faire avant la session)

### IntelliJ
#### Installation
#### Configuration
1. Scala
2. SBT

### Docker
Dans cette session, nous allons utiliser des [dockers](https://www.docker.com/) ([conteneurs d'applications](https://fr.wikipedia.org/wiki/Docker_\(logiciel\))) pour exécuter nos applications.  
Un docker permet d'exécuter une application sans se soucier de l'OS ni de ses dépendances.  
Les liens ci-dessous permettent d'installer Docker sur votre machine en fonction de l'OS:  
[Windows](https://docs.docker.com/docker-for-windows/install/) | [Ubuntu](https://docs.docker.com/engine/install/ubuntu/) | [Mac](https://docs.docker.com/docker-for-mac/install/)

### Docker-compose
Pour les utilisateus de Linux, vous avez besoin d'installer [docker-compose](https://www.digitalocean.com/community/tutorials/how-to-install-and-use-docker-compose-on-ubuntu-20-04). Pour les autres (Mac et Windows), cet utilitaire est déjà inclus dans Docker Desktop (installé plus haut).

## Lab Session

### Créer une application Scala

### Compiler l'application

### Tester et débugger l'application

### Lancer le cluster spark
Télécharger le fichier docker-compose.yaml (https://raw.githubusercontent.com/osekoo/hands-on-spark-scala/main/spark/docker-compose.yaml). Il se trouve également dans le repo /spark.  
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


### Packger et déployer l'application
