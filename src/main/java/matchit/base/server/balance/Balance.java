package matchit.base.server.balance;

public class Balance {
    private final String locationName;
    private final String productName;
    private final int total;

    public Balance(String locationName, String productName, int total) {
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
