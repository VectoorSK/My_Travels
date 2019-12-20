# My_Travels

## Présentation de l'application

### Écran d'accueil

S'affiche pendant une seconde au démarrage de l'application.

![0 Loading](https://user-images.githubusercontent.com/48760638/71226371-e6961880-22db-11ea-8ba6-281d92abbae5.png)

### Onglet "Voyages"

Tous les voyages chargés depuis l'API sont affichés sous forme de liste qu'il est possible de trier selon plusieurs critères (date/pays/continent). Les détails d'un voyage sont accessibles en cliquant dessus.

![1 Main](https://user-images.githubusercontent.com/48760638/71226372-e6961880-22db-11ea-9a3c-573a63513aa1.png)
***
Le détail d'un voyage permet d'accéder aux informations suivantes :
- la date du séjour
- la durée
- le nombre d'étapes et la distance parcouru
- la liste des étapes (Les détails sont également accessibles en cliquant dessus)

Le button de géolocalisation (en bas à gauche) permet d'afficher le circuit du voyage sur une carte (google maps).

![2 Travel](https://user-images.githubusercontent.com/48760638/71226373-e6961880-22db-11ea-82fe-0d5afb2b9bed.png)
***
Le détail d'une étape permet d'accéder aux informations suivantes :
- le nom et photo de la ville
- la description
- une liste de photos

Les photos peuvent s'afficher en plein écran avec leur description en cliquant dessus. Le button de géolocalisation permet d'afficher la position de l'étape sur une carte (google maps).

![3 Step](https://user-images.githubusercontent.com/48760638/71226374-e6961880-22db-11ea-9799-60164ef2eb55.png)
***

### Onglet "Carte"

2 affichages sont disponibles sur la carte :
- mode "Circuit" : Tous les voyages et étapes sont affichés sur une carte (google maps). Ils sont cliquables afin d'accéder au détail de ceux-ci.
- mode "Pays" : Tous les pays ayant été visités sont surlignés avec une couleur correspondante au continent.

La dernière capture montre le set complet de bordure permettant le surlignage du mode "Pays" (non accessible).

![4 Map](https://user-images.githubusercontent.com/48760638/71226375-e6961880-22db-11ea-8a78-3d5abdbac1b9.png)
***

### Onglet "Statistiques"

Regroupe un ensemble de données correspondant aux différents voyages :

**`Statistiques générales`**

Données visibles :
- Total du nombre de pays visités + Pays le plus visité (en terme de nombre)
- Total du nombre de kilomètre parcouru + Pays avec la plus grande distance de parcours
- Point le plus éloigné atteint + Pays et ville de ce point
- Total de jours passés à voyager + Pays le plus visité (en terme de jours)

**`Pays visités`**

Affiche sur un plan du monde le nombre de pays visités par continent.

**`Répartition des voyages`**

Diagramme circulaire représentant le nombre de voyage par continent.

![5 Stats](https://user-images.githubusercontent.com/48760638/71226377-e6961880-22db-11ea-9d33-fae066bf82f3.png)
