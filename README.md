# ScalaFlix

Pequeña herramienta de visualización de videos, que sirve como muestra de puesta en
prácticas todos aquellos conceptos que en el WorkShop de Scala se enseñan. Esta
herramienta está construida sobre Play Framework, Slick, Scala 2.12 y SBT 1.3.8.
Contiene dos servicios que soportan una interfaz web. Dichos servicios son llamados
Metrics y Streaming, usando para guardar las métricas de uso de la app y el otro
es empleado para el manejo de videos: dígase carga, reproducción o desestimación de
los mismos.

### Instalación

##### Windows

La herramienta, en etapa de desarrollo emplea Docker para correr todos sus 
servicios así como. No es necesario para poder ejecutarla instalar nada aparte de
eso. Por ende:
 
* Visite el [sitio oficial](https://docs.docker.com/docker-for-windows/install/)
  y continúe las instrucciones que en el sitio guían para realizar dicha
  instalación.

* Una vez teniendo Docker instalado Docker, este viene con
  [Docker compose](https://docs.docker.com/compose/) que se necesita para levantar
  en conjunto todos los containers que serán los componentes de la herramienta.
  
* Antes de ejecutar el Docker-compose, usando el archivo docker-compose-windows.yml,
  asegurarse de que el en este archivos los valores no completados en los mapeos
  de volúmenes están resueltos. En estos mapeos es necesario poner las rutas
  completas que faltan, como dice el nombre del mapeo.
  
  Ejemplo:
  `\<Ruta completa a una carpeta para los videos>:/data`, dicho mapeo
  se puede resolver transformándolo a: 
  `C:\Users\Juan\Videos:/data`, de esta forma el contenido del volúmen interno 
  del container en el path: `/data` será accessible desde afuera en la ruta
  especificada.

* Luego de tener dichos mapeos hechos, correr la aplicación iendo desde un terminal
  y escribiendo el comando: `docker-compose -f docker-compose-windows.yml up`.
