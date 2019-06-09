package start.myapp.service.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import start.myapp.dao.FileImageDao;
import start.myapp.model.FileImage;
import start.myapp.service.FileImageService;
import start.myapp.utils.FileType;

@Service
public class FileImageServiceImpl implements FileImageService {
	@Autowired
	private FileImageDao fileImageDao;

	@Override
	public FileImage save(FileImage fileImage) {
		// TODO Auto-generated method stub
		return fileImageDao.save(fileImage);
	}

	@Override
	public FileImage findFileImageByExt(FileType type) {
		// TODO Auto-generated method stub
		SimpleExpression eq = Restrictions.eq("fileType", type);
		Order order = Order.asc("fileType");
		List<FileImage> list = fileImageDao.findByCriterion(eq, order);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

}
