package model;


public class Action {
    private String description;
    private Long timestamp;

    /**
     * Constructor para una nueva acción.
     * Captura el momento exacto de la creación.
     * @param description La descripción de la acción realizada.
     */
    public Action(String description) {
        this.description = description;
        this.timestamp = System.currentTimeMillis(); // Captura el tiempo actual en milisegundos
    }

    public String getDescription() {
        return description;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}

