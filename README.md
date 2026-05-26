# Miniproyecto 2: Sudoku NDV

## Descripción
Este proyecto es una implementación en Java de un juego de Sudoku sencillo con una arquitectura basada en el patrón modelo-vista-controlador (MVC). El código fuente se organiza en `src/main/java` y recursos relacionados en `src/main/resources`. El objetivo es proporcionar una solución educativa que demuestre conceptos de  estructuras de datos y una interfaz de usuario basada en JavaFX.

## Tecnologías y herramientas
- Java (con soporte de módulos, incluyendo `module-info.java`).
- Maven Wrapper (mvnw.cmd para Windows) para compilar y gestionar dependencias sin necesidad de instalar Maven globalmente.
- JavaFX para la interfaz de usuario (ficheros `ViewGame.fxml` y clases en `view/`).
- Arquitectura MVC: principalmente en `controller`, `model`, y `view`.
- Estructura de paquetes: `com.example.miniproyecto2.*`.

## Requisitos
- Java Development Kit (JDK) compatible con el proyecto (recomendado 11 o superior).
- Windows (este proyecto incluye el wrapper Maven `mvnw.cmd`).
- Acceso a la consola para ejecutar comandos de Maven.

## Cómo empezar (Windows)
1) Abrir una terminal en el directorio raíz del proyecto:

```
cd c:\Users\lojutus\IdeaProjects\miniproyecto-2-sudoku-nvd
```

2) Verificar la versión de Maven Wrapper (Windows):

```
mvnw.cmd -v
```

3) Construir el proyecto (sin pruebas) para ganar rapidez:

```
mvnw.cmd -DskipTests package
```

4) Ejecutar la aplicación (si el proyecto está configurado para usar `exec:java` o un proceso JavaFX desde Maven). Algunas configuraciones pueden requerir:

- Ejecutar la clase principal directamente si está empaquetada en `target/classes`:

```
java --module-path path\\to\\javafx-sdk-XX\\lib --add-modules javafx.controls,javafx.fxml -jar target/com.example.miniproyecto2-1.0-SNAPSHOT.jar
```

- O usar el plugin de ejecución de Maven si está configurado en `pom.xml`:

```
mvnw.cmd javafx:run
```

> Nota: La ejecución exacta puede depender de la configuración de tu `pom.xml` y del módulo `ViewGame.fxml` y la clase `GameStage` de la vista. Consulta la sección de Arquitectura para más detalles.

5) Ejecutar pruebas (si están definidas):

```
mvnw.cmd test
```

## Estructura del proyecto
- `src/main/java/` – código fuente Java
  - `module-info.java` – módulo del proyecto
  - `com/example/miniproyecto2/` – paquete base
  - `controller/` – controladores (logica de interacción entre vista y modelo)
  - `model/` – modelos de Sudoku y nodos/ matrices
  - `view/` – lógica de vistas y gráficos (p. ej. `GameStage.java`)
- `src/main/resources/` – recursos del proyecto
  - `com/example/miniproyecto2/ViewGame.fxml` – layout de la interfaz
  - `images/` – imágenes usadas por la UI
  - `game.css` – estilos CSS para JavaFX
- `target/` – artefactos generado por Maven tras la compilación

## Arquitectura y diseño
- Modelo: Clases en `model/` que representan la estructura de una cuadrícula de Sudoku, validaciones y lógica de juego (e.g. `Sudoku`, `NodeMatrix`, `AbstractSudoku`).
- Vista: Clases en `view/` y el archivo FXML `ViewGame.fxml` que definen la interfaz gráfica y la interacción del usuario.
- Controlador: Clases en `controller/` que coordinan la lógica entre la vista y el modelo, gestionando acciones del usuario y actualizaciones de la UI.


## Versiones y licencia
- Este proyecto está licenciado para uso educativo. 

## Autor(es)
- Jose David Hurtado Gomez
- Nerie Vazquez Diaz


## Notas finales
- Si encuentras problemas al ejecutar la UI en tu entorno, verifica las rutas de JavaFX y la configuración de módulos en tu `pom.xml`.
