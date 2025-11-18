-- Insertar datos en persona (ya existente)
INSERT INTO
    `persona_db`.`persona`(`cc`,`nombre`,`apellido`,`genero`,`edad`)
VALUES
    (123456789,'Pepe','Perez','M',30),
    (987654321,'Pepito','Perez','M',null),
    (321654987,'Pepa','Juarez','F',30),
    (147258369,'Pepita','Juarez','F',10),
    (963852741,'Fede','Perez','M',18);

-- Insertar datos en profesion
INSERT INTO
    `persona_db`.`profesion`(`id`,`nom`,`des`)
VALUES
    (100001,'Ingeniero de Sistemas','Diseña, desarrolla y mantiene sistemas de software y hardware'),
    (100002,'Médico General','Diagnostica y trata enfermedades en pacientes'),
    (100003,'Abogado','Asesora y representa clientes en asuntos legales'),
    (100004,'Contador','Maneja estados financieros y contabilidad de empresas'),
    (100005,'Diseñador Gráfico','Crea soluciones visuales para comunicación');

-- Insertar datos en telefono
INSERT INTO
    `persona_db`.`telefono`(`num`,`oper`,`duenio`)
VALUES
    ('3001234567','Claro',123456789),
    ('3102345678','Movistar',123456789),
    ('3203456789','Tigo',987654321),
    ('3009876543','Claro',321654987),
    ('3158765432','Movistar',147258369),
    ('3187654321','Tigo',963852741);

-- Insertar datos en estudios
INSERT INTO
    `persona_db`.`estudios`(`id_prof`,`cc_per`,`fecha`,`univer`)
VALUES
    (100001,123456789,'2015-06-15','Universidad Javeriana'),
    (100002,123456789,'2018-08-20','Universidad Nacional'),
    (100001,321654987,'2014-12-10','Universidad de los Andes'),
    (100003,321654987,'2019-05-30','Universidad Externado'),
    (100004,987654321,'2020-03-25','Universidad del Rosario'),
    (100005,147258369,'2022-11-15','Universidad Jorge Tadeo Lozano'),
    (100001,963852741,'2021-07-08','Universidad Distrital');

COMMIT;