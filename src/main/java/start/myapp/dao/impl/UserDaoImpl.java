package start.myapp.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import start.myapp.dao.UserDao;
import start.myapp.model.User;

@Repository
public class UserDaoImpl implements UserDao {
	@Autowired
	private SessionFactory sessionFactory;

	private Session currentSession() {

		return sessionFactory.getCurrentSession();
	}

	@Override
	public User save(User user) {
		// TODO Auto-generated method stub
		currentSession().save(user);
		return user;
	}

	@Override
	public User update(User user) {
		// TODO Auto-generated method stub
		currentSession().update(user);
		return user;
	}

	@Override
	public User findOne(UUID uuid) {
		// TODO Auto-generated method stub
		Object user = currentSession().get(User.class, uuid);
		if (user != null) {
			return (User) user;
		}
		return null;
	}

	@Override
	public User findByCriterion(Criterion criterion) {
		// TODO Auto-generated method stub
//		List users = currentSession().createQuery("from User u where u.username=:username")
//				.setParameter("username", username).list();
//		if (users.size() == 0) {
//			return null;
//		} else {
//			return (User) users.get(0);
//		}
		List users = currentSession().createCriteria(User.class).add(criterion).list();
		if (users.size() == 0) {
			return null;
		} else {
			return (User) users.get(0);
		}
	}

	@Override
	public List<User> findAll() {
		// TODO Auto-generated method stub
		List users = currentSession().createQuery("from User").list();
		if (users.size() == 0) {
			return new ArrayList<User>();
		} else {
			return (List<User>) users;
		}
	}

}
