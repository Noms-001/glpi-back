# Spring Data JPA - Utilisation de CASE

## Introduction

L'expression `CASE` permet d'effectuer des traitements conditionnels directement dans une requête JPQL ou SQL.

Elle est équivalente à :

```java
if (...) {
    ...
}
else {
    ...
}
```

mais exécutée directement par la base de données.

---

# Syntaxe générale

```sql
CASE
    WHEN condition1 THEN valeur1
    WHEN condition2 THEN valeur2
    ELSE valeurParDefaut
END
```

---

# Exemple simple

Supposons une entité :

```java
@Entity
public class Ticket {

    @Id
    private Long id;

    private String status;
}
```

---

# CASE dans un SELECT

```java
@Query("""
SELECT
    CASE
        WHEN t.status = 'OPEN' THEN 'Ouvert'
        WHEN t.status = 'CLOSED' THEN 'Fermé'
        ELSE 'Inconnu'
    END
FROM Ticket t
""")
List<String> getStatusLabels();
```

Résultat :

```text
Ouvert
Fermé
Ouvert
Inconnu
```

---

# CASE avec une Projection DTO

DTO :

```java
public record TicketDto(
        Long id,
        String label
) {
}
```

Repository :

```java
@Query("""
SELECT new com.example.dto.TicketDto(
    t.id,
    CASE
        WHEN t.status = 'OPEN' THEN 'Ouvert'
        WHEN t.status = 'CLOSED' THEN 'Fermé'
        ELSE 'Inconnu'
    END
)
FROM Ticket t
""")
List<TicketDto> findAllDtos();
```

---

# CASE avec des valeurs numériques

Supposons :

```java
@Entity
public class ItemCost {

    @Id
    private Long id;

    private Double costTotal;
}
```

Classification des coûts :

```java
@Query("""
SELECT
    CASE
        WHEN i.costTotal > 100000
            THEN 'HIGH'
        ELSE 'LOW'
    END
FROM ItemCost i
""")
List<String> getCategories();
```

---

# CASE avec plusieurs niveaux

```java
@Query("""
SELECT
    CASE
        WHEN i.costTotal >= 1000000
            THEN 'CRITIQUE'

        WHEN i.costTotal >= 500000
            THEN 'IMPORTANT'

        ELSE 'NORMAL'
    END
FROM ItemCost i
""")
List<String> getCostLevel();
```

---

# CASE dans une fonction SUM

Compter les tickets ouverts.

```java
@Query("""
SELECT
    SUM(
        CASE
            WHEN t.status = 'OPEN'
            THEN 1
            ELSE 0
        END
    )
FROM Ticket t
""")
Long countOpenTickets();
```

Equivalent SQL :

```sql
SELECT SUM(
    CASE
        WHEN status = 'OPEN'
            THEN 1
        ELSE 0
    END
)
FROM ticket
```

---

# Plusieurs compteurs dans une seule requête

```java
@Query("""
SELECT
    SUM(
        CASE
            WHEN t.status = 'OPEN'
            THEN 1
            ELSE 0
        END
    ),

    SUM(
        CASE
            WHEN t.status = 'CLOSED'
            THEN 1
            ELSE 0
        END
    )

FROM Ticket t
""")
Object[] getStatistics();
```

Résultat :

```text
[15, 8]
```

15 tickets ouverts

8 tickets fermés

---

# CASE avec COUNT

```java
@Query("""
SELECT
    CASE
        WHEN t.status = 'OPEN'
            THEN 'OPEN'
        ELSE 'OTHER'
    END,
    COUNT(t)
FROM Ticket t
GROUP BY
    CASE
        WHEN t.status = 'OPEN'
            THEN 'OPEN'
        ELSE 'OTHER'
    END
""")
List<Object[]> statistics();
```

Résultat :

```text
OPEN  | 15
OTHER | 24
```

---

# CASE avec GROUP BY

```java
@Query("""
SELECT
    CASE
        WHEN i.costTotal >= 1000000
            THEN 'CRITIQUE'
        ELSE 'NORMAL'
    END,
    SUM(i.costTotal)
FROM ItemCost i
GROUP BY
    CASE
        WHEN i.costTotal >= 1000000
            THEN 'CRITIQUE'
        ELSE 'NORMAL'
    END
""")
List<Object[]> getStatistics();
```

Résultat :

```text
CRITIQUE | 35000000
NORMAL   | 8500000
```

---

# CASE dans le WHERE

Possible mais rarement recommandé.

```java
@Query("""
SELECT t
FROM Ticket t
WHERE
    CASE
        WHEN :status IS NULL
            THEN true
        ELSE t.status = :status
    END
""")
List<Ticket> search(
        @Param("status") String status
);
```

---

# Alternative recommandée

Plus lisible :

```java
@Query("""
SELECT t
FROM Ticket t
WHERE (
    :status IS NULL
    OR t.status = :status
)
""")
List<Ticket> search(
        @Param("status") String status
);
```

---

# CASE dans une Native Query

```java
@Query(value = """
SELECT
    CASE
        WHEN cost_total >= 1000000
            THEN 'CRITIQUE'
        ELSE 'NORMAL'
    END AS level,
    SUM(cost_total)
FROM item_cost
GROUP BY level
""", nativeQuery = true)
List<Object[]> statistics();
```

---

# Cas pratique GLPI

Classification des coûts :

```java
@Query("""
SELECT
    CASE
        WHEN i.costTotal >= 1000000
            THEN 'CRITIQUE'

        WHEN i.costTotal >= 500000
            THEN 'IMPORTANT'

        ELSE 'NORMAL'
    END,

    SUM(i.costTotal)

FROM ItemCost i

GROUP BY
    CASE
        WHEN i.costTotal >= 1000000
            THEN 'CRITIQUE'

        WHEN i.costTotal >= 500000
            THEN 'IMPORTANT'

        ELSE 'NORMAL'
    END
""")
List<Object[]> getCostStatistics();
```

Résultat :

```text
CRITIQUE  | 15 000 000
IMPORTANT |  8 000 000
NORMAL    |  2 000 000
```

Cette approche est particulièrement utile pour préparer directement les données destinées aux graphiques Vue.js sans avoir à effectuer de calculs supplémentaires côté frontend.

---

# Bonnes pratiques

## Utiliser CASE lorsque :

* une valeur dépend d'une condition ;
* une catégorisation est nécessaire ;
* des statistiques doivent être calculées ;
* des agrégations conditionnelles sont requises.

## Éviter CASE lorsque :

* une simple condition `OR` suffit ;
* la logique métier devient trop complexe ;
* le traitement peut être réalisé plus clairement dans le Service.

## Préférer :

```java
SUM(
    CASE
        WHEN condition
        THEN 1
        ELSE 0
    END
)
```

pour les statistiques et les tableaux de bord.
