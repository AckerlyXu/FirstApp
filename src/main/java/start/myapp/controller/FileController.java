package start.myapp.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import start.myapp.model.FileImage;
import start.myapp.model.User;
import start.myapp.model.UserFile;
import start.myapp.model.UserFilePage;
import start.myapp.service.FileImageService;
import start.myapp.service.FileService;
import start.myapp.service.UserService;
import start.myapp.utils.FileType;
import start.myapp.utils.FileUtil;

@Controller
@RequestMapping("/file")
public class FileController {
	@Autowired
	private FileService fileService;
	@Autowired
	private UserService userService;

	@Autowired
	private FileImageService fileImageService;

	@RequestMapping(path = "/add", method = RequestMethod.GET)
	public String addFile(Model model, HttpSession httpSession) {
		UserFile file = new UserFile();
		User user = (User) httpSession.getAttribute("user");
		List<UserFile> directories = fileService.getAllDirectory(user.getId());

		model.addAttribute("directories", directories);
		model.addAttribute("userfile", file);
		return "addFile";
	}

	private User user;

	// 初始化用户数据
	@ModelAttribute
	public void initUser(Model model, HttpSession httpSession) {
		this.user = (User) httpSession.getAttribute("user");
	}

	// @InitBinder("userFile")
	// public void initAccountBinder(WebDataBinder binder) {

	// binder.setFieldDefaultPrefix("userFile.");
	// }

	@RequestMapping(path = "/add", method = RequestMethod.POST)
	public String addFile(UserFile userFile, MultipartFile file, HttpServletRequest request, ModelMap model,
			RedirectAttributes redirectAttributes) {

		String fileRoot = request.getServletContext().getRealPath("") + File.separator + user.getId();

		if (StringUtils.isNotBlank(userFile.getFilename())) {

			if (userFile.getParent() != null && userFile.getParent().getId() != null) {
				UserFile parent = fileService.findOne(userFile.getParent().getId());

				// 设置父文件夹
				userFile.setParent(parent);
				// 设置自身路径
				userFile.setFilePath(parent.getFilePath() + File.separator + userFile.getFilename());
			} else {
				userFile.setFilePath(userFile.getFilename());
				userFile.setPath("");
			}
			if (userFile.getParent() != null && userFile.getParent().getId() != null) {

			}
			File sysFile = new File(fileRoot, userFile.getFilePath());
			if (userFile.isDirectory()) {

				if (sysFile.exists() && sysFile.isDirectory()) {
					model.addAttribute("filenameError", "该文件夹已经创建");
					initDataForAddFile(model, userFile);
					return "addFile";
				} else {
					boolean isSuccess = sysFile.mkdirs();
					if (isSuccess) {
						// 设置所属用户
						userFile.setUser(user);
						FileImage folder = fileImageService.findFileImageByExt(FileType.FOLDER);

						// 设置图标
						userFile.setFileImage(folder);

						fileService.saveFile(userFile);
						return "redirect:list";
					} else {
						model.addAttribute("filenameError", "文件夹创建失败");
						initDataForAddFile(model, userFile);
						return "addFile";
					}
				}
			} else {
				if (file != null) {
					if (sysFile.exists() && sysFile.isFile()) {
						if (sysFile.exists() && sysFile.isDirectory()) {
							model.addAttribute("filenameError", "该文件已经创建");
							initDataForAddFile(model, userFile);
							return "addFile";
						}
					}
					try {
						// 设置文件大小
						userFile.setFileSize(userFile.formatFilesize(file.getSize()));
						// 保存文件
						file.transferTo(sysFile);
						// 获得文件类型
						FileType type = start.myapp.utils.FileUtil.getType(sysFile.getAbsolutePath());
						FileImage image = null;
						if (type != null) {
							// 获得对应的FileImage
							image = fileImageService.findFileImageByExt(type);
							userFile.setFileImage(image);
						} else {
							image = fileImageService.findFileImageByExt(FileType.RM);
						}

						userFile.setFileImage(image);

						// 设置所属用户
						userFile.setUser(userService.findOne(user.getId()));
						if (userFile.getParent() != null && userFile.getParent().getId() != null) {
//							 UserFile parent = new UserFile();
//							 parent.setId(userFile.getParent().ge);
							Integer id = userFile.getParent().getId();
							UserFile parent = fileService.findOne(id);

							// 设置父文件夹
							userFile.setParent(parent);
							// 设置文件路径
							userFile.setFilePath(parent.getFilePath() + "/" + userFile.getFilename());

						}

						fileService.saveFile(userFile);
						redirectAttributes.addAttribute("currentPage", "1");
						return "redirect:list?currentPage={currentPage}";
					} catch (Exception e) {
						model.addAttribute("file", "文件创建失败");
						initDataForAddFile(model, userFile);
						return "addFile";
					}
				} else {
					model.addAttribute("file", "文件不能为空");
					initDataForAddFile(model, userFile);
					return "addFile";
				}
			}
		} else {

			model.addAttribute("filenameError", "文件名不能为空");
			initDataForAddFile(model, userFile);
			return "addFile";

		}
	}

	private void initDataForAddFile(ModelMap model, UserFile userFile) {
		List<UserFile> directories = fileService.getAllDirectory(user.getId());

		model.addAttribute("directories", directories);
		model.addAttribute("userfile", userFile);
	}

	@RequestMapping(path = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public String getFileList(UserFilePage page, ModelMap model) {

		page.setUserId(user.getId());

		fileService.getPageData(page);
		model.addAttribute("page", page);

		List<UserFile> directories = fileService.getAllDirectory(user.getId());

		model.addAttribute("directories", directories);

		return "fileList";

	}

	@RequestMapping(method = RequestMethod.GET, path = "download")
	public ResponseEntity<byte[]> download(int fileId, HttpServletRequest request) {
		UserFile userFile = fileService.findOne(fileId);

		// 如果不是当前用户，并且当前文件未分享，则不能下载
		if (!userFile.getUser().getId().toString().equals(user.getId().toString()) && !userFile.isShared()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new byte[0]);
		}
		String filepath = userFile.getFilePath();
		String realPath = request.getServletContext().getRealPath(user.getId().toString() + File.separator + filepath);
		File file = new File(realPath);
		if (file.exists() && file.isFile()) {
			ByteArrayOutputStream output = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024 * 10];
			FileInputStream input = null;
			try {
				input = new FileInputStream(realPath);
				FileType type = FileUtil.getType(realPath);

				int n = 0;
				while (-1 != (n = input.read(buffer))) {

					output.write(buffer, 0, n);

					output.flush();
				}
				output.close();
				String downloadname = file.getName();
				if (type != null) {
					downloadname += ("." + type.toString().toLowerCase());
				}
				byte[] content = output.toByteArray();
				return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(content.length)
						.header(HttpHeaders.CONTENT_DISPOSITION,
								"attachment; filename=" + URLEncoder.encode(downloadname, "UTF-8"))
						.body(content);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					input.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return null;
	}

	@RequestMapping(method = RequestMethod.POST, path = "/move")
	@ResponseBody
	public Map move(Integer parentId, @RequestParam("id") Integer fileId, HttpServletRequest request) {

		// 获得文件对应的userFile
		UserFile userfile = fileService.findOne(fileId);

		// 如果不是当前用户，不能移动文件
		if (!userfile.getUser().getId().toString().equals(user.getId().toString())) {
			return null;
		}
		// 获得父文件
		UserFile parent = fileService.findOne(parentId);
		// 计算根路径
		String rootPath = request.getServletContext().getRealPath("") + File.separator + user.getId() + File.separator;
		// 根据父文件路径获得目标文件路径
		String targetFilePath = rootPath + parent.getFilePath() + File.separator + userfile.getFilename();

		File file = new File(targetFilePath);
		if (file.exists() && file.isFile()) {
			// 如果已经存在返回错误消息
			Map map = new HashMap();
			map.put("code", 400);
			map.put("msg", "该文件夹中已经存在该文件");
			return map;
		} else {

			FileOutputStream output = null;
			FileInputStream input = null;

			try {
				// 把源文件读到目的地址
				output = new FileOutputStream(file);

				byte[] buffer = new byte[1024 * 10];

				input = new FileInputStream(rootPath + userfile.getFilePath());

				int n = 0;
				while (-1 != (n = input.read(buffer))) {

					output.write(buffer, 0, n);

					output.flush();

				}
				// 删除源文件
				new File(rootPath + userfile.getFilePath()).delete();
				// 更新父文件
				userfile.setParent(parent);
				// 更新路径
				userfile.setPath(parent.getPath() + "-" + userfile.getId());
				// 更新文件路径
				userfile.setFilePath(parent.getFilePath() + File.separator + userfile.getFilename());
				fileService.update(userfile);
				Map map = new HashMap<>();
				map.put("code", 200);
				map.put("msg", "移动成功");
				return map;

			} catch (Exception e) {
				// TODO: handle exception
			} finally {
				try {
					if (input != null) {

						input.close();

					}
					if (output != null) {
						output.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			return null;

		}
	}
}
