package com.chenpan.heart.diary.db;

import java.sql.SQLException;

import com.chenpan.heart.diary.application.MyApplication;
import com.j256.ormlite.dao.Dao;

public final class DaoManager {
	private static DbHelper mDbhelper;

	public static <D extends Dao<T, ?>, T> D getDao(Class<T> clazz)
			throws SQLException {
		if (mDbhelper == null) {
			mDbhelper = new DbHelper(MyApplication.getAppContext());
		}

		return mDbhelper.getDao(clazz);
	}
}