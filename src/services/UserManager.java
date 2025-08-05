package services;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import model.Action;
import model.Role;
import model.User;

/**
 * Gestiona todas las operaciones relacionadas con los usuarios en el sistema.
 * Esta clase actúa como un servicio que centraliza la lógica de negocio para
 * crear, leer, actualizar y eliminar (CRUD) usuarios.
 * Almacena los usuarios en memoria utilizando un arreglo y aplica un sistema de
 * permisos basado en roles para controlar quién puede realizar cada acción.
 */
public class UserManager {
    // Arreglo para almacenar todos los usuarios en memoria.
    private User[] users;

    public UserManager() {
        // Inicializamos el arreglo vacío.
        this.users = new User[0];
    }

    /**
     * Create: Crea un nuevo usuario. Solo permitido para Administradores.
     * 
     * @param actor El usuario que realiza la acción.
     * @return El usuario recién creado, o null si no tiene permisos.
     */
    public User createUser(User actor, String name, String userName, String password, Role role) {
        if (actor.getRole() != Role.ADMINISTRATOR) {
            System.out.println("Acción denegada: Solo los administradores pueden crear usuarios.");
            return null; // Acción no permitida
        }

        // Generar un ID de usuario único
        String userId = UUID.randomUUID().toString();
        User newUser = new User(userId, userName, name, password, Role.STANDARD);

        // Crear un nuevo arreglo con espacio para un usuario más
        User[] newUsers = Arrays.copyOf(this.users, this.users.length + 1);
        newUsers[newUsers.length - 1] = newUser;
        this.users = newUsers; // Reemplazar el arreglo antiguo

        actor.addAction("Creó el usuario: " + userName);
        return newUser;
    }

    /**
     * Read: Obtiene el arreglo completo de usuarios. Solo para Administradores.
     * 
     * @param actor El usuario que realiza la acción.
     * @return Un arreglo con todos los usuarios, o un arreglo vacío si no tiene
     *         permisos.
     */
    public User[] getUsers(User actor) {
        if (actor.getRole() == Role.ADMINISTRATOR) {

            return this.users;
        }
        return new User[0]; // Los usuarios estándar no pueden ver la lista completa.
    }

    /**
     * Read: Busca un usuario por su ID.
     * 
     * @param userId El ID del usuario a buscar.
     * @return El objeto User si se encuentra, o null si no.
     */
    public User findUserById(String userId) {
        for (User user : this.users) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }
        return null; // Retorna null si no se encuentra el usuario

    }

    /**
     * Update: Actualiza los datos de un usuario.
     * - Administradores pueden actualizar a cualquier usuario.
     * - Usuarios Estándar solo pueden actualizar su propio perfil (y no su rol).
     * 
     * @param actor          El usuario que realiza la acción.
     * @param userIdToUpdate El ID del usuario a modificar.
     * @return true si el usuario fue encontrado y actualizado, false en caso
     *         contrario.
     */
    public boolean updateUser(User actor, String userIdToUpdate, String newName, String newUserName, String newPassword,
            Role newRole) {
        User userToUpdate = findUserById(userIdToUpdate);
        if (userToUpdate == null) {
            return false; // Usuario a actualizar no encontrado
        }

        // Un administrador puede actualizar a cualquier usuario
        if (actor.getRole() == Role.ADMINISTRATOR) {
            userToUpdate.setName(newName);
            userToUpdate.setUserName(newUserName);
            userToUpdate.setPassword(newPassword);
            userToUpdate.setRole(newRole);
            actor.addAction("Actualizó el perfil de " + userToUpdate.getUserName());
            return true;
        }

        // Un usuario estándar solo puede actualizar su propio perfil
        if (actor.getRole() == Role.STANDARD && actor.getUserId().equals(userIdToUpdate)) {
            userToUpdate.setName(newName);
            userToUpdate.setUserName(newUserName);
            userToUpdate.setPassword(newPassword);
            // Un usuario estándar no puede cambiar su propio rol.
            actor.addAction("Actualizó información de perfil.");
            return true;
        }

        System.out.println("Acción denegada: No tiene permisos para actualizar este usuario.");
        return false; // Acción no permitida por falta de permisos
    }

    /**
     * Delete: Elimina un usuario. Solo permitido para Administradores.
     * 
     * @param actor          El usuario que realiza la acción.
     * @param userIdToDelete El ID del usuario a eliminar.
     * @return true si el usuario fue encontrado y eliminado, false en caso
     *         contrario.
     */
    public boolean deleteUser(User actor, String userIdToDelete) {
        if (actor.getRole() != Role.ADMINISTRATOR) {
            System.out.println("Acción denegada: Solo los administradores pueden eliminar usuarios.");
            return false; // Acción no permitida
        }

        int userIndex = -1;
        // 1. Encontrar el índice del usuario a eliminar
        for (int i = 0; i < this.users.length; i++) {
            if (this.users[i].getUserId().equals(userIdToDelete)) {
                userIndex = i;
                break;
            }
        }

        if (userIndex == -1) {
            return false; // Usuario a eliminar no encontrado
        }

        // 2. Crear un nuevo arreglo con un tamaño menor
        User[] newUsers = new User[this.users.length - 1];

        // 3. Copiar todos los elementos excepto el que se va a eliminar
        System.arraycopy(this.users, 0, newUsers, 0, userIndex);
        System.arraycopy(this.users, userIndex + 1, newUsers, userIndex, this.users.length - userIndex - 1);

        // 4. Reemplazar el arreglo antiguo
        this.users = newUsers;
        actor.addAction("Eliminó el usuario con ID: " + userIdToDelete);
        return true;
    }

    /**
     * Muestra el historial de acciones de un usuario específico.
     * Solo los administradores pueden ejecutar esta función.
     * 
     * @param actor        El administrador que realiza la solicitud.
     * @param userIdToView El ID del usuario cuyo historial se desea ver.
     */
    public void viewUserActions(User actor, String userIdToView) {
        if (actor.getRole() != Role.ADMINISTRATOR) {
            System.out.println("Acción denegada: Solo los administradores pueden ver el historial de acciones.");
            return;
        }

        User userToView = findUserById(userIdToView);
        if (userToView == null) {
            System.out.println("Error: No se encontró un usuario con el ID proporcionado.");
            return;
        }

        System.out.println("\n--- Historial de Acciones para el Usuario ---");
        System.out.println("ID de Usuario: " + userToView.getUserId());
        System.out.println("Nombre de Usuario: " + userToView.getUserName());
        System.out.println("-------------------------------------------");

        Action[] history = userToView.getActionHistory();
        if (history.length == 0) {
            System.out.println("Este usuario no tiene acciones registradas.");
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            for (Action action : history) {
                System.out.printf("Fecha: %s | Acción: %s\n", sdf.format(new Date(action.getTimestamp())),
                        action.getDescription());
            }
        }
        System.out.println("-------------------------------------------");
    }
}
