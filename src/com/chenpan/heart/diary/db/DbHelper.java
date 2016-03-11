package com.chenpan.heart.diary.db;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.chenpan.heart.diary.tab.DillTab;
import com.chenpan.heart.diary.tab.ImageDiaryTab;
import com.chenpan.heart.diary.tab.TextDiaryTab;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DbHelper extends OrmLiteSqliteOpenHelper {
	private static final String DB_NAME = "diary.db";
	private static final int DB_VERSION = 1;
	@SuppressWarnings("unused")
	private static final String TAG = "DbHelper";

	public DbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database,
			ConnectionSource connectionSource) {
		try {
			//建表
			TableUtils.createTable(connectionSource, TextDiaryTab.class);
			TableUtils.createTable(connectionSource, ImageDiaryTab.class);
			TableUtils.createTable(connectionSource, DillTab.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}  
	}

	@Override
	public void onUpgrade(SQLiteDatabase database,
			ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			//更新表
			
			TableUtils.createTable(connectionSource, TextDiaryTab.class);
			TableUtils.createTable(connectionSource, ImageDiaryTab.class);
			TableUtils.createTable(connectionSource, DillTab.class);
			onCreate(database, connectionSource);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
