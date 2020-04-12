# BookLand

En este documento vamos a sentar las bases del proyecto para la asignatura [Diseño y Pruebas 2](https://github.com/gii-is-DP2) con el cuál trabajaremos a lo largo de la asignatura. Para ello vamos a proveer:
- Una descripción a grandes rasgos de la aplicación web que planeamos implementar
- Entidades que se verán involucradas para el desarrollo de la aplicación web
- Historias de usuario para describir el funcionamiento de la aplicación web
- Escenarios positivos y negativos para cada historia de usuario que permitirán explicar las restricciones y aclarar el uso que se planea de la aplicación web

## Participantes

- [Juan Fernández Fernández](https://github.com/juaferfer11)
- [Estefanía Ganfornina Triguero](https://github.com/estefaniagan)
- [Javier García Cerrada](https://github.com/JavierGarCe)
- [Francisco Perejón Barrios](https://github.com/fraperbar)
- [Fernando Romero Rioja](https://github.com/ferromrio)


## Descripción 

BookLand es un portal web enfocado a la literatura. En él se podrá encontrar información sobre libros y noticias relacionadas con el mundo de la literatura.
  
  Buscamos la interacción con los usuarios y crear una comunidad lectora, por lo que le damos la opción de hacer reviews sobre libros y hacer publicaciones como *fanfics* y *fanarts*.
  
  Además llevaremos a cabo reuniones para discutir sobre libros a las que los usuarios podrán apuntarse a través de la página web.

## Entidades

Las entidades que se verán involucradas en la aplicación web son:
-  **Libro**: representará a una obra literaria. Tendrá los sigiuentes atributos: título, género, autor, fecha de publicación, número de páginas, editorial, ISBN, sinopsis y verificado (boolean).

-  **Review**: opiniones que escribirán los usuarios de la aplicación web sobre un libro. Sus atributos serán: libro y usuario asociado, nota (0, 1, 2, 3, 4 ó 5), título y opinión.

-  **Publicación de fan**: publicaciones que pueden hacer los usuarios sobre los libros. En ellas podrán publicar tanto texto como imágennes. Por tanto, podrán usarlas para *postear* teorías, *fanfics*, *fanarts*, etc. Los atributos serán: libro y usuario asociado, título, descripción e imágenes.

-  **Reunión**: representa a una reunión para discutir un libro concreto y sus atributos serán: lugar de celebración, fecha y hora de celebración, duración, aforo, lista de participantes y libro asociado.

-  **Noticia**: artículo relacionado con el mundo de la literatura y que constará de: titular, fecha, cuerpo, nombre del redactor, etiquetas, imágenes y el libro en cuestión.

## Historias de usuario

- **HU-01**: Añadir libro  
  
  **Como** usuario   
  **Quiero** poder añadir un libro a la aplicación   
  **Para** poder compartir un libro  
  
  Cuando el administrador publique un libro, éste estará marcado como verificado. Sin embargo, cuando lo haga un usario de la aplicación no estarán verificados, siendo el administrador quien disponga del privilegio para verificarlo (HU-04).

|Escenario positivo|Escenario negativo|
|-|-|
|Dado que el usuario estgrantri, cuando intenta añadir un libro con datos correctos: *El nombre del viento*, fantasía, por Patrick Rothfuss, publicado el 27/03/2007, con 613 páginas, editorial Plaza y Janés, ISBN 9780345805362 y sinopsis correcta, entonces se añade correctamente al sistema y no queda como verificado |Dado el usuario javgarcer, cuandoo intenta añadir un libro con datos incorrectos ya que pone como ISBN 1234, entonces el sistema le indica los fallos para que los corrija |
|Dado que el usuario administrador, cuando intenta añadir un libro con datos correctos: *El nombre del viento*, fantasía, por Patrick Rothfuss, publicado el 27/03/2007, con 613 páginas, editorial Plaza y Janés, ISBN 9780345805362 y sinopsis correcta, entonces se añade correctamente al sistema y queda como verificado|Dado el usuario ferromrio, cuando  añadir un libro con código ISBN 9780345805362, entonces el sistema le indica que dicho código ISBN ya está en uso porque el libro *El nombre del viento* lo tiene|


- **HU-02**: Editar libro  
  
  **Como** usuario  
  **Quiero** poder editar mis los libros que he añadido  
  **Para** poder corregir posibles errores o actualizar su información  


|Escenario positivo|Escenario negativo|
|-|-|
|Dado el usuario fraperbar que ha publicado el libro *El temor de un hombre sabio*, cuando intenta modificar sus datos, entonces éstos quedan editados en el sistema|Dado el usuario juaferfer y el libro *El temor de un hombre sabio* propiedad del usuario fraperbar, cuando juaferfer intenta editar *El temor de un hombre sabio*, entonces el sistema no se lo permite |
||Dado el usuario fraperbar que ha publicado el libro *El temor de un hombre sabio*, cuando intenta modificar sus datos con datos erróneos como ISBN 1234 y número de páginas -12, entonces el sistema le indica que no es un ISBN válido y que el número de páginas debe ser mayor de 0|
||Dado el usuario fraperbar que ha publicado el libro *El temor de un hombre sabio* y que ya se encuentra publicado *El nombre del viento* con ISBN 9780345805362, cuando intenta modificar *El temor de un hombre sabio* con ISBN 9780345805362, entonces el sistema le indica que no es un ISBN válido ya que se encuentra en uso|

- **HU-03**: Borrar libro  
  
  **Como** administrador  
  **Quiero** poder eliminar un libro que he añadido   
  **Para** solventar errores y eliminar libros que publiquen usuarios y no considere adecuados
  
  Nótese que si el administrador decide borrar un libro que tiene asociado reviews, reuniones o publicaciones, éstas se eliminarán también. Por dicho motivo se le pedirá verificar el borrado al administrador con un mensaje de confirmación.

|Escenario positivo|Escenario negativo|
|-|-|
|Dado el usuario administrador y el libro *Muerte en el Nilo* que tiene asociado 3 reviews y 2 publicaciones, cuando el administrador intenta borrar *Muerte en el Nilo* y lo confirma, entonces tanto las reviews, como las publicaciones como el libro quedan eliminados del sistema | Dado el usuario administrador y el libro *Muerte en el Nilo* que tiene asociado 3 reviews y 2 publicaciones, cuando el administrador intenta borrar *Muerte en el Nilo* y no lo confirma, entonces el libro no se borra|
|| Dado el usuario estgantri y el libro *Africanus, el hijo del consul*, cuando estgantri intenta borrar *Africanus, el hijo del consul*, entonces el sistema le indcica que no dispone de los permisos necesarios ya que sólo el administrador puede eliminar libros|


- **HU-04**: Verificar libro  
  
  **Como** administrador del sistema  
  **Quiero** poder verificar libros que hayan subido los usuarios  
  **Para** que los usuarios sepan que se trata de un libro real

|Escenario positivo|Escenario negativo|
|-|-|
|Dado el usuario administrador y el libro *Los pilares de la tierra* no verificado, cuando el administrador intenta verificarlo, entonces *Los pilares de la tierra* queda como verificado en el sistema|Dado el usuario administrador y el libro *Fortunata y Jacinta* verificado, cuando el administrador intenta verificarlo, entonces el sistema le indica que ya está verificado|

- **HU-05**: Gestionar noticias  
  
  **Como** administrador del sistema  
  **Quiero** poder gestionar las noticias que se visibles en la página  
  **Para** compartir información interesante con los usuarios, incentivarlos a la lectura y a seguir visitándonos

|Escenario positivo|Escenario negativo|
|-|-|
|Dado el usuario administrador, cuando intenta publicar una noticia con datos correcto sobre *Los detectives salvajes*, entonces la noticia queda publicada en el sistema|Dado el usuario administrador, cuando intenta publicar una noticia con datos erróneos (fecha en futuro por ejemplo) sobre *Los detectives salvajes*, entonces se indican los fallos en el formulario (en dicho caso, la fecha no puede ser futura)|
||Dado el usuario fraperbar, cuando intenta publicar una noticia con datos correcto sobre *Los detectives salvajes*, entonces el sistema le indica que no dispone de los permisos suficientes|
|Dado el usuario administrador y la noticia con título "Se anuncia la fecha de lanzamiento de *Las puertas de piedra*", cuando el administrador intenta actualizar dicha noticia con datos correctos, entonces la noticia queda actualizada en el sistema|Dado el usuario administrador y la noticia con título "Se anuncia la fecha de lanzamiento de *Las puertas de piedra*", cuando el administrador intenta actualizar dicha noticia con datos errónes (como no poner una URL válida para una imagen), entonces se le indican los fallos en el formulario (en este caso URL no válida)|
||Dado el usuario juaferfer y la noticia con título "Se anuncia la fecha de lanzamiento de *Las puertas de piedra*", cuando el juaferfer intenta actualizar dicha noticia, entonces el sisteme le indica que no disponie del permiso para editar una noticia|
|Dado el usuario administrador y la noticia con título "Se anuncia la fecha de lanzamiento de *Las puertas de piedra*", cuando el administrador intenta borrar dicha noticia, entonces la noticia queda eliminada del sistema|Dado el usuario ferromrio y la noticia con título "Se anuncia la fecha de lanzamiento de *Las puertas de piedra*", cuando ferromrio intenta borrar dicha noticia, entonces el sistema le indica que no dispone de permisos suficientes para borrar noticias|

- **HU-06**: Gestionar reviews
  
  **Como** usuario  
  **Quiero** poder gestionar reviews de libros de la aplicación  
  **Para** compartir mi opinión acerca del mismo con el resto de usuarios  

|Escenario positivo|Escenario negativo|
|-|-|
|Dado el usuario estgantri y el libro *La tesis de Nancy* que tiene incluido en sus libros leídos, cuando estgantri intenta publicar una review sobre *La tesis de Nancy* con datos correctos, entonces dicha review queda publicada en el sistema| Dado el usuario estgantri y el libro *La tesis de Nancy* que tiene incluido en sus libros leídos, cuando estgantri intenta publicar una review sobre *La tesis de Nancy* con datos incorrectos: nota diferente de 0, 1, 2, 3, 4 o 5, entonces el sistema le indica en el formulario que la nota debe ser uno de esos valores.|
||Dado el usuario javgarcer y el libro *La tesis de Nancy* que no tiene incluido en sus libros leídos, cuando javgarcer intenta publicar una review sobre *La tesis de Nancy* con datos correctos, entonces el sistema le indica que para opinar sobre un libro debe haberlo leído|
||Dado el usuario juaferfer y el libro *La tesis de Nancy* que tiene incluido en sus libros leídos y del que ya ha escrito una review, cuando juaferfer intenta publicar otra review sobre *La tesis de Nancy*, entonces el sistema le indica que sólo puede hacer una review por libro y que si desea puede editar dicha review|
|Dado el usuario juaferfer y el libro *La tesis de Nancy* que tiene incluido en sus libros leídos y del que ya ha escrito una review, cuando juaferfer intenta editar su review sobre *La tesis de Nancy*, entonces el sistema actualiza su review|Dado el usuario juaferfer y el libro *La tesis de Nancy* que tiene incluido en sus libros leídos y del que ya ha escrito una review, cuando juaferfer intenta editar su review sobre *La tesis de Nancy* pero introduce como nota 6 que es un dato erróneo, entonces el sistema le indica en el formulario que la nota debe tomar uno de los siguientes valores: 0, 1, 2, 3, 4 o 5|
||Dado el usuario fraperbar y el libro *La tesis de Nancy* que tiene incluido en sus libros leídos y del que ya ha escrito una review juaferfer, cuando fraperbar intenta editar la review sobre *La tesis de Nancy* de juaferfer, entonces el sistema le indica que no puede editar la review de otro usuario|
|Dado el usuario juaferfer y el libro *La tesis de Nancy* que tiene incluido en sus libros leídos y del que ya ha escrito una review, cuando juaferfer intenta eliminar su review sobre *La tesis de Nancy*, entonces el sistema la elimina|Dado el usuario fraperbar y el libro *La tesis de Nancy* que tiene incluido en sus libros leídos y del que ya ha escrito una review juaferfer, cuando fraperbar intenta eliminar la review sobre *La tesis de Nancy* de juaferfer, entonces el sistema le indica que no puede editar la review de otro usuario|
|Dado el usuario admnistrador y que el usuario juafefer ha hecho del libro *La tesis de Nancy*, que tiene incluido en sus libros leídos, una review, cuando el adminitrador intenta eliminar la review de otro usuario se lo permite para mantener el orden en la aplicación||

- **HU-07**: Gestionar publicaciones de fan  
  
 **Como** usuario  
  **Quiero** poder gestionar publicaciones  
  **Para** poder compartir con otros lectores teorías, imágenes, dibujos o *fanfics* de mis libros favoritos

|Escenario positivo|Escenario negativo|
|-|-|
|Dado el usuario javgarcer y el libro *El señor de los anillos* que tiene marcado como leído, cuando intenta escribir un *fanfic* como publicación de dicho libro, entonces dicha publicación queda insertada en el sistema|Dado el usuario estgantri y el libro *El señor de los anillos* que no tiene marcado como leído, cuando intenta escribir un *fanfic* como publicación de dicho libro, el sistema le indica que para hacer una publicación sobre un libro debe tenerlo como leído|
||Dado el usuario javgarcer y el libro *El señor de los anillos* que tiene marcado como leído, cuando intenta escribir un *fanfic* como publicación de dicho libro con datos erróneos (por ejemplo URL de imagen no válida), entonces el sistema le indica los fallos (en este caso que la URL no es válida)|
|Dado el usuario ferromrio y el libro *La verdad sobre el caso Harry Quebert* que tiene marcado como leído y del que ha hecho una publicación, cuando intenta editar la publicación de dicho libro, entonces dicha publicación queda actualizada en el sistema|Dado el usuario ferromrio y el libro *La verdad sobre el caso Harry Quebert* que tiene marcado como leído y del que ha hecho una publicación y el usuario fraperbar que también tiene el libro marcado como leído, cuando fraperbar intenta editar la publicación de ferromrio, entonces el sistema le indica que no dispone de los permisos necesarios|
||Dado el usuario juafefer y el libro *El árbol de la ciencia* que tiene marcado como leído y del que ha hecho una publicación, cuando intenta editar la publicación de dicho libro con datos erróneos (como URL de imagen errónea), entonces el sistema le indica los fallos (URL no válida en este caso)|
|Dado el usuario juaferfer y el libro *El árbol de la ciencia* que tiene marcado como leído y del que ha hecho una publicación, cuando intenta eliminar la publicación de dicho libro, entonces dicha publicación queda eliminada del sistema|Dado el usuario juaferfer y el libro *El árbol de la ciencia* que tiene marcado como leído y del que ha hecho una publicación y el usuario javgarcer, cuando javgarcer intenta eliminar la publicación de dicho libro de juafefer, entonces el sistema le indica que no dispone de los permisos necesarios|
|Dado el usuario administrador y por otro lado a  juaferfer que tiene el libro *El árbol de la ciencia* marcado como leído y del que ha hecho una publicación, cuando el administrador intenta eliminar la publicación de juaferfer, el sistema se lo permite para que pueda controlar las publicaciones que se suben a la página||

- **HU-08**: Crear reunión  
  
  **Como** administrador  
  **Quiero** poder crear reuniones sobre un libro determinado  
  **Para** que los lectores puedan hablar sobre dicho libro

|Escenario positivo|Escenario negativo|
|-|-|
|Dado el usuario administrador, cuando crea una reunión con datos correctos (reunión del libro *El nombre del viento* que está verificado, en la casa de la cultura de Los Palacios y Villafranca, el 01/05/2020 a las 19:00, duración de 2 horas, aforo 40 personas), entonces se crea la reunión para la fecha especificada sobre *El nombre del viento*|Dado el usuario administrador, cuando crea una reunión con datos correctos (reunión del libro *Luces de Bohemia* que no está verificado, en la casa de la cultura de Los Palacios y Villafranca, el 01/05/2020 a las 19:00, duración de 2 horas, aforo 40 personas), entonces el sistema indica que para hacer una reunión sobre un libro debe estar verificado|
||Dado el usuario administrador, cuando crea una reunión con datos erróneos (reunión del libro *El nombre del viento* que está verificado, en la casa de la cultura de Los Palacios y Villafranca, el 30/02/2020 a las 19:00, duración de 2 horas, aforo 40 personas), entonces el sistema indica que no es una fecha válida|
||Dado el usuario administrador, cuando crea una reunión con datos erróneos (reunión del libro *El nombre del viento* que está verificado, en la casa de la cultura de Los Palacios y Villafranca, el 25/02/2020 a las 19:00, duración de 2 horas, aforo 40 personas), entonces el sistema indica que no es una fecha válida ya que debe haber como mínimo un plazo de 3 días para que se apunten usuarios|
||Dado el usuario administrador, cuando crea una reunión con datos erróneos (reunión del libro *El nombre del viento* que está verificado, en la casa de la cultura de Los Palacios y Villafranca, el 25/02/2020 a las 19:00, duración de media horas, aforo 40 personas), entonces el sistema indica que no es una fecha válida ya que debe durar como mínimo una hora|

- **HU-09**: Inscribirse a reunión  
  
  **Como** usuario  
  **Quiero** poder apuntarme a reuniones  
  **Para** hablar asistir a ellos y discutir sobre un libro concreto

|Escenario positivo|Escenario negativo|
|-|-|
|Dado el usuario estgantri que se ha leido el libro *La piedra filosofal*, cuando estgantri a fecha 04/03/2020 hace click en inscribirse en una reunión de dicho libro el día 06/03/2020 a las 19:30 cuyo aforo es 50 personas y van apuntadas 23, entonces queda inscrita a la reunión|Dado el usuario estgantri que se ha leido el libro *Los girasoles ciegos* y además está inscrita a una reunión de *La piedra filosofal* el día 06/03/2020 a las 18:00 con duración de 3 horas, cuando estgantri a fecha 04/03/2020 hace click en inscribirse en una reunión de *Los girasoles ciegos* el día 06/03/2020 a las 19:30 cuyo aforo es 20 personas y van apuntadas 4, entonces el sistema le indica que le coincide con otra reunión a la que está inscrita|
||Dado el usuario javgarcer que no se ha leido el libro *La piedra filosofal*, cuando javgarcer a fecha 04/03/2020 hace click en inscribirse en una reunión de dicho libro el día 06/03/2020 cuyo aforo es 50 personas y van apuntadas 23, entonces el sistema le indica que para asistir a una reunión de un libro debe tenerlo marcado como leído|
||Dado el usuario estgantri que se ha leido el libro *La piedra filosofal*, cuando estgantri a fecha 04/03/2020 hace click en inscribirse en una reunión de dicho libro el día 06/03/2020 cuyo aforo es 50 personas y van apuntadas 50, entonces el sistema le indica que ya se ha superado el aforo|
||Dado el usuario estgantri que se ha leido el libro *La piedra filosofal*, cuando estgantri a fecha 07/03/2020 hace click en inscribirse en una reunión de dicho libro el día 06/03/2020 cuyo aforo es 50 personas y van apuntadas 50, entonces el sistema le indica que ya se ha celebrado la reunión|

- **HU-10**: Desapuntarse a una reunión  
  
  **Como** usuario  
  **Quiero** poder desapuntarme de reuniones  
  **Para** que otra persona pueda apuntarse en caso de que no pueda ir o haya otra reunión a la misma hora que me interese más

|Escenario positivo|Escenario negativo|
|-|-|
|Dado el usuario fraperbar que se ha leido el libro *Las memorias políticas del emperador Augusto* y está inscrito a una reunión del mismo con fecha 08/05/2020, cuando fraperbarr a fecha 04/03/2020 hace click en desinscribirse de la reunión, entonces queda desinscrito de dicha reunión|Dado el usuario juafefer que se ha leido el libro *Las memorias políticas del emperador Augusto* y no está inscrito a la reunión del mismo con fecha 08/05/2020, cuando juafefer intenta desapuntarse a través de URL, entonces el sistema no se lo permite y le indica que no es una acción legal|
||Dado el usuario fraperbar que se ha leido el libro *Las memorias políticas del emperador Augusto* y está inscrito a una reunión del mismo con fecha 08/05/2020, cuando fraperbarr a fecha 09/03/2020 hace click en desinscribirse de la reunión, entonces el sistema le indica que la reunión ya se ha celebrado|

- **HU-11**: Filtrar reuniones por atributos  
  
  **Como** usuario  
  **Quiero** poder filtrar reuniones por un atributo concreto  
  **Para** poder encontrar fácilmente una que me interese

|Escenario positivo|Escenario negativo|
|-|-|
|Dado el usuario ferromrio y que hay una reunión en el sistema del libro *El nombre del viento*, cuando ferromrio selecciona buscar por título de libro e introduce *El nombre del viento*, entonces el sistema le mostrará los detalles de esa reunión|Dado el usuario ferromrio y que hay una reunión en el sistema del libro *El temor de un hombre sabio*, cuando ferromrio selecciona buscar por título de libro e introduce *El temor de un hombre sabio*, entonces el sistema le indicará que no dispone de ninguna reunión que se adecúe a la búsqueda|
|Dado el usuario ferromrio y que hay varias reuniones en el sistema del libro *El temor de un hombre sabio*, cuando ferromrio selecciona buscar por título de libro e introduce *El temor de un hombre sabio*, entonces el sistema le mostrará un listado con esaas reunines||

- **HU-12**: Filtrar libros por atributos  
  
  **Como** usuario  
  **Quiero** poder filtrar los libros por un atributo concreto  
  **Para** encontrar con mayor facilidad los libros que quiero

|Escenario positivo|Escenario negativo|
|-|-|
|Dado el usuario ferromrio y que hay 12 libros en el sistema del autor Brandon Sanderson, cuando ferromrio selecciona buscar por autor e introduce *Brandon Sanderson*, entonces el sistema le mostrará un listado con los 12 libros de dicho autor|Dado el usuario ferromrio y que no hay ningún libro con ISBN 33, cuando ferromrio selecciona buscar por ISBN e introduce 33, entonces el sistema le indicará que no dispone de ningún libro que se adecúe a la búsqueda|
|Dado el usuario ferromrio y que hay solo un libro en el sistema del autor Pío Baroja, cuando ferromrio selecciona buscar por autor e introduce *Pío Baroja*, entonces el sistema le mostrará directamente los detalles de dicho libro||

- **HU-13**: Añadir libro a leídos  
  
  **Como** usuario  
  **Quiero** poder añadir un libro a mi colección de libros leídos  
  **Para** tener constancia de qué libros he leído y poder acceder a reuniones, así como escribir reviews y publicaciones de ellos.

|Escenario positivo|Escenario negativo|
|-|-|
|Dado el usuario estgantri y el libro *Yo, Julia* que no tiene marcado como leído, cuando le da a marcar como leído, entonces el libro *Yo, Julia* se añade a su colección de libros leídos|Dado el usuario estgantri y el libro *La piedra filosofal* que ya tiene marcado como leído, cuando le da a marcar como leído de nuevo, entonces el sistema le indica que ya lo tiene en su colección de libros leídos|
|Dado el usuario estgantri y el libro *El príncipe mestizo* que tiene marcado como libro a leer, cuando le da a marcar como leído, entonces el libro *El príncipe mestizo* se añade a su colección de libros leídos y se retira de la colección de libros a leer||

- **HU-14**: Colección de libros a leer en el perfil  
  
  **Como** usuario  
  **Quiero** poder añadir un libro a mi colección de libros a leer  
  **Para** tener una lista de futuras lecturas y libros que me interesan

|Escenario positivo|Escenario negativo|
|-|-|
Dado el usuario estgantri y el libro *Las reliquias de la muerte* que no tiene marcado como leído ni como a leer, cuando le da a marcar como a leer, entonces el libro *Las reliquias de la muerte* se añade a su colección de libros a leer|Dado el usuario estgantri y el libro *La piedra filosofal* que ya tiene marcado como leído, cuando le da a marcar como a leer, entonces el sistema le indica que ya lo ha leído|

- **HU-15**: Noticias de libros que he criticado 
  
  **Como** usuario  
  **Quiero** poder ver las noticias de los libros a los que he escrito una review  
  **Para** tener información de ellos, ya que posiblemente la noticia me resulte de interés

|Escenario positivo|Escenario negativo|
|-|-|
|Dado el usuario fraperbar que ha escrito una review de *El nombre del viento* y que hay una noticia de dicha obra, cuando pide ver noticias relacionadas con libros que ha criticado, entonces el sistema le muestra dicha noticia|Dado el usuario javgarcer que no ha escrito ninguna review, cuando solicita ver noticias relacionadas con los libros que ha criticado, entonces el sistema le indica que no ha hecho ninguna review|

- **HU-16**: Recomendaciones 
  
  **Como** usuario  
  **Quiero** que se me recomienden libros de acuerdo a los géneros de los libros que leo  
  **Para** descubrir nuevas obras que me puedan resultar de interés

|Escenario positivo|Escenario negativo|
|-|-|
|Dado que el usuario javgarcer tiene en su colección de libros leídos como género más repetido fantasía, cuando pide que se le recomienden libros que no ha leído, entonces el sistema le recomienda libros de fantasía que no ha leído, como: *Elantris*, *El camino de los reyes*, etc. |Dado que el usuario juaferfer que no tiene ningún libro como leído en su perfil, cuando pide que se le recomienden libros, entonces el sistema le indica que para ello necesita tener algún libro como leído para ver sus gustos|
||Dado que el usuario fraperbar que tiene como género más leído fantasía pero no hay ñibros de fantasía que no haya leído en el sistema, cuando pide que se le recomienden libros, entonces el sistema le indicará que no hay más libros de su género favorito|

- **HU-17**: Top libros más leídos por los usuarios 
  
  **Como** usuario  
  **Quiero** saber cuáles son los libros más leídos por el resto de usuarios de la página  
  **Para** ver cuáles son los libros del momento

|Escenario positivo|Escenario negativo|
|-|-|
|Dado que hay usuarios en el sistema con libros en su colección de leídos, cuando el usuario estgantri solicita ver los libros más leídos, entonces el sistema le muesrta un top basado en los libros leídos por los usuarios|Dado que las colecciones de libros leídos de todos los usuarios están vacías, cuando el usuario fraperbar solicita ver los libros más leídos, entonces el sistema le indica que no es posible ya que no hay datos suficientes|

- **HU-18**: Top libros mejor valorados por los usuarios
  
  **Como** usuario  
  **Quiero** saber cuáles son los libros mejor valorados por el resto de usuarios de la página  
  **Para** ver posibles próximos títulos a abordad

|Escenario positivo|Escenario negativo|
|-|-|
|Dado que hay usuarios en el sistema que han realizado alguna review, cuando el usuario ferrommio solicita ver los libros mejor valorados, entonces el sistema le muestra un top basado en los libros con media más alta de sus review|Dado que no se ha hecho ninguna review a ningún libro, cuando el usuario javgarcer solicita ver los libros mejor valorados, entonces el sistema le indica que no se puede mostrar porque aún no hay datos suficientes en la aplicación|

- **HU-19**: Información sobre las reuniones
  
  **Como** administrador  
  **Quiero** conocer información y estadisticas sobre las reuniones de forma mensual  
  **Para** analizar la información y mejorar la organización de las reuniones que se harán en adelante

|Escenario positivo|Escenario negativo|
|-|-|
|Dado el usuario administrador, cuando solicita ver las estadisticas de las reuniones del último mes, entonces el sistema le muestra información como: número de reuniones el último mes, número de asistentes total, número de asistentes por género de libro y número de asistentes por día de la semana|Dado el usuario administrador y que no se ha hecho ninguna reunión el último mes, cuando solicita ver las estadisticas de las reuniones del último mes, entonces el sistema le indica que no hay reuniones el último mes|
  
- **HU-20**: Verificar usuario
  
  **Como** usuario administrador  
  **Quiero** poder verificar usuarios que considere de confianza  
  **Para** que los libros que esos usuarios suban se encuentren automaticamente verificados  

|Escenario positivo|Escenario negativo|
|-|-|
|Dado el usuario administrador y el usuario fraperbar no verificado, cuando el administrador verifica a fraperbar, entonces todos sus libros que no estuvieran verificados se verifican y a partir de ese momento los libros que fraperbar añada se marcarán como verificados directamente|Dado el usuario administrador y el usuario ferromrio ya verificado, cuando el administrador intenta verificaa a ferromrio, el sistema le indica que ya está verificado|

## Planificación Sprint 2

En la asignatura se recomienda enfocar la implementación mediante la programación en pareja.  
  
  Si embargo en nuestro grupo somos impares y creemos que dicha metodología nos resultaría tediosa necesitaríamos emplear más tiempo y además necesitarían reunirse las parejas.
    
Por tanto, proponemos realizar un círculo en el que cada miembro del equipo se encargue de revisar el trabajo realizado por otros una vez que lo haya subido a la rama correspondiente del repositorio antes de relizar los *merges*.
  
Para el Sprint 2 debemos implementar el 66% de las historias de usuario (13), sus respectivas pruebas unitarias y las pruebas automatizadas usando Travis.
  
Provisionalmente, esperando el visto bueno por parte del profesorado de las historias de usuario, para este sprint planeamos implementar todas excepto las relacionadas con las reuniones y con las noticias.
  
Para consultar la asignación ver el Kanban en el repositorio.