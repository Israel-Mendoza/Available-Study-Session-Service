INSERT INTO users (id)
VALUES (1001), -- School subjects for user 1001
       (1002), -- Languages subjects for user 1002
       (1003); -- Science subjects for user 1003

INSERT INTO subjects (name, description, user_id, is_archived)
VALUES
    -- User 1001 subjects
    ('Mathematics', 'All about numbers and equations', 1001, FALSE),
    ('English', 'Literature and language studies', 1001, FALSE),
    -- User 1002 subjects
    ('Spanish', 'Learn the Spanish language', 1002, FALSE),
    ('French', 'Learn the French language', 1002, FALSE),
    ('Sanskrit', 'Ancient language studies', 1002, TRUE), -- Archived subject
    -- User 1003 subjects
    ('Physics', 'Study of matter and energy', 1003, FALSE),
    ('Chemistry', 'Study of substances and reactions', 1003, FALSE);

INSERT INTO topics (name, description, subject_id)
VALUES
    -- Topics for Mathematics (subject_id = 1)
    ('Algebra', 'Study of mathematical symbols and rules', 1),
    ('Geometry', 'Study of shapes and their properties', 1),
    ('Calculus', 'Study of change and motion', 1),
    -- Topics for English (subject_id = 2)
    ('Grammar', 'Rules of language structure', 2),
    ('Literature', 'Study of written works', 2),
    ('Writing', 'Practice of composing text', 2),
    -- Topics for Spanish (subject_id = 3)
    ('Vocabulary', 'Words and their meanings', 3),
    ('Grammar', 'Rules of Spanish language structure', 3),
    ('Conversation', 'Practice speaking Spanish', 3),
    -- Topics for French (subject_id = 4)
    ('Vocabulary', 'Words and their meanings', 4),
    ('Grammar', 'Rules of French language structure', 4),
    ('Conversation', 'Practice speaking French', 4),
    -- Topics for Physics (subject_id = 5)
    ('Mechanics', 'Study of motion and forces', 5),
    ('Thermodynamics', 'Study of heat and energy', 5),
    ('Optics', 'Study of light and vision', 5),
    -- Topics for Chemistry (subject_id = 6)
    ('Organic Chemistry', 'Study of carbon-containing compounds', 6),
    ('Inorganic Chemistry', 'Study of inorganic compounds', 6),
    ('Physical Chemistry', 'Study of physical properties of molecules', 6);