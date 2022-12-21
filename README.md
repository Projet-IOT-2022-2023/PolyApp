# PolyApp
[![java](https://img.shields.io/badge/language-java-%23f34b7d.svg?style=plastic)](https://en.wikipedia.org/wiki/Java_(programming_language)) 
[![android](https://img.shields.io/badge/platform-android-0078d7.svg?style=plastic)](https://en.wikipedia.org/wiki/Android_(operating_system))

Ce projet a pour but de créer une application Android permettant de se créer un profil, ajouter des amis, et de pouvoir visualiser leur emploi du temps. L'application utilisera notamment les fonctionnalités natives du smartphone tel que la caméra, le Wi-Fi et le bluetooth.

## Installation

### Prérequis
Ce projet nécessite l'installation d'[Android Studio](https://developer.android.com/studio/index.html) et de [Git](https://git-scm.com/).

## Utilisation

### Page d'accueil

Si l'utilisateur lance l'application pour la première fois, il est redirigé vers une page lui demandant d'enregistrer son nom, prénom et sa promotion. Une fois ces informations renseignées, l'utilisateur est redirigé vers la page d'accueil qui lui permet de visualiser ses données personnelles. Un QR Code est également généré à partir de ces données, ce qui permet de le scanner avec un autre appareil pour ajouter l'utilisateur comme ami.

### Page des amis

La page des amis permet à l'utilisateur de visualiser ses amis et de les ajouter. Pour ajouter un ami, l'utilisateur doit scanner le QR Code de l'ami qu'il souhaite ajouter et le valider.

### Ajout d'un ami

Lorsque l'utilisateur scanne le QR Code d'un ami, il est redirigé vers une page lui demandant de valider l'ajout de l'ami. Si l'utilisateur valide l'ajout, l'ami est ajouté à la liste des amis de l'utilisateur. Si l'utilisateur annule l'ajout, l'ami n'est pas ajouté à sa liste d'amis.

### Visualisation de sa liste d'amis

Lorsque l'utilisateur clique sur le bouton "Mes amis", il est redirigé vers une page lui permettant de visualiser sa liste d'amis. Chaque ami est représenté par un canvas contenant le nom, le prénom et la promotion de l'ami. Lorsque l'utilisateur clique sur un ami, il est redirigé vers une page lui permettant de visualiser son emploi du temps.

### Consultation de l'emploi du temps d'un ami

Lorsque l'utilisateur clique sur un ami, il est redirigé vers une page lui permettant de visualiser son emploi du temps. L'emploi du temps est représenté par des onglets qui correspondent aux dates de la semaine. Lorsque l'utilisateur clique sur un onglet, il est redirigé vers une page lui permettant de visualiser les cours de la journée sélectionnée. Chaque cours est représenté par un canvas contenant le nom du cours, le nom du professeur, le lieu du cours et l'heure de début et de fin du cours.

## Fonctionnement de l'application

### Architecture de l'application

Ci-dessous, vous trouverez le diagramme de classes de l'application:

![Architecture](https://github.com/Projet-IOT-2022-2023/PolyApp/raw/main/imgs/scheme-polyapp.png)

Ce diagramme permet de visualiser les différentes intéractions entre les classes de l'application ainsi que les liens entre les classes et les interfaces (XML).

### Définition des classes

Le projet est composé de 24 classes. 

Il y a tout d'abord la classe `MainActivity` qui est la classe principale de l'application. C'est elle qui est appelée au lancement de l'application.

Les autres classes sont réparties dans 2 dossiers:

#### Le dossier `edt`
Ce dossier contient les classes permettant de gérer et afficher l'emploi du temps de l'utilisateur. Il contient également des classes permettant de gérer les utilisateurs et leurs amis.

Pour cela, il y a une partie de gestion de base de données (BDD) avec les classes:
- `DBSyntax` qui permet de définir les différentes requêtes SQL utilisées dans l'application (nom des tables, colonnes, etc.)
- `DBManager` qui permet de gérer la BDD et de l'initialiser si elle n'existe pas (création des tables, insertion des données, etc.)

Il y a une partie permettant de faire la liaison entre le reste de l'application et la BDD avec les classes:
- `UserStruct` qui permet de définir un utilisateur et ses attributs
- `EventStruct` qui permet de définir un événement (nom, salle, jour, heure, etc.) et ses attributs
- `UserManager` qui permet de gérer les utilisateurs et leurs amis (ajout, suppression, recherche, etc.)
- `EDTGet` qui permet de récupérer les données de l'emploi du temps de l'utilisateur et de ses amis

Il y a une partie permettant d'actualiser l'emploi du temps de l'utilisateur (via le serveur de l'université) avec les classes:
- `ÈDTRefresh` qui permet de lancer la mise à jour de l'emploi du temps de toutes les promos de Polytech
- `EDTDownload` qui permet de télécharger l'emploi du temps d'une promo de Polytech sur le serveur de l'université
- `EDTParser` qui permet de parser le fichier iCal téléchargé et de récupérer les données de l'emploi du temps

Enfin, il y a une partie permettant d'afficher l'emploi du temps de l'utilisateur et de ses amis avec les classes:
- `EDTActivity` qui permet d'afficher l'emploi du temps de l'utilisateur ou de ses amis

Il y a également une classe `NbParsing` qui permet de compter le nombre de parsing effectués par l'application (pour savoir si le téléchargement de l'emploi du temps est terminé) et une classe `Specialities` qui permettent de définir le nombre de promotions de Polytech et les spécialités de Polytech.

#### Le dossier `ui`
Ce dossier contient les classes permettant de gérer les affichages de l'application (en dehors de l'emploi du temps), ainsi que l'ajout d'amis (via le QR Code) et l'envoi de la liste des utilisateurs à un autre appareil (via le bluetooth).

Ce dossier est composé de 3 sous-dossiers:

##### Le sous-dossier `bluetooth`
Ce sous-dossier contient les classes permettant de gérer l'envoi de la liste des utilisateurs à un autre appareil via le bluetooth.
Il est composé de deux classes:
- `BluetoothFragment` qui permet de lancer `BluetoothActivity`.
- `BluetoothActivity` qui permet d'afficher la liste des appareils bluetooth disponibles et de choisir l'appareil à qui envoyer la liste des utilisateurs. Elle permet également de gérer l'envoi de la liste des utilisateurs à l'appareil choisi.

##### Le sous-dossier `friends`
Ce sous-dossier contient les classes permettant de gérer la page des amis de l'utilisateur.

Il y a deux parties dans ce sous-dossier.

La première partie permet de gérer l'ajout d'amis via le QR Code. Elle est composée de trois classes: `ScanFragment`et `QRCodeActivity` qui permettent de scanner le QR Code d'un ami et de l'ajouter dans la BDD (via `UserManager`). En cas d'erreur, la toisième classe `NoScanResultException` est exécutée.

La deuxième partie permet de gérer la page des amis de l'utilisateur. Elle est composée de trois classes: `FriendsActivity` qui permet d'afficher la liste des amis de l'utilisateur. Chaques amis est affiché via la classe `FriendListAdapter`.

Enfin, il y a une classe `MyFriendsFragment` qui permet de lancer `FriendsActivity` (pour afficher la liste des amis de l'utilisateur) ou `QRCodeActivity` (pour ajouter un ami via le QR Code).

##### Le sous-dossier `home`
Ce sous-dossier contient les classes permettant de gérer la page d'accueil de l'application.

Il est composé de deux classes:
- `HomeFragment` qui permet d'afficher la page d'accueil de l'application. Si l'utilisateur a saisi son nom, prénom et promo, il est affiché sur la page d'accueil avec son QR Code. Sinon, il est invité à saisir ses informations via la classe `HomeEditActivity`.
- `HomeEditActivity` qui permet de saisir les informations de l'utilisateur (nom, prénom, promo) et de les enregistrer dans la BDD (via `UserManager`).

## Protocole de communication

### Mise en place du protocole wifi

### Mise en place du protocole bluetooth

Afin de comprendre comment a été mis en place en place le bluetooth au sein de l'application, il nous parait essentiel d'aborder le contenu de l'activité ou est utilisé le bluetooth.
Voici ci-contre une image de l'activité `BluetoothActivity`:

Cette activité est constituée de différents éléments:
- Un bouton 'Get Visible' qui permet de rendre l'appareil visible pour les autres appareils bluetooth.
- Un bouton 'Listen' qui permet de lancer l'écoute des appareils bluetooth et ainsi trouver les appareils appairés.
- Une ListView qui permet d'afficher la liste des appareils bluetooth appairés trouvés et de se connecter à l'appareil choisi.
- Un TextView qui permet d'afficher le statut connecté, en cours de connexion ou non connecté.
- Un bouton 'Envoyer' qui permet de transférer des données à l'appareil connecté.

Pour mettre en place, ce protocole plusieurs étapes ont été nécessaires.

Tout d'abord afin de mettre en place le bluetooth il est nécessaire d'ajouter des permissions dans le fichier `AndroidManifest.xml`.

Les permissions nécessaires sont BLUETOOTH, BLUETOOTH_ADMIN, BLUETOOTH_SCAN, BLUETOOTH_ADVERTISE, BLUETOOTH_CONNECT, ACCESS_FINE_LOCATION et ACCESS_COARSE_LOCATION. Pour BLUETOOTH, BLUETOOTH_ADMIN et ACCESS_COARSE_LOCATION, il est nécessaire de préciser la version de l'API jusqu'à laquelle la permission est autorisée. Pour les autres permissions, il n'est pas nécessaire de préciser la version de l'API. Cela permet de ne pas avoir de problème de compatibilité avec les anciennes versions d'Android.

Après avoir ajouté les permissions, dans le fichier BluetoothActivity.java, la première étape pour mettre en place le bluetooth est de vérifier si l'appareil dispose du bluetooth en utilisant un BluetoothAdapter. Si l'appareil dispose du bluetooth, on peut alors utiliser le bluetooth en acceptant la notification qui sera affiché. Sinon, un message d'erreur est affiché.

Ensuite, vient la réalisation des différentes activité pour chaque bouton de l'activité.

Concernant le bouton 'Listen', afin de pouvoir interroger les appareils appairés, il est nécessaire d'appeler la fonction getBondedDevices() de l'objet BluetoothAdapter. Cette fonction renvoie une liste d'appareils appairés. Pour chaque appareil appairé, on ajoute le nom de l'appareil dans la liste de l'activité. Pour cela, on utilise un ArrayAdapter qui permet d'afficher la liste des appareils appairés.

La liste des appareils appairés est affichée dans la ListView de l'activité et lorsque l'utilisateur clique sur un appareil, il va se connecter à l'appareil choisi.

Par la suite, si l'appareil désiré n'apparaît pas dans la liste des appareils appairés, il sera possible via le bouton 'Get visible' de rendre l'appareil visible pour les autres utilisateurs mais pour pouvoir l'ajouter l'utilisateur devra associer l'appareil en passant par les paramètres de son téléphone.

Pour finir sur le bluetooth, le bouton 'envoyer' permettra à l'utilisateur d'envoyer ces données vers un autre. Afin de mettre en place cela, il a fallu réaliser 3 classes. La première permettant de mettre l'appareil se connectant à l'autre en tant que serveur. La seconde quant à elle configure l'autre appareil en tant que client et la 3eme classe va gérer l'envoi des données.


## Credits

> Copyright (c) 2022 Lypotech