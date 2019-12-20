# My Travels

## Présentation de l'application

![logo](https://user-images.githubusercontent.com/48760638/71228967-2e20a280-22e4-11ea-86f2-100448191758.png)

"My Travels" est une application permettant de sauvegarder ses voyages et de les visualiser de différente façon (en détail/sur une carte). Des statistiques sur l'ensemble des voyages sont également disponibles.

## Détail de l'application

### Écran d'accueil & Navigation

L'écran d'accueil s'affiche pendant une seconde au démarrage de l'application.
Les différents onglets (Voyages / Carte / Statistiques) sont accessibles via la barre de navigation en bas de l'écran.

![loading](https://user-images.githubusercontent.com/48760638/71295556-a5097a00-237c-11ea-911c-d4e3c2e9992c.png)

### Onglet "Voyages"

Tous les voyages chargés depuis l'API sont affichés sous forme de liste qu'il est possible de trier selon plusieurs critères (date/pays/continent). Les détails d'un voyage sont accessibles en cliquant dessus.

![1 Main](https://user-images.githubusercontent.com/48760638/71226372-e6961880-22db-11ea-9a3c-573a63513aa1.png)
***
Le détail d'un voyage permet d'accéder aux informations suivantes :
- la date du séjour
- la durée
- le nombre d'étapes et la distance parcourue
- la liste des étapes (les détails sont également accessibles en cliquant dessus.)

Le boutton de géolocalisation (en bas à gauche) permet d'afficher le circuit du voyage sur une carte (google maps).

![2 Travel](https://user-images.githubusercontent.com/48760638/71226373-e6961880-22db-11ea-82fe-0d5afb2b9bed.png)
***
Le détail d'une étape permet d'accéder aux informations suivantes :
- le nom et photo de la ville
- la description
- une liste de photos

Les photos peuvent s'afficher en plein écran avec leur description en cliquant dessus. Le boutton de géolocalisation permet d'afficher la position de l'étape sur une carte (google maps).

![3 Step](https://user-images.githubusercontent.com/48760638/71226374-e6961880-22db-11ea-9799-60164ef2eb55.png)
***

### Onglet "Carte"

2 affichages sont disponibles sur la carte :
- mode "Circuit" : tous les voyages et étapes sont affichés sur une carte (google maps). Ils sont cliquables afin d'accéder au détail de ceux-ci.
- mode "Pays" : tous les pays ayant été visités sont surlignés avec une couleur correspondante au continent.

La dernière capture montre le set complet de bordure permettant le surlignage du mode "Pays" (non-accessible).

![4 Map](https://user-images.githubusercontent.com/48760638/71226375-e6961880-22db-11ea-8a78-3d5abdbac1b9.png)
***

### Onglet "Statistiques"

Regroupe un ensemble de données correspondant aux différents voyages :

**`Statistiques générales`**

Données visibles :
- Total du nombre de pays visités + Pays le plus visité (en terme de nombre)
- Total du nombre de kilomètres parcouru + Pays avec la plus grande distance de parcours
- Point le plus éloigné atteint + Pays et ville de ce point
- Total de jours passés à voyager + Pays le plus visité (en terme de jours)

**`Pays visités`**

Affiche sur un plan du monde le nombre de pays visités par continent.

**`Répartition des voyages`**

Diagramme circulaire représentant le nombre de voyages par continent.

![5 Stats](https://user-images.githubusercontent.com/48760638/71226377-e6961880-22db-11ea-9d33-fae066bf82f3.png)

## Détail technique

### Architecture

Le code java est reparti en 4 dossiers :
- **activity :**
contient 2 activités (écran d'accueil et principal) et les fragments qui sont insérés dans l'activité principale. Les fragments sont répartis en 3 sous-dossiers correspondant aux onglets de la navigation (travel / map / stats)

- **adapter :**
contient les adapters correspondant aux 3 recycler view (Travel / Step / Img)

- **model :**
contient les classes des différents objets :
  - Border : objet récupéré dans 'border.json' qui contient une liste de Coord.
  - Coord : contient latitude et longitude.
  - Country : objet récupéré dans 'country.json'.
  - Img : contient un lien image et une description.
  - Step : contient les infos d'une étape et une liste d'Img.
  - Travel : contient les infos d'un voyage et une liste de Step.

- **network :** 
contient GetDataService et RetrofitClientInstance qui permettent de faire les appels de l'API.

![architecture](https://user-images.githubusercontent.com/48760638/71230900-b6567600-22eb-11ea-862c-81f9039ed136.png)

### Fonctionnalités

- **Retrofit :**
Permet de faire des appels GET sur l'API de MyTravels.

- **Recycler View & Adapter :**
Permet d'afficher des listes d'éléments customs de façon dynamique.

- **Activités & Fragments :** 
2 activités : écran d'accueil + principal / fragments : insérés dans l'écran principal.

- **Google Map SDK Service :**
Utilisation de l'API de Google afin d'afficher les cartes et leur contenu (<https://developers.google.com/maps/documentation/android-sdk/intro>).

- **HelloChart :**
Utilisation de la librairie "HelloChart" permettant d'afficher le diagramme circulaire (<https://github.com/lecho/hellocharts-android>).
