## **Aprendiendo Spring WebFlux**

Este es un proyecto de ejemplo para aprender a utilizar Spring WebFlux, un marco de trabajo reactiva para construir aplicaciones web escalables y de alta concurrencia en Java.

## **¿Qué es Spring WebFlux?**

Spring WebFlux es un marco de trabajo reactiva para construir aplicaciones web. Utiliza el modelo de programación reactiva para manejar las solicitudes web de forma eficiente y escalable. Spring WebFlux es compatible con dos tipos de programación reactiva: Reactor y RxJava.

## **Tecnologías utilizadas**

### **En este proyecto de ejemplo, utilizamos las siguientes tecnologías:**

Spring Boot: un marco de trabajo para construir aplicaciones Java que se ejecutan de forma independiente.
Spring WebFlux: marco de trabajo reactiva de Spring para construir aplicaciones web.
MongoDB: base de datos NoSQL que admite documentos JSON y proporciona escalabilidad y alta disponibilidad.
Project Reactor: librería de programación reactiva de Java.

### **El proyecto sigue la estructura de directorios estándar de Spring Boot:**

src/main/java/cl.sterbe.app: contiene los archivos fuente de la aplicación.
src/main/resources: contiene los archivos de recursos de la aplicación, como archivos de propiedades y archivos de configuración.
src/test: contiene los archivos de prueba de la aplicación.
Ejecución del proyecto
Para ejecutar el proyecto, se debe clonar el repositorio y navegar hasta el directorio del proyecto. Luego, se debe ejecutar la aplicación utilizando el comando ./mvnw spring-boot:run. La aplicación estará disponible en http://localhost:8080.

## **Configuración de la base de datos**

Este proyecto utiliza MongoDB como base de datos. Para configurar la conexión a la base de datos, se debe establecer la propiedad spring.data.mongodb.uri en el archivo application.properties.

## **Contribución**

Todos los comentarios y sugerencias son bienvenidos.

## **Recursos adicionales**

Documentación de Spring WebFlux
Documentación de MongoDB
Documentación de Project Reactor
Documentación de Spring Boot
