## Nivel de acabado

Nivel hasta 10 puntos.

## Realización de los requisitos

En cuanto a los requisitos para alcanzar el 10 en el entragables 1:

- Aplicación de nivel 2: la que definimos en el sprint 1 (consultar user-stories.md en la raíz del repositorio) 
  
- Hemos desarrollado pruebas unitarias usando Junit 5 y Mockito para testear servicios, controladores, formatters y validators.
  
- Hemos automatizado la ejecución de las pruebas siguiendo lo explicado sobre Travis CI.

- Hemos parametrizado la amplia mayoría de pruebas

- Hemos implementado 3 servicios externos a través de APIs REST: https://poemist.github.io/poemist-apidoc, https://api.quotable.io y https://api.itbook.store.

- Para el A+ del entregable 1 se nos propuso hacer pruebas que verificaran la clase SecurityConfig y se han abordado con el enfoque E2E y se encuentran en la clase SecurityConfigurationE2ETest.

Y para alcanzar el 10 en el entregable 2:

- Ya se habían definido en el sprint 1 al menos un escenario positivo y negativo por cada HU.

- Se han realizado pruebas de integración con la BD.

- Se han desarrollado pruebas de IU para 12 HU (necesario para alcanzar el 9) cubriendo todos sus escenarios definidos.

- Se han realizado pruebas E2E para todos los controladores de la aplicación.

- Se han realizado pruebas de integración para los tres servicios externos consumidos.

- Para el A+ hemos usado Cucumber a la hora de implementar las pruebas de IU.

Historias de usuario por parejas desarrolladas en el sprint 3:

-  Javier García (desarrollador) y Ferando Romero (revisor): 11  y 22

-  Fernando Romero (desarrollador) y Francisco Perejón (revisor): 9

-  Francisco Perejón (desarrollador) y Juan Fernández (revisor): 10

-  Juan Fernández (desarrollador) y Estefanía Ganfornina (revisor): 20

-  Estefanía Ganfornina (desarrollador) y Javier García (revisor): 18 y 19

También se puede consultar el Kanban en el proyecto del repositorio para más información.

## Análisis retrospectivo del Sprint

En general el grupo al completo ha trabajado forma organizada y ha obtenido resultados satisfactorios.  
  
Hemos acabado ya con todas las HU definidas y sus respectivas pruebas (las vistas hasta ahora), de forma que para el siguiente sprint nos podemos centrar en abordar las nuevas pruebas, corregir los fallos que se descubran y seguir optando al A+.

Hemos conseguido que las pruebas de IU se ejecuten en travis para que se comprueben en cada commit. Creemos que es muy útil ya que son pruebas frágiles que al menor cambio pueden dejar de funcionar y se benefician mucho de un sistema de integración continua como Travis CI.

Se adjunta en esta misma carpeta un informe con las horas dedicadas en el sprint.






