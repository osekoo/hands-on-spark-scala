## Sommaire
1. [Objectifs](#objectifs)
2. [Prérequis](#prérequis)
3. [Lab Session](#lab-session)

## Objectifs
L'objectif de cette session pratique est:
- d'implémenter un mini-projet [Apache Spark](https://spark.apache.org/) en [Scala](https://www.scala-lang.org/) en utilisant [IntelliJ](https://www.jetbrains.com/idea/),
- de compiler/tester/débugger l'application en utilisant l'utilitaire [SBT](https://www.scala-sbt.org/),
- de lancer un cluster Spark sur une machine locale,
- de packager l'application et l'exécuter sur le cluster local Spark.

## Prérequis
_A faire avant la session_

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

1. ### Scala (2.12.x)
- Lancez IntelliJ,
- cliquez sur `Plugins` ou (`Configure > Plugins`),
- dans la barre de recherche, tapez `scala` et installer le plugin tel indiqué ci-dessous
  ![image](https://user-images.githubusercontent.com/49156499/110211898-08f06380-7e99-11eb-9aed-22566cd25788.png)
- redémarrez IntelliJ

2. ### SBT (1.x)  
   Installez SBT en suivant les mêmes étapes que ci-dessus
   ![image](https://user-images.githubusercontent.com/49156499/110212000-916f0400-7e99-11eb-9e24-680b14bbbfc0.png)

3. ### Java (1.8)  
A installer plus tard avec IntelliJ.  
_Note_:
Scala requiert une version de JDK 1.8 ou supérieure. La matrice de compatibilité entre Scala et JDK est disponible [ici](https://docs.scala-lang.org/overviews/jdk-compatibility/overview.html).
   Pour des raisons pratiques, nous allons utiliser la version 1.8 dans ce lab. Veuillez noter que la version du JDK doit être en ligne avec celle supportée par [Spark](https://docs.qubole.com/en/latest/user-guide/engines/spark/spark-supportability.html).

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

### Mise en route
1. Télécharger l'archive du projet disponible sur GitHub à l'adresse https://github.com/osekoo/hands-on-spark-scala/tree/develop  
2. Extraire le contenu de l'archive
3. Démarrer IntelliJ,
4. Dans le menu `File` cliquer sur `Open`. Naviguez jusqu'au répertoire `get-started` et ouvrez-le.

Le projet devra s'initiliser, SBT reconnu et les dépendances téléchargées.

### Implémentation
Comme exemple, nous allons implémenter le programme `WordCount` en utilisant `Spark SQL`.  
Ouvrez le fichier `src\main\scala\WordCount.scala` pour prendre connaissance de l'implémentation.  
Ouvrez également le fichier build.sbt et analysez le contenu.  


### Compilation et exécution
Dans cette section vous allez lancer sur votre machine locale le cluster spark en utilisant Docker.  
#### Lancement du master
Ouvrez un terminal et exécutez la commande suivante:
```
docker-compose up spark-master
```
Une fois cette commande exécutée, vous pouvez lancer le dashboard de spark disponible à l'adresse http://localhost:8080/. Le dashboard affiche l'état du cluster y compris le nombre de workers (pour l'instant 0), le nombre d'applications en cours d'exécution et le nombre d'applications terminées.  

#### Lancement des workers
Ouvrez un autre terminal et exécutez la commande suivante:
```
docker-compose up spark-worker
```
Cette commande lance un worker et le connecte au cluster.  
Maintenant si vous consultez le dashboard vous devrez voir un worker dispnible.  En cliquant sur la lien worker-XXXX vous pouvez consulter les détail du worker.  
Il est possible d'augment le nombre de workers. Pour ce faire, il suffit d'éditer le fichier docker-compose.yml et modifier la ligne replicas: 1` en remplaçant le `1` par le nombre de workers désirés.  Il faut ensuite arrêter spark-worker avec la commande Ctl+C dans le terminal et relancer la commande.  

#### Exécution du job (application wordcount)
Ouvrez un autre terminal et exécutez la commande suivante:
```
docker-compose up spark-submit
```
Cette commande compile l'application (comportement par défaut) et l'exécute sur le cluster sark précédemment lancé.  
Pour ce projet, vous otenez l'affiche des dataframes sur la console.  
Le fichier compilé se retrouve également dans le répertoire `./target/scala-2.12`. Ce qui vous permet plus tard de le copier et de l'exéuter sur un serveur distant.


### Packaging (manuel, sans docker)
IntelliJ reconnait automatiquement les projets Scala et adapte l'environnement en conséquence.  
Vous trouverez en bas un onglet intitulé "sbt shell". Il suffit d'ouvrir cet onglet pour lancer le moteur SBT.  
La commande `compile` permet de compiler votre application.  
La commande `package` permet de créer le package de l'application qui sera exécuté sur Spark.
Elle produit un fichier `jar` qui est mis dans la racine du répertoire `target/scala-<scala_version>` et porte le nom `<project_name>_<scala_version>_<application_version>.jar`.
Parfois il est nécessaire d'exécuter la commande `clean` avant de compiler ou de packager votre programme.

