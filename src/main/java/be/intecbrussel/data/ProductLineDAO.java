package be.intecbrussel.data;

import be.intecbrussel.entities.ProductLine;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class ProductLineDAO {

    private final EntityManagerFactory emf;

    public ProductLineDAO() {
        emf = EMFactory.getEMF();
    }

    public List<ProductLine> getAllProductLines() {
        EntityManager em = emf.createEntityManager();
//        return em.createQuery("SELECT p FROM ProductLine p").getResultList();
        return em.createQuery("FROM ProductLine", ProductLine.class).getResultList();
    }

    public ProductLine getProductLineByName(String productLine) {
        EntityManager em = emf.createEntityManager();
        return em.find(ProductLine.class, productLine);
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
