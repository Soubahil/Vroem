package data;

import model.Product;
import model.ProductLine;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class ProductDAO {

    private final EntityManagerFactory emf;

    public ProductDAO() {
        emf = EMFactory.getEMF();
    }

    public List<Product> getAllProducts() {
        EntityManager em = emf.createEntityManager();
        return em.createQuery("FROM Product", Product.class).getResultList();
    }

    public Product getProductByCode(String productCode) {
        EntityManager em = emf.createEntityManager();
        return em.find(Product.class, productCode);
    }

    public Product getProductByName(String productName) {
        EntityManager em = emf.createEntityManager();
        return em.find(Product.class, productName);
    }

    public Product getProductByProductLine(ProductLine productLine) {
        EntityManager em = emf.createEntityManager();
        return em.find(Product.class, productLine);
    }

    public void addProductLine(ProductLine productLine) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(productLine);
        em.getTransaction().commit();
    }

    public void updateProductLine(ProductLine productLine)  {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(productLine);
        em.getTransaction().commit();
    }

    public void deleteProductLine(ProductLine productLine) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.remove(em.contains(productLine) ? productLine : em.merge(productLine));
        em.getTransaction().commit();
    }
}
