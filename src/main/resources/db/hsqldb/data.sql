-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO users(username,password,enabled) VALUES ('admin1','4dm1n',TRUE);
INSERT INTO authorities VALUES ('admin1','admin');
-- One owner user, named owner1 with passwor 0wn3r
INSERT INTO users(username,password,enabled) VALUES ('owner1','0wn3r',TRUE);
INSERT INTO authorities VALUES ('owner1','owner');
-- One vet user, named vet1 with passwor v3t
INSERT INTO users(username,password,enabled) VALUES ('vet1','v3t',TRUE);
INSERT INTO authorities VALUES ('vet1','veterinarian');

INSERT INTO readers(id,first_name,last_name,address,city,telephone,verified,username) VALUES (1, 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '6085551023',TRUE, 'admin1');
INSERT INTO readers(id,first_name,last_name,address,city,telephone,verified,username) VALUES (2, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '6085551749',FALSE, 'owner1');
INSERT INTO readers(id,first_name,last_name,address,city,telephone,verified,username) VALUES (3, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '6085558763',TRUE, 'vet1');

INSERT INTO vets VALUES (1, 'James', 'Carter');
INSERT INTO vets VALUES (2, 'Helen', 'Leary');
INSERT INTO vets VALUES (3, 'Linda', 'Douglas');
INSERT INTO vets VALUES (4, 'Rafael', 'Ortega');
INSERT INTO vets VALUES (5, 'Henry', 'Stevens');
INSERT INTO vets VALUES (6, 'Sharon', 'Jenkins');


INSERT INTO specialties VALUES (1, 'radiology');
INSERT INTO specialties VALUES (2, 'surgery');
INSERT INTO specialties VALUES (3, 'dentistry');

INSERT INTO vet_specialties VALUES (2, 1);
INSERT INTO vet_specialties VALUES (3, 2);
INSERT INTO vet_specialties VALUES (3, 3);
INSERT INTO vet_specialties VALUES (4, 2);
INSERT INTO vet_specialties VALUES (5, 1);

INSERT INTO types VALUES (1, 'cat');
INSERT INTO types VALUES (2, 'dog');
INSERT INTO types VALUES (3, 'lizard');
INSERT INTO types VALUES (4, 'snake');
INSERT INTO types VALUES (5, 'bird');
INSERT INTO types VALUES (6, 'hamster');

INSERT INTO owners VALUES (1, 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '6085551023', 'owner1');
INSERT INTO owners VALUES (2, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '6085551749', 'owner1');
INSERT INTO owners VALUES (3, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '6085558763', 'owner1');
INSERT INTO owners VALUES (4, 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '6085553198', 'owner1');
INSERT INTO owners VALUES (5, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '6085552765', 'owner1');
INSERT INTO owners VALUES (6, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '6085552654', 'owner1');
INSERT INTO owners VALUES (7, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '6085555387', 'owner1');
INSERT INTO owners VALUES (8, 'Maria', 'Escobito', '345 Maple St.', 'Madison', '6085557683', 'owner1');
INSERT INTO owners VALUES (9, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '6085559435', 'owner1');
INSERT INTO owners VALUES (10, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '6085555487', 'owner1');

INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (1, 'Leo', '2010-09-07', 1, 1);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (2, 'Basil', '2012-08-06', 6, 2);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (3, 'Rosy', '2011-04-17', 2, 3);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (4, 'Jewel', '2010-03-07', 2, 3);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (5, 'Iggy', '2010-11-30', 3, 4);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (6, 'George', '2010-01-20', 4, 5);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (7, 'Samantha', '2012-09-04', 1, 6);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (8, 'Max', '2012-09-04', 1, 6);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (9, 'Lucky', '2011-08-06', 5, 7);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (10, 'Mulligan', '2007-02-24', 2, 8);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (11, 'Freddy', '2010-03-09', 5, 9);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (12, 'Lucky', '2010-06-24', 2, 10);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (13, 'Sly', '2012-06-08', 1, 10);

INSERT INTO visits(id,pet_id,visit_date,description) VALUES (1, 7, '2013-01-01', 'rabies shot');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (2, 8, '2013-01-02', 'rabies shot');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (3, 8, '2013-01-03', 'neutered');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (4, 7, '2013-01-04', 'spayed');


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

INSERT INTO meetings(id,name,place,start,end,capacity,book_id) VALUES (1,'Primera reunion','Circulo joven de Los Palacios', '2020-10-20 19:30', '2020-10-20 21:00', 30, 7);
INSERT INTO meetings(id,name,place,start,end,capacity,book_id) VALUES (2,'Reunion club de lectura ETSII','A1.13', '2020-06-30 12:00', '2020-06-30 14:00', 50, 1);
INSERT INTO meetings(id,name,place,start,end,capacity,book_id) VALUES (3,'Libro forum','Biblioteca ETSII', '2020-10-20 19:30', '2020-10-20 21:00', 20, 2);
INSERT INTO meetings(id,name,place,start,end,capacity,book_id) VALUES (4,'Reunion sin asistentes','Nowhere', '2020-10-20 19:30', '2020-10-20 21:00', 20, 10);
INSERT INTO meeting_assistants(id,meeting_id, user_username) VALUES (1, 1, 'admin1');
INSERT INTO meeting_assistants(id,meeting_id, user_username) VALUES (2, 1, 'owner1');
INSERT INTO meeting_assistants(id,meeting_id, user_username) VALUES (3, 2, 'admin1');
INSERT INTO meeting_assistants(id,meeting_id, user_username) VALUES (4, 2, 'owner1');
INSERT INTO meeting_assistants(id,meeting_id, user_username) VALUES (5, 3, 'admin1');
INSERT INTO meeting_assistants(id,meeting_id, user_username) VALUES (6, 3, 'owner1');


INSERT INTO news(id, name, head, fecha, body, redactor, tags, img) VALUES (1, 'Noticia 1', 'Harry potter vuelve', '2013-01-01','increible pero cierto','Jhon Doe','#impresionant','https://images-na.ssl-images-amazon.com/images/I/51jqJQf7B7L._SY445_QL70_ML2_.jpg' );
INSERT INTO news(id, name, head, fecha, body, redactor, tags, img) VALUES (2, 'Noticia 2', 'Rafel Nadal publica un nuevo libro', '2015-03-03','Rafel Nadal, que no Rafael Nadal, nos vuelve a impresionar','Jhon Doe','#amazing#new','https://images-eu.ssl-images-amazon.com/images/I/51QsPyrJdYL.jpg' );
INSERT INTO news(id, name, head, fecha, body, redactor, tags, img) VALUES (3, 'Noticia 3', 'IT resurge tras su llegada al cine', '2015-03-03','El final un poco defraudante','Jhon Doe','#cinema#fear','https://www.popularlibros.com/imagenes_grandes/9788497/978849759379.JPG' );
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

INSERT INTO wished_book (book_id, user_username) VALUES(2, 'admin1');
INSERT INTO wished_book (book_id, user_username) VALUES(4, 'admin1');

INSERT INTO reviews(id,raiting,title,opinion,book_id,user_username) VALUES (1,4,'Libro muy recomendable','Es un libro magnifico muy recomendable para todo el mundo, me ha encantado',1,'owner1');
INSERT INTO reviews(id,raiting,title,opinion,book_id,user_username) VALUES (2,2,'Libro mediocre','Es un libro que no me ha aportado nada nuevo',1,'admin1');
INSERT INTO reviews(id,raiting,title,opinion,book_id,user_username) VALUES (3,3,'Libro agradable','Es un libro fácil de leer que te hace olvidarte de tus preocupaciones',1,'vet1');
INSERT INTO reviews(id,raiting,title,opinion,book_id,user_username) VALUES (4,5,'Libro muy recomendable','Es un libro magnifico muy recomendable para todo el mundo, me ha encantado',2,'owner1');
INSERT INTO reviews(id,raiting,title,opinion,book_id,user_username) VALUES (5,3,'Libro para pasar el rato','Es un libro corto y curioso',3,'vet1');
INSERT INTO reviews(id,raiting,title,opinion,book_id,user_username) VALUES (6,3,'Libro ameno','Es un libro interesante pero muy tipico',4,'vet1');
INSERT INTO reviews(id,raiting,title,opinion,book_id,user_username) VALUES (7,4,'Libro interesante','Es un libro magnifico innovativo que trata una tematica poco comun',5,'owner1');



INSERT INTO publications (id,title,description,publication_date,image,user_id,book_id) VALUES (1,'publication 1', 'this is tests data', '2020-03-07','https://lh3.googleusercontent.com/proxy/9xJwN4k_Q-pPsRiDF6biPeUar08kxIY9qEKMk9k2oOF_JHMly-x4fA0JuXPpS7WR-bJBCiSlfaRQ97ohxkQvU4X2gQMFOS16W1zdoX4Tg7Bl4APN4ObQtlGjaYwbavENT07Uql5UrHK9VnviQAP_OxNVYh0','admin1',1);
INSERT INTO publications (id,title,description,publication_date,image,user_id,book_id) VALUES (2,'publication 2', 'this is tests data', '2020-03-07','https://los40es00.epimg.net/los40/imagenes/los40classic/2018/03/foto-test.png','owner1',1);
INSERT INTO publications (id,title,description,publication_date,image,user_id,book_id) VALUES (3,'publication 3', 'this is tests data', '2020-03-07','https://economipedia.com/wp-content/uploads/test-de-estr%C3%A9s.png','admin1',2);
INSERT INTO publications (id,title,description,publication_date,image,user_id,book_id) VALUES (4,'publication 4', 'this is tests data', '2020-03-07','https://www.estilosdeaprendizaje.org/wp-content/uploads/2020/02/testestilosdeaprendizaje.png','owner1',7);
INSERT INTO publications (id,title,description,publication_date,image,user_id,book_id) VALUES (5,'publication 5', 'this is tests data', '2020-03-07','https://i.ytimg.com/vi/7UH5cSfDBps/maxresdefault.jpg','admin1',8);
INSERT INTO publications (id,title,description,publication_date,image,user_id,book_id) VALUES (6,'publication 6', 'this is tests data', '2020-03-07','https://www.frikipandi.com/wp-content/uploads/2015/05/bigstock-Test-word-on-white-keyboard-640x330.jpg','owner1',10);
