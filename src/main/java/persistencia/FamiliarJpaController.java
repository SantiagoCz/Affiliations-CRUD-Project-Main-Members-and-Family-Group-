
package persistencia;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import logica.Afiliado;
import logica.Familiar;
import logica.Titular;
import persistencia.exceptions.NonexistentEntityException;


public class FamiliarJpaController implements Serializable {

    public FamiliarJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public FamiliarJpaController(){
        emf = Persistence.createEntityManagerFactory("AfiliacionesPU");
    }      
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Familiar familiar) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Afiliado afiliadoFamiliar = familiar.getAfiliadoFamiliar();
            if (afiliadoFamiliar != null) {
                afiliadoFamiliar = em.getReference(afiliadoFamiliar.getClass(), afiliadoFamiliar.getId());
                familiar.setAfiliadoFamiliar(afiliadoFamiliar);
            }
            Titular titular = familiar.getTitular();
            if (titular != null) {
                titular = em.getReference(titular.getClass(), titular.getId());
                familiar.setTitular(titular);
            }
            em.persist(familiar);
            if (afiliadoFamiliar != null) {
                afiliadoFamiliar.getFamiliares().add(familiar);
                afiliadoFamiliar = em.merge(afiliadoFamiliar);
            }
            if (titular != null) {
                titular.getFamiliarTitular().add(familiar);
                titular = em.merge(titular);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Familiar familiar) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Familiar persistentFamiliar = em.find(Familiar.class, familiar.getId());
            Afiliado afiliadoFamiliarOld = persistentFamiliar.getAfiliadoFamiliar();
            Afiliado afiliadoFamiliarNew = familiar.getAfiliadoFamiliar();
            Titular titularOld = persistentFamiliar.getTitular();
            Titular titularNew = familiar.getTitular();
            if (afiliadoFamiliarNew != null) {
                afiliadoFamiliarNew = em.getReference(afiliadoFamiliarNew.getClass(), afiliadoFamiliarNew.getId());
                familiar.setAfiliadoFamiliar(afiliadoFamiliarNew);
            }
            if (titularNew != null) {
                titularNew = em.getReference(titularNew.getClass(), titularNew.getId());
                familiar.setTitular(titularNew);
            }
            familiar = em.merge(familiar);
            if (afiliadoFamiliarOld != null && !afiliadoFamiliarOld.equals(afiliadoFamiliarNew)) {
                afiliadoFamiliarOld.getFamiliares().remove(familiar);
                afiliadoFamiliarOld = em.merge(afiliadoFamiliarOld);
            }
            if (afiliadoFamiliarNew != null && !afiliadoFamiliarNew.equals(afiliadoFamiliarOld)) {
                afiliadoFamiliarNew.getFamiliares().add(familiar);
                afiliadoFamiliarNew = em.merge(afiliadoFamiliarNew);
            }
            if (titularOld != null && !titularOld.equals(titularNew)) {
                titularOld.getFamiliarTitular().remove(familiar);
                titularOld = em.merge(titularOld);
            }
            if (titularNew != null && !titularNew.equals(titularOld)) {
                titularNew.getFamiliarTitular().add(familiar);
                titularNew = em.merge(titularNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = familiar.getId();
                if (findFamiliar(id) == null) {
                    throw new NonexistentEntityException("The familiar with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(int id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Familiar familiar;
            try {
                familiar = em.getReference(Familiar.class, id);
                familiar.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The familiar with id " + id + " no longer exists.", enfe);
            }
            Afiliado afiliadoFamiliar = familiar.getAfiliadoFamiliar();
            if (afiliadoFamiliar != null) {
                afiliadoFamiliar.getFamiliares().remove(familiar);
                afiliadoFamiliar = em.merge(afiliadoFamiliar);
            }
            Titular titular = familiar.getTitular();
            if (titular != null) {
                titular.getFamiliarTitular().remove(familiar);
                titular = em.merge(titular);
            }
            em.remove(familiar);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Familiar> findFamiliarEntities() {
        return findFamiliarEntities(true, -1, -1);
    }

    public List<Familiar> findFamiliarEntities(int maxResults, int firstResult) {
        return findFamiliarEntities(false, maxResults, firstResult);
    }

    private List<Familiar> findFamiliarEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Familiar.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Familiar findFamiliar(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Familiar.class, id);
        } finally {
            em.close();
        }
    }

    public int getFamiliarCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Familiar> rt = cq.from(Familiar.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
