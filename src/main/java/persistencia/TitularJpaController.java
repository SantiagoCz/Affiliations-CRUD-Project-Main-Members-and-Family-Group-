
package persistencia;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import logica.Afiliado;
import logica.Empresa;
import logica.Familiar;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import logica.Titular;
import persistencia.exceptions.NonexistentEntityException;


public class TitularJpaController implements Serializable {

    public TitularJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public TitularJpaController(){
        emf = Persistence.createEntityManagerFactory("AfiliacionesPU");
    }      
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Titular titular) {
        if (titular.getFamiliarTitular() == null) {
            titular.setFamiliarTitular(new ArrayList<Familiar>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Afiliado afiliadoTitular = titular.getAfiliadoTitular();
            if (afiliadoTitular != null) {
                afiliadoTitular = em.getReference(afiliadoTitular.getClass(), afiliadoTitular.getId());
                titular.setAfiliadoTitular(afiliadoTitular);
            }
            Empresa empresa = titular.getEmpresa();
            if (empresa != null) {
                empresa = em.getReference(empresa.getClass(), empresa.getId());
                titular.setEmpresa(empresa);
            }
            List<Familiar> attachedFamiliarTitular = new ArrayList<Familiar>();
            for (Familiar familiarTitularFamiliarToAttach : titular.getFamiliarTitular()) {
                familiarTitularFamiliarToAttach = em.getReference(familiarTitularFamiliarToAttach.getClass(), familiarTitularFamiliarToAttach.getId());
                attachedFamiliarTitular.add(familiarTitularFamiliarToAttach);
            }
            titular.setFamiliarTitular(attachedFamiliarTitular);
            em.persist(titular);
            if (afiliadoTitular != null) {
                afiliadoTitular.getTitulares().add(titular);
                afiliadoTitular = em.merge(afiliadoTitular);
            }
            if (empresa != null) {
                empresa.getTitualares().add(titular);
                empresa = em.merge(empresa);
            }
            for (Familiar familiarTitularFamiliar : titular.getFamiliarTitular()) {
                Titular oldTitularOfFamiliarTitularFamiliar = familiarTitularFamiliar.getTitular();
                familiarTitularFamiliar.setTitular(titular);
                familiarTitularFamiliar = em.merge(familiarTitularFamiliar);
                if (oldTitularOfFamiliarTitularFamiliar != null) {
                    oldTitularOfFamiliarTitularFamiliar.getFamiliarTitular().remove(familiarTitularFamiliar);
                    oldTitularOfFamiliarTitularFamiliar = em.merge(oldTitularOfFamiliarTitularFamiliar);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Titular titular) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Titular persistentTitular = em.find(Titular.class, titular.getId());
            Afiliado afiliadoTitularOld = persistentTitular.getAfiliadoTitular();
            Afiliado afiliadoTitularNew = titular.getAfiliadoTitular();
            Empresa empresaOld = persistentTitular.getEmpresa();
            Empresa empresaNew = titular.getEmpresa();
            List<Familiar> familiarTitularOld = persistentTitular.getFamiliarTitular();
            List<Familiar> familiarTitularNew = titular.getFamiliarTitular();
            if (afiliadoTitularNew != null) {
                afiliadoTitularNew = em.getReference(afiliadoTitularNew.getClass(), afiliadoTitularNew.getId());
                titular.setAfiliadoTitular(afiliadoTitularNew);
            }
            if (empresaNew != null) {
                empresaNew = em.getReference(empresaNew.getClass(), empresaNew.getId());
                titular.setEmpresa(empresaNew);
            }
            List<Familiar> attachedFamiliarTitularNew = new ArrayList<Familiar>();
            for (Familiar familiarTitularNewFamiliarToAttach : familiarTitularNew) {
                familiarTitularNewFamiliarToAttach = em.getReference(familiarTitularNewFamiliarToAttach.getClass(), familiarTitularNewFamiliarToAttach.getId());
                attachedFamiliarTitularNew.add(familiarTitularNewFamiliarToAttach);
            }
            familiarTitularNew = attachedFamiliarTitularNew;
            titular.setFamiliarTitular(familiarTitularNew);
            titular = em.merge(titular);
            if (afiliadoTitularOld != null && !afiliadoTitularOld.equals(afiliadoTitularNew)) {
                afiliadoTitularOld.getTitulares().remove(titular);
                afiliadoTitularOld = em.merge(afiliadoTitularOld);
            }
            if (afiliadoTitularNew != null && !afiliadoTitularNew.equals(afiliadoTitularOld)) {
                afiliadoTitularNew.getTitulares().add(titular);
                afiliadoTitularNew = em.merge(afiliadoTitularNew);
            }
            if (empresaOld != null && !empresaOld.equals(empresaNew)) {
                empresaOld.getTitualares().remove(titular);
                empresaOld = em.merge(empresaOld);
            }
            if (empresaNew != null && !empresaNew.equals(empresaOld)) {
                empresaNew.getTitualares().add(titular);
                empresaNew = em.merge(empresaNew);
            }
            for (Familiar familiarTitularOldFamiliar : familiarTitularOld) {
                if (!familiarTitularNew.contains(familiarTitularOldFamiliar)) {
                    familiarTitularOldFamiliar.setTitular(null);
                    familiarTitularOldFamiliar = em.merge(familiarTitularOldFamiliar);
                }
            }
            for (Familiar familiarTitularNewFamiliar : familiarTitularNew) {
                if (!familiarTitularOld.contains(familiarTitularNewFamiliar)) {
                    Titular oldTitularOfFamiliarTitularNewFamiliar = familiarTitularNewFamiliar.getTitular();
                    familiarTitularNewFamiliar.setTitular(titular);
                    familiarTitularNewFamiliar = em.merge(familiarTitularNewFamiliar);
                    if (oldTitularOfFamiliarTitularNewFamiliar != null && !oldTitularOfFamiliarTitularNewFamiliar.equals(titular)) {
                        oldTitularOfFamiliarTitularNewFamiliar.getFamiliarTitular().remove(familiarTitularNewFamiliar);
                        oldTitularOfFamiliarTitularNewFamiliar = em.merge(oldTitularOfFamiliarTitularNewFamiliar);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = titular.getId();
                if (findTitular(id) == null) {
                    throw new NonexistentEntityException("The titular with id " + id + " no longer exists.");
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
            Titular titular;
            try {
                titular = em.getReference(Titular.class, id);
                titular.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The titular with id " + id + " no longer exists.", enfe);
            }
            Afiliado afiliadoTitular = titular.getAfiliadoTitular();
            if (afiliadoTitular != null) {
                afiliadoTitular.getTitulares().remove(titular);
                afiliadoTitular = em.merge(afiliadoTitular);
            }
            Empresa empresa = titular.getEmpresa();
            if (empresa != null) {
                empresa.getTitualares().remove(titular);
                empresa = em.merge(empresa);
            }
            List<Familiar> familiarTitular = titular.getFamiliarTitular();
            for (Familiar familiarTitularFamiliar : familiarTitular) {
                familiarTitularFamiliar.setTitular(null);
                familiarTitularFamiliar = em.merge(familiarTitularFamiliar);
            }
            em.remove(titular);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Titular> findTitularEntities() {
        return findTitularEntities(true, -1, -1);
    }

    public List<Titular> findTitularEntities(int maxResults, int firstResult) {
        return findTitularEntities(false, maxResults, firstResult);
    }

    private List<Titular> findTitularEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Titular.class));
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

    public Titular findTitular(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Titular.class, id);
        } finally {
            em.close();
        }
    }

    public int getTitularCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Titular> rt = cq.from(Titular.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
