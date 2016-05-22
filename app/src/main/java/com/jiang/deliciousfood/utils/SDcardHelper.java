package com.jiang.deliciousfood.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Environment;
import android.os.StatFs;

public class SDcardHelper {
	/**
	 * 判断Sdcard是否可用
	 */

	public static boolean isSDcardMounted() {
		boolean state = Environment.getExternalStorageState().
				equals(Environment.MEDIA_MOUNTED);
		return state;
	}

	/**
	 * 获取SDcard目录
	 * @return
	 */
	public static String getSDCardPath() {
		String path = Environment.getExternalStorageDirectory().getAbsolutePath();
		return path;
	}

	/**
	 * 返回总大小
	 * @return
	 */
	public static long getSDCardSize() {
		if (isSDcardMounted()) {
			//
			StatFs sf = new StatFs(getSDCardPath());
			long count = sf.getBlockCount();
			long size = sf.getBlockSize();
			return count * size;
		}
		return 0;
	}

	/**
	 * 返回剩余空间
	 */

	public static long getSDCardFreeSize() {
		if (isSDcardMounted()) {
			StatFs sFs = new StatFs(getSDCardPath());
			long count = sFs.getFreeBlocks();
			long size = sFs.getBlockSize();
			return count * size;
		}
		return 0;
	}

	/**
	 * 获取可用空间
	 */

	public static long getSDCardAvailableSize() {
		if (isSDcardMounted()) {
			StatFs sFs = new StatFs(getSDCardPath());
			long count = sFs.getAvailableBlocks();
			long size = sFs.getBlockSize();
			return count * size;
		}
		return 0;
	}

	/**
	 *
	 * @param data  保存的数据
	 * @param dir  保存的文件或者目录
	 * @param filename 保存的文件名
	 * @return
	 */
	public static boolean saveFileToSDCard(byte[] data, String dir,String filename)     {
		if (isSDcardMounted()) {//判断Sdcard是否可用
			File filedir = new File(getSDCardPath() + File.separator + dir);
			if (!filedir.exists()) {//是否存在目录，不存在则创建
				filedir.mkdirs();
			}

			if (getSDCardAvailableSize() >= data.length) {//判断空间是否够用				
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(new File(filedir+"/"+filename));
					fos.write(data);
					fos.flush();
					return true;
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finally {
					if (fos != null) {
						try {
							fos.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}

		return false;
	}

	public static byte[] readFileFromSDCard(String filepath)    {
		if (isSDcardMounted()) {
			File file = new File(filepath);
			ByteArrayOutputStream  byteArrayOutputStream = null;
			if (file.exists()) {
				FileInputStream fileInputStream = null;
				try {
					fileInputStream = new FileInputStream(file);
					byteArrayOutputStream = new ByteArrayOutputStream();
					byte[] buffer = new byte[1024];
					int length = 0;
					while ((length = fileInputStream.read(buffer)) != -1) {
						byteArrayOutputStream.write(buffer, 0, length);
					}

					return byteArrayOutputStream.toByteArray();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if (fileInputStream != null) {
						try {
							fileInputStream.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		return null;
	}
}
