CREATE TABLE productos (
    idProducto INT(6) PRIMARY KEY,
    nombre VARCHAR(40),
    precio DECIMAL(16,2)
);

CREATE TABLE ventas (
    idVenta INT(6) PRIMARY KEY,
    idProducto INT(6),
    cantidad INT(6),
    FOREIGN KEY (idProducto) REFERENCES productos(idProducto)
);


INSERT INTO productos (idProducto, nombre, precio) VALUES
(1, 'LAPTOP', 3000.00),
(2, 'PC', 4000.00),
(3, 'MOUSE', 100.00),
(4, 'TECLADO', 150.00),
(5, 'MONITOR', 2000.00),
(6, 'MICROFONO', 350.00),
(7, 'AUDIFONOS', 450.00);

INSERT INTO ventas (idVenta, idProducto, cantidad) VALUES
(1, 5, 8),
(2, 1, 15),
(3, 6, 13),
(4, 6, 4),
(5, 2, 3),
(6, 5, 1),
(7, 4, 5),
(8, 2, 5),
(9, 6, 2),
(10, 1, 8);

select * from ventas;


select productos.* from productos
right join ventas on productos.idProducto = ventas.idProducto
GROUP by 1

select productos.*, SUM(ventas.cantidad) as 'total de productos vendidos' from productos
right join ventas on productos.idProducto = ventas.idProducto
GROUP BY 1


select productos.*, 
IFNULL(SUM(ventas.cantidad),0) as 'total de productos vendidos',
 (productos.precio * IFNULL(SUM(ventas.cantidad), 0)) AS 'sub total'
from productos
left join ventas on productos.idProducto = ventas.idProducto
GROUP BY 1 


