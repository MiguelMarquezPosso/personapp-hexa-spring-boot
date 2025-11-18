// Este script se ejecuta automáticamente durante la inicialización
// No necesitamos autenticarnos manualmente aquí

// Crear la base de datos persona_db (se crea automáticamente al usarla)
db = db.getSiblingDB('persona_db');

// Crear usuario para la aplicación
db.createUser({
  user: "persona_db",
  pwd: "persona_db",
  roles: [
    { role: "readWrite", db: "persona_db" },
    { role: "dbAdmin", db: "persona_db" }
  ]
});

// Crear colecciones
db.createCollection("persona");
db.createCollection("profesion");
db.createCollection("telefono");
db.createCollection("estudios");

print('Base de datos persona_db y usuario creados exitosamente');