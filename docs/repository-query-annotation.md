# Spring Data JPA - Utilisation de @Query

## Pourquoi utiliser @Query ?

Les Query Methods deviennent difficiles à lire lorsque la requête devient complexe.

Exemple :

```java
findByNameAndEmailAndStatusAndCreatedAtBetween(...)
```

Dans ce cas il est préférable d'utiliser :

```java
@Query(...)
```

---

## JPQL

Exemple :

```java
@Query("""
SELECT u
FROM User u
WHERE u.email = :email
""")
User findByEmail(@Param("email") String email);
```

---

## Plusieurs paramètres

```java
@Query("""
SELECT u
FROM User u
WHERE u.name = :name
AND u.status = :status
""")
List<User> search(
        @Param("name") String name,
        @Param("status") String status
);
```

---

## LIKE

```java
@Query("""
SELECT u
FROM User u
WHERE lower(u.name)
LIKE lower(concat('%', :keyword, '%'))
""")
List<User> searchByKeyword(
        @Param("keyword") String keyword
);
```

---

## ORDER BY

```java
@Query("""
SELECT u
FROM User u
ORDER BY u.createdAt DESC
""")
List<User> findRecentUsers();
```

---

## JOIN

```java
@Query("""
SELECT t
FROM Ticket t
JOIN t.requester r
WHERE r.id = :id
""")
List<Ticket> findByRequester(
        @Param("id") Long requesterId
);
```

---

## Requête de mise à jour

```java
@Modifying
@Transactional
@Query("""
UPDATE User u
SET u.status = :status
WHERE u.id = :id
""")
int updateStatus(
        @Param("id") Long id,
        @Param("status") String status
);
```

---

## Suppression

```java
@Modifying
@Transactional
@Query("""
DELETE FROM User u
WHERE u.id = :id
""")
void deleteUser(
        @Param("id") Long id
);
```

---

## Projection DTO

```java
@Query("""
SELECT new com.demo.dto.UserDto(
    u.id,
    u.name
)
FROM User u
""")
List<UserDto> findAllDtos();
```

---

## Bonnes pratiques

1. Utiliser Query Method pour les requêtes simples.
2. Utiliser @Query pour les requêtes complexes.
3. Utiliser JPQL avant Native SQL.
4. Garder les Repository focalisés sur l'accès aux données.
5. Utiliser Pageable pour les gros volumes de données.

```
```
