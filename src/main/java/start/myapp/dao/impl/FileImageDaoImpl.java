package start.myapp.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import start.myapp.dao.FileImageDao;
import start.myapp.dao.UserDao;
import start.myapp.model.FileImage;

@Repository
public class FileImageDaoImpl implements FileImageDao {
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private UserDao userDao;

	private Session currentSession() {

		return sessionFactory.getCurrentSession();
	}

	@Override
	public List<FileImage> findByCriterion(Criterion criterion, Order order) {
		// TODO Auto-generated method stub
		List list = currentSession().createCriteria(FileImage.class).add(criterion).addOrder(order).list();
		if (list.size() > 0) {
			return (List<FileImage>) list;
		} else {
			return new ArrayList<>();
		}
	}

	@Override
	public FileImage save(FileImage fileImage) {
		// TODO Auto-generated method stub
		currentSession().save(fileImage);
		return fileImage;
	}

	@Override
	public FileImage getImageLazily(Integer id) {
		// TODO Auto-generated method stub
		FileImage image = (FileImage) currentSession().load(FileImage.class, id);
		return image;
	}

}
