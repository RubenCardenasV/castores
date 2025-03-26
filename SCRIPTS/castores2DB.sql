

CREATE TABLE roles (
  idRol INT PRIMARY KEY,
  nombre VARCHAR(50) NOT NULL
);

CREATE TABLE usuarios (
  idUsuario INT PRIMARY KEY,
  nombre VARCHAR(100),
  correo VARCHAR(50),
  contrasena VARCHAR(25),
  idRol INT(2),
  estatus INT(1) DEFAULT 1,
  FOREIGN KEY (idRol) REFERENCES roles(idRol)
);

CREATE TABLE productos (
  idProducto int PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  descripcion TEXT,
  cantidad INT DEFAULT 0,
  estatus INT(1) DEFAULT 1
);

CREATE TABLE tipos_movimiento (
  idTipo INT PRIMARY KEY,
  nombre VARCHAR(50) NOT NULL
);


CREATE TABLE movimientos (
  idMovimiento INT PRIMARY KEY AUTO_INCREMENT,
  idProducto INT,
  idUsuario INT,
  idTipo INT,
  cantidad INT NOT NULL,
  fechaHora DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (idProducto) REFERENCES productos(idProducto),
  FOREIGN KEY (idUsuario) REFERENCES usuarios(idUsuario),
  FOREIGN KEY (idTipo) REFERENCES tipos_movimiento(idTipo)
);


INSERT into roles (idRol,nombre)values
(1,'administrador'),
(2,'almacenista');

select * from usuarios;

INSERT into usuarios (idUsuario,nombre,correo,contrasena,idRol) values
(1,'admin','admin@castores.com','123',1),
(2,'almacenista1','almacen1@castores.com','123',2);

select * from productos;

select * from movimientos;

select * from tipos_movimiento

insert into tipos_movimiento (idTipo,nombre) values
(1,'entrada'),
(2,'salida');

SELECT VERSION();







