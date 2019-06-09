package start.myapp.service;

import start.myapp.model.FileImage;
import start.myapp.utils.FileType;

public interface FileImageService {
	FileImage save(FileImage fileImage);

	FileImage findFileImageByExt(FileType type);
}
