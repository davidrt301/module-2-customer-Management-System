
package model;

import java.util.Arrays;

/**
 * Representa a un usuario dentro del sistema de gestión.
 * Esta clase encapsula toda la información relevante de un usuario, incluyendo
 * sus credenciales, rol y un historial de las acciones que ha realizado.
 * La relación con la clase `Action` es de composición, lo que significa que
 * cada `User` es propietario y gestiona su propio historial de acciones.
 */

public class User {
    private String userId;
    private String userName;
    private String name;
    private String password;
    private Role role;
    private Action[] actionHistory;


    public User(String userId, String userName, String name, String password, Role role) {
        this.userId = userId;
        this.userName = userName;
        this.name = name;
        this.password = password;
        this.role = role;
        this.actionHistory = new Action[0];
    }


    public String getUserId() {
        return userId;
    }


    public String getUserName() {
        return userName;
    }


    public String getName() {
        return name;
    }


    public String getPassword() {
        return password;
    }


    public Role getRole() {
        return role;
    }


    public Action[] getActionHistory() {
        return actionHistory;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }


    public void setName(String name) {
        this.name = name;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Registra una nueva acción en el historial del usuario usando un arreglo.
     * Esto demuestra la relación de composición: el User crea y posee sus Actions.
     * Cada vez que se llama, se crea un nuevo arreglo para almacenar la acción.
     * @param description La descripción de la acción a registrar.
     */
    public void addAction(String description) {
        // 1. Crear la nueva acción.
        Action newAction = new Action(description);

        // 2. Crear un nuevo arreglo con un espacio más.
        // Usamos Arrays.copyOf para crear una copia del arreglo original con un tamaño mayor.
        Action[] newHistory = Arrays.copyOf(this.actionHistory, this.actionHistory.length + 1);

        // 3. Añadir la nueva acción al final del nuevo arreglo.
        newHistory[newHistory.length - 1] = newAction;

        // 4. Reemplazar el historial antiguo con el nuevo.
        this.actionHistory = newHistory;
    }

    

    
    
    
}