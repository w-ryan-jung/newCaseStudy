package matchit.base.server.product;

import matchit.base.server.database.DataAccess;
import matchit.base.server.database.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProductDataAccess extends DataAccess<Product> {

    private static final class ProductMapper implements Mapper<Product> {

        @Override
        public Product map(ResultSet resultSet) throws SQLException {
            return new Product(resultSet.getString("product_name"),
                    resultSet.getInt("price"));
        }
    }

    public ProductDataAccess(String driverUrl){
        super(driverUrl, new ProductMapper());
    }

    public Product addProduct(String productName, int price){
        insert("INSERT INTO product(product_name, price) VALUES (?,?)",
                productName, price);
        return new Product(productName, price);
    }

    public List<Product> getProducts() {
        return query("SELECT * FROM product");
    }

    public boolean updateProduct(String product){
        return execute("UPDATE product SET product_name = ?",product) > 0;
    }

    public boolean deleteProduct(String product){
        return execute("DELETE FROM product WHERE product_name = ?",product) > 0;
    }

}
