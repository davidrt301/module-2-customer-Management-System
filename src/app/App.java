package app;

import model.Role;
import model.User;
import services.UserManager;

public class App {
    public static void main(String[] args) throws Exception {
        UserManager userManager = new UserManager();
        System.out.println("--- INICIANDO PERMISOS POR ROL ---");

        User rootAdmin = new User("root-001", "root", "Root Admin", "rootpass", Role.ADMINISTRATOR);
        System.out.println("Usuario " + rootAdmin.getUserName() + " creado para iniciar el sistema.");

        // prubas de admin--------
        System.out.println("\n--- 1. Pruebas como Administrador (" + rootAdmin.getName() + ") ---");

        // Admin crea usuarios
        System.out.println("\n[Admin] Creando un usuario estándar y otro administrador...");
        User standardUser1 = userManager.createUser(rootAdmin, "Carlos Standard", "carlosst1234", "pass123",
                Role.STANDARD);
        User standardUser2 = userManager.createUser(rootAdmin, "Juan Standard", "juanst567", "pass456", Role.STANDARD);
        User standardUser3 = userManager.createUser(rootAdmin, "Sandra Standard", "sandra890", "pass789",
                Role.STANDARD);
        User anotherAdmin = userManager.createUser(rootAdmin, "Ana Admin", "anaadm", "pass098", Role.ADMINISTRATOR);
        System.out.println("Usuario '" + standardUser1.getName() + "' creado con éxito.");
        System.out.println("Usuario '" + standardUser2.getName() + "' creado con éxito.");
        System.out.println("Usuario '" + standardUser3.getName() + "' creado con éxito.");
        System.out.println("Usuario '" + anotherAdmin.getName() + "' creado con éxito.");
        // Para crear usuarios, necesitamos un administrador. Creamos el primero
        // "manualmente" para empezar.

        // Admin ve todos los usuarios
        System.out.println("\n[Admin] Viendo la lista de usuarios...");
        User[] allUsers = userManager.getUsers(rootAdmin);
        System.out.println("Total de usuarios en el sistema: " + allUsers.length);
        for (int i = 0; allUsers.length > i; i++) {
            System.out.println("- " + allUsers[i].getUserId() + " - " + allUsers[i].getName() + " ("
                    + allUsers[i].getRole().getName() + ")");
        }

        // Admin actualiza a otro usuario
        System.out.println("\n[Admin] Actualizando al usuario '" + standardUser2.getName() + "'...");
        boolean updatedByAdmin = userManager.updateUser(rootAdmin, standardUser2.getUserId(),
                "Juan Standard (Modificado)", "JuanNEW", "newpass", Role.ADMINISTRATOR);
        if (updatedByAdmin) {
            User updatedUser = userManager.findUserById(standardUser2.getUserId());
            System.out.println("Éxito. Nuevo nombre: " + updatedUser.getName());
        }

        // Admin elimina a otro usuario
        System.out.println("\n[Admin] Eliminando al usuario '" + anotherAdmin.getName() + "'...");
        boolean deletedByAdmin = userManager.deleteUser(rootAdmin, anotherAdmin.getUserId());
        System.out.println("Usuario eliminado: " + (deletedByAdmin ? "Sí" : "No"));

        // Pruebas estandar---------
        System.out.println("\n--- 2. Pruebas como Usuario Estándar (" + standardUser1.getName() + ") ---");

        // Estándar intenta crear un usuario (debe fallar)
        System.out.println("\n[Estándar] Intentando crear un nuevo usuario...");
        User failedUser = userManager.createUser(standardUser1, "Test User", "test", "testpass", Role.STANDARD);
        if (failedUser == null) {
            System.out.println("Intento fallido");
        }

        // Estándar intenta ver la lista de usuarios (debe recibir una lista vacía)
        System.out.println("\n[Estándar] Intentando ver la lista de usuarios...");
        User[] userListView = userManager.getUsers(standardUser1);
        System.out.println("Usuarios visibles para el rol estándar: " + userListView.length);

        // Estándar actualiza su propio perfil (debe funcionar)
        System.out.println("\n[Estándar] Actualizando su propio perfil...");
        boolean selfUpdated = userManager.updateUser(standardUser1, standardUser1.getUserId(), "Carlos Modificado",
                "carlos.standard", "mypassword", Role.ADMINISTRATOR);
        if (selfUpdated) {
            System.out.println("Éxito. Nuevo nombre: " + standardUser1.getName() + ". El rol no debió cambiar: "
                    + standardUser1.getRole().getName());
        }

        // Estándar intenta eliminar a otro usuario (debe fallar)
        System.out.println("\n[Estándar] Intentando eliminar al administrador '" + rootAdmin.getName() + "'...");
        boolean failedDelete = userManager.deleteUser(standardUser1, rootAdmin.getUserId());
        if (!failedDelete) {
            System.out.println("Intento fallido");
        }

        // Ver historial de acciones ----------
        System.out.println("\n--- 3. Pruebas de visualización de historial ---");

        // Admin ve el historial de un usuario estándar (debe funcionar)
        System.out.println("\n[Admin] Viendo el historial de '" + standardUser1.getName() + "'...");
        userManager.viewUserActions(rootAdmin, standardUser1.getUserId());

        // Usuario estándar intenta ver el historial de un admin (debe fallar)
        System.out.println("\n[Estándar] Intentando ver el historial de '" + rootAdmin.getName() + "'...");
        userManager.viewUserActions(standardUser1, rootAdmin.getUserId());

        System.out.println("\n--- FIN ---");

    }
}
