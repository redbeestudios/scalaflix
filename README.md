# ScalaFlix

Pequeña herramienta de visualización de videos, que sirve como muestra de puesta en
prácticas todos aquellos conceptos que en el WorkShop de Scala se enseñan. Esta
herramienta está construida sobre Play Framework, Slick, Scala 2.12 y SBT 1.3.6.
Contiene dos servicios que soportan una interfaz web. Dichos servicios son llamados
Metrics y Streaming, usando para guardar las métricas de uso de la app y el otro
es empleado para el manejo de videos: dígase carga, reproducción o desestimación de
los mismos.

### Instalación

#### Windows

La herramienta, en etapa de desarrollo emplea Docker para correr todos sus 
servicios así como. No es necesario para poder ejecutarla instalar nada aparte de
eso. Por ende:

1. Instalar **IntelliJ Idea**.

2. Instalar **Java 8**.

3. Instalar **Scala 2.12.10**.

4. Instalar **SBT 1.3.6**.
 
5. Visite el [sitio oficial](https://docs.docker.com/docker-for-windows/install/)
   y continúe las instrucciones que en el sitio guían para realizar dicha
   instalación.

6. Una vez teniendo Docker instalado Docker, este viene con
   [Docker compose](https://docs.docker.com/compose/) que se necesita para levantar
   en conjunto todos los containers que serán los componentes de la herramienta.
  
7. Antes de ejecutar el Docker-compose, usando el archivo docker-compose-windows.yml,
   asegurarse de que el en este archivos los valores no completados en los mapeos
   de volúmenes están resueltos. En estos mapeos es necesario poner las rutas
   completas que faltan, como dice el nombre del mapeo.
  
   Ejemplo:
   `\<Ruta completa a una carpeta para los videos>:/data`, dicho mapeo
   se puede resolver transformándolo a: 
   `C:\Users\Juan\Videos:/data`, de esta forma el contenido del volúmen interno 
   del container en el path: `/data` será accessible desde afuera en la ruta
   especificada.

8. Luego de tener dichos mapeos hechos, correr la aplicación iendo desde un terminal
   y escribiendo el comando: `docker-compose -f docker-compose-windows.yml up`.
  
9. Para deternerlo basta hacer un `Ctrl+C` en el terminal donde se ejecutó.

#### Linux

1. Para instalar Scala, Java 8, y SBT se puede usar [sdkman](https://sdkman.io/).

2. Es prácticamente ídem a lo explicado arriba para **Windows**, la diferencia está en
que el archivo `.yml` para usar es el llamada `docker-compose.yml` a secas.

#### API

[Colección de Postman](https://www.getpostman.com/collections/8164c6f0cf07560a0a8f)

#### Tests

A medida que se implementan funcionalidades se necesita comprobar que las mismas
siguen los requerimientos. En este proyecto, existen dos tipos de tests: de unidad
y de integración. El propósito de ambos es (respectivamente): comprobar que las
unidades atómicas de código (funciones de clases) hacen lo que deberían, y el otro
comprueba que componentes del sistema interactúan correctamente entre sí (ejemplo:
el backend correctamente persiste cierto registro en base de datos) 

Los tests de unidad son lanzados desde un terminal haciendo: `sbt test`, mientras
los de integración: `sbt it:test`. SBT permite también correr tests de proyectos
específicos. Digamos que deseamos sólo correr los de unidad de metrics, para ello
es necesario lanzar en el terminal: `sbt metrics/test`. Con esta sintaxis de
anteponer el nombre de un proyecto seguido de `/` contextualizamos un comando a
ese proyecto solamente. Ejemplos:

* Limpiar todo el proyecto para compilar nuevamente: `sbt clean`.
* Correr los tests de integración del proyecto streaming: `sbt streaming/it:test`.
* Correr todos los tests juntos: `sbt ;test ;it:test`. El `;` sirve para lanzar
varias órdenes de sbt en una misma sentencia.

**Nota:** Para más información de lo que es posible con *SBT* en la documentación
adjunta al Workshop existe un capítulo dedicado al mismo.
