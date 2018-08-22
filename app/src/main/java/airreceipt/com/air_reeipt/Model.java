package airreceipt.com.air_reeipt;

public class Model {
    String store;
    String price;
    int icon;

    //constructor
    public Model(String store, String price, int icon) {
        this.store = store;
        this.price = price;
        this.icon = icon;
    }

    //getters

    public String getStore() {
        return this.store;
    }

    public String getPrice() {
        return this.price;
    }

    public int getIcon() {
        return this.icon;
    }

}
