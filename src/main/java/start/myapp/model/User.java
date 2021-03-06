package start.myapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "`user`")
public class User {
	@Id

	@Type(type = "uuid-char")
	private UUID id;

	public void setId(UUID id) {
		this.id = id;
	}

	private String username;
	@Column(name = "`password`")
	private String password;

	// 一对多映射userFile
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
	private List<UserFile> userFiles = new ArrayList<>();
	private boolean isAdmin;

	public String getUsername() {
		return username;
	}

	// 和UserFile建立关联的便捷方法
	public void addUserFile(UserFile file) {
		getUserFiles().add(file);
		file.setUser(this);
	}

	public List<UserFile> getUserFiles() {
		return userFiles;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public UUID getId() {
		return id;
	}

}
