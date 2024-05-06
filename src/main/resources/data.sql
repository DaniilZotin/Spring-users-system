INSERT INTO "user" (email, first_name, last_name, birth_date, address, phone_number)
SELECT 'john@example.com', 'John', 'Doe', '1990-01-01', '123 Main St', '+380673456789'
WHERE NOT EXISTS (SELECT 1 FROM "user" WHERE email = 'john@example.com');

INSERT INTO "user" (email, first_name, last_name, birth_date, address, phone_number)
SELECT 'jane@example.com', 'Jane', 'Doe', '1992-05-15', '456 Elm St', '+380987654321'
WHERE NOT EXISTS (SELECT 1 FROM "user" WHERE email = 'jane@example.com');

INSERT INTO "user" (email, first_name, last_name, birth_date, address, phone_number)
SELECT 'alice@example.com', 'Alice', 'Smith', '1985-09-20', '789 Oak St', '+380951222333'
WHERE NOT EXISTS (SELECT 1 FROM "user" WHERE email = 'alice@example.com');

INSERT INTO "user" (email, first_name, last_name, birth_date, address, phone_number)
SELECT 'bob@example.com', 'Bob', 'Johnson', '1978-11-30', '101 Pine St', '+380504555666'
WHERE NOT EXISTS (SELECT 1 FROM "user" WHERE email = 'bob@example.com');

INSERT INTO "user" (email, first_name, last_name, birth_date, address, phone_number)
SELECT 'mary@example.com', 'Mary', 'Brown', '1982-07-10', '202 Maple St', '+380997888999'
WHERE NOT EXISTS (SELECT 1 FROM "user" WHERE email = 'mary@example.com');
