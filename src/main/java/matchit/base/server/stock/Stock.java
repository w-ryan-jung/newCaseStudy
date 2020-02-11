package matchit.base.server.stock;

public class Stock {
    private final int stockId;
    private final int userId;
    private final String locationName;
    private final String productName;
    private final int amount;
    private final long date;

    public Stock(int stockId, int userId, String locationName, String productName, int amount, long date) {
        this.stockId = stockId;
        this.userId = userId;
        this.locationName = locationName;
        this.productName = productName;
        this.amount = amount;
        this.date = date;
    }

    public int getStockId() {
        return stockId;
    }

    public int getUserId() {
        return userId;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getProductName() {
        return productName;
    }

    public int getAmount() {
        return amount;
    }

    public long getDate() {
        return date;
    }
}
