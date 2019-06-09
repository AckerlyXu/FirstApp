package start.myapp.service.impl;

import java.util.List;
import java.util.UUID;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import start.myapp.dao.UserDao;
import start.myapp.model.User;
import start.myapp.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Override
	public User save(User user) {
		// TODO Auto-generated method stub
		if (user.getId() == null) {
			user.setId(UUID.randomUUID());
		}
		return userDao.save(user);
	}

	@Override
	public User update(User user) {
		// TODO Auto-generated method stub
		return userDao.update(user);
	}

	@Override
	public User findOne(UUID uuid) {
		// TODO Auto-generated method stub
		return userDao.findOne(uuid);
	}

	@Override
	public User findUserByUsername(String username) {
		// TODO Auto-generated method stub

		return userDao.findByCriterion(Restrictions.eq("username", username));
	}

	@Override
	public List<User> findAll() {
		// TODO Auto-generated method stub
		return userDao.findAll();
	}

	@Override
	public User findUserByUsernameAndPassword(User user) {
		// TODO Auto-generated method stub
		Criterion criterion = Restrictions.and(Restrictions.eq("username", user.getUsername()),
				Restrictions.eq("password", user.getPassword()));
		return userDao.findByCriterion(criterion);
	}

	@Override
	public User changePassword(User user) {
		// TODO Auto-generated method stub
		return userDao.update(user);

	}

	@Override
	public void delete(User user) {
		// TODO Auto-generated method stub

		userDao.delete(user);
	}

}
