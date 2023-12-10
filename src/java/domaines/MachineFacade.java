/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domaines;

import entities.Employe;
import entities.Machine;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

/**
 *
 * @author akhmim
 */
@Stateless
public class MachineFacade extends AbstractFacade<Machine> {

    @PersistenceContext(unitName = "RappelPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MachineFacade() {
        super(Machine.class);
    }

    public List<Object[]> nbEmploye() {
        List<Object[]> machines = null;
//        machines = em.createQuery("SELECT e FROM Machine m, Employe e WHERE m.employe = :employe").getResultList();

        machines = em.createQuery("SELECT\n"
                + "    COUNT(m.id),\n"
                + "    e.nom\n"
                + "FROM\n"
                + "    Machine m,\n"
                + "    Employe e\n"
                + "WHERE\n"
                + "    m.employe = e.id\n"
                + "GROUP BY\n"
                + "    e.nom").getResultList();
//        createQuery("select count(m.id),s.nom from Employe e, Service s where e.service.id=s.id group by s.nom")
        return machines;
    }

    @Override
    public List<Machine> findAll() {
        return em.createQuery("SELECT m FROM Machine m", Machine.class)
                .getResultList();
    }

    public Machine findById(int employeId) {
        return em.find(Machine.class, employeId);
    }

    public List<Machine> findByEmployee(String employeeName) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Machine> criteriaQuery = criteriaBuilder.createQuery(Machine.class);
        Root<Machine> machineRoot = criteriaQuery.from(Machine.class);

        Join<Machine, Employe> employeJoin = machineRoot.join("employe");

        criteriaQuery.select(machineRoot)
                .where(criteriaBuilder.equal(employeJoin.get("nom"), employeeName));

        TypedQuery<Machine> typedQuery = em.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }

}
