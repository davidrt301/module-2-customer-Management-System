package model;

/**
 * Define los roles que un usuario puede tener en el sistema.
 * Los roles determinan los permisos y el nivel de acceso.
 */
public enum Role {
    /**
     * Rol de Administrador, con permisos para realizar todas las operaciones.
     */
    ADMINISTRATOR("Administrador"),
    /**
     * Rol Estándar, con permisos limitados.
     */
    STANDARD("Estándar");

    private final String name;

    /**
     * Constructor para el enumerado de roles.
     * 
     * @param name El nombre legible del rol.
     */
    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
