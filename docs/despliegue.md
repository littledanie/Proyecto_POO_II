# Despliegue de Gestiona Commerce en Render (Docker)

## Prerrequisitos
- Cuenta gratuita en [Render](https://render.com/)
- Repositorio GitHub: `littledanie/Proyecto_POO_II`
- Proyecto en Supabase con credenciales JDBC (Session Pooler)

## Arquitectura de despliegue

```
GitHub (push a main)
    ↓ auto-deploy
Render (Web Service · Docker)
    ↓ Dockerfile multi-stage
    ├── Etapa 1: maven:3.9-eclipse-temurin-17 → compila WAR
    └── Etapa 2: tomcat:10.1-jdk17 → sirve ROOT.war
```

## Configuración del servicio en Render

| Campo             | Valor                          |
|-------------------|--------------------------------|
| Name              | `gestiona-commerce`            |
| Region            | Oregon (us-west) o la más cercana |
| Runtime           | **Docker**                     |
| Instance Type     | **Free**                       |
| Branch            | `main`                         |
| Dockerfile Path   | `./Dockerfile`                 |
| Docker Build Context | `.` (raíz)                  |

## Variables de entorno (agregar a partir de la Etapa 2)

| Variable      | Descripción                                          | Ejemplo                                                    |
|---------------|------------------------------------------------------|------------------------------------------------------------|
| `DB_URL`      | JDBC URL del Session Pooler de Supabase              | `jdbc:postgresql://aws-0-us-east-1.pooler.supabase.com:5432/postgres?sslmode=require` |
| `DB_USER`     | Usuario del pooler                                   | `postgres.xxxxxxxxxxxx`                                    |
| `DB_PASSWORD` | Contraseña del proyecto Supabase                     | *(tu contraseña)*                                          |

> **Importante:** En Render, las variables de entorno se configuran en el panel del servicio: **Dashboard → servicio → Environment → Add Environment Variable**. Render las inyecta automáticamente al contenedor Docker.

## Flujo de despliegue

1. Hacer `git push origin main`.
2. Render detecta el push y ejecuta el build automáticamente.
3. El `Dockerfile` ejecuta `mvn clean package` y copia el WAR a Tomcat.
4. Render asigna una URL pública (ej. `https://gestiona-commerce.onrender.com`).
5. Verificar que la página carga correctamente.

## Notas sobre el plan gratuito

- Los servicios gratuitos en Render **se suspenden tras 15 minutos de inactividad** y tardan ~30 segundos en reactivarse.
- El primer build (descarga de imágenes Docker + dependencias Maven) tarda entre 3 y 8 minutos.
- Los builds posteriores (con caché) tardan entre 1 y 3 minutos.
