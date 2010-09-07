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
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.text.TextWatcher;
import android.text.Editable;

import android.content.Intent;

import java.util.Vector;

public class SelectEmailAddress extends Activity
{
 //   private final static String TAG = "QuickSendTo.SelectEmailAddress";
	
	public final static String INTENT_PARAM_EMAILADDRESS = "email";
	
	private TextWatcher _filterTextWatcher;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectemail);
		
		// 
		ListView contact_list = (ListView)findViewById(R.id.contact_list);
		Vector<String> emailAddressList = ContactMailadressListing.listup(this);
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.selectemail_entry, emailAddressList);
		contact_list.setAdapter(adapter);
		
		contact_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String item = (String) adapter.getItem(position);
// 				Log.d(TAG, "onItemClick position="+position+ " id="+id);
				
				Intent intent = new Intent();
				intent.putExtra(INTENT_PARAM_EMAILADDRESS, item);
				
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		
		EditText filterText = (EditText) findViewById(R.id.contact_search);
		
		_filterTextWatcher = new TextWatcher() {
			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
			        int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
			        int count) {
				adapter.getFilter().filter(s);
				
//	 			Log.d(TAG, "onTextChanged "+ s);
			}

		};
		
        filterText.addTextChangedListener(_filterTextWatcher);
		
		
	}
	@Override
	protected void onDestroy() {
		EditText filterText = (EditText) findViewById(R.id.contact_search);
		filterText.removeTextChangedListener(_filterTextWatcher);
		super.onDestroy();
	}
};
