package com.chenpan.heart.diary.tabhelper;

import java.sql.SQLException;
import java.util.List;

import com.chenpan.heart.diary.db.DaoManager;
import com.chenpan.heart.diary.tab.TextDiaryTab;
import com.j256.ormlite.dao.Dao;

public class TextDiaryTabHelper {
	private static TextDiaryTabHelper textDiaryTabHelper;
	private Dao<TextDiaryTab, String> textDiaryDao;

	/**
	 * ���� :textDiaryTabHelper.java
	 * 
	 * @author Administrator ��������: ����ʱ��:����10:24:24
	 */
	private TextDiaryTabHelper() {
		try {
			textDiaryDao = DaoManager.getDao(TextDiaryTab.class);
		} catch (SQLException e) {
			e.printStackTrace();

		}
	}

	// get instance
	public static TextDiaryTabHelper getInstance() {
		if (textDiaryTabHelper == null) {
			textDiaryTabHelper = new TextDiaryTabHelper();
		}
		return textDiaryTabHelper;
	}

	// Ormlite operate
	public TextDiaryTab insert(TextDiaryTab outCheckListTab) {
		try {
			return textDiaryDao.createIfNotExists(outCheckListTab);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<TextDiaryTab> queryAll() {
		List<TextDiaryTab> list = null;
		try {
			list = textDiaryDao.queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();

		}
		return list;
	}

	public int delete(TextDiaryTab textDiary) {

		try {
			return textDiaryDao.delete(textDiary);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

	public int update(TextDiaryTab textDiary) {
		/*boolean isexist = false;
		isexist = isExist(textDiary);*/
		try {
			
				return textDiaryDao.update(textDiary);
			
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * ���ݱ����ѯ
	 * 
	 * @param title
	 * @return
	 */
	public List<TextDiaryTab> queryBytitle(String title) {
		List<TextDiaryTab> list = null;
		try {
			list = textDiaryDao.queryBuilder().where().eq("title", title)
					.query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * ����ʱ���ѯ
	 * 
	 * @param createtime
	 * @return
	 */
	public List<TextDiaryTab> queryBycreatetime(String createtime) {
		List<TextDiaryTab> list = null;
		try {
			list = textDiaryDao.queryBuilder().where()
					.eq("creatTime", createtime).query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public void clear() {
		List<TextDiaryTab> list = null;
		try {
			list = textDiaryDao.queryForAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int i;
		for (i = 0; i < list.size(); i++) {
			TextDiaryTab Out_Check_Result_Tab = list.get(i);
			try {
				textDiaryDao.delete(Out_Check_Result_Tab);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * ��ѯ�Ƿ���ڵ�ǰ��¼
	 */

	public boolean isExist(TextDiaryTab textDiary) {
		TextDiaryTab list = null;
		boolean isexist = false;
		try {
			list = textDiaryDao.queryForId(String.valueOf(textDiary.getID()));
			if (list != null ) {
				isexist = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isexist;
	}
}
