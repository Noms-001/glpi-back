# Spring Data JPA - Native Query

## Introduction

Une Native Query permet d'écrire directement du SQL.

```java
@Query(
    value = """
    SELECT *
    FROM users
    WHERE email = :email
    """,
    nativeQuery = true
)
User findByEmail(
        @Param("email") String email
);
```

---

## Jointure SQL

```java
@Query(
    value = """
    SELECT t.*
    FROM tickets t
    INNER JOIN requester r
        ON r.id = t.requester_id
    WHERE r.id = :id
    """,
    nativeQuery = true
)
List<Ticket> findRequesterTickets(
        @Param("id") Long requesterId
);
```

---

## Agrégation

```java
@Query(
    value = """
    SELECT
        item_type,
        SUM(cost_total)
    FROM item_cost
    GROUP BY item_type
    """,
    nativeQuery = true
)
List<Object[]> sumByType();
```

---

## Pagination

```java
@Query(
    value = """
    SELECT *
    FROM users
    """,
    countQuery = """
    SELECT COUNT(*)
    FROM users
    """,
    nativeQuery = true
)
Page<User> findAllUsers(Pageable pageable);
```

---
