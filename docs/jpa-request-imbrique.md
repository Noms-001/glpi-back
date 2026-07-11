# Spring Data JPA - Requêtes Imbriquées (Subqueries)

## Introduction

Une requête imbriquée (Subquery) est une requête exécutée à l'intérieur d'une autre requête.

Elle permet notamment :

* de filtrer des données ;
* de calculer des statistiques ;
* de comparer avec une moyenne, un maximum ou un minimum ;
* de vérifier l'existence de données liées.

---

# Syntaxe générale

```sql
SELECT ...
FROM table
WHERE colonne IN (
    SELECT ...
)
```

---

# Exemple SQL

Trouver les utilisateurs ayant créé au moins un ticket.

```sql
SELECT *
FROM users u
WHERE u.id IN (
    SELECT t.requester_id
    FROM ticket t
);
```

---

# Subquery avec IN

## Entités

```java
@Entity
public class User {

    @Id
    private Long id;

    private String name;
}
```

```java
@Entity
public class Ticket {

    @Id
    private Long id;

    @ManyToOne
    private User requester;
}
```

---

## Repository

```java
@Query("""
SELECT u
FROM User u
WHERE u.id IN (
    SELECT t.requester.id
    FROM Ticket t
)
""")
List<User> findUsersWithTickets();
```

---

# Subquery avec NOT IN

Trouver les utilisateurs sans ticket.

```java
@Query("""
SELECT u
FROM User u
WHERE u.id NOT IN (
    SELECT t.requester.id
    FROM Ticket t
)
""")
List<User> findUsersWithoutTickets();
```

---

# Subquery avec EXISTS

Retourne les utilisateurs possédant au moins un ticket.

```java
@Query("""
SELECT u
FROM User u
WHERE EXISTS (
    SELECT t
    FROM Ticket t
    WHERE t.requester = u
)
""")
List<User> findUsersWithAtLeastOneTicket();
```

---

# Subquery avec NOT EXISTS

```java
@Query("""
SELECT u
FROM User u
WHERE NOT EXISTS (
    SELECT t
    FROM Ticket t
    WHERE t.requester = u
)
""")
List<User> findUsersWithoutTickets();
```

---

# Subquery avec MAX

Trouver l'élément ayant le coût le plus élevé.

```java
@Query("""
SELECT i
FROM ItemCost i
WHERE i.costTotal = (
    SELECT MAX(x.costTotal)
    FROM ItemCost x
)
""")
List<ItemCost> findMostExpensive();
```

---

# Subquery avec MIN

```java
@Query("""
SELECT i
FROM ItemCost i
WHERE i.costTotal = (
    SELECT MIN(x.costTotal)
    FROM ItemCost x
)
""")
List<ItemCost> findCheapest();
```

---

# Subquery avec AVG

Trouver les coûts supérieurs à la moyenne.

```java
@Query("""
SELECT i
FROM ItemCost i
WHERE i.costTotal >
(
    SELECT AVG(x.costTotal)
    FROM ItemCost x
)
""")
List<ItemCost> findAboveAverage();
```

---

# Subquery avec COUNT

Trouver les utilisateurs ayant plus de 5 tickets.

```java
@Query("""
SELECT u
FROM User u
WHERE (
    SELECT COUNT(t)
    FROM Ticket t
    WHERE t.requester = u
) > 5
""")
List<User> findHeavyUsers();
```

---

# Subquery dans SELECT

⚠️ Support limité selon le fournisseur JPA.

Souvent préférable en Native Query.

Exemple SQL :

```sql
SELECT
    u.id,
    (
        SELECT COUNT(*)
        FROM ticket t
        WHERE t.requester_id = u.id
    ) AS ticket_count
FROM users u;
```

---

# Subquery dans Native Query

```java
@Query(value = """
SELECT
    u.id,
    u.name,
    (
        SELECT COUNT(*)
        FROM ticket t
        WHERE t.requester_id = u.id
    ) AS ticket_count
FROM users u
""", nativeQuery = true)
List<Object[]> getStatistics();
```

---

# Cas pratique GLPI

Trouver les catégories dont le coût total est supérieur à la moyenne.

```java
@Query("""
SELECT i.itemType,
       SUM(i.costTotal)
FROM ItemCost i
GROUP BY i.itemType
HAVING SUM(i.costTotal) >
(
    SELECT AVG(x.costTotal)
    FROM ItemCost x
)
""")
List<Object[]> getImportantCategories();
```

---

# IN vs EXISTS

## IN

```java
WHERE id IN (
    SELECT ...
)
```

Utiliser lorsque la sous-requête retourne peu de lignes.

---

## EXISTS

```java
WHERE EXISTS (
    SELECT ...
)
```

Utiliser lorsque la sous-requête retourne beaucoup de lignes.

Souvent plus performant.

---

# Bonnes pratiques

## Utiliser une subquery pour :

* MAX
* MIN
* AVG
* COUNT
* EXISTS
* NOT EXISTS

---

## Préférer JOIN lorsque possible

Mauvais :

```java
SELECT u
FROM User u
WHERE u.id IN (
    SELECT t.requester.id
    FROM Ticket t
)
```

Meilleur :

```java
SELECT DISTINCT u
FROM User u
JOIN Ticket t
ON t.requester = u
```

---

## Utiliser EXISTS pour les gros volumes

```java
WHERE EXISTS (...)
```

généralement plus performant que :

```java
WHERE id IN (...)
```

---

# Résumé

| Opérateur  | Utilisation          |
| ---------- | -------------------- |
| IN         | Vérifier une liste   |
| NOT IN     | Exclure une liste    |
| EXISTS     | Vérifier l'existence |
| NOT EXISTS | Vérifier l'absence   |
| MAX        | Valeur maximale      |
| MIN        | Valeur minimale      |
| AVG        | Moyenne              |
| COUNT      | Comptage             |

Les requêtes imbriquées sont très utiles pour les statistiques, tableaux de bord, rapports GLPI et analyses de coûts.
