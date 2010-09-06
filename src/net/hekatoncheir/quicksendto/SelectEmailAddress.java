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

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TableLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.BaseAdapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.text.TextWatcher;
import android.text.Editable;
import android.os.Handler;

import android.content.Intent;

import android.net.Uri;
import android.provider.ContactsContract;
import android.database.Cursor;

import android.app.ProgressDialog;

import java.util.Vector;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class SelectEmailAddress extends Activity
{
    private final static String TAG = "QuickSendTo.SelectEmailAddress";
	private Handler _handler = new Handler();
	
	public final static String INTENT_PARAM_EMAILADDRESS = "email";
	
	private ListView _contactList;
	private Vector<String> _emailAddressList = new Vector<String>();
    
	private ArrayAdapter _adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectemail);
		
		// 
		loadContact();
		
		// 
		ListView contact_list = (ListView)findViewById(R.id.contact_list);
		contact_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListView listView = (ListView) parent;
				String item = (String) _adapter.getItem(position);
// 				Log.d(TAG, "onItemClick position="+position+ " id="+id);
				
				Intent intent = new Intent();
				intent.putExtra(INTENT_PARAM_EMAILADDRESS, item);
				
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		_adapter = new ArrayAdapter<String>(this, R.layout.selectemail_entry, _emailAddressList);
		contact_list.setAdapter(_adapter);
		
		EditText filterText = (EditText) findViewById(R.id.contact_search);
        filterText.addTextChangedListener(_filterTextWatcher);
		
		
	}
	@Override
	protected void onDestroy() {
		EditText filterText = (EditText) findViewById(R.id.contact_search);
		filterText.removeTextChangedListener(_filterTextWatcher);
		super.onDestroy();
	}

	
	private void loadContact()
	{
		Uri uri = ContactsContract.Contacts.CONTENT_URI;
		String[] projection = new String[] {
			ContactsContract.Contacts._ID,
			ContactsContract.Contacts.DISPLAY_NAME
		};
		String[] selectionArgs = null;
		String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

		Map<Integer, String> displayNameSet = new HashMap<Integer, String>();
		Set<String> emailAddressSet = new HashSet<String>();
		
		Cursor cursor = managedQuery(uri, projection, null, selectionArgs, sortOrder);
		while (cursor.moveToNext()){
			String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			if( displayName == null)displayName = "";
			int id = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
			displayNameSet.put(new Integer(id), displayName);
		}
		cursor.close();

		Cursor emails = getContentResolver().query( 
			ContactsContract.CommonDataKinds.Email.CONTENT_URI, 
			null, 
			null, null, null);
		
		while (emails.moveToNext()){
			int id = emails.getInt(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID));
			String emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
			String displayName = displayNameSet.get(new Integer(id));
			
			if( emailAddress != null && displayName != null && !emailAddressSet.contains(emailAddress)){
				//Log.d(TAG, displayName + " " +emailAddress);
				addContentListData(displayName, emailAddress);
				
				emailAddressSet.add(emailAddress);
			}
		}
		emails.close();

	}
    
	private void addContentListData (String displayName, String emailAddress)
	{
		String e = GetAddressString(displayName, emailAddress);
		_emailAddressList.add(e);
	}
	
	private TextWatcher _filterTextWatcher = new TextWatcher() {
		public void afterTextChanged(Editable s) {
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
		        int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
		        int count) {
		    _adapter.getFilter().filter(s);
			
// 			Log.d(TAG, "onTextChanged "+ s);
		}

	};

	static String GetAddressString(String displayName, String emailAddress)
	{
		if( displayName == null || displayName == emailAddress)return emailAddress;
		return "\""+displayName+"\" <" + emailAddress + ">";
	}

};
