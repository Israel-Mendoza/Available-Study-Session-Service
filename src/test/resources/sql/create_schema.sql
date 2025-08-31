CREATE TABLE IF NOT EXISTS users (
    id INT NOT NULL PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS subjects (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    is_archived BOOLEAN DEFAULT FALSE,
    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS topics (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    subject_id INT NOT NULL,
    FOREIGN KEY (subject_id) REFERENCES subjects(id)
);
--