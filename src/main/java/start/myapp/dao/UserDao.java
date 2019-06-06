package start.myapp.dao;

import java.util.List;
import java.util.UUID;

import org.hibernate.criterion.Criterion;

import start.myapp.model.User;

public interface UserDao {
	User save(User user);

	User update(User user);

	User findOne(UUID uuid);

	User findByCriterion(Criterion criterion);

	List<User> findAll();
}
