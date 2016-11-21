// (c) 2016 uchicom
package com.uchicom.lister;

import java.io.File;

/**
 * @author uchicom: Shigeki Uchiyama
 *
 */
public class FileItem {

	private File file;
	private String status;
	public FileItem(File file, String status) {
		this.file = file;
		this.status = status;
	}
	/**
	 * fileを取得します.
	 *
	 * @return file
	 */
	public File getFile() {
		return file;
	}
	/**
	 * fileを設定します.
	 *
	 * @param file file
	 */
	public void setFile(File file) {
		this.file = file;
	}
	/**
	 * statusを取得します.
	 *
	 * @return status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * statusを設定します.
	 *
	 * @param status status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

}
