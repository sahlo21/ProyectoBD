-- Crear tabla para auditar la entrada y salida de usuarios
-- Primero verificamos si la tabla ya existe
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[auditoria]') AND type in (N'U'))
BEGIN
    CREATE TABLE auditoria (
        id INT IDENTITY(1,1) PRIMARY KEY,
        id_usuario INT NOT NULL,
        nombre_usuario VARCHAR(100) NOT NULL,
        accion VARCHAR(20) NOT NULL,  -- 'LOGIN' o 'LOGOUT'
        fecha_hora DATETIME NOT NULL
    );

    -- Índices para mejorar el rendimiento de las consultas
    CREATE INDEX idx_usuario ON auditoria(id_usuario);
    CREATE INDEX idx_accion ON auditoria(accion);
    CREATE INDEX idx_fecha ON auditoria(fecha_hora);

    -- SQL Server usa EXEC sp_addextendedproperty para agregar comentarios/descripciones
    EXEC sp_addextendedproperty 'MS_Description', 'Tabla para auditar la entrada y salida de usuarios a la aplicación', 'SCHEMA', 'dbo', 'TABLE', 'auditoria', NULL, NULL;
    EXEC sp_addextendedproperty 'MS_Description', 'Identificador único del registro de auditoría', 'SCHEMA', 'dbo', 'TABLE', 'auditoria', 'COLUMN', 'id';
    EXEC sp_addextendedproperty 'MS_Description', 'Identificador (cédula) del usuario', 'SCHEMA', 'dbo', 'TABLE', 'auditoria', 'COLUMN', 'id_usuario';
    EXEC sp_addextendedproperty 'MS_Description', 'Nombre de usuario que realizó la acción', 'SCHEMA', 'dbo', 'TABLE', 'auditoria', 'COLUMN', 'nombre_usuario';
    EXEC sp_addextendedproperty 'MS_Description', 'Tipo de acción: LOGIN o LOGOUT', 'SCHEMA', 'dbo', 'TABLE', 'auditoria', 'COLUMN', 'accion';
    EXEC sp_addextendedproperty 'MS_Description', 'Fecha y hora en que se realizó la acción', 'SCHEMA', 'dbo', 'TABLE', 'auditoria', 'COLUMN', 'fecha_hora';
END;