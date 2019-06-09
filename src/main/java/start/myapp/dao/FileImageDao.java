package start.myapp.dao;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

import start.myapp.model.FileImage;

public interface FileImageDao {

	List<FileImage> findByCriterion(Criterion criterion, Order order);

	FileImage save(FileImage fileImage);

	FileImage getImageLazily(Integer id);
}
