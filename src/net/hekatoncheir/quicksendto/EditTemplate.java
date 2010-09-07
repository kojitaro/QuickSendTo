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

import java.util.Vector;

import android.app.Activity;
import android.os.Bundle;

import android.content.Intent;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;



import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.view.View;

import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.content.ContentValues;

public class EditTemplate extends Activity
{
	private static final int ACTIVITY_ID_SELECT_EMAIL = 0;

	public final static String INTENT_PARAM_ID = "template_id";
	
	ImageButton _btn_mailselect;
	Button _btn_save;
	Button _btn_cancel;
	EditText _title;
	EditText _subject;
	EditText _message;
	
	MultiAutoCompleteTextView _mail;
	
	boolean _isUpdate;
	long _template_id;
	
	private DatabaseHelper _dbhelper;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
		_dbhelper = new DatabaseHelper(this);
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edittemplate);
        
        _btn_mailselect = (ImageButton)findViewById(R.id.edittemplate_btn_mailselect);
        _btn_mailselect.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View view){
        		startActivityForResult( new Intent(EditTemplate.this, SelectEmailAddress.class), ACTIVITY_ID_SELECT_EMAIL);
        				}
        	});
        _btn_mailselect.setImageResource (android.R.drawable.ic_dialog_email);
        
        _btn_save = (Button)findViewById(R.id.edittemplate_btn_save);
		_btn_save.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
				save();
				setResult(RESULT_OK);
				finish();
			}
		});

		_btn_cancel = (Button)findViewById(R.id.edittemplate_btn_cancel);
		_btn_cancel.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		
		
		_title = (EditText)findViewById(R.id.edittemplate_title);
		_subject = (EditText)findViewById(R.id.edittemplate_subject);
		_message = (EditText)findViewById(R.id.edittemplate_message);

		
		Vector<String> emailAddressList = ContactMailadressListing.listup(this);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, emailAddressList);
		_mail = (MultiAutoCompleteTextView)findViewById(R.id.edittemplate_mail);
		_mail.setAdapter(adapter);
		_mail.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
		
		_isUpdate = false;
		Intent intent = getIntent();
		_template_id = intent.getLongExtra (INTENT_PARAM_ID, -1);
		
		SQLiteDatabase db = _dbhelper.getWritableDatabase();
		String query = "SELECT "
			+ BaseColumns._ID+","
			+ DatabaseHelper.TITLE + ","
			+ DatabaseHelper.SUBJECT + ","
			+ DatabaseHelper.MAILADDRESS + ","
			+ DatabaseHelper.MESSAGE
			+ " FROM " + DatabaseHelper.TABLE_NAME
			+ " WHERE " + BaseColumns._ID +"=?";
		
		String args[] = {""+_template_id};
		
		Cursor c = db.rawQuery(query, args);
		boolean b = c.moveToFirst();
		if (b){
//			long cid = c.getLong (0);
			String title = c.getString(1);
			String subject = c.getString(2);
			String mailaddress = c.getString(3);
			String message = c.getString(4);
			
			_title.setText(title);
			_subject.setText(subject);
			_mail.setText(mailaddress);
			_message.setText(message);
			
			_isUpdate = true;
		}
		
		
	}
	public void onActivityResult(int requestCode, int resultCode,  Intent data) {
		if (requestCode == ACTIVITY_ID_SELECT_EMAIL) {
			if (resultCode == RESULT_OK) {
				String emailAddress = data.getStringExtra(SelectEmailAddress.INTENT_PARAM_EMAILADDRESS);
				String o = _mail.getText().toString();
				if( o.length()>0){
					emailAddress = o+","+emailAddress;
				}
				_mail.setText(emailAddress);
			}
		}
	}
	
	public void save()
	{
		SQLiteDatabase db = _dbhelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.TITLE, _title.getText().toString());
		values.put(DatabaseHelper.MAILADDRESS, _mail.getText().toString());
		values.put(DatabaseHelper.SUBJECT, _subject.getText().toString());
		values.put(DatabaseHelper.MESSAGE, _message.getText().toString());
		
		if( _isUpdate ){
			db.update(DatabaseHelper.TABLE_NAME,values, BaseColumns._ID + "=" + _template_id, null);
		}else{
			db.insert(DatabaseHelper.TABLE_NAME,null,values);
		}
		db.close();
		
	}
	

};
