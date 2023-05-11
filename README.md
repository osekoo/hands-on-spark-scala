## Sommaire
1. [Objectifs](#objectifs)
2. [Prérequis](#prérequis)
3. [Lab Session](#lab-session)

### Edit 2023
Les versions suivantes doivent être utilisées dans la suite de ce tutoriel:
- Spark 3.4.0
- Scala 2.13.10
- Java 1.8

La procédure reste identique.

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

1. Scala (2.12.x)
- Lancez IntelliJ,
- cliquez sur `Plugins` ou (`Configure > Plugins`),
- dans la barre de recherche, tapez `scala` et installer le plugin tel indiqué ci-dessous
![image](https://user-images.githubusercontent.com/49156499/110211898-08f06380-7e99-11eb-9aed-22566cd25788.png)
- redémarrez IntelliJ

2. SBT (1.x)  
Installez SBT en suivant les mêmes étapes que ci-dessus
![image](https://user-images.githubusercontent.com/49156499/110212000-916f0400-7e99-11eb-9e24-680b14bbbfc0.png)

3. Java (1.8)  
A voir plus tard.

### Docker
Dans cette session, pour des raisons pratiques, nous allons utiliser des [dockers](https://www.docker.com/) ([conteneurs d'applications](https://fr.wikipedia.org/wiki/Docker_\(logiciel\))) pour exécuter nos applications.  
Un docker est un conteneur d'applications permettant d'exécuter une application indépendamment du système d'exploitation et de ses dépendances.  
L'application est packagée d'une façon autonome (avec toutes ses dépendances) pour être exécutée sur n'importe quel système.  
Les liens ci-dessous vous guident dans l'installation de Docker sur votre machine locale:  
[Windows](https://docs.docker.com/docker-for-windows/install/) | [Ubuntu](https://docs.docker.com/engine/install/ubuntu/) | [Mac](https://docs.docker.com/docker-for-mac/install/).  
Veuillez réaliser l'installation avant la session.

### Docker-compose
Les utilisateurs de Linux ont besoin d'installer [docker-compose](https://www.digitalocean.com/community/tutorials/how-to-install-and-use-docker-compose-on-ubuntu-20-04). Pour les autres (Mac et Windows), cet utilitaire est déjà inclus dans Docker Desktop (installé plus haut).

### Spark
Nous avons besoin d'installer Spark 3.0.2 en mode standalone afin de pouvoir utiliser la commande `spark-submit` pour envoyer nos jobs sur notre cluster local.  
Veuillez suivre les liens ci-dessous pour installer Spark sur votre machine en fonction de votre OS:  
- [Windows](http://www.xavierdupre.fr/app/sparkouille/helpsphinx/lectures/spark_install.html#installation-de-spark-sous-windows)
- [Linux](http://www.xavierdupre.fr/app/sparkouille/helpsphinx/lectures/spark_install.html#installation-de-spark-sous-linux)
- [Mac](https://notadatascientist.com/install-spark-on-macos/)
  
  
## Lab Session

### Mise en route
1. Démarrez IntelliJ,

2. cliquez sur `New Project`
![image](https://user-images.githubusercontent.com/49156499/110211719-2cff7500-7e98-11eb-8902-3225c7a02c7f.png)

3. sélectionnez `Scala` (dans le panel de à gauche) et `sbt` (dans celui de droite)
![image](https://user-images.githubusercontent.com/49156499/110212124-31c52880-7e9a-11eb-87f9-a57268f5fb71.png)

4. à l'étape suivante:
- donnez un nom à votre projet (`get-started`)
- sélectionnez la version 1.8 de `JDK` si elle est installée sinon déroulez la liste et cliquez sur `Download JDK` (comme indiquer ci-dessous)
![image](https://user-images.githubusercontent.com/49156499/110212389-6d142700-7e9b-11eb-9593-af827b9126b0.png)
- sélectionnez une version 1.x de SBT (e.g 1.4.7)
- sélectionnez la version 2.12.13 de Scala et cochez la case `Sources`
![image](https://user-images.githubusercontent.com/49156499/110212481-d136eb00-7e9b-11eb-979f-8ef8bd566393.png)

5. cliquez sur `Finish`

6. vous devez obtenir une structure identique à celle de la figure ci-dessous
![image](https://user-images.githubusercontent.com/49156499/110212536-0d6a4b80-7e9c-11eb-8bc3-0aa92a88f037.png)

### Configuration

- Ouvrez le fichier `build.sbt` qui se trouve dans le panel de gauche. Il doit contenir 3 lignes:  
```(scala)
name := "get-started" // le nom de votre projet
version := "0.1" // la version de votre application
scalaVersion := "2.12.13" // la version de Scala (l'information la plus importante!)
```

- Nous allons compléter ce fichier avec les dépendances de Spark (version 3.0.2) qui se trouvent dans le [dépôt Maven](https://mvnrepository.com/artifact/org.apache.spark). Ce dépôt contient tous les modules et frameworks Spark

- Pour notre use-case, nous allons utiliser les frameworks suivants:
    - [`Spark Core`](https://mvnrepository.com/artifact/org.apache.spark/spark-core_2.12/3.0.2) la libraire de base de Spark,
    - [`Spark SQL`](https://mvnrepository.com/artifact/org.apache.spark/spark-sql_2.12/3.0.2) pour les requêtes SQL,
    - [`Spark MLlib`](https://mvnrepository.com/artifact/org.apache.spark/spark-mllib_2.12/3.0.2) pour le machine learning

- Pour récupérer la bonne version d'un framework/module
    - allez sur le dépôt du framework et cliquez sur la version désirée (dans notre cas 3.0.2),
![image](https://user-images.githubusercontent.com/49156499/110213736-98017980-7ea1-11eb-89a4-363f1c294a66.png)
    - ensuite cliquez sur l'onglet `SBT` et copier le contenu de la zone de texte  
![image](https://user-images.githubusercontent.com/49156499/110213775-c1220a00-7ea1-11eb-88f8-4659443081d5.png)
    - collez ce contenu dans le fichier `build.sbt` comme indiqué ci-dessous  
```(scala)
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.0.2"
)
```
   
Au final, votre fichier `build.sbt` doit ressembler à ceci:   

```(scala)
name := "get-started" // le nom de votre projet
version := "0.1" // la version de votre application
scalaVersion := "2.12.13" // la version de Scala (l'information la plus importante!)

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.0.2",
  "org.apache.spark" %% "spark-sql" % "3.0.2",
  "org.apache.spark" %% "spark-mllib" % "3.0.2" % "provided"
)
```
![image](https://user-images.githubusercontent.com/49156499/110214679-02b4b400-7ea6-11eb-9703-16477da0a1d8.png)

Une fois ces dépendances ajoutées, IntelliJ va afficher un pop-up (en bas à gauche) avec 3 options, `Refresh` project, `Enable Auto-Import` et `Ignore`. Cliquez sur `Enable Auto-Import` de telle manière que IntelliJ télécharge automatiquement les dépendances à chaque mise-à-jour du fichier `build.sbt`.
  
Si toutefois le pop-up n'a pas été affiché, cliquez sur le panel `sbt` d'IntelliJ et ensuite sur l'icône `reload`
![image](https://user-images.githubusercontent.com/49156499/110214842-cf265980-7ea6-11eb-9d76-55de4c42f61a.png)
![image](https://user-images.githubusercontent.com/49156499/110214858-dc434880-7ea6-11eb-8a6b-3b6c75df497c.png)
  
IntelliJ affiche une erreur au cas où une dépendance n'a pas été trouvée.
![image](https://user-images.githubusercontent.com/49156499/110214930-3b08c200-7ea7-11eb-91fd-6659ac5785fd.png)


### Implémentation
Comme exemple, nous allons implémenter le programme `WordCount` en utilisant `Spark SQL`.  
Pour ce faire:
- Faites un clique-droit sur le répertoire `src/main/scala`,
- donnez un nom pour votre fichier (e.g `WordCount`) et cliquez sur `New > Scala Class`
- et faites un double-clique sur `Object` dans liste.

![image](https://user-images.githubusercontent.com/49156499/110215198-c6cf1e00-7ea8-11eb-8a3f-31df0a40b04b.png)



### Compilation

### Test et débuggage

### Cluster spark (local)
Télécharger le fichier [docker-compose.yaml](https://raw.githubusercontent.com/osekoo/hands-on-spark-scala/main/spark/docker-compose.yaml). Il se trouve également dans le repo /get-started.  
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



### Packaging
IntelliJ reconnait automatiquement les projets Scala et adapte l'environnement en conséquence.  
Vous trouverez en bas un onglet intitulé "sbt shell". Il suffit d'ouvrir cet onglet pour lancer le moteur SBT.  
La commande `compile` permet de compiler votre application.  
La commande `package` permet de créer le package de l'application qui sera exécuté sur Spark.
Elle produit un fichier `jar` qui est mis dans la racine du répertoire `target/scala-<scala_version>` et porte le nom `<project_name>_<scala_version>_<application_version>.jar`.
Parfois il est nécessaire d'exécuter la commande `clean` avant de compiler ou de packager votre programme.

### Exécution sur cluster Spark
Une fois le package obtenu, à l'aide de la commande `spark-submit` nous allons exécuter notre application sur notre cluster local que nous avons lancé un peu plus haut.
Si ce n'est pas encore le cas, c'est le moment de le lancer avec la commande `docker-compose up`.  

```(shell)
spark-submit \
  --master spark://localhost:7077 \
  --deploy-mode client \
  --executor-cores 4 \
  --num-executors 1 \
  --files ./ulysses.txt \
  --class WordCount \
  target/scala-2.12/get-started_2.12-0.1.jar
```
