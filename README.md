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

## Credits

> Copyright (c) 2022 Lypotech
