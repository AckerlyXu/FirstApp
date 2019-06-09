package start.myapp.dao;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

import start.myapp.model.UserFile;

public interface FileDao {
	List<UserFile> findByCriterion(Criterion criterion, Order order);

	UserFile save(UserFile userFile);

	UserFile findOne(Integer id);

	UserFile findOnewLazily(Integer id);

	UserFile update(UserFile file);

	int getCountByCriterion(Criterion and);

	List<UserFile> getPagedUserFile(Criterion criterion, int start, int pageSize, Order order);
}
