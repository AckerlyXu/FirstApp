package start.myapp.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import start.myapp.dao.FileDao;
import start.myapp.dao.FileImageDao;
import start.myapp.model.SharedFilePage;
import start.myapp.model.User;
import start.myapp.model.UserFile;
import start.myapp.model.UserFilePage;
import start.myapp.service.FileService;

@Service
public class FileServiceImpl implements FileService {
	@Autowired
	private FileDao fileDao;
	@Autowired
	private FileImageDao fileImageDao;

	@Override
	public List<UserFile> getAllDirectory(UUID id) {
		// TODO Auto-generated method stub
		SimpleExpression ideq = Restrictions.eq("user.id", id);
		SimpleExpression isDirectory = Restrictions.eq("isDirectory", true);
		LogicalExpression and = Restrictions.and(ideq, isDirectory);

		List<UserFile> files = fileDao.findByCriterion(and, Order.asc("filePath"));
		return files;
	}

	@Override
	public UserFile saveFile(UserFile userFile) {
		// TODO Auto-generated method stub
		// 获得父文件夹的路径
		String parentPath = "";
		if (userFile.getParent() != null && userFile.getParent().getId() != null) {
			parentPath = userFile.getParent().getPath();
			// 设置parent
			userFile.setParent(fileDao.findOnewLazily(userFile.getParent().getId()));
		}
		if (userFile.getFileImage() != null && userFile.getFileImage().getId() != null) {
			// 设置图片
			userFile.setFileImage(fileImageDao.getImageLazily(userFile.getFileImage().getId()));
		}
		UserFile file = fileDao.save(userFile);
		if (StringUtils.isNoneBlank(parentPath)) {
			file.setPath(parentPath + "-" + file.getId());
		} else {
			file.setPath(file.getId() + "");
		}
		return fileDao.update(file);
	}

	@Override
	public UserFile findOne(Integer id) {
		// TODO Auto-generated method stub
		return fileDao.findOne(id);
	}

	@Override
	public void getPageData(UserFilePage page) {
		List<Criterion> criterions = new ArrayList<>();
		UserFile file = new UserFile();

		Criterion and = null;
		if (StringUtils.isNoneBlank(page.getFilename())) {
			criterions.add(Restrictions.like("filename", "%" + page.getFilename() + "%"));
		}
		if (page.getDirectoryId() != null) {
			UserFile parent = fileDao.findOne(page.getDirectoryId());
			criterions.add(Restrictions.like("path", parent.getPath() + "%"));
		}

		if (page.getUserId() != null) {
			criterions.add(Restrictions.eq("user.id", page.getUserId()));
		}
		if (!criterions.isEmpty()) {
			Criterion[] criterionsArray = criterions.toArray(new Criterion[criterions.size()]);
			and = Restrictions.and(criterionsArray);
		}
		page.setTotalCount(fileDao.getCountByCriterion(and));

		page.setTotalPage((int) Math.ceil(page.getTotalCount() * 1.0 / 2));
		Order order = Order.asc("filePath");
		page.setFiles(fileDao.getPagedUserFile(and, ((page.getCurrentPage() - 1) * 2), 2, order));

	}

	@Override
	public UserFile update(UserFile userfile) {
		// TODO Auto-generated method stub
		return fileDao.update(userfile);
	}

	@Override
	public void getSharedPageData(SharedFilePage page) {
		// TODO Auto-generated method stub
		SimpleExpression shared = Restrictions.eq("isShared", true);
		page.setTotalCount(fileDao.getCountByCriterion(shared));

		page.setTotalPage((int) Math.ceil(page.getTotalCount() * 1.0 / 2));

		page.setFiles(fileDao.getPagedUserFile(shared, ((page.getCurrentPage() - 1) * 2), 2, null));
	}

	@Override
	public List<UserFile> findToDelete(User user) {
		// TODO Auto-generated method stub
		SimpleExpression eq = Restrictions.eq("user.id", user.getId());
		List<UserFile> userFiles = fileDao.findByCriterion(eq, Order.desc("filePath"));
		return userFiles;
	}

}
