-- Script para modificar la columna 'name' en la tabla 'Cargo' de INT a VARCHAR
ALTER TABLE Cargo MODIFY COLUMN name VARCHAR(100);

-- Nota: Si estás usando MySQL, la sintaxis anterior funcionará.
-- Si estás usando SQL Server, usa esta sintaxis:
-- ALTER TABLE Cargo ALTER COLUMN name VARCHAR(100);

-- Si estás usando PostgreSQL, usa esta sintaxis:
-- ALTER TABLE Cargo ALTER COLUMN name TYPE VARCHAR(100);

-- Si estás usando SQLite, necesitarás recrear la tabla ya que SQLite no soporta ALTER COLUMN:
-- 1. Crear una tabla temporal con la nueva estructura
-- CREATE TABLE Cargo_temp (idCargo INTEGER, name VARCHAR(100), precio_evento REAL);
-- 2. Copiar los datos, convirtiendo el campo name a texto
-- INSERT INTO Cargo_temp SELECT idCargo, CAST(name AS TEXT), precio_evento FROM Cargo;
-- 3. Eliminar la tabla original
-- DROP TABLE Cargo;
-- 4. Renombrar la tabla temporal
-- ALTER TABLE Cargo_temp RENAME TO Cargo;