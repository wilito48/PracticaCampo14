# Sistema de Votación Electrónica

**Versión 2.0**

Sistema de votación electrónica desarrollado en Java Swing, siguiendo el patrón de arquitectura MVC y con integración a base de datos MySQL. Permite la gestión de usuarios, candidatos y la emisión de votos de manera segura y profesional.

---

## 📋 Descripción
Este sistema permite a los usuarios autenticarse, votar por candidatos y a los administradores gestionar el proceso electoral, candidatos y usuarios. Incluye una interfaz moderna, validaciones y mensajes claros, y está diseñado para ser fácilmente mantenible y extensible.

---

## ✨ Características principales
- Interfaz gráfica moderna y responsiva (Java Swing)
- Separación estricta de lógica (MVC)
- Gestión de usuarios y autenticación (usuario y administrador)
- Registro y gestión de candidatos
- Emisión de votos única por usuario
- Panel de administración con control total del proceso
- Persistencia de datos en MySQL
- Documentación profesional y código limpio

---

## 🛠️ Requisitos
- **Java 17** o superior
- **MySQL** (recomendado XAMPP o similar)
- IDE recomendado: Eclipse, IntelliJ IDEA o NetBeans

---

## ⚡ Instalación y configuración
1. **Clona o descarga este repositorio**
2. **Configura la base de datos:**
   - Importa el archivo `sistema_votacion.sql` en tu servidor MySQL (puedes usar phpMyAdmin o línea de comandos)
   - Asegúrate de que el usuario, contraseña y nombre de la base de datos en `ConexionDB.java` coincidan con tu entorno
3. **Abre el proyecto en tu IDE favorito**
4. **Agrega el conector JDBC de MySQL** a tu proyecto (archivo `mysql-connector-java-x.x.x.jar`)
5. **Compila y ejecuta** la clase `Main.java`

---

## 🗄️ Estructura del proyecto
```
votacion_sistema/
  ├── src/
  │   ├── votacion/                  # Main y arranque de la app
  │   ├── votacion_modelo/           # Clases de dominio: Usuario, Candidato, SistemaVotacion
  │   ├── votacion_controlador/      # Lógica de negocio y controladores MVC
  │   ├── votacion_vista/            # Vistas Swing (ventanas y paneles)
  │   ├── votacion_util/             # Utilidades y conexión a base de datos
  │   ├── votacion_excepciones/      # Excepciones personalizadas
  │   └── module-info.java           # Módulo Java
  ├── sistema_votacion.sql           # Script de base de datos
  └── README.md                      # Este archivo
```

---

## 🚀 Ejecución rápida
1. Inicia tu servidor MySQL (XAMPP, WAMP, etc.)
2. Importa `sistema_votacion.sql`
3. Ejecuta `Main.java` desde tu IDE
4. Selecciona el rol (usuario o administrador) y comienza a usar el sistema

---

## 👨‍💻 Créditos
- **Desarrollador:** Sistema de Votación (versión 2.0)
- **Colaboradores:** [Kervyn Willy, Percy y Jefferson]

---

## 📄 Licencia
Este proyecto es de uso académico y puede ser adaptado y mejorado libremente. Si lo usas o modificas, se agradece el reconocimiento al autor original. 