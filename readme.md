# 📚 Sistema de Gestión Académica

> Proyecto desarrollado en **Java** para la materia **Programación 2** (Tecnicatura Superior en Informática Aplicada - UTN INSPT). Permite la administración de una institución educativa mediante arquitectura en capas y persistencia en archivos planos.

---

## 📋 Consigna del Trabajo Práctico 

Desarrollo de una aplicación para gestionar la información de una institución educativa, cumpliendo con los siguientes requerimientos:

### 🧩 Objetos y Entidades
* **Estudiante:** ID, DNI, nombre, apellido.
* **Profesor:** ID, DNI, nombre, apellido.
* **Aula:** Código, capacidad.
* **Inscripción:** ID estudiante, Código aula.
* **Asignación:** ID profesor, Código aula.

### 🔗 Relaciones
* **Aulas ↔ Estudiantes:** Uno a muchos.
* **Profesores → Aulas:** Uno a uno.

### ⚙️ Funcionalidades del Sistema
Menú principal con control absoluto para un administrador, incluyendo:
* Operaciones **CRUD** (Ingresar, modificar, consultar y eliminar) sobre **estudiantes, profesores y aulas**.
* Asignación de **profesores a aulas**.
* Asignación de **estudiantes a aulas**.
* **Consultas específicas:**
  * Nombre del estudiante con su aula asignada.
  * Los estudiantes a cargo de un profesor.
  * El profesor asignado a un estudiante.
* Menúes específicos por cada entidad con opciones CRUD y funcionalidades relacionadas.

---

## 🛠️ Tecnologías Utilizadas
* **Java** (Programación Orientada a Objetos)
* **Arquitectura en Capas:** Vistas, Controladores, DAOs y DTOs.
* **Persistencia:** Archivos de texto plano (`.txt`).
* **Utilidades de validación.**

---

## 🚀 Cómo ejecutar el proyecto

1. Clonar el repositorio:
   ```bash
   git clone [https://github.com/mwojtasikINSPT/gestionAcademica.git]
2. Abrir el proyecto en tu entorno de desarrollo o IDE favorito (NetBeans, IntelliJ IDEA, Eclipse, etc.).
3. Asegurarse de configurar el JDK de Java correspondiente.
4. Ejecutar la clase principal (Main) del sistema para operar mediante la interfaz de consola.