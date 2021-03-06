package project.spring.sales.dao;

import java.util.List;

import project.spring.beans.CommonInterface;
import project.spring.product.vo.ProductVo;
import project.spring.sales.vo.ProductInfoDTO;

public interface SalesDAO extends CommonInterface{

	public int salesCount(String memId);
	public List productorlist(String memId, int start, int end);
	public String userIdch(String memId);
	public List getCategory();
	public String makeprCode(ProductVo productDTO);
	public int insertProduct(ProductVo productDTO);
	public ProductVo getDatail(String prCode);
	public int updateProduct(ProductVo dto);

}
