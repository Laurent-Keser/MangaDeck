# Description

Application de critique de Manga/Anime. L'utilisateur à la possibilité de se connecter/se créer un compte. Une fois connecté, il peut consulter les différents mangas/anime et en choisir un afin de consulter les informations de œuvre, de la noter où encore la commenter afin de donner son avis aux autres personnes inscrites.
L'utilisateur à également la possibilité de consulter son profil afin de changer sa photo de profil et consulter l'ensemble de ses commentaires postés.
L'architecture du projet est en MVVM.
Un menu coulissant a gauche de l'écran permet de se déplacer dans certaines parties de l'application

**Note:** Le mot de passe doit contenir au minimum 6 caractères, 1 majuscule, 1 caractère spécial, 1 chiffre.
		  Le pseudo ne peux contenir que des chiffres et des lettres.

# Gestion de la persistence

L'application va conserver les différents mangas/animes ainsi qu'une image de représentation, une description, une note et les commentaires.
La gestion de l'Authentification se fait via l'outil "Authentification" de Firebase, le stockage en cloud des images se fait via l'outil "Storage" du même système et la database utilisée est la database NoSql "Firestore Database" où les données sont découpées en collections et documents.

# Détails

Le visuel de l'application se situe dans le dossier "res". Il est notamment composé de certaines images vectorielles utilisées et non stockées dans Firebase, de la police d'écriture et de différentes ressources définies comme par exemple les couleurs et les chaines de caractère afin d'apporter de la structure et de pouvoir facilement modifier les données.
L'ensemble de la vue est découpée en différents fragments (xml) contenant différents éléments comme des boutons ou des recyclerView pour "dupliqué" l'affichage d'items spécifiques. Le tout étant construit sur base de constraint layouts afin d'avoir une bonne structure visuelle.
La navigation entre les différents affichages de l'application se fait via le fichier navigation.xml

Chaque RecyclerView possède son Adaptateur qui va lui permettre d'être populé par un item donné afin de remplir son rôle de liste dynamique.

Dans ce projet, les data bindings sont implémentées afin d'avoir un code plus lisible et cela permet d'utiliser des "data class" afin que les changements soient directement répercutés sur la vue.

Notre ViewModel va posséder la logique de notre programme, il y en aura un pour chaque fragment de notre vue.


 






