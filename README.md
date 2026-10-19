# BeeStore — API REST de gestion des produits

## Présentation

BeeStore est une application backend développée avec Java et Spring Boot. Elle expose une API REST permettant de gérer les produits : création, consultation, modification, suppression et recherche par nom ou catégorie.

L’API utilise une architecture organisée en couches afin de séparer les contrôleurs, les services, l’accès aux données et les objets de transfert.

## Technologies utilisées

- **Java 17**
- **Spring Boot 3.5.6**
- Spring Web — création de l’API REST
- Spring Data JPA — persistance des données
- Hibernate — mapping objet-relationnel
- H2 — base de données en mémoire pour le développement
- MySQL — base de données prévue pour la production
- Swagger / OpenAPI — documentation et test de l’API
- Jakarta Bean Validation — validation des données
- Lombok — réduction du code répétitif
- Maven — gestion des dépendances et construction du projet
- JUnit / Spring Boot Test — tests
- JaCoCo — mesure de la couverture des tests
- Spring Boot Actuator — supervision et vérification de santé

## Fonctionnalités

- Création d’un produit.
- Consultation de tous les produits.
- Consultation d’un produit par son identifiant.
- Modification d’un produit existant.
- Suppression d’un produit.
- Recherche des produits par nom ou catégorie.
- Validation des données entrantes.
- Gestion centralisée des exceptions.
- Documentation interactive de l’API avec Swagger.
- Endpoint de santé pour la supervision de l’application.

## Structure du projet

```text
src/
├── main/
│   ├── java/com/beedigital/beestore/
│   │   ├── config/       # Configuration Swagger
│   │   ├── controller/   # Endpoints REST
│   │   ├── dto/          # Objets de requête et de réponse
│   │   ├── entity/       # Entités JPA
│   │   ├── exception/    # Gestion des exceptions
│   │   ├── repository/   # Accès aux données
│   │   ├── service/      # Logique métier
│   │   └── BeeStoreApplication.java
│   └── resources/
│       ├── application.properties
│       ├── application-dev.properties
│       └── application-prod.properties
└── test/
    └── java/             # Tests automatisés
```

## Prérequis

- JDK 17 ou version compatible avec le projet.
- Maven, ou le Maven Wrapper si celui-ci est présent dans le dépôt.
- Git pour récupérer le projet.

## Installation et lancement

### 1. Récupérer le projet

```bash
git clone https://github.com/Mongi-Kaabia/demo-devops.git
cd demo-devops
```

### 2. Lancer l’application

Depuis le dossier du projet, avec Maven installé :

```bash
mvn spring-boot:run
```

L’application utilise par défaut le profil `dev` et le port `8080`.

### 3. Accéder aux interfaces

- **Swagger UI :** http://localhost:8080/swagger-ui/index.html
- **Documentation OpenAPI JSON :** http://localhost:8080/api-docs
- **Console H2 :** http://localhost:8080/h2-console
- **Santé de l’application :** http://localhost:8080/actuator/health

Si l’URL Swagger habituelle ne fonctionne pas, essayer également http://localhost:8080/swagger-ui.html.

### 4. Configuration de la console H2

En développement, les paramètres configurés sont :

- JDBC URL : `jdbc:h2:mem:beestore_dev`
- Username : `sa`
- Password : laisser vide

La base H2 est en mémoire et le schéma est recréé à chaque démarrage. Les données de développement ne sont donc pas conservées après l’arrêt de l’application.

## Endpoints de l’API

La base des routes est `/api/v1/produits`.

| Méthode | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/produits` | Créer un produit |
| GET | `/api/v1/produits` | Récupérer tous les produits |
| GET | `/api/v1/produits/{id}` | Récupérer un produit par ID |
| PUT | `/api/v1/produits/{id}` | Modifier un produit |
| DELETE | `/api/v1/produits/{id}` | Supprimer un produit |
| GET | `/api/v1/produits/search?nom=...` | Rechercher par nom |
| GET | `/api/v1/produits/search?categorie=...` | Rechercher par catégorie |

### Exemple de création d’un produit

Requête `POST /api/v1/produits` :

```json
{
  "nom": "Clavier mécanique",
  "description": "Clavier mécanique USB",
  "prix": 129.99,
  "stock": 15,
  "categorie": "Informatique",
  "actif": true
}
```

Le nom, le prix et le stock sont obligatoires. Le prix doit être strictement positif, le stock doit être supérieur ou égal à zéro, et le nom doit contenir entre 2 et 100 caractères.

Le champ `dateCreation` est renseigné automatiquement lors de la création du produit.

## Profils de configuration

### Développement (`dev`)

- Base H2 en mémoire.
- Console H2 activée.
- Schéma recréé au démarrage.

### Production (`prod`)

Le profil de production utilise MySQL. Il nécessite la configuration des variables d’environnement suivantes :

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`

Ne placez pas de mots de passe ou d’identifiants sensibles directement dans le dépôt Git.

## Tests

Pour exécuter les tests automatisés :

```bash
mvn test
```

Le projet inclut JaCoCo pour générer un rapport de couverture des tests.

## Licence

Aucune licence spécifique n’a été confirmée pour ce projet.
