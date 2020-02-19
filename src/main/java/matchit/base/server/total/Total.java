package matchit.base.server.total;

public class Total {
    private final String locationName;
    private final String productName;
    private final int total;

    public Total(String locationName, String productName, int total) {
        this.locationName = locationName;
        this.productName = productName;
        this.total = total;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getProductName() {
        return productName;
    }

    public int getTotal() {
        return total;
    }
}
