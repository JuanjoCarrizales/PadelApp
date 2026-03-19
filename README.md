# **MatchPoint: Marcador de Pádel Inteligente**

MatchPoint es una aplicación de escritorio para el registro y seguimiento de partidos de pádel. En esta aplicación se sientan las bases para a posteriori, implementar estos conocimientos a Android Studio y hacer la app funcional. 

### 

## **Descripción:**

La función principal de MatchPoint es brindar al usuario un marcador intuitivo y cómodo con el que poder llevar el conteo de sus partidos sin tener que memorizar ni recordar cada punto. Está hecha basándose en las siguientes características:

* Sigue las reglas oficiales del pádel.
* Guarda automáticamente tus partidos en una base de datos.
* Muestra un resumen breve de tus estadísticas.
* Funciona sin conexión a internet.

Esta aplicación simula la interfaz de un *smartwatch*, permitiendo contar puntos, cronometrar partidos y guardar estadísticas personales de forma sencilla y rápida.

### 

## **Características:**

### **Marcador de Pádel Completo:**

En la primera pestaña de la aplicación, podemos encontrar las siguientes características:

* Puntuación oficial de pádel (0, 15, 30, 40, iguales, Ventaja).
* Sistema de Juegos y Sets.
* Tie-Break automático (si se llega al 6-6, se activa el tie, de manera que se gane con diferencia de 2 puntos).
* Detección automática de ganador.
* Reinicio automático al finalizar.

### **Cronómetro Inteligente:**

En la pestaña del cronómetro podemos encontrar las siguientes características:

* Inicio/pausa con un botón. Cuenta también con un botón de reinicio de partido.
* Cálculo automático de kcal (300 kcal/hora). Se hace un cálculo medio en base a las kcal gastadas de media en un ejercicio de 1h:30min.
* Control de acceso al marcador (solo puedes empezar a contar los puntos si activas el cronómetro).
* En el momento en el que le das al botón de "play" al cronómetro, empieza a guardar automáticamente en la base de datos.

### **Sistema de Deshacer:**

Se cuenta con un botón de deshacer, situado en la parte inferior del marcador de la primera página de la aplicación. Con él podemos hacer:

* Deshacer el último punto marcado (sea nuestro o del rival).
* Guardar el historial completo de acciones/puntos hechos.
* Este botón está bloqueado igual que los botones del marcador. Si no se activa o se para el cronómetro, éste no se puede pulsar.

### **Estadísticas:**

En la tercera pestaña que es de estadísticas, podemos encontrar los siguientes datos:

* Total de partidos jugados.
* Victorias y derrotas de la pareja principal.
* Duración media de los partidos jugados.

Cuenta con actualización automática. Tras finalizar cada partido, estos datos se actualizan.

### **Interfaz:**

Como se puede apreciar en la app, en diseño es oscuro, adecuado para no cansar la vista y sea más estético. En la interfaz podemos encontrar:

* 3 páginas navegables por gestos (deslizar).
* *Responsive* con *aspect ratio* fijo (4:5). 
* Escalado dinámico del contenido, adecuándose a estilos preestablecidos.
* El tamaño mínimo es de 280x350px.

### **Persistencia de Datos:**

MatchPoint cuenta con una base de datos SQLite local, en la que se realizan los guardados automáticos de cada partido registrado, tiene un historial completo de puntos jugados en cada partido y así, poder mostrar más adelante, más datos.

### 

## **Tecnologías usadas:**

|**Tecnología**|**Versión**|**Uso**|
|-|-|-|
|**Java**|17 (*compilación*) / 21 (*runtime*)|Lenguaje principal|
|**JavaFX**|21.0.1|Framework de interfaz gráfica|
|**Maven**|3.x|Gestión de dependencias y *build*|
|**SQLite**|3.45.1|Base de datos local|
|**NetBeans**|22+|IDE de desarrollo|

### 

## **Requisitos:**

### **Sistema Operativo y Software:**

**Es necesario contar con las siguientes características para poder obtener la experiencia completa de la app:**

* Windows 10/11.
* macOS 10.14+.
* Linux (Ubuntu 20.04+).
* JDK 21 (OpenJDK o Oracle JDK).
* Maven 3.6+.
* Git (para clonar el repositorio)

### 

## **Instalación de MatchPoint:**

**1. Clonar el repositorio:**
git clone https:*//github.com/JuanjoCarrizales/PadelApp.git
cd padelApp*


**2. Compilar el proyecto:**
*mvn clean install*


**3. Ejecutar la aplicación:**
*mvn javafx:run*


### **Alternativa: Ejecutar desde NetBeans**

1. Abre NetBeans
2. File → *Open Project*
3. Selecciona la carpeta "padelApp"
4. Click derecho en el proyecto → *Run*

### 

## **Uso de MatchPoint:**

### **Iniciar un Partido:**

1. **Página 1 (Partido)**: Verás el marcador inicial en '0 - 0' (No deja pulsar el marcador).
2. Desliza hacia la izquierda para ir a Página 2 (Cronómetro).
3. Pulsa el botón "**▶"** para iniciar el cronómetro.
4. Vuelve a Página 1 deslizando hacia la derecha (ahora dejará pulsar el marcador).
5. Marca puntos pulsando los botones del marcador de cada pareja (siendo tu pareja el del lado izquierdo de la pantalla).

### **Durante el Partido:**

* **Si se gana un punto**: Pulsa el botón de la pareja ganadora.
* **Si fué un error el punto anterior y se quiere volver hacia atras**: Pulsa el botón "**↶"** (solo funciona cronómetro activo).
* **Pausar/Reanudar**: Ir  a la página 2 y pulsa "**⏸"** / "**▶".**
* **Reiniciar partido**: Pulsa el botón "**↺"** y confirmar.

### **Ver Estadísticas:**

1. Desliza hacia la izquierda dos veces hasta Página 3 (Estadísticas).
2. Podrás encontrar:

   * Partidos jugados (de tu pareja/tuyos).
   * Tus victorias y derrotas.
   * Duración media de los partidos que has jugado.

### **Navegación por Gestos:**

* **Deslizar izquierda**: Página siguiente.
* **Deslizar derecha**: Página anterior.
* **Puntos de navegación**: Indican página en que página nos encontramos.

### 

**Flujo de Ejecución:**
1. Usuario abre aplicación de escritorio → Main.start()
2. Se conecta automáticamente a la BD → DatabaseManager.conectar()
3. Se crean las tablas → crearTablas()
5. Usuario/jugador inicia el cronómetro → partido.iniciarPartido()
6. Usuario/jugador marca puntos → partido.addPuntoPareja1/2()
7. Se guarda en la BD → guardarPunto()
8. Partido termina → finalizarPartido()
9. Reinicio automático → reiniciar()

### 

## **Base de Datos:**

### **Tablas:**

La app automáticamente crea una base de datos con las siguientes tablas:

#### ***1. 'jugadores':***

|**Campo**|**Tipo**|**Descripción**|
|-|-|-|
|id|INTEGER PK|Identificador único|
|nombre|TEXT|Nombre del jugador/usuario|
|email|TEXT UNIQUE|Email (se usará en la aplicación de escritorio)|
|password\_hash|TEXT|Contraseña encriptada|
|creado\_desde\_app|INTEGER|1 = Matchpoint versión Wear Os, 2 = MatchPoint versión móvil|
|fecha\_creacion|TEXT|Fecha de registro|

#### 

#### ***2. 'partidos':***

|**Campo**|**Tipo**|**Descripción**|
|-|-|-|
|id|INTEGER PK|Identificador único|
|fecha\_inicio|TEXT|Inicio del partido|
|fecha\_fin|TEXT|Final del partido|
|duracion\_total|INTEGER|Duración total del partido|
|nombre\_pareja1|TEXT DEFAULT|Nombre por defecto: "pareja 1"|
|nombre\_pareja2|TEXT DEFAULT|Nombre por defecto: "pareja 2"|
|sets\_pareja1|INTEGER|Sets ganados|
|sets\_pareja2|INTEGER|Sets ganados|
|ganador|INTEGER|pareja 1 o pareja 2|
|partido\_finalizado|INTEGER|0=no, 1=sí|

#### 

#### ***3. 'puntos':***

|**Campo**|**Tipo**|**Descripción**|
|-|-|-|
|id|INTEGER PK|Identificador único|
|id\_partido|INTEGER FK|Partido al que pertenece|
|timestamp|INTEGER|Segundo del partido|
|pareja\_ganadora|INTEGER|1 o 2|
|puntos\_pareja1|INTEGER|Puntos pareja 1|
|puntos\_pareja2|INTEGER|Puntos pareja 2|
|juegos\_pareja1|INTEGER|Juegos pareja 1|
|juegos\_pareja2|INTEGER|Juegos pareja 2|
|sets\_pareja1|INTEGER|Sets pareja 1|
|sets\_pareja2|INTEGER|Sets pareja 2|
|ganador|INTEGER|pareja 1 o pareja 2|
|tiebreak|INTEGER|0=no, 1=sí|

### 

## **Roadmap del proyecto:**

### **Completado: (MatchPoint versión escritorio)**

* \[✓] Marcador completo de pádel.
* \[✓] Cronómetro con kcal quemadas.
* \[✓] Sistema de deshacer los puntos.
* \[✓] Sistema de reinicio de partido completo.
* \[✓] Base de datos en SQLite.
* \[✓] Estadísticas básicas del partido.
* \[✓] Interfaz *responsive*.
* \[✓] Navegación entre pestañas por gestos.

### **En Desarrollo: (MatchPoint - *Dashboard* en Android Studio)**

* \[ ] Versión móvil completa.
* \[ ] Uso de Firebase para la sincronización de datos.
* \[ ] Sistema de login/registro de jugador/usuario.
* \[ ] Vinculación de partidos con jugador/usuario.
* \[ ] Visualización de estadísticas avanzadas.
* \[ ] Gráficos de rendimiento.
* \[ ] Gráfico de días que más juegas.
* \[ ] Historial de partidos.
* \[ ] Ubicaciones más concurrentes de juego.

### **Próximos pasos: Migración a Android:**

#### **MatchPoint para WearOS:**

* \[ ] Adaptación de la versión de escritorio a Wear OS.
* \[ ] Optimización para pantalla tipos de pantalla.
* \[ ] Sincronización con el teléfono móvil.

### 

## **Capturas de pantalla:**

### **Página 1: Marcador**

![Marcador](docs/Captura\_página1\_marcador.png)

*Interfaz principal con marcador de puntos, juegos, sets y botón de deshacer.*

### **Página 2: Cronómetro**

![Cronómetro](docs/Captura\_página2\_cronómetro.png)

*Cronómetro con contador de kcal.*

### **Página 3: Estadísticas**

![Estadísticas](docs/Captura\_página3\_estadísticas.png)

*Breve resumen de: total de partidos jugados, victorias, derrotas y duración media de los partidos.*

### 

## **Testing:**

**Estado del Testing:**

* \[✓] Lógica del partido (100%).
* \[✓] Cronómetro (100%).
* \[✓] Sistema de deshacer (100%).
* \[✓] Reinicio (100%).
* \[✓] Estadísticas (100%).
* \[✓] Navegación (100%).
* \[✓] *Responsive* (100%).
* \[✓] Base de datos (100%).
* \[✓] Casos extremos (100%).

### 

## **Autor:**

* **Juan José Carrizales Quiroga**.

### 

## **Contacto:**

* **Email**: Juanjo.carrizales98@gmail.com
* **GitHub**: [GitHub](https://github.com/JuanjoCarrizales)
* **LinkedIn**: [LinkedIn](https://www.linkedin.com/in/juan-jose-carrizales-quiroga/)

### 

<div align="center">
  <strong>Proyecto personal hecho para la comunidad de pádel</strong>
</div>

