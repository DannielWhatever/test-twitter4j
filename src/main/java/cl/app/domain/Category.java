package cl.app.domain;

import java.io.Serializable;

/**
 * Created by Daniel on 29-02-2016.
 */
public class Category implements Serializable {

    private String name;

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
