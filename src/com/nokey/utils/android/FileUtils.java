package com.nokey.utils.android;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileSystemView;

public class FileUtils {
	/**
	 * 没有文件夹就创建一个
	 * @param dirPath
	 * @return file 文件本身
	 */
	public static File checkCreateDir(String dirPath) {
		File f = new File(dirPath);
		if (!f.exists()) {
			f.mkdirs();
		}
		return f;
	}

	/**
	 * 复制到目标文件夹并重命名保留扩展名。
	 * 
	 * @param file
	 * @param destDir
	 * @param newName
	 */
	public static void copyRename(File file, File destDir, String newName) {
		try {
			Path path = file.toPath();
			String oldFileName = path.getFileName().toString();

			int dotIndex = oldFileName.lastIndexOf(".");
			String suffix = ".png";
			if (dotIndex > 0) {
				suffix = oldFileName.substring(dotIndex, oldFileName.length());
			}
			Files.copy(path, destDir.toPath().resolve(newName + suffix), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据后缀将文件分组
	 * 
	 * @param files
	 * @param suffixs
	 * @return key为文件名，value为对应文件列表的Map
	 */
	public static Map<String, List<File>> groupFilesBySuffix(File[] files, String[] suffixs) {
		Map<String, List<File>> grouped = new HashMap<>();
		for (File f : files) {
			String realName = f.getName();
			for (String suffix : suffixs) {
				if (suffix != null && f.getName().contains(suffix)) {
					realName = f.getName().replace(suffix, "");
					break;
				}
			}
			if (grouped.get(realName) == null) {
				List<File> nameFiles = new ArrayList<File>();
				nameFiles.add(f);
				grouped.put(realName, nameFiles);
			} else {
				grouped.get(realName).add(f);
			}
		}
		return grouped;
	}

	public static void openFolder(File file) {
		try {
			java.awt.Desktop.getDesktop().open(file);
		} catch (IOException e) {
			ToolsUtils.showMsgDialog("打开文件夹发生异常。");
		}
	}

	/**
	 * 选择一系列文件
	 * 
	 * @return 文件集合
	 */
	public static File[] selectFilesAndDir() {
		JFileChooser jfc = new JFileChooser();
		// 设置当前路径为桌面路径,否则将我的文档作为默认路径
		FileSystemView fsv = FileSystemView.getFileSystemView();
		jfc.setCurrentDirectory(fsv.getHomeDirectory());
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jfc.setMultiSelectionEnabled(true);
		jfc.showDialog(new JLabel(), "确定");
		File[] files = jfc.getSelectedFiles();
		return files;
	}

	/**
	 * 选择单个文件，如果目标不存在则返回上级目录。
	 * @return file
	 */
	public static File selectDir() {
		JFileChooser jfc = new JFileChooser();
		FileSystemView fsv = FileSystemView.getFileSystemView();
		jfc.setCurrentDirectory(fsv.getHomeDirectory());
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfc.setMultiSelectionEnabled(false);
		jfc.showDialog(new JLabel(), "确定");
		File f = jfc.getSelectedFile();
		if (f == null) {
			return null;
		} else if (f.exists()) {
			return f;
		} else {
			return f.getParentFile();
		}
	}

}
