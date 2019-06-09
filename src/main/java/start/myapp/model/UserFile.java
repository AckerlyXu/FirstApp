package start.myapp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

@Entity
public class UserFile {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	public void setId(Integer id) {
		this.id = id;
	}

	private String filename;

	private String filePath;
	private boolean isDirectory;

	private boolean isShared;
	private String path;

	@CreationTimestamp
	private Date createdDate;

	private String fileSize;

	public boolean isShared() {
		return isShared;
	}

	public void setShared(boolean isShared) {
		this.isShared = isShared;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	@ManyToOne()
	private User user;

	@ManyToOne
	@JoinColumn(name = "ext_id")
	private FileImage fileImage;

	public FileImage getFileImage() {
		return fileImage;
	}

	public void setFileImage(FileImage fileImage) {
		this.fileImage = fileImage;
	}

	public boolean isDirectory() {
		return isDirectory;
	}

	public void setDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
	}

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "parentId")
	private UserFile parent;

	@OneToMany(mappedBy = "parent", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<UserFile> children = new ArrayList<>();

	public String getFilename() {
		return filename;
	}

	public List<UserFile> getChildren() {
		return children;
	}

	// 设置子文件(夹)的工具方法
	public void addChild(UserFile file) throws Exception {
		this.children.add(file);
		file.setParent(this);
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UserFile getParent() {
		return parent;
	}

	public void setParent(UserFile parent) {
		this.parent = parent;

	}

	public Integer getId() {
		return id;
	}

	// 格式化文件大小
	public String formatFilesize(Long fileSize) {
		// 如果大于一兆
		if (fileSize * 1.0 / 1024 / 1024 > 1) {
			return ((int) (fileSize * 1.0 / 1024 / 1024)) + "MB";
		} else if (fileSize * 1.0 / 1024 > 1) {
			return ((int) (fileSize * 1.0 / 1024)) + "KB";
		} else {
			return fileSize + "B";
		}
	}
}
