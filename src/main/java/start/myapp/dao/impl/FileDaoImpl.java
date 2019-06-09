package start.myapp.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import start.myapp.dao.FileDao;
import start.myapp.dao.UserDao;
import start.myapp.model.User;
import start.myapp.model.UserFile;

@Repository
public class FileDaoImpl implements FileDao {
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private UserDao userDao;

	private Session currentSession() {

		return sessionFactory.getCurrentSession();
	}

	@Override
	public List<UserFile> findByCriterion(Criterion criterion, Order order) {
		// TODO Auto-generated method stub
		return (List<UserFile>) currentSession().createCriteria(UserFile.class).add(criterion).addOrder(order).list();

	}

	@Override
	public UserFile save(UserFile userFile) {
		// TODO Auto-generated method stub
		// if(userFile.getUser().getId())
		User user = userDao.findOneLazily(userFile.getUser().getId());
		// userService.findOne(userFile.getUser().getId());
		userFile.setUser(user);
		if (userFile.getParent().getId() == null) {
			userFile.setParent(null);
		}
		currentSession().save(userFile);
		return userFile;
	}

	@Override
	public UserFile findOne(Integer id) {
		// TODO Auto-generated method stub
		return (UserFile) currentSession().get(UserFile.class, id);
	}

	@Override
	public UserFile findOnewLazily(Integer id) {
		// TODO Auto-generated method stub
		return (UserFile) currentSession().load(UserFile.class, id);
	}

	@Override
	public UserFile update(UserFile file) {
		// TODO Auto-generated method stub
		currentSession().update(file);
		;
		return file;
	}

	@Override
	public int getCountByCriterion(Criterion and) {
		// TODO Auto-generated method stub
		return ((Long) currentSession().createCriteria(UserFile.class).add(and).setProjection(Projections.rowCount())
				.uniqueResult()).intValue();

	}

	@Override
	public List<UserFile> getPagedUserFile(Criterion criterion, int start, int pageSize, Order order) {

		Criteria temp = currentSession().createCriteria(UserFile.class).setFetchMode("fileImage", FetchMode.EAGER)
				.add(criterion);
		if (order != null) {
			temp.addOrder(order);
		}
		return (List<UserFile>) temp.setFirstResult(start).setMaxResults(pageSize).list();
	}

}
