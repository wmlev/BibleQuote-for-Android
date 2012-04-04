/*
 * Copyright (C) 2011 Scripture Software (http://scripturesoftware.org/)
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
 *
 *  @ File Name : SimpleHistoryManager.java
 *  @ Date : 04.03.2012
 *  @ Author : Vladimir Yakushev
 *  
 */

package com.BibleQuote.managers.History;

import java.util.LinkedList;

import com.BibleQuote.entity.ItemList;
import com.BibleQuote.exceptions.FileAccessException;
import com.BibleQuote.utils.OSISLink;

public class SimpleHistoryManager implements IHistoryManager {
	
	private final int HISTORY_LENGHT;
	private IHistoryRepository repository;
	
	public SimpleHistoryManager(IHistoryRepository repository, int lenght) {
		this.repository = repository;
		this.HISTORY_LENGHT = lenght;
	}
	
	public synchronized void addLink(OSISLink link) {
		String humanLink = String.format("%1$s: %2$s %3$s:%4$s", 
				link.getModuleID(), link.getBookName(), 
				link.getChapterNumber(), link.getVerseNumber());
		ItemList newItem = new ItemList(link.getPath(), humanLink);
		
		LinkedList<ItemList> history = getLinks();
		if (history.contains(newItem)) {
			history.remove(newItem);
		}
		history.addFirst(newItem);
		
		while (history.size() > this.HISTORY_LENGHT) {
			history.removeLast();
		}

		repository.save(history);
	}
	
	public synchronized LinkedList<ItemList> getLinks() {
		try {
			return repository.load();
		} catch (FileAccessException e) {
			return new LinkedList<ItemList>();
		}
	}
}