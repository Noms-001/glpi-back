# Spring Data JPA - Query Methods

## Introduction

Spring Data JPA permet de générer automatiquement des requêtes SQL à partir du nom des méthodes déclarées dans un Repository.

Exemple :

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByName(String name);

}
```

Spring génère automatiquement :

```sql
SELECT * FROM users WHERE name = ?
```

---

## Structure générale

```java
findBy<Property>
```

Exemple :

```java
List<User> findByEmail(String email);
```

---

## Conditions multiples

```java
List<User> findByNameAndEmail(String name, String email);
```

Equivalent SQL :

```sql
SELECT * FROM users
WHERE name = ?
AND email = ?
```

---

## Utilisation de OR

```java
List<User> findByNameOrEmail(String name, String email);
```

---

## Comparaisons

### Greater Than

```java
List<Product> findByPriceGreaterThan(Double price);
```

### Less Than

```java
List<Product> findByPriceLessThan(Double price);
```

### Between

```java
List<Product> findByPriceBetween(Double min, Double max);
```

---

## Recherche LIKE

### Contains

```java
List<User> findByNameContaining(String keyword);
```

### Starts With

```java
List<User> findByNameStartingWith(String prefix);
```

### Ends With

```java
List<User> findByNameEndingWith(String suffix);
```

---

## Gestion des valeurs null

```java
List<User> findByEmailIsNull();
```

```java
List<User> findByEmailIsNotNull();
```

---

## Tri

```java
List<User> findByNameOrderByCreatedAtDesc(String name);
```

---

## Vérification d'existence

```java
boolean existsByEmail(String email);
```

---

## Comptage

```java
long countByStatus(String status);
```

---

## Suppression

```java
void deleteByEmail(String email);
```

---

## Premier résultat

```java
User findFirstByOrderByIdDesc();
```

ou

```java
User findTopByOrderByIdDesc();
```

---

## Pagination

```java
Page<User> findByStatus(
        String status,
        Pageable pageable
);
```

Utilisation :

```java
PageRequest.of(0, 10);
```

---

## Liste des mots-clés courants

| Mot-clé      | Exemple                 |
| ------------ | ----------------------- |
| And          | findByNameAndEmail      |
| Or           | findByNameOrEmail       |
| Between      | findByPriceBetween      |
| LessThan     | findByPriceLessThan     |
| GreaterThan  | findByPriceGreaterThan  |
| Like         | findByNameLike          |
| Containing   | findByNameContaining    |
| StartingWith | findByNameStartingWith  |
| EndingWith   | findByNameEndingWith    |
| IsNull       | findByEmailIsNull       |
| IsNotNull    | findByEmailIsNotNull    |
| OrderBy      | findByNameOrderByIdDesc |
| Exists       | existsByEmail           |
| Count        | countByStatus           |

---

## Quand utiliser les Query Methods ?

Utiliser les Query Methods lorsque :

* la requête est simple ;
* aucun JOIN complexe n'est nécessaire ;
* aucune agrégation complexe n'est nécessaire ;
* la lisibilité reste correcte.
