package com.chenpan.heart.diary.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import android.util.Base64;

/**
 * @className FileUtil
 * @author goubaihu
 * @function ��Bitmapת�����ֽ����顢
 * @createTime 2014��11��28��
 */
public class FileUtil {
	/**
	 * д�뱾�������ļ�
	 * 
	 * @param str
	 */
	public static boolean writeToFile(String str, String fileString) {
		File file = new File(fileString);
		FileOutputStream fos = null;
		FileInputStream fis = null;
		try {
			if (!file.exists()) {
				File file2 = new File(file.getParent());
				file2.mkdir();
			}
			if (!file.isDirectory()) {
				file.createNewFile();
			}

			fos = new FileOutputStream(file);
			fos.write(str.getBytes());
			fos.close();
			fis = new FileInputStream(file);
			fis.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean writeToFile(String desFilePath, InputStream is)
			throws Exception {
		return writeToFile(new File(desFilePath), is);
	}

	/**
	 * ��ȡ�ļ�������
	 * 
	 * @param filePath
	 *            �ļ�·��
	 * @return �ļ�������
	 */
	public static InputStream getInputStream(String filePath) {
		if (!SDCardStatu.isSDCardAvailable()) {
			return null;
		}
		File gsyDataFile = new File(filePath);
		if (!gsyDataFile.exists()) {
			return null;
		}
		try {
			return new FileInputStream(gsyDataFile);
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	/**
	 * д���ļ�
	 * 
	 * @param desFile
	 *            ·��
	 * @param is
	 *            д����
	 * @return �Ƿ�д��ɹ�
	 * @throws Exception
	 */
	public static boolean writeToFile(File desFile, InputStream is)
			throws Exception {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(desFile, false);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
			return true;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
			}
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
			}
		}
	}

	/**
	 * �ж��ļ��Ƿ����
	 * 
	 * @param filePath
	 *            �ļ�·��
	 * @return �ж�����
	 */
	public static boolean isExist(String filePath) {
		try {
			return new File(filePath).exists();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * ɾ���ļ�
	 * 
	 * @param path
	 *            �ļ�·��
	 * @return �ж�ɾ���ɹ�
	 */
	public static boolean deleteFileByPath(String path) {
		if (StringUtils.isEmpty(path)) {
			return false;
		}
		File file = new File(path);
		return file.delete();
	}

	/**
	 * ɾ��Ŀ¼�µ��ļ�
	 * 
	 * @param directory
	 *            Ŀ¼·��
	 * @return �ж�ɾ���ɹ�
	 */
	public static boolean deleteFilesByDirectory(File directory) {
		if (directory != null && directory.exists() && directory.isDirectory()) {
			for (File item : directory.listFiles()) {
				item.delete();
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @���������� <p>
	 *        ���ļ�ת����String
	 *        </P>
	 * @param
	 * @return String
	 * @throws IOException
	 * @throws NullPointerException
	 */
	public static String fileToString(String path) throws Exception {
		File file = new File(path);
		InputStream inStream = new FileInputStream(file);
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		String strData = Base64.encodeToString(data, Base64.DEFAULT);
		// String strData = new String(data);
		outStream.close();
		inStream.close();
		return strData;

	}

	/**
	 * 
	 * @���������� <p>
	 *        ��stringת����file
	 *        </P>
	 * @param
	 * @return String data filePath ·��
	 *         StarConstant.PHONE_PATH+"recoder/amyy.amr"
	 * @throws IOException
	 * @throws NullPointerException
	 */
	public static void stringToFile(String data, String filePath) {
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;
		try {
			File distFile = new File(filePath);
			if (!distFile.getParentFile().exists())
				distFile.getParentFile().mkdirs();
			bufferedReader = new BufferedReader(new StringReader(data));
			bufferedWriter = new BufferedWriter(new FileWriter(distFile));
			char buf[] = new char[1024]; // �ַ�������
			int len;
			while ((len = bufferedReader.read(buf)) != -1) {
				bufferedWriter.write(buf, 0, len);
			}
			bufferedWriter.flush();
			bufferedReader.close();
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 
	 * @param oldPath
	 *            String ԭ�ļ�·��
	 * @param newPath
	 *            String ���ƺ�·��
	 * @return boolean
	 */
	public static void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // �ļ�����ʱ
				InputStream inStream = new FileInputStream(oldPath); // ����ԭ�ļ�
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1024];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // �ֽ��� �ļ���С
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("���Ƶ����ļ���������");
			e.printStackTrace();

		}

	}

}
