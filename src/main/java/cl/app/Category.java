package cl.app;

/**
 * Created by Daniel on 29-02-2016.
 */
public enum Category {

    JAVA("Java"),
    NET(".NET"),
    JAVASCRIPT("Javascript"),
    OTHER("Others");

    private final String name;

    Category(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
