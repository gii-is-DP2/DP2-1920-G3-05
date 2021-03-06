-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO users(username,password,enabled) VALUES ('admin1','4dm1n',TRUE);
INSERT INTO authorities VALUES ('admin1','admin');
-- One owner user, named owner1 with passwor 0wn3r
INSERT INTO users(username,password,enabled) VALUES ('owner1','0wn3r',TRUE);
INSERT INTO authorities VALUES ('owner1','owner');
-- One vet user, named vet1 with passwor v3t
INSERT INTO users(username,password,enabled) VALUES ('vet1','v3t',TRUE);
INSERT INTO authorities VALUES ('vet1','veterinarian');

INSERT INTO users(username,password,enabled) VALUES ('reader1','reader',TRUE);
INSERT INTO authorities VALUES ('reader1','reader');

INSERT INTO users(username,password,enabled) VALUES ('reader2','reader',TRUE);
INSERT INTO authorities VALUES ('reader2','reader');

INSERT INTO readers(id,first_name,last_name,address,city,telephone,verified,username) VALUES (1, 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '6085551023',TRUE, 'admin1');
INSERT INTO readers(id,first_name,last_name,address,city,telephone,verified,username) VALUES (2, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '6085551749',FALSE, 'owner1');
INSERT INTO readers(id,first_name,last_name,address,city,telephone,verified,username) VALUES (3, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '6085558763',TRUE, 'vet1');
INSERT INTO readers(id,first_name,last_name,address,city,telephone,verified,username) VALUES (4, 'George', 'Rodriquez', '2694 Commerce St.', 'McFarland', '6085558764',TRUE, 'reader1');

INSERT INTO genres VALUES (1, 'Fantasy');
INSERT INTO genres VALUES (2, 'Adventure');
INSERT INTO genres VALUES (3, 'Romance');
INSERT INTO genres VALUES (4, 'Contemporary');
INSERT INTO genres VALUES (5, 'Mystery');
INSERT INTO genres VALUES (6, 'Horror');
INSERT INTO genres VALUES (7, 'Historical');
INSERT INTO genres VALUES (8, 'Science');
INSERT INTO genres VALUES (9, 'Fiction');
INSERT INTO genres VALUES (10, 'Motivational');
INSERT INTO genres VALUES (11, 'Development');
INSERT INTO genres VALUES (12, 'Personal');
INSERT INTO genres VALUES (13, 'Art');
INSERT INTO genres VALUES (14, 'Cooking');
INSERT INTO genres VALUES (15, 'Memoir');
INSERT INTO genres VALUES (16, 'Paranormal');
INSERT INTO genres VALUES (17, 'Thriller');
INSERT INTO genres VALUES (18, 'Dystopian');
INSERT INTO genres VALUES (19, 'Guide');
INSERT INTO genres VALUES (20, 'History');
INSERT INTO genres VALUES (21, 'Travel');
INSERT INTO genres VALUES (22, 'Novel');
INSERT INTO genres VALUES (23, 'Health');
INSERT INTO genres VALUES (24, 'Humor');

INSERT INTO books(id,title,author,genre_id,ISBN,pages,synopsis,editorial,publication_date,verified,image,user_username) VALUES (1, 'IT', 'Stephen King', 6,9788466345347,1138,'¿Quien mata a los niños de un pequeño pueblo norteamericano? Esto es lo que se proponen averiguar los protagonistas de esta novela.','Viking Press','1986-09-15',TRUE,'https://imagessl3.casadellibro.com/a/l/t5/93/9788497593793.jpg','owner1');
INSERT INTO books(id,title,author,genre_id,ISBN,pages,synopsis,editorial,publication_date,verified,image,user_username) VALUES (2, 'Harry Potter y la piedra filosofal', 'J.K. Rowling', 1, 9788498382662 , 223, 'Harry Potter se ha quedado huerfano y vive en casa de sus abominables tios hasta que un buen dia recibe una carta que cambiara su vida para siempre.', 'Bloomsbury','1997-06-26',TRUE,'https://imagessl2.casadellibro.com/a/l/t5/62/9788498382662.jpg','admin1');
INSERT INTO books(id,title,author,genre_id,ISBN,pages,synopsis,editorial,publication_date,verified,image,user_username) VALUES (3, 'Harry Potter y la camara secreta', 'J.K. Rowling', 1, 9788498382679 , 251, 'Tras derrotar una vez mas a lord Voldemort, su siniestro enemigo en Harry Potter y la piedra filosofal, Harry espera impaciente en casa de sus insoportables tios el inicio del segundo curso del Colegio Hogwarts de Magia.', 'Bloomsbury','1998-07-02',FALSE,'https://imagessl9.casadellibro.com/a/l/t5/79/9788498382679.jpg','owner1');
INSERT INTO books(id,title,author,genre_id,ISBN,pages,synopsis,editorial,publication_date,verified,image,user_username) VALUES (4, 'Dime quien soy', 'Julia Navarro', 9, 9788490322222,1097,'Un periodista recibe una propuesta para investigar la azarosa vida de su bisabuela, una mujer de la que solo se sabe que huyo de España abandonando a su marido y a su hijo poco antes de que estallara la Guerra Civil.',' Plaza & Janes','2010-01-01',TRUE,'https://imagessl2.casadellibro.com/a/l/t5/22/9788490322222.jpg','owner1');
INSERT INTO books(id,title,author,genre_id,ISBN,pages,synopsis,editorial,publication_date,verified,image,user_username) VALUES (5, 'Dispara, yo ya estoy muerto', 'Julia Navarro', 7, 9788466333719 , 912, 'La familia Zucker es expulsada de la Rusia zarista por su condicion de judios. Entre Ahmed y Samuel, patriarca de los Zucker, se creara una amistad por encima de las diferencias religiosas y politicas.', 'Plaza & Janes','2013-04-21',TRUE,'https://imagessl9.casadellibro.com/a/l/t5/19/9788466333719.jpg','owner1');
INSERT INTO books(id,title,author,genre_id,ISBN,pages,synopsis,editorial,publication_date,verified,image,user_username) VALUES (6, 'Las marcas de la muerte', 'Veronica Roth',9 , 9788427211582 , 480, 'El don de la joven CYRA consiste en provocar dolor. El mismo dolor atroz que ella siente en todo momento. El don de AKOS le hace inmune a los dones de los demas.', 'HarperCollins','2016-05-13',FALSE,'https://imagessl2.casadellibro.com/a/l/t5/82/9788427211582.jpg','owner1');
INSERT INTO books(id,title,author,genre_id,ISBN,pages,synopsis,editorial,publication_date,verified,image,user_username) VALUES (7, 'Destinos divididos', 'Veronica Roth', 9, 9788427213401,448,'Las vidas de CYRA y AKOS se rigen por los destinos que vaticinaron los oraculos el dia de su nacimiento. Una vez decididos, los destinos son inmutables.','RBA Molino','2018-05-22',TRUE,'https://imagessl1.casadellibro.com/a/l/t5/01/9788427213401.jpg','vet1');
INSERT INTO books(id,title,author,genre_id,ISBN,pages,synopsis,editorial,publication_date,verified,image,user_username) VALUES (8, 'La chica de nieve', 'Javier Castillo', 5, 9788491292661 , 512, 'Nueva York, 1998, cabalgata de Accion de Gracias. Kiera Templeton, de tan solo tres años, desaparece entre la multitud.', 'SUMA','2020-03-12',TRUE,'https://imagessl1.casadellibro.com/a/l/t5/61/9788491292661.jpg','vet1');
INSERT INTO books(id,title,author,genre_id,ISBN,pages,synopsis,editorial,publication_date,verified,image,user_username) VALUES (9, 'Un cuento perfecto', 'Elisabet Benavent', 3, 9788491291916 , 640, 'Erase una vez una mujer que lo tenia todo y un chico que no tenia nada. Erase una vez una historia de amor entre el exito y la duda. Erase una vez un cuento perfecto.', 'SUMA','2020-02-20',FALSE,'https://imagessl6.casadellibro.com/a/l/t5/16/9788491291916.jpg','vet1');
INSERT INTO books(id,title,author,genre_id,ISBN,pages,synopsis,editorial,publication_date,verified,image,user_username) VALUES (10, 'El Principito', 'Antoine de Saint-Exupery', 22, 9788498381498 , 96, 'Un aviador queda incomunicado en el desierto tras sufrir una averia en su avion a mil millas de cualquier region habitada. Alli se encontrara con un pequeño principe de cabellos de oro que afirma vivir en el asteroide B 612.', 'Salamandra','1943-04-01',FALSE,'https://imagessl7.casadellibro.com/a/l/t5/07/9788498386707.jpg','vet1');
INSERT INTO books(id,title,author,genre_id,ISBN,pages,synopsis,editorial,publication_date,verified,image,user_username) VALUES (11, 'El hijo del italiano', 'Rafel Nadal', 4, 9788408208426 , 480, 'Mateu crece en una familia rota que no siente como suya. Desde pequeño lucha por dejar atrás los gritos y la miseria de la Mina, la casa más pobre de Caldes de Malavella.', 'Planeta','2019-05-14',TRUE,'https://imagessl6.casadellibro.com/a/l/t5/26/9788408208426.jpg','vet1');
INSERT INTO books(id,title,author,genre_id,ISBN,pages,synopsis,editorial,publication_date,verified,image,user_username) VALUES (12, 'Reina Roja', 'Juan Gomez-Jurado', 4, 9788466664417 , 568, 'Antonia Scott es una mujer muy especial. Tiene un don que es al mismo tiempo una maldición: una extraordinaria inteligencia. Gracias a ella ha salvado decenas de vidas, pero también lo ha perdido todo.', 'S.A. EDICIONES B','2018-05-14',TRUE,'https://imagessl7.casadellibro.com/a/l/t5/17/9788466664417.jpg','reader1');
INSERT INTO books(id,title,author,genre_id,ISBN,pages,synopsis,editorial,publication_date,verified,image,user_username) VALUES (13, 'Loba negra', 'Juan Gomez-Jurado', 4, 9788466666497 , 552, 'Antonia Scott no tiene miedo a nada. Solo a sí misma.NUNCA FUE. Pero hay alguien más peligroso que ella. Alguien que podría vencerla. TAN DIFÍCIL', 'S.A. EDICIONES B','2018-05-14',TRUE,'https://imagessl7.casadellibro.com/a/l/t5/97/9788466666497.jpg','reader1');
INSERT INTO books(id,title,author,genre_id,ISBN,pages,synopsis,editorial,publication_date,verified,image,user_username) VALUES (14, 'La musica del silencio', 'Patrick Rothfuss', 1, 9788401343575 , 160, 'Auri es uno de los personajes más populares y rodeados de misterio que aparecen en El nombre del viento y El temor de un hombre sabio. Hasta ahora la conocíamos a través de Kvothe. La música del silencio nos permitirá ver el mundo a través de Auri y nos dará la oportunidad de aprender lo que hasta ahora únicamente sabía ella', 'Plaza y Janes','2014-01-11',TRUE,'https://images-na.ssl-images-amazon.com/images/I/51MVAYGJsqL._SX324_BO1,204,203,200_.jpg','reader1');
INSERT INTO books(id,title,author,genre_id,ISBN,pages,synopsis,editorial,publication_date,verified,image,user_username) VALUES (15, 'Libro sin ninguna relacion', 'Vacio', 1, 9788401376443 , 160, 'Vacio', 'Vacio','2014-01-11',TRUE,'https://www.example.jpg','reader1');

INSERT INTO read_book (id,book_id, user_username) VALUES(1,3, 'vet1');
INSERT INTO read_book (id,book_id, user_username) VALUES(2,2, 'vet1');
INSERT INTO read_book (id,book_id, user_username) VALUES(3,6, 'vet1');
INSERT INTO read_book (id,book_id, user_username) VALUES(4,1, 'owner1');
INSERT INTO read_book (id,book_id, user_username) VALUES(5,1, 'admin1');
INSERT INTO read_book (id,book_id, user_username) VALUES(6,4, 'owner1');
INSERT INTO read_book (id,book_id, user_username) VALUES(7,5, 'owner1');
INSERT INTO read_book (id,book_id, user_username) VALUES(8,2, 'owner1');
INSERT INTO read_book (id,book_id, user_username) VALUES(9,6, 'owner1');
INSERT INTO read_book (id,book_id, user_username) VALUES(10,7, 'admin1');
INSERT INTO read_book (id,book_id, user_username) VALUES(11,1, 'vet1');
INSERT INTO read_book (id,book_id, user_username) VALUES(12,8, 'admin1');
INSERT INTO read_book (id,book_id, user_username) VALUES(13,9, 'admin1');
INSERT INTO read_book (id,book_id, user_username) VALUES(14,10, 'admin1');
INSERT INTO read_book (id,book_id, user_username) VALUES(15,11, 'admin1');
INSERT INTO read_book (id,book_id, user_username) VALUES(16,6, 'reader1');
INSERT INTO read_book (id,book_id, user_username) VALUES(17,7, 'reader1');
INSERT INTO read_book (id,book_id, user_username) VALUES(18,8, 'reader1');
INSERT INTO read_book (id,book_id, user_username) VALUES(19,9, 'reader1');
INSERT INTO read_book (id,book_id, user_username) VALUES(20,10, 'reader1');
INSERT INTO read_book (id,book_id, user_username) VALUES(21,11, 'reader1');
INSERT INTO read_book (id,book_id, user_username) VALUES(22,1, 'reader2');


INSERT INTO wished_book (book_id, user_username) VALUES(3, 'admin1');
INSERT INTO wished_book (book_id, user_username) VALUES(4, 'admin1');
INSERT INTO wished_book (book_id, user_username) VALUES(10, 'vet1');
INSERT INTO wished_book (book_id, user_username) VALUES(7, 'vet1');


INSERT INTO meetings(id,name,place,start,end,capacity,book_id) VALUES (1,'Primera reunion','Circulo joven de Los Palacios', '2020-10-20 19:30', '2020-10-20 21:00', 30, 7);
INSERT INTO meetings(id,name,place,start,end,capacity,book_id) VALUES (2,'Reunion club de lectura ETSII','A1.13', '2020-06-30 12:00', '2020-06-30 14:00', 50, 1);
INSERT INTO meetings(id,name,place,start,end,capacity,book_id) VALUES (3,'Libro forum','Biblioteca ETSII', '2020-10-20 19:30', '2020-10-20 21:00', 20, 2);
INSERT INTO meetings(id,name,place,start,end,capacity,book_id) VALUES (4,'Reunion sin asistentes','Nowhere', '2020-05-20 19:30', '2020-05-20 21:00', 20, 10);
INSERT INTO meetings(id,name,place,start,end,capacity,book_id) VALUES (5,'Reunion prueba aforo','Nowhere', '2020-03-15 16:30', '2020-03-15 21:00', 2, 8);
INSERT INTO meetings(id,name,place,start,end,capacity,book_id) VALUES (6,'Club de lectura','Circulo joven de Los Palacios', '2020-03-20 19:30', '2020-03-20 21:00', 30, 13);
INSERT INTO meetings(id,name,place,start,end,capacity,book_id) VALUES (7,'Club de lectura','A1.13', '2020-03-10 12:00', '2020-03-10 14:00', 50, 9);
INSERT INTO meetings(id,name,place,start,end,capacity,book_id) VALUES (8,'Libro','Biblioteca', '2020-03-07 19:30', '2020-03-07 21:00', 20, 13);
INSERT INTO meetings(id,name,place,start,end,capacity,book_id) VALUES (9,'Club de lectura','Biblioteca', '2020-03-21 19:30', '2020-03-21 21:00', 20, 14);
INSERT INTO meetings(id,name,place,start,end,capacity,book_id) VALUES (10,'Club de lectura','Circulo joven de Los Palacios', '2020-03-16 19:30', '2020-03-16 21:00', 30, 6);
INSERT INTO meetings(id,name,place,start,end,capacity,book_id) VALUES (11,'Club de lectura','A1.13', '2020-03-20 12:00', '2020-03-20 14:00', 50, 4);
INSERT INTO meetings(id,name,place,start,end,capacity,book_id) VALUES (12,'Libro','Biblioteca', '2020-03-16 19:30', '2020-03-16 21:00', 20, 5);
INSERT INTO meetings(id,name,place,start,end,capacity,book_id) VALUES (13,'Hijo del taliano','Biblioteca', '2020-03-20 19:30', '2020-03-20 21:00', 20, 12);
INSERT INTO meetings(id,name,place,start,end,capacity,book_id) VALUES (14,'Club de lectura','Biblioteca Tolkien', '2020-09-20 19:30', '2020-09-20 21:00', 4000, 1);


INSERT INTO meeting_assistants(id,meeting_id, user_username) VALUES (1, 1, 'admin1');
INSERT INTO meeting_assistants(id,meeting_id, user_username) VALUES (2, 1, 'owner1');
INSERT INTO meeting_assistants(id,meeting_id, user_username) VALUES (3, 2, 'admin1');
INSERT INTO meeting_assistants(id,meeting_id, user_username) VALUES (4, 2, 'owner1');
INSERT INTO meeting_assistants(id,meeting_id, user_username) VALUES (5, 3, 'admin1');
INSERT INTO meeting_assistants(id,meeting_id, user_username) VALUES (6, 3, 'owner1');
INSERT INTO meeting_assistants(id,meeting_id, user_username) VALUES (7, 5, 'admin1');
INSERT INTO meeting_assistants(id,meeting_id, user_username) VALUES (8, 5, 'owner1');
INSERT INTO meeting_assistants(id,meeting_id, user_username) VALUES (9, 10, 'vet1');
INSERT INTO meeting_assistants(id,meeting_id, user_username) VALUES (10, 10, 'reader1');
INSERT INTO meeting_assistants(id,meeting_id, user_username) VALUES (11, 12, 'owner1');
INSERT INTO meeting_assistants(id,meeting_id, user_username) VALUES (13, 13, 'owner1');
INSERT INTO meeting_assistants(id,meeting_id, user_username) VALUES (14, 11, 'admin1');
INSERT INTO meeting_assistants(id,meeting_id, user_username) VALUES (15, 11, 'reader1');
INSERT INTO meeting_assistants(id,meeting_id, user_username) VALUES (16, 7, 'admin1');
INSERT INTO meeting_assistants(id,meeting_id, user_username) VALUES (17, 7, 'reader1');


INSERT INTO news(id, name, head, fecha, body, redactor, tags, img) VALUES (1, 'Noticia 1', 'Harry potter vuelve', '2013-01-01','increible pero cierto','Jhon Doe','#impresionant','https://imagessl4.casadellibro.com/a/l/t5/44/9788498387544.jpg' );
INSERT INTO news(id, name, head, fecha, body, redactor, tags, img) VALUES (2, 'Noticia 2', 'Rafel Nadal publica un nuevo libro', '2015-03-03','Rafel Nadal, que no Rafael Nadal, nos vuelve a impresionar','Jhon Doe','#amazing#new','https://www.elnacional.cat/uploads/s1/13/75/37/7/Rafael%20Nadal%20periodista%20escriptor%20-Sergi%20Alc%C3%A0zar%20-%204_12_543x814.jpg' );
INSERT INTO news(id, name, head, fecha, body, redactor, tags, img) VALUES (3, 'Noticia 3', 'IT resurge tras su llegada al cine', '2015-03-03','El final un poco defraudante','Jhon Doe','#cinema#fear','https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/IT_%282017_film%29_logo.svg/1200px-IT_%282017_film%29_logo.svg.png' );
INSERT INTO news(id, name, head, fecha, body, redactor, tags, img) VALUES (4, 'Noticia 4', 'La nueva novela de Julia Navarro, «Tú no matarás», saldrá a la venta en octubre', '2018-09-05','La nueva novela de Julia Navarro, «Tú no matarás» (Plaza & Janés y en catalán, en Rosa dels Vents), se publicará el próximo 25 de octubre.','Jhon Doe','#impresionant','https://static4.abc.es/media/cultura/2018/09/05/Portada-Tu-no-mataras-Julia-navarro-kjVH-U301327176608Z9B-220x330@abc.jpg' );
INSERT INTO news(id, name, head, fecha, body, redactor, tags, img) VALUES (5, 'Noticia 5', 'El fin y otros inicios, la autora de Divergente regresa con algo que recuerda a Black Mirror', '2020-02-17','La escritora estadounidense Veronica Roth, conocida por su trilogía Divergente, ha publicado algo totalmente diferente a lo que hasta ahora habíamos visto en sus novelas.','Cristina BR','#amazing #new','https://infoliteraria.com/wp-content/uploads/2020/02/El-fin-y-otros-inicios.jpg' );
INSERT INTO news(id, name, head, fecha, body, redactor, tags, img) VALUES (6, 'Noticia 6', 'La editorial Salamandra trae una nueva obra de J.K.Rowling', '2018-03-09','La editorial traerá próximamente la traducción del brillante discurso de la famosa escritora de la saga de Harry Potter, J.K. Rowling, que pronunció delante de los graduados del 2008 en la Universidad de Harvard.','Cristina DPL','#new','https://infoliteraria.com/wp-content/uploads/2018/03/vivir_j.k.rowling-768x768.jpg' );

INSERT INTO book_in_new (neew_id, book_id) VALUES (1,2);
INSERT INTO book_in_new (neew_id, book_id) VALUES (1,3);
INSERT INTO book_in_new (neew_id, book_id) VALUES (2,11);
INSERT INTO book_in_new (neew_id, book_id) VALUES (3,1);
INSERT INTO book_in_new (neew_id, book_id) VALUES (4,4);
INSERT INTO book_in_new (neew_id, book_id) VALUES (4,5);
INSERT INTO book_in_new (neew_id, book_id) VALUES (5,6);
INSERT INTO book_in_new (neew_id, book_id) VALUES (5,5);
INSERT INTO book_in_new (neew_id, book_id) VALUES (6,3);
INSERT INTO book_in_new (neew_id, book_id) VALUES (6,4);

INSERT INTO reviews(id,raiting,title,opinion,book_id,user_username) VALUES (1,4,'Libro muy recomendable','Es un libro magnifico muy recomendable para todo el mundo, me ha encantado',1,'owner1');
INSERT INTO reviews(id,raiting,title,opinion,book_id,user_username) VALUES (2,2,'Libro mediocre','Es un libro que no me ha aportado nada nuevo',1,'admin1');
INSERT INTO reviews(id,raiting,title,opinion,book_id,user_username) VALUES (3,3,'Libro agradable','Es un libro fácil de leer que te hace olvidarte de tus preocupaciones',1,'vet1');
INSERT INTO reviews(id,raiting,title,opinion,book_id,user_username) VALUES (4,5,'Libro muy recomendable','Es un libro magnifico muy recomendable para todo el mundo, me ha encantado',2,'owner1');
INSERT INTO reviews(id,raiting,title,opinion,book_id,user_username) VALUES (5,3,'Libro para pasar el rato','Es un libro corto y curioso',3,'vet1');
INSERT INTO reviews(id,raiting,title,opinion,book_id,user_username) VALUES (6,3,'Libro ameno','Es un libro interesante pero muy tipico',4,'vet1');
INSERT INTO reviews(id,raiting,title,opinion,book_id,user_username) VALUES (7,4,'Libro interesante','Es un libro magnifico innovativo que trata una tematica poco comun',5,'owner1');
INSERT INTO reviews(id,raiting,title,opinion,book_id,user_username) VALUES (8,4,'Libro muy recomendable','Es un libro magnifico muy recomendable para todo el mundo, me ha encantado',6,'reader1');
INSERT INTO reviews(id,raiting,title,opinion,book_id,user_username) VALUES (9,3,'Libro mediocre','Es un libro que no me ha aportado nada nuevo',6,'vet1');
INSERT INTO reviews(id,raiting,title,opinion,book_id,user_username) VALUES (10,3,'Libro agradable','Es un libro fácil de leer que te hace olvidarte de tus preocupaciones',12,'reader1');
INSERT INTO reviews(id,raiting,title,opinion,book_id,user_username) VALUES (11,5,'Libro muy recomendable','Es un libro magnifico muy recomendable para todo el mundo, me ha encantado',8,'reader1');
INSERT INTO reviews(id,raiting,title,opinion,book_id,user_username) VALUES (12,3,'Libro para pasar el rato','Es un libro corto y curioso',9,'reader1');
INSERT INTO reviews(id,raiting,title,opinion,book_id,user_username) VALUES (13,2,'Libro mediocre','Es un libro que no me ha aportado nada nuevo',10,'reader1');
INSERT INTO reviews(id,raiting,title,opinion,book_id,user_username) VALUES (14,4,'Libro interesante','Es un libro magnifico innovativo que trata una tematica poco comun',11,'reader1');


INSERT INTO publications (id,title,description,publication_date,user_id,book_id) VALUES (1,'publication 1', 'this is tests data', '2020-03-07','admin1',1);
INSERT INTO publications (id,title,description,publication_date,user_id,book_id) VALUES (2,'publication 2', 'this is tests data', '2020-03-07','owner1',1);
INSERT INTO publications (id,title,description,publication_date,user_id,book_id) VALUES (3,'publication 3', 'this is tests data', '2020-03-07','admin1',2);
INSERT INTO publications (id,title,description,publication_date,user_id,book_id) VALUES (4,'publication 4', 'this is tests data', '2020-03-07','owner1',7);
INSERT INTO publications (id,title,description,publication_date,user_id,book_id) VALUES (5,'publication 5', 'this is tests data', '2020-03-07','admin1',8);
INSERT INTO publications (id,title,description,publication_date,user_id,book_id) VALUES (6,'publication 6', 'this is tests data', '2020-03-07','owner1',10);