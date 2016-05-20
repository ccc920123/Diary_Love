package com.cdjysdkj.diary.tabhelper;

import java.sql.SQLException;
import java.util.List;

import com.cdjysdkj.diary.db.DaoManager;
import com.cdjysdkj.diary.tab.ImageDiaryTab;
import com.j256.ormlite.dao.Dao;

public class ImageDiaryTabHelper {
	private static ImageDiaryTabHelper imageDiaryTabHelper;
	private Dao<ImageDiaryTab, String> imageDiaryDao;
	/**
	 * 类名 :ImageDiaryTabHelper.java
	 * 
	 * @author Administrator 功能描述: 创建时间:上午10:24:24
	 */
	private ImageDiaryTabHelper() {
		try {
			imageDiaryDao = DaoManager.getDao(ImageDiaryTab.class);
		} catch (SQLException e) {
			e.printStackTrace();

		}
	}

	// get instance
	public static ImageDiaryTabHelper getInstance() {
		if (imageDiaryTabHelper == null) {
			imageDiaryTabHelper = new ImageDiaryTabHelper();
		}
		return imageDiaryTabHelper;
	}
	// Ormlite operate
		public ImageDiaryTab insert(ImageDiaryTab outCheckListTab) {
			try {
				return imageDiaryDao.createIfNotExists(outCheckListTab);
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}

		public List<ImageDiaryTab> queryAll() {
			List<ImageDiaryTab> list = null;
			try {
				list = imageDiaryDao.queryForAll();
			} catch (SQLException e) {
				e.printStackTrace();

			}
			return list;
		}

		public int delete(ImageDiaryTab imageDiary) {

			try {
				return imageDiaryDao.delete(imageDiary);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0;
			}
		}

		public int update(ImageDiaryTab imageDiary) {
			/*boolean isexist = false;
			isexist = isExist(textDiary);*/
			try {
				
					return imageDiaryDao.update(imageDiary);
				
			} catch (SQLException e) {
				e.printStackTrace();
				return 0;
			}
		}

		/**
		 * 根据标题查询
		 * 
		 * @param title
		 * @return
		 */
		public List<ImageDiaryTab> queryByimagePath(String path) {
			List<ImageDiaryTab> list = null;
			try {
				list = imageDiaryDao.queryBuilder().where().eq("imagePath", path)
						.query();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return list;
		}

		

		public void clear() {
			List<ImageDiaryTab> list = null;
			try {
				list = imageDiaryDao.queryForAll();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int i;
			for (i = 0; i < list.size(); i++) {
				ImageDiaryTab Out_Check_Result_Tab = list.get(i);
				try {
					imageDiaryDao.delete(Out_Check_Result_Tab);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
}
