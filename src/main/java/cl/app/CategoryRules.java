package cl.app;

/**
 * Created by Daniel on 01-03-2016.
 */
public final class CategoryRules extends Rules<Category> {

    @Override
    protected void initRules() {
        rulesFor(Category.JAVA)
                .add(status -> status.getText().toLowerCase().contains("java"))
                .apply();
        rulesFor(Category.NET)
                .add(status -> status.getText().toLowerCase().contains(".net"))
                .apply();
        rulesFor(Category.JAVASCRIPT)
                .add(status -> status.getText().toLowerCase().contains("javascript"))
                .apply();
        rulesFor(Category.OTHER)
                .add(status -> {
                    String text = status.getText().toLowerCase();
                    return !(text.contains("java") || text.contains("javascript") || text.contains(".net"));
                })
                .apply();
    }

}
