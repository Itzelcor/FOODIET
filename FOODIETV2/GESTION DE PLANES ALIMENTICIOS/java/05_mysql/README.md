# Conexion MySQL

1. Ejecuta primero `planes_alimentacion_mysql.sql` en MySQL.
2. Cambia usuario y clave en `MainBD.java` si hace falta.
3. Compila usando el conector de MySQL:

```bash
javac -cp mysql-connector-j.jar *.java
java -cp .;mysql-connector-j.jar MainBD
```

En Linux o Mac se usa `:` en lugar de `;` en el classpath.
