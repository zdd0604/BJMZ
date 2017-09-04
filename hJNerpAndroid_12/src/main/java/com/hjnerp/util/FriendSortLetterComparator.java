package com.hjnerp.util;

import java.util.Comparator;

import com.hjnerp.model.FriendInfo;

/**
 * Compare friend's sort letter.
 * 
 */
public class FriendSortLetterComparator implements Comparator<FriendInfo> {

	public int compare(FriendInfo o1, FriendInfo o2) {

		if (o1.getSortLetter() == '@' || o2.getSortLetter() == '#') {
			return -1;
		} else if (o1.getSortLetter() == '#' || o2.getSortLetter() == '@') {
			return 1;
		} else {
			return o1.getSortLetter() - o2.getSortLetter();
		}
	}

}
