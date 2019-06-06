package start.myapp.service;

import java.util.List;
import java.util.UUID;

import start.myapp.model.User;

public interface UserService {
	User save(User user);

	User update(User user);

	User findOne(UUID uuid);

	User findUserByUsername(String username);

	List<User> findAll();

	User findUserByUsernameAndPassword(User user);

	User changePassword(User user);
}
