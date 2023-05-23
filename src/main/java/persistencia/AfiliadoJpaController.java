
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
import logica.Afiliado;
import logica.Familiar;
import persistencia.exceptions.NonexistentEntityException;


public class AfiliadoJpaController implements Serializable {

    public AfiliadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public AfiliadoJpaController(){
        emf = Persistence.createEntityManagerFactory("AfiliacionesPU");
    }      
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Afiliado afiliado) {
        if (afiliado.getTitulares() == null) {
            afiliado.setTitulares(new ArrayList<Titular>());
        }
        if (afiliado.getFamiliares() == null) {
            afiliado.setFamiliares(new ArrayList<Familiar>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Titular> attachedTitulares = new ArrayList<Titular>();
            for (Titular titularesTitularToAttach : afiliado.getTitulares()) {
                titularesTitularToAttach = em.getReference(titularesTitularToAttach.getClass(), titularesTitularToAttach.getId());
                attachedTitulares.add(titularesTitularToAttach);
            }
            afiliado.setTitulares(attachedTitulares);
            List<Familiar> attachedFamiliares = new ArrayList<Familiar>();
            for (Familiar familiaresFamiliarToAttach : afiliado.getFamiliares()) {
                familiaresFamiliarToAttach = em.getReference(familiaresFamiliarToAttach.getClass(), familiaresFamiliarToAttach.getId());
                attachedFamiliares.add(familiaresFamiliarToAttach);
            }
            afiliado.setFamiliares(attachedFamiliares);
            em.persist(afiliado);
            for (Titular titularesTitular : afiliado.getTitulares()) {
                Afiliado oldAfiliadoTitularOfTitularesTitular = titularesTitular.getAfiliadoTitular();
                titularesTitular.setAfiliadoTitular(afiliado);
                titularesTitular = em.merge(titularesTitular);
                if (oldAfiliadoTitularOfTitularesTitular != null) {
                    oldAfiliadoTitularOfTitularesTitular.getTitulares().remove(titularesTitular);
                    oldAfiliadoTitularOfTitularesTitular = em.merge(oldAfiliadoTitularOfTitularesTitular);
                }
            }
            for (Familiar familiaresFamiliar : afiliado.getFamiliares()) {
                Afiliado oldAfiliadoFamiliarOfFamiliaresFamiliar = familiaresFamiliar.getAfiliadoFamiliar();
                familiaresFamiliar.setAfiliadoFamiliar(afiliado);
                familiaresFamiliar = em.merge(familiaresFamiliar);
                if (oldAfiliadoFamiliarOfFamiliaresFamiliar != null) {
                    oldAfiliadoFamiliarOfFamiliaresFamiliar.getFamiliares().remove(familiaresFamiliar);
                    oldAfiliadoFamiliarOfFamiliaresFamiliar = em.merge(oldAfiliadoFamiliarOfFamiliaresFamiliar);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Afiliado afiliado) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Afiliado persistentAfiliado = em.find(Afiliado.class, afiliado.getId());
            List<Titular> titularesOld = persistentAfiliado.getTitulares();
            List<Titular> titularesNew = afiliado.getTitulares();
            List<Familiar> familiaresOld = persistentAfiliado.getFamiliares();
            List<Familiar> familiaresNew = afiliado.getFamiliares();
            List<Titular> attachedTitularesNew = new ArrayList<Titular>();
            for (Titular titularesNewTitularToAttach : titularesNew) {
                titularesNewTitularToAttach = em.getReference(titularesNewTitularToAttach.getClass(), titularesNewTitularToAttach.getId());
                attachedTitularesNew.add(titularesNewTitularToAttach);
            }
            titularesNew = attachedTitularesNew;
            afiliado.setTitulares(titularesNew);
            List<Familiar> attachedFamiliaresNew = new ArrayList<Familiar>();
            for (Familiar familiaresNewFamiliarToAttach : familiaresNew) {
                familiaresNewFamiliarToAttach = em.getReference(familiaresNewFamiliarToAttach.getClass(), familiaresNewFamiliarToAttach.getId());
                attachedFamiliaresNew.add(familiaresNewFamiliarToAttach);
            }
            familiaresNew = attachedFamiliaresNew;
            afiliado.setFamiliares(familiaresNew);
            afiliado = em.merge(afiliado);
            for (Titular titularesOldTitular : titularesOld) {
                if (!titularesNew.contains(titularesOldTitular)) {
                    titularesOldTitular.setAfiliadoTitular(null);
                    titularesOldTitular = em.merge(titularesOldTitular);
                }
            }
            for (Titular titularesNewTitular : titularesNew) {
                if (!titularesOld.contains(titularesNewTitular)) {
                    Afiliado oldAfiliadoTitularOfTitularesNewTitular = titularesNewTitular.getAfiliadoTitular();
                    titularesNewTitular.setAfiliadoTitular(afiliado);
                    titularesNewTitular = em.merge(titularesNewTitular);
                    if (oldAfiliadoTitularOfTitularesNewTitular != null && !oldAfiliadoTitularOfTitularesNewTitular.equals(afiliado)) {
                        oldAfiliadoTitularOfTitularesNewTitular.getTitulares().remove(titularesNewTitular);
                        oldAfiliadoTitularOfTitularesNewTitular = em.merge(oldAfiliadoTitularOfTitularesNewTitular);
                    }
                }
            }
            for (Familiar familiaresOldFamiliar : familiaresOld) {
                if (!familiaresNew.contains(familiaresOldFamiliar)) {
                    familiaresOldFamiliar.setAfiliadoFamiliar(null);
                    familiaresOldFamiliar = em.merge(familiaresOldFamiliar);
                }
            }
            for (Familiar familiaresNewFamiliar : familiaresNew) {
                if (!familiaresOld.contains(familiaresNewFamiliar)) {
                    Afiliado oldAfiliadoFamiliarOfFamiliaresNewFamiliar = familiaresNewFamiliar.getAfiliadoFamiliar();
                    familiaresNewFamiliar.setAfiliadoFamiliar(afiliado);
                    familiaresNewFamiliar = em.merge(familiaresNewFamiliar);
                    if (oldAfiliadoFamiliarOfFamiliaresNewFamiliar != null && !oldAfiliadoFamiliarOfFamiliaresNewFamiliar.equals(afiliado)) {
                        oldAfiliadoFamiliarOfFamiliaresNewFamiliar.getFamiliares().remove(familiaresNewFamiliar);
                        oldAfiliadoFamiliarOfFamiliaresNewFamiliar = em.merge(oldAfiliadoFamiliarOfFamiliaresNewFamiliar);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = afiliado.getId();
                if (findAfiliado(id) == null) {
                    throw new NonexistentEntityException("The afiliado with id " + id + " no longer exists.");
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
            Afiliado afiliado;
            try {
                afiliado = em.getReference(Afiliado.class, id);
                afiliado.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The afiliado with id " + id + " no longer exists.", enfe);
            }
            List<Titular> titulares = afiliado.getTitulares();
            for (Titular titularesTitular : titulares) {
                titularesTitular.setAfiliadoTitular(null);
                titularesTitular = em.merge(titularesTitular);
            }
            List<Familiar> familiares = afiliado.getFamiliares();
            for (Familiar familiaresFamiliar : familiares) {
                familiaresFamiliar.setAfiliadoFamiliar(null);
                familiaresFamiliar = em.merge(familiaresFamiliar);
            }
            em.remove(afiliado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Afiliado> findAfiliadoEntities() {
        return findAfiliadoEntities(true, -1, -1);
    }

    public List<Afiliado> findAfiliadoEntities(int maxResults, int firstResult) {
        return findAfiliadoEntities(false, maxResults, firstResult);
    }

    private List<Afiliado> findAfiliadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Afiliado.class));
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

    public Afiliado findAfiliado(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Afiliado.class, id);
        } finally {
            em.close();
        }
    }

    public Afiliado findAfiliadoDni(String dni) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Afiliado> query = em.createQuery("SELECT a FROM Afiliado a WHERE a.dni = :dni", Afiliado.class);
            query.setParameter("dni", dni);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }    
    
    public int getAfiliadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Afiliado> rt = cq.from(Afiliado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
