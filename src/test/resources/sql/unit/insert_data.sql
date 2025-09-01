-- Insert users with fixed IDs
INSERT INTO users (id)
VALUES (1001),
       (1002),
       (1003);

-- Insert subjects
INSERT INTO subjects (name, description, user_id, is_archived)
VALUES ('Mathematics', 'All about numbers and equations', 1001, FALSE),
       ('English', 'Literature and language studies', 1001, FALSE),
       ('Spanish', 'Learn the Spanish language', 1002, FALSE),
       ('French', 'Learn the French language', 1002, FALSE),
       ('Sanskrit', 'Ancient language studies', 1002, TRUE),
       ('Physics', 'Study of matter and energy', 1003, FALSE),
       ('Chemistry', 'Study of substances and reactions', 1003, FALSE);

-- Insert topics
INSERT INTO topics (name, description, subject_id)
VALUES ('Algebra', 'Study of mathematical symbols and rules', 1),
       ('Geometry', 'Study of shapes and their properties', 1),
       ('Calculus', 'Study of change and motion', 1),

       ('Grammar', 'Rules of language structure', 2),
       ('Literature', 'Study of written works', 2),
       ('Writing', 'Practice of composing text', 2),

       ('Vocabulary', 'Words and their meanings', 3),
       ('Grammar', 'Rules of Spanish language structure', 3),
       ('Conversation', 'Practice speaking Spanish', 3),

       ('Vocabulary', 'Words and their meanings', 4),
       ('Grammar', 'Rules of French language structure', 4),
       ('Conversation', 'Practice speaking French', 4),

       ('Mechanics', 'Study of motion and forces', 5),
       ('Thermodynamics', 'Study of heat and energy', 5),
       ('Optics', 'Study of light and vision', 5),

       ('Organic Chemistry', 'Study of carbon-containing compounds', 6),
       ('Inorganic Chemistry', 'Study of inorganic compounds', 6),
       ('Physical Chemistry', 'Study of physical properties of molecules', 6);
