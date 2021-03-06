# Spark et Scala: Session Pratique

## Objectifs
L'objectif de cette session pratique est:
- d'implémenter un mini-projet [Apache Spark](https://spark.apache.org/) en [Scala](https://www.scala-lang.org/) en utilisant [IntelliJ](https://www.jetbrains.com/idea/),
- de compiler/tester/débugger l'application en utilisant l'utilitaire [SBT](https://www.scala-sbt.org/),
- de lancer un cluster Spark sur une machine locale,
- de packager l'application et l'exécuter sur le cluster local Spark.

## Les prérequis (à faire avant la session)

### Installation et configuration d'`IntelliJ`
[`IntelliJ IDEA`](https://www.jetbrains.com/idea/download/) est un [IDE](https://fr.wikipedia.org/wiki/Environnement_de_d%C3%A9veloppement) permettant de développer des logiciels dans plusieurs langages notamment `Java` et `Scala`.  
Nous allons l'utiliser lors de cette session pratique car il intègre plusieurs outils qui facilitent le développement des applications en Scala.  
  
La version `IntelliJ Community` est disponible [ici](https://www.jetbrains.com/idea/download/).  
Téléchargez et installez la version compatible avec votre machine.  
Commande spéciale pour `Ubuntu 16.04` ou plus:
```
sudo snap install intellij-idea-community --classic
```
Note: votre compte étudiant de Dauphine vous donne accès gratuitement à la version `Ultimate`. Pour cela, il suffit de vous [enregistrer](https://account.jetbrains.com/login) avec votre adresse mail de Dauphine et de valider l'inscription.  
  
Nous allons maintenant configurer `IntelliJ` avec les versions de Spark, Java, Scala et SBT qui sont compatibles entre elles.  
Il y a une relation très forte entre les versions de ces langages/frameworks/outils: `Spark ==> Scala ==> (Java, SBT)`.  
e.g: `Spark 3.0.2 ==> Scala 2.12.x ==> (Java 8, SBT 1.x)`
  
Dans cette session, nous allons utiliser [Spark 3.0.2](https://spark.apache.org/docs/3.0.2/).  

1. Scala (2.12.x)
2. Java (1.8)
3. SBT (1.x)

### Docker
Dans cette session, pour des raisons pratiques, nous allons utiliser des [dockers](https://www.docker.com/) ([conteneurs d'applications](https://fr.wikipedia.org/wiki/Docker_\(logiciel\))) pour exécuter nos applications.  
Un docker est un conteneur d'applications permettant d'exécuter une application indépendamment du système d'exploitation et de ses dépendances.  
L'application est packagée d'une façon autonome (avec toutes ses dépendances) pour être exécutée sur n'importe quel système.  
Les liens ci-dessous vous guident dans l'installation de Docker sur votre machine locale:  
[Windows](https://docs.docker.com/docker-for-windows/install/) | [Ubuntu](https://docs.docker.com/engine/install/ubuntu/) | [Mac](https://docs.docker.com/docker-for-mac/install/).  
Veuillez réaliser l'installation avant la session.

### Docker-compose
Les utilisateurs de Linux ont besoin d'installer [docker-compose](https://www.digitalocean.com/community/tutorials/how-to-install-and-use-docker-compose-on-ubuntu-20-04). Pour les autres (Mac et Windows), cet utilitaire est déjà inclus dans Docker Desktop (installé plus haut).
  

## Lab Session

### Créer une application Scala

### Compiler l'application

### Tester et débugger l'application

### Lancer le cluster spark
Télécharger le fichier [docker-compose.yaml](https://raw.githubusercontent.com/osekoo/hands-on-spark-scala/main/spark/docker-compose.yaml). Il se trouve également dans le repo /spark.  
Sauvegarder ce fichier dans un répertoire sur lequel vous avez les droits (par exemple myworkspace/spark)  
Aller dans ce répertoire et exécuter la commande
```
docker-compose up
```
Une fois le script lancé, allez à l'adresse http://localhost:8080/ pour voir l'état de votre cluster spark. Vous devez voir le master et un worker.  
Pour augmenter le nombre de workers (scale up), il suffit de lancer la commande suivante:
```
docker-compose up --scale spark-worker=5
```
où `5` correspond au nombre de workers que vous souhaitez lancer. Vous pouvez varier ce nombre et voir l'impact sur le cluster.


### Packager et déployer l'application
