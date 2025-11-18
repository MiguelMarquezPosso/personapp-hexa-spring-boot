// Usar la base de datos persona_db
db = db.getSiblingDB('persona_db');

// Verificar si las colecciones existen, si no, crearlas
const collections = ["persona", "profesion", "telefono", "estudios"];
collections.forEach(collectionName => {
    if (!db.getCollectionNames().includes(collectionName)) {
        db.createCollection(collectionName);
    }
});

// Limpiar colecciones existentes (opcional, para reinicialización)
db.persona.deleteMany({});
db.profesion.deleteMany({});
db.telefono.deleteMany({});
db.estudios.deleteMany({});

// Insertar datos en la colección persona
db.persona.insertMany([
    {
       "_id": NumberInt(123456789),
       "nombre": "Pepe",
       "apellido": "Perez",
       "genero": "M",
       "edad": NumberInt(30),
       "_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
    },
    {
       "_id": NumberInt(987654321),
       "nombre": "Pepito",
       "apellido": "Perez",
       "genero": "M",
       "_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
    },
    {
       "_id": NumberInt(321654987),
       "nombre": "Pepa",
       "apellido": "Juarez",
       "genero": "F",
       "edad": NumberInt(30),
       "_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
    },
    {
       "_id": NumberInt(147258369),
       "nombre": "Pepita",
       "apellido": "Juarez",
       "genero": "F",
       "edad": NumberInt(10),
       "_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
    },
    {
       "_id": NumberInt(963852741),
       "nombre": "Fede",
       "apellido": "Perez",
       "genero": "M",
       "edad": NumberInt(18),
       "_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
    }
], { ordered: false });

// Insertar datos en la colección profesion
db.profesion.insertMany([
    {
        "_id": NumberInt(100001),
        "nom": "Ingeniero de Sistemas",
        "des": "Diseña, desarrolla y mantiene sistemas de software y hardware",
        "_class": "co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument"
    },
    {
        "_id": NumberInt(100002),
        "nom": "Médico General",
        "des": "Diagnostica y trata enfermedades en pacientes",
        "_class": "co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument"
    },
    {
        "_id": NumberInt(100003),
        "nom": "Abogado",
        "des": "Asesora y representa clientes en asuntos legales",
        "_class": "co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument"
    },
    {
        "_id": NumberInt(100004),
        "nom": "Contador",
        "des": "Maneja estados financieros y contabilidad de empresas",
        "_class": "co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument"
    },
    {
        "_id": NumberInt(100005),
        "nom": "Diseñador Gráfico",
        "des": "Crea soluciones visuales para comunicación",
        "_class": "co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument"
    }
], { ordered: false });

// Insertar datos en la colección telefono
db.telefono.insertMany([
    {
        "_id": "3001234567",
        "num": "3001234567",
        "oper": "Claro",
        "duenio": NumberInt(123456789),
        "_class": "co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument"
    },
    {
        "_id": "3102345678",
        "num": "3102345678",
        "oper": "Movistar",
        "duenio": NumberInt(123456789),
        "_class": "co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument"
    },
    {
        "_id": "3203456789",
        "num": "3203456789",
        "oper": "Tigo",
        "duenio": NumberInt(987654321),
        "_class": "co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument"
    },
    {
        "_id": "3009876543",
        "num": "3009876543",
        "oper": "Claro",
        "duenio": NumberInt(321654987),
        "_class": "co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument"
    },
    {
        "_id": "3158765432",
        "num": "3158765432",
        "oper": "Movistar",
        "duenio": NumberInt(147258369),
        "_class": "co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument"
    },
    {
        "_id": "3187654321",
        "num": "3187654321",
        "oper": "Tigo",
        "duenio": NumberInt(963852741),
        "_class": "co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument"
    }
], { ordered: false });

// Insertar datos en la colección estudios
db.estudios.insertMany([
    {
        "_id": {
            "id_prof": NumberInt(100001),
            "cc_per": NumberInt(123456789)
        },
        "id_prof": NumberInt(100001),
        "cc_per": NumberInt(123456789),
        "fecha": ISODate("2015-06-15T00:00:00Z"),
        "univer": "Universidad Javeriana",
        "_class": "co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument"
    },
    {
        "_id": {
            "id_prof": NumberInt(100002),
            "cc_per": NumberInt(123456789)
        },
        "id_prof": NumberInt(100002),
        "cc_per": NumberInt(123456789),
        "fecha": ISODate("2018-08-20T00:00:00Z"),
        "univer": "Universidad Nacional",
        "_class": "co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument"
    },
    {
        "_id": {
            "id_prof": NumberInt(100001),
            "cc_per": NumberInt(321654987)
        },
        "id_prof": NumberInt(100001),
        "cc_per": NumberInt(321654987),
        "fecha": ISODate("2014-12-10T00:00:00Z"),
        "univer": "Universidad de los Andes",
        "_class": "co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument"
    },
    {
        "_id": {
            "id_prof": NumberInt(100003),
            "cc_per": NumberInt(321654987)
        },
        "id_prof": NumberInt(100003),
        "cc_per": NumberInt(321654987),
        "fecha": ISODate("2019-05-30T00:00:00Z"),
        "univer": "Universidad Externado",
        "_class": "co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument"
    },
    {
        "_id": {
            "id_prof": NumberInt(100004),
            "cc_per": NumberInt(987654321)
        },
        "id_prof": NumberInt(100004),
        "cc_per": NumberInt(987654321),
        "fecha": ISODate("2020-03-25T00:00:00Z"),
        "univer": "Universidad del Rosario",
        "_class": "co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument"
    },
    {
        "_id": {
            "id_prof": NumberInt(100005),
            "cc_per": NumberInt(147258369)
        },
        "id_prof": NumberInt(100005),
        "cc_per": NumberInt(147258369),
        "fecha": ISODate("2022-11-15T00:00:00Z"),
        "univer": "Universidad Jorge Tadeo Lozano",
        "_class": "co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument"
    },
    {
        "_id": {
            "id_prof": NumberInt(100001),
            "cc_per": NumberInt(963852741)
        },
        "id_prof": NumberInt(100001),
        "cc_per": NumberInt(963852741),
        "fecha": ISODate("2021-07-08T00:00:00Z"),
        "univer": "Universidad Distrital",
        "_class": "co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument"
    }
], { ordered: false });
