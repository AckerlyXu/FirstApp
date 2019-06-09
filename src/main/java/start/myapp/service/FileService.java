package start.myapp.service;

import java.util.List;
import java.util.UUID;

import start.myapp.model.SharedFilePage;
import start.myapp.model.User;
import start.myapp.model.UserFile;
import start.myapp.model.UserFilePage;

public interface FileService {
	List<UserFile> getAllDirectory(UUID id);

	UserFile saveFile(UserFile userFile);

	UserFile findOne(Integer id);

	void getPageData(UserFilePage page);

	UserFile update(UserFile userfile);

	void getSharedPageData(SharedFilePage sharedFilePage);

	List<UserFile> findToDelete(User user);
}
