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
-  **Libro**: representará a una obra literaria. Tendrá los sigiuentes atributos: título, autor, fecha de publicación, número de páginas, editorial, ISBN, sinopsis y verificado (boolean).

-  **Review**: opiniones que escribirán los usuarios de la aplicación web sobre un libro. Sus atributos serán: libro y usuario asociado, nota (0, 1, 2, 3, 4 ó 5), título y opinión.

-  **Publicación de fan**: publicaciones que pueden hacer los usuarios sobre los libros. En ellas podrán publicar tanto texto como imágennes. Por tanto, podrán usarlas para *postear* teorías, *fanfics*, *fanarts*, etc. Los atributos serán: libro y usuario asociado, título, descripción e imágenes.

-  **Reunión**: representa a una reunión para discutir un libro concreto y sus atributos serán: lugar de celebración, fecha y hora de celebración, aforo, lista de participantes y libro asociado.

-  **Noticia**: artículo relacionado con el mundo de la literatura y que constará de: titular, cuerpo, nombre del redactor, etiquetas, imágenes y el libro en cuestión.

## Historias de usuario

- **HU-01**: Añadir libro  
  
  **Como** usuario   
  **Quiero** poder añadir un libro a la aplicación   
  **Para** poder compartir un libro  
  
  Cuando el administrador publique un libro, éste estará marcado como verificado. Sin embargo, cuando lo haga un usario de la aplicación no estarán verificados, siendo el administrador quien disponga del privilegio para verificarlo (HU-04).

|Escenario positivo|Escenario negativo|
|-|-|
|El usuario intenta añadir un libro y se añade correctamente al sistema |El usuario intenta añadir un libro con datos incorrectos en el formulario y el sistema le indica los fallos para que los corrija |
||El usuario intenta añadir un libro pero el código ISBN introducido ya se encuentra en uso, luego el sistema se lo indica permitiéndolo corregirlo|


- **HU-02**: Editar libro  
  
  **Como** usuario  
  **Quiero** poder editar mis los libros que he añadido  
  **Para** poder corregir posibles errores o actualizar su información  


|Escenario positivo|Escenario negativo|
|-|-|
|El usuario intenta modificar un libro y éste queda editado en el sistema|El usuario intenta editar un libro que no es de su propiedad y el sistema no se lo permite |
||El usuario intentar editar un libro pero introduce datos incorrectos y el sistema se lo indica dándole la opción a corregir los fallos|
||El usuario intenta modificar el código ISBN a uno que ya se encuentra en uso y el sistema se lo indica para que lo corrija|

- **HU-03**: Borrar libro  
  
  **Como** administrador  
  **Quiero** poder eliminar un libro que he añadido   
  **Para** solventar errores y eliminar libros que publiquen usuarios y no considere adecuados
  
  Nótese que si el administrador decide borrar un libro que tiene asociado reviews, reuniones o publicaciones, éstas se eliminarán también. Por dicho motivo se le pedirá verificar el borrado al administrador con un mensaje de confirmación.

|Escenario positivo|Escenario negativo|
|-|-|
|El administrador intenta borrar un libro y se borra correctamente | Un usuario intenta borrar un libro y el sistema indica que no tiene permiso |


- **HU-04**: Verificar libro  
  
  **Como** administrador del sistema  
  **Quiero** poder verificar libros que hayan subido los usuarios  
  **Para** que los usuarios sepan que se trata de un libro real

|Escenario positivo|Escenario negativo|
|-|-|
|Como administrador intento verificar un libro que ha subido un usuario y éste queda verificado en el sistema | Como usuario de la aplicación intento verificar un libro y el sistema no me lo permite ya que no dispongo de dichos privilegios |
||El administrador intenta verificar un libro que ya está verificado y le muestra un mensaje de error|

- **HU-05**: Publicar noticia  
  
  **Como** administrador del sistema  
  **Quiero** poder publicar noticias que sean visibles en la página  
  **Para** compartir información interesante con los usuarios e incentivarlos a la lectura y a seguir visitándonos

|Escenario positivo|Escenario negativo|
|-|-|
|El administrador intenta publicar una noticia con datos correctos y la noticia queda publicada en el sistema|El administrador intenta publicar una noticia con datos erróneos y el sistema le devuelve al formulario indicando los fallos encontrados|
||Un usuario intenta publicar una noticia y se le indica que no tiene permisos suficientes|

- **HU-06**: Editar noticia  
  
  **Como** administrador del sistema  
  **Quiero** poder editar noticias ya publicadas  
  **Para** solventar errores y para poder actualizarlas con información más reciente si fuera necesario

|Escenario positivo|Escenario negativo|
|-|-|
|El administrador intenta editar una noticia con datos correctos y la noticia queda publicada en el sistema|El administrador intenta editar una noticia con datos erróneos y el sistema le devuelve al formulario indicando los fallos encontrados|
||Un usuario intenta editar una noticia y se le indica que no tiene permisos suficientes|

- **HU-07**: Borrar noticia  
  
  **Como** administrador  
  **Quiero** poder borrar una noticia  
  **Para** quitarlas de la aplicación cuando considere oportuno

|Escenario positivo|Escenario negativo|
|-|-|
|El administrador intenta borrar una noticia y queda eliminada del sistema|Un usuario intenta borrar una noticia y se le indica que no dispone del permiso necesario|

- **HU-08**: Publicar review  
  
  **Como** usuario  
  **Quiero** poder escribir reviews de libros de la aplicación  
  **Para** compartir mi opinión acerca del mismo con el resto de usuarios  

|Escenario positivo|Escenario negativo|
|-|-|
|El usuario intenta publicar su review de un libro y queda publicada en el sistema| El usuario intenta publicar una review con datos incorrectos y el sistema se lo indica para que los corrija|
||El usuario intenta publicar una review de un libro que no tiene marcado como leído y el sistema le indica que debe haber leído el libro para dar su opinión sobre el mismo|
||El usuario intenta publicar una review de un libro del que ya ha escrito una review previamente y el sistema le indica que no puede escribir más de una review de un mismo libro|

- **HU-09**: Editar review  
  
  **Como** usuario  
  **Quiero** poder editar las reviews que haya redactado previamente  
  **Para** poder corregir errores o por si cambio de opinión respecto a la obra

|Escenario positivo|Escenario negativo|
|-|-|
|El usuario intenta editar una review que ha escrito previamente y ésta queda actualizada en el sistema|El usuario intenta editar una review que ha escrito previamente introduciendo datos erróneos y el sistema vuuelve al formulario indicando los fallos y dando opción a corregir los fallos encontrados|
||El usuario intenta editar una review que no es suya y el sistema no se lo permite|


- **HU-10**: Borrar review  
  
  **Como** usuario  
  **Quiero** poder borrar las reviews que haya redactado previamente  
  **Para** solventar reviews erróneas o por si el usuario no quiere que permanezcan en la página

|Escenario positivo|Escenario negativo|
|-|-|
|El usuario intenta borrar una review que ha escrito y queda eliminada del sistema|El usuario intenta borrar una review que no le pertenece y el sistema le indica que no tiene permiso para ello|

- **HU-11**: Publicar publicaciones de fan  
  
 **Como** usuario  
  **Quiero** poder crear publicaciones  
  **Para** poder compartir con otros lectores teorías, imágenes o historias de mis libros favoritos

|Escenario positivo|Escenario negativo|
|-|-|
|El usuario intenta crear una publicación de un libro y se crea correctamente|El usuario intenta crear una publicación de un libro que no tiene marcado como leído y el sistema le indica este hecho|
||El usuario intenta crear una publicación de un libro con datos erróneos y el sistema le indica los fallos permitiendo que los solvente|

- **HU-12**: Editar publicaciones de fan  
  
  **Como** usuario  
  **Quiero** poder editar mis publicaciones  
  **Para** que pueda corregir posibles errores o por si necesita añadir nueva información 

|Escenario positivo|Escenario negativo|
|-|-|
|El usuario intenta editar una publicación de un libro y se edita correctamente|El usuario intenta editar una publicación de un libro que no es suya y el sistema le indica que no tiene permisos|
||El usuario intenta editar una publicación de un libro con datos erróneos y el sistema le indica los fallos permitiendo que los solvente|

- **HU-13**: Borrar publicaciones de fan  
  
  **Como** usuario  
  **Quiero** poder borrar mis publicaciones  
  **Para** eliminar aquellas que ya no quiera compartir

|Escenario positivo|Escenario negativo|
|-|-|
|El usuario intenta borrar una publicación de un libro y se elimina correctamente|El usuario intenta borrar una publicación de un libro que no es suya y el sistema le indica que no tiene permisos|

- **HU-14**: Crear reunión  
  
  **Como** administrador  
  **Quiero** poder crear reuniones sobre un libro determinado  
  **Para** que los lectores puedan hablar sobre dicho libro

|Escenario positivo|Escenario negativo|
|-|-|
|El administrador introduce los datos correctamente y se crea la reunión para la fecha especificada sobre el libro acordado|El administrador introduce los datos correctamente pero el libro sobre el que va a tratar la reunión no existe en la página, se vuelve al formulario indicando este suceso|
||El administrador introduce datos erróneos y se vuelve al formulario indicándolo|

- **HU-15**: Inscribirse a reunión  
  
  **Como** usuario  
  **Quiero** poder apuntarme a reuniones  
  **Para** hablar sobre un libro concreto

|Escenario positivo|Escenario negativo|
|-|-|
|El usuario hace click en inscribirse y aparece como inscrito para dicha reunión|El usuario hace click en inscribirse pero ya tiene una reunión a esa hora y se le indica este hecho|
||El usuario hace click en inscribirse pero no tiene el libro marcado como leído y se le indica|
||El usuario hace click en inscribirse pero el aforo ya se ha superado y se le indica|

- **HU-16**: Desapuntarse a una reunión  
  
  **Como** usuario  
  **Quiero** poder desapuntarme de reuniones  
  **Para** que otra persona pueda apuntarse en caso de que no pueda ir o haya otra reunión a la misma hora que me interese más

|Escenario positivo|Escenario negativo|
|-|-|
|El usuario hace click en desapuntarse de la reunión y desaparece su nombre de la lista de participantes|El usuario intenta desapuntarse de una reunión a la que no está inscrito mediante URL|

- **HU-17**: Filtrar reuniones por atributos  
  
  **Como** usuario  
  **Quiero** poder filtrar reuniones por un atributo concreto  
  **Para** poder encontrar fácilmente una que me interese

|Escenario positivo|Escenario negativo|
|-|-|
|El usuario selecciona correctamente el atributo por el que quiero buscar las reuniones y aparece una lista con las reuniones que pasen el filtro|El usuario escribe una valor para el atributo para el que no existe ninguna reunión, se mostraría un mensaje indicando este hecho|

- **HU-18**: Filtrar libros por atributos  
  
  **Como** usuario  
  **Quiero** poder filtrar los libros por un atributo concreto  
  **Para** encontrar con mayor facilidad los libros que quiero

|Escenario positivo|Escenario negativo|
|-|-|
|El usuario selecciona correctamente el atributo por el que quiero buscar los libros y aparece una lista con los que pasen el filtro|El usuario escribe una valor para el atributo para el que no existe ningun libro y se mostraría un mensaje indicando este hecho|

- **HU-19**: Añadir libro a leídos  
  
  **Como** usuario  
  **Quiero** poder añadir un libro a mi colección de libros leídos  
  **Para** tener constancia de qué libros he leído y poder acceder a reuniones, así como escribir reviews y publicaciones de ellos.

|Escenario positivo|Escenario negativo|
|-|-|
|El usuario selecciona un libro y queda añadido a su lista de libros leídos|El usuario selecciona un libro que ya se encuentra en su lista de leídos y se le muestra un mensaje de error|
|El usuario selecciona un libro que estaba en la lista de libros a leer y queda añadido a su lista de libros leídos, además de eliminado de la lista de libros a leer||

- **HU-20**: Colección de libros a leer en el perfil  
  
  **Como** usuario  
  **Quiero** poder añadir un libro a mi colección de libros a leer  
  **Para** tener una lista de futuras lecturas y libros que me interesan

|Escenario positivo|Escenario negativo|
|-|-|
|El usuario selecciona un libro y queda añadido a su lista de libros a leer|El usuario intenta añadir un libro que ya se encuentra en la lista de libros leídos y el mensaje le muestra un mensaje de error|

- **HU-21**: Noticias de libros que he criticado 
  
  **Como** usuario  
  **Quiero** poder ver las noticias de los libros a los que he escrito una review  
  **Para** tener información de ellos, ya que posiblemente la noticia me resulte de interés

|Escenario positivo|Escenario negativo|
|-|-|
|El usuario selecciona ver las noticias de los libros que ha criticado y se le muestran|El usuario selecciona ver las noticias de los libros que ha criticado y como no ha hecho ninguna review, se le indica este hecho|

## Planificación Sprint 2

En la asignatura se recomienda enfocar la implementación mediante la programación en pareja.  
  
  Si embargo en nuestro grupo somos impares y creemos que dicha metodología nos resultaría tediosa necesitaríamos emplear más tiempo y además necesitarían reunirse las parejas.
    
Por tanto, proponemos realizar un círculo en el que cada miembro del equipo se encargue de revisar el trabajo realizado por otros una vez que lo haya subido a la rama correspondiente del repositorio antes de relizar los *merges*.
  
Para el Sprint 2 debemos implementar el 66% de las historias de usuario (14), sus respectivas pruebas unitarias y las pruebas automatizadas usando Travis.
  
Provisionalmente, esperando el visto bueno por parte del profesorado de las historias de usuario, para este sprint planeamos implementar todas excepto las relacionadas con las reuniones y con las noticias.
  
Para consultar la asignación ver el Kanban en el repositorio.