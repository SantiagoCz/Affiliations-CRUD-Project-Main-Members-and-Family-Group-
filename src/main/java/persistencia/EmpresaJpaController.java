
package persistencia;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import logica.Titular;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import logica.Empresa;
import persistencia.exceptions.NonexistentEntityException;


public class EmpresaJpaController implements Serializable {

    public EmpresaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public EmpresaJpaController(){
        emf = Persistence.createEntityManagerFactory("AfiliacionesPU");
    }      
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empresa empresa) {
        if (empresa.getTitualares() == null) {
            empresa.setTitualares(new ArrayList<Titular>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Titular> attachedTitualares = new ArrayList<Titular>();
            for (Titular titualaresTitularToAttach : empresa.getTitualares()) {
                titualaresTitularToAttach = em.getReference(titualaresTitularToAttach.getClass(), titualaresTitularToAttach.getId());
                attachedTitualares.add(titualaresTitularToAttach);
            }
            empresa.setTitualares(attachedTitualares);
            em.persist(empresa);
            for (Titular titualaresTitular : empresa.getTitualares()) {
                Empresa oldEmpresaOfTitualaresTitular = titualaresTitular.getEmpresa();
                titualaresTitular.setEmpresa(empresa);
                titualaresTitular = em.merge(titualaresTitular);
                if (oldEmpresaOfTitualaresTitular != null) {
                    oldEmpresaOfTitualaresTitular.getTitualares().remove(titualaresTitular);
                    oldEmpresaOfTitualaresTitular = em.merge(oldEmpresaOfTitualaresTitular);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Empresa empresa) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresa persistentEmpresa = em.find(Empresa.class, empresa.getId());
            List<Titular> titualaresOld = persistentEmpresa.getTitualares();
            List<Titular> titualaresNew = empresa.getTitualares();
            List<Titular> attachedTitualaresNew = new ArrayList<Titular>();
            for (Titular titualaresNewTitularToAttach : titualaresNew) {
                titualaresNewTitularToAttach = em.getReference(titualaresNewTitularToAttach.getClass(), titualaresNewTitularToAttach.getId());
                attachedTitualaresNew.add(titualaresNewTitularToAttach);
            }
            titualaresNew = attachedTitualaresNew;
            empresa.setTitualares(titualaresNew);
            empresa = em.merge(empresa);
            for (Titular titualaresOldTitular : titualaresOld) {
                if (!titualaresNew.contains(titualaresOldTitular)) {
                    titualaresOldTitular.setEmpresa(null);
                    titualaresOldTitular = em.merge(titualaresOldTitular);
                }
            }
            for (Titular titualaresNewTitular : titualaresNew) {
                if (!titualaresOld.contains(titualaresNewTitular)) {
                    Empresa oldEmpresaOfTitualaresNewTitular = titualaresNewTitular.getEmpresa();
                    titualaresNewTitular.setEmpresa(empresa);
                    titualaresNewTitular = em.merge(titualaresNewTitular);
                    if (oldEmpresaOfTitualaresNewTitular != null && !oldEmpresaOfTitualaresNewTitular.equals(empresa)) {
                        oldEmpresaOfTitualaresNewTitular.getTitualares().remove(titualaresNewTitular);
                        oldEmpresaOfTitualaresNewTitular = em.merge(oldEmpresaOfTitualaresNewTitular);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = empresa.getId();
                if (findEmpresa(id) == null) {
                    throw new NonexistentEntityException("The empresa with id " + id + " no longer exists.");
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
            Empresa empresa;
            try {
                empresa = em.getReference(Empresa.class, id);
                empresa.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empresa with id " + id + " no longer exists.", enfe);
            }
            List<Titular> titualares = empresa.getTitualares();
            for (Titular titualaresTitular : titualares) {
                titualaresTitular.setEmpresa(null);
                titualaresTitular = em.merge(titualaresTitular);
            }
            em.remove(empresa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Empresa> findEmpresaEntities() {
        return findEmpresaEntities(true, -1, -1);
    }

    public List<Empresa> findEmpresaEntities(int maxResults, int firstResult) {
        return findEmpresaEntities(false, maxResults, firstResult);
    }

    private List<Empresa> findEmpresaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empresa.class));
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

    public Empresa findEmpresa(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empresa.class, id);
        } finally {
            em.close();
        }
    }
    
    public Empresa findEmpresaCuit(String cuit) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Empresa> query = em.createQuery("SELECT e FROM Empresa e WHERE e.cuit = :cuit", Empresa.class);
            query.setParameter("cuit", cuit);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
    

    public int getEmpresaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empresa> rt = cq.from(Empresa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
