# Spring Data JPA - IF, COALESCE et Fonctions Conditionnelles

## Introduction

En SQL et JPQL, il existe plusieurs mécanismes permettant de gérer des conditions ou des valeurs nulles :

* CASE
* COALESCE
* NULLIF
* IF (Native SQL selon le SGBD)

Dans JPQL, **CASE** et **COALESCE** sont standards et portables.

---

# COALESCE

## Principe

Retourne la première valeur non nulle.

Syntaxe :

```sql
COALESCE(valeur1, valeur2, valeur3, ...)
```

---

## Exemple SQL

```sql
SELECT COALESCE(email, 'Aucun email')
FROM users;
```

Si :

```text
email = null
```

Résultat :

```text
Aucun email
```

---

# COALESCE en JPQL

Supposons :

```java
@Entity
public class User {

    @Id
    private Long id;

    private String name;

    private String email;
}
```

Repository :

```java
@Query("""
SELECT COALESCE(
    u.email,
    'Aucun email'
)
FROM User u
""")
List<String> getEmails();
```

---

# COALESCE avec plusieurs valeurs

```java
@Query("""
SELECT COALESCE(
    u.mobilePhone,
    u.phone,
    'Non renseigné'
)
FROM User u
""")
List<String> getPhones();
```

Ordre d'évaluation :

```text
mobilePhone
↓
phone
↓
Non renseigné
```

---

# COALESCE dans une projection DTO

```java
@Query("""
SELECT new com.demo.dto.UserDto(
    u.id,
    COALESCE(
        u.email,
        'Non défini'
    )
)
FROM User u
""")
List<UserDto> findDtos();
```

---

# COALESCE avec SUM

Évite les valeurs nulles.

Sans COALESCE :

```java
@Query("""
SELECT SUM(i.costTotal)
FROM ItemCost i
""")
Double total();
```

Si aucune ligne :

```text
null
```

Avec COALESCE :

```java
@Query("""
SELECT COALESCE(
    SUM(i.costTotal),
    0
)
FROM ItemCost i
""")
Double total();
```

Résultat :

```text
0
```

---

# COALESCE dans ton projet GLPI

```java
@Query("""
SELECT COALESCE(
    SUM(i.costTotal),
    0
)
FROM ItemCost i
WHERE i.itemType = :type
""")
Double totalByType(
        @Param("type") String type
);
```

Très utile pour éviter :

```java
NullPointerException
```

---

# NULLIF

## Principe

Retourne NULL si les deux valeurs sont identiques.

Syntaxe :

```sql
NULLIF(a, b)
```

Equivalent :

```sql
CASE
    WHEN a = b
    THEN NULL
    ELSE a
END
```

---

## Exemple

```sql
SELECT NULLIF(10, 10);
```

Résultat :

```text
NULL
```

---

```sql
SELECT NULLIF(10, 20);
```

Résultat :

```text
10
```

---

# NULLIF en JPQL

```java
@Query("""
SELECT NULLIF(
    u.email,
    ''
)
FROM User u
""")
List<String> getEmails();
```

Si :

```text
email = ""
```

Résultat :

```text
NULL
```

---

# IF en SQL

Certaines bases comme MySQL possèdent :

```sql
IF(condition, valeur1, valeur2)
```

Exemple :

```sql
SELECT IF(
    cost_total > 100000,
    'HIGH',
    'LOW'
)
FROM item_cost;
```

---

# IF en JPQL

JPQL ne supporte pas IF.

❌ Incorrect :

```java
@Query("""
SELECT IF(
    u.active,
    'ACTIVE',
    'INACTIVE'
)
FROM User u
""")
```

---

# Alternative JPQL

Utiliser CASE :

```java
@Query("""
SELECT
    CASE
        WHEN u.active = true
        THEN 'ACTIVE'
        ELSE 'INACTIVE'
    END
FROM User u
""")
List<String> statuses();
```

---

# Native Query avec IF

Pour MySQL :

```java
@Query(value = """
SELECT
    IF(
        cost_total > 100000,
        'HIGH',
        'LOW'
    )
FROM item_cost
""", nativeQuery = true)
List<String> categories();
```

---

# Comparaison

| Fonction | JPQL | Native SQL |
| -------- | ---- | ---------- |
| CASE     | ✅    | ✅          |
| COALESCE | ✅    | ✅          |
| NULLIF   | ✅    | ✅          |
| IF       | ❌    | ✅ (MySQL)  |

---

# Bonnes pratiques

## Préférer

```java
CASE
```

pour les conditions.

---

## Préférer

```java
COALESCE
```

pour gérer les valeurs nulles.

---

## Utiliser

```java
COALESCE(SUM(...), 0)
```

pour toutes les statistiques.

---

## Éviter

```java
IF(...)
```

dans JPQL.

Utiliser :

```java
CASE
```

qui est standard et compatible avec tous les fournisseurs JPA.
