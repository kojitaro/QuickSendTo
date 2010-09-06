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

public class Template
{
	// use only for class QuickSendTo
	public long _id = -1;
	
	public String _title;
	public String _subject;
	public String _mailaddress;
	public String _message;
	
	public Template(long id, String title, String subject, String mailaddress, String message)
	{
		_id = id;
		_title = title;
		_subject = subject;
		_mailaddress = mailaddress;
		_message = message;
	}

	public String toString()
	{
		if( _title != null && _title.length() > 0 )return _title;
		if( _mailaddress != null && _mailaddress.length() > 0)return _mailaddress;
		return "(null)";
	}
	
};