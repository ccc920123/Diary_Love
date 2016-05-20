package com.cdjysdkj.diary.tabhelper;

import java.sql.SQLException;
import java.util.List;

import com.cdjysdkj.diary.db.DaoManager;
import com.cdjysdkj.diary.tab.DillTab;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;

public class DillTabHelper {
	private static DillTabHelper DillTabHelper;
	private Dao<DillTab, String> dillDao;

	/**
	 * 类名 :DillTabHelper.java
	 * 
	 * @author Administrator 功能描述: 创建时间:上午10:24:24
	 */
	private DillTabHelper() {
		try {
			dillDao = DaoManager.getDao(DillTab.class);
		} catch (SQLException e) {
			e.printStackTrace();

		}
	}

	// get instance
	public static DillTabHelper getInstance() {
		if (DillTabHelper == null) {
			DillTabHelper = new DillTabHelper();
		}
		return DillTabHelper;
	}

	// Ormlite operate
	public DillTab insert(DillTab dill) {
		try {
			return dillDao.createIfNotExists(dill);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<DillTab> queryAll() {
		List<DillTab> list = null;
		try {
			list = dillDao.queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();

		}
		return list;
	}

	public int delete(DillTab dill) {

		try {
			return dillDao.delete(dill);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public int update(DillTab dill) {
		try {

			return dillDao.update(dill);

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
	public String[] queryByGroup() {
		String[] resultArray = null;
		try {
			GenericRawResults<String[]> rawResults =

			dillDao.queryRaw(

			"select distinct time  from DillTab ");

			// there should be 1 result

			List<String[]> results = rawResults.getResults();

			// the results array should have 1 value
			if (results != null && results.size() > 0) {
				resultArray = new String[results.size()];
				for (int i = 0; i < results.size(); i++) {
					resultArray[i] = results.get(i)[0];
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return resultArray;
	}
	/**
	 * 查询账户类型
	 * 
	 * @param title
	 * @return
	 */
	public String[] queryByAccount() {
		String[] resultArray = null;
		try {
			GenericRawResults<String[]> rawResults =

			dillDao.queryRaw(

			"select distinct accountType  from DillTab ");

			// there should be 1 result

			List<String[]> results = rawResults.getResults();

			// the results array should have 1 value
			if (results != null && results.size() > 0) {
				resultArray = new String[results.size()];
				for (int i = 0; i < results.size(); i++) {
					resultArray[i] = results.get(i)[0];
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return resultArray;
	}

	/**
	 * 根据时间查询
	 * 
	 * @param createtime
	 * @return
	 */
	public List<DillTab> queryBytime(String time) {
		List<DillTab> list = null;
		try {
			list = dillDao.queryBuilder().where().eq("time", time).query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * 根据时间查询
	 * 
	 * @param createtime
	 * @return
	 */
	public List<DillTab> queryByAccount(String accountType) {
		List<DillTab> list = null;
		try {
			list = dillDao.queryBuilder().where().eq("accountType", accountType).query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * 根据时间查询
	 * 
	 * @param createtime
	 * @return
	 */
	public List<DillTab> queryBytimeAndcashtype(String time, String takeType,
			String buyType, String paytype) {
		List<DillTab> list = null;
		try {
			list = dillDao.queryBuilder().where().eq("detailtime", time).and()
					.eq("takeType", takeType).and().eq("type", buyType).and()
					.eq("accountType", paytype).query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public void clear() {
		List<DillTab> list = null;
		try {
			list = dillDao.queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		int i;
		for (i = 0; i < list.size(); i++) {
			DillTab Out_Check_Result_Tab = list.get(i);
			try {
				dillDao.delete(Out_Check_Result_Tab);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
