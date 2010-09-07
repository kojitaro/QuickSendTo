/*
 * Copyright (C) 2010 Kouji Ohura
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.hekatoncheir.quicksendto;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

class DatabaseHelper extends SQLiteOpenHelper
{
	public static final String DB_NAME = "quicksendto.db";
	public static final String TABLE_NAME = "MAIL_TEMPLATE";
	
	public static final String TITLE = "TITLE";
	public static final String MAILADDRESS = "MAILADDRESS";
	public static final String SUBJECT = "SUBJECT";
	public static final String MESSAGE = "MESSAGE";

	DatabaseHelper(Context context) {
		super(context, DB_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE "+ TABLE_NAME +" ("
			+ BaseColumns._ID + " INTEGER PRIMARY KEY,"
			+ TITLE + " TEXT,"
			+ MAILADDRESS + " TEXT,"
			+ SUBJECT + " TEXT,"
			+ MESSAGE + " TEXT"
			+");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
		onCreate(db);
	}
}

