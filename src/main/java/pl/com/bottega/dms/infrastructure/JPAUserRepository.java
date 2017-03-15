package pl.com.bottega.dms.infrastructure;

import pl.com.bottega.dms.application.user.User;
import pl.com.bottega.dms.application.user.UserRepository;
import pl.com.bottega.dms.model.EmployeeId;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class JPAUserRepository implements UserRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void put(User user) {
        entityManager.persist(user);
    }

    @Override
    public User findByEmployeeId(EmployeeId id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User findByUserName(String username) {
        CriteriaBuilder criteriaBuiler = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuiler.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.where(criteriaBuiler.equal(root.get("userName"), username));
        Query query = entityManager.createQuery(criteriaQuery);
        List<User> users = query.getResultList();
        if (users.size() == 0)
            return null;
        else
            return users.get(0);
    }

    @Override
    public User findByLoginAndHashedPassword(String login, String password) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        Long loginLong = null;
        try {
            loginLong = Long.valueOf(login);
        } catch (Exception e) {
        }

        criteriaQuery.where(
                criteriaBuilder.or(
                        criteriaBuilder.equal(root.get("employeeId").get("id"), loginLong),
                        criteriaBuilder.equal(root.get("userName"), login)),
                criteriaBuilder.equal(root.get("hashedPassword"), password));

        Query query = entityManager.createQuery(criteriaQuery);
        List<User> users = query.getResultList();

        if (users.size() == 0)
            return null;
        else
            return users.get(0);
    }

}
