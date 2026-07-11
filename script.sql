-- Activer les clés étrangères dans SQLite
PRAGMA foreign_keys = ON;

-- =========================
-- TABLE status
-- =========================
CREATE TABLE status (
    id INTEGER PRIMARY KEY,
    couleur TEXT NOT NULL
);

-- =========================
-- TABLE langue
-- =========================
CREATE TABLE langue (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    code TEXT NOT NULL UNIQUE
);

-- =========================
-- TABLE langue_statut
-- =========================
CREATE TABLE langue_statut (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_status INTEGER NOT NULL,
    id_langue INTEGER NOT NULL,
    value TEXT NOT NULL,

    FOREIGN KEY (id_status) REFERENCES status(id),
    FOREIGN KEY (id_langue) REFERENCES langue(id)
);

CREATE TABLE cost_type (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    type_cost TEXT NOT NULL
)

CREATE TABLE item_cost (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    ticket_id INTEGER NOT NULL,
    item_id INTEGER NOT NULL,
    item_type TEXT NOT NULL,
    cost DECIMAL(10, 2),
    super_cost DECIMAL(10, 2),
    open_cost DECIMAL(10, 2),
    group_id INTEGER
);

-- =========================
-- INSERT status
-- =========================
INSERT INTO status (id, couleur) VALUES
(1, '#22C55E'), -- vert moderne
(2, '#F97316'), -- orange moderne
(6, '#6B7280'); -- gris moderne

-- =========================
-- INSERT langue
-- =========================
INSERT INTO langue (code)
VALUES ('MG');

-- =========================
-- INSERT traductions des status
-- =========================
INSERT INTO langue_statut (id_status, id_langue, value)
VALUES
(
    1,
    (SELECT id FROM langue WHERE code = 'MG'),
    'Vavao'
),
(
    2,
    (SELECT id FROM langue WHERE code = 'MG'),
    'Efa Manao'
),
(
    6,
    (SELECT id FROM langue WHERE code = 'MG'),
    'Vita'
);