package Model.Interface;

import java.sql.SQLException;
import java.util.Collection;

import Model.Bean.ProductBean;

public interface ProductModel_intf {
	
	public void doSave(ProductBean product) throws SQLException;

	public boolean doDelete(int code) throws SQLException;

	public ProductBean doRetrieveByKey(int code) throws SQLException;
	
	public Collection<ProductBean> doRetrieveAll(String order) throws SQLException;

	public StringBuilder doRetrieveByInput(String string) throws SQLException;
}
