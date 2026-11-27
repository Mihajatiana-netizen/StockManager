
PRAGMA foreign_keys = ON;

-- 1. TABLE DES CATEGORIES
CREATE TABLE IF NOT EXISTS categories (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL UNIQUE,
    description TEXT
);

-- 2. TABLE DES ARTICLES
CREATE TABLE IF NOT EXISTS articles (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    reference TEXT NOT NULL UNIQUE,
    name TEXT NOT NULL,
    description TEXT,
    purchase_price REAL NOT NULL DEFAULT 0
        CHECK (purchase_price >= 0),
    selling_price REAL NOT NULL DEFAULT 0
        CHECK (selling_price >= 0),
    quantity INTEGER NOT NULL DEFAULT 0
        CHECK (quantity >= 0),
    minimum_stock INTEGER NOT NULL DEFAULT 5
        CHECK (minimum_stock >= 0),
    category_id INTEGER,
    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (category_id)
        REFERENCES categories(id)
        ON DELETE SET NULL
);

-- 3. TABLE DES MOUVEMENTS
CREATE TABLE IF NOT EXISTS movements (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    article_id INTEGER NOT NULL,
    type TEXT NOT NULL
        CHECK (type IN ('ENTRY', 'EXIT')),
    quantity INTEGER NOT NULL
        CHECK (quantity > 0),
    movement_date TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    description TEXT,

    FOREIGN KEY (article_id)
        REFERENCES articles(id)
        ON DELETE RESTRICT
);

-- INDEX POUR AMELIORER LES RECHERCHES
CREATE INDEX IF NOT EXISTS idx_articles_category
ON articles(category_id);

CREATE INDEX IF NOT EXISTS idx_movements_article
ON movements(article_id);

CREATE INDEX IF NOT EXISTS idx_movements_date
ON movements(movement_date);
