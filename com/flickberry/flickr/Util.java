package com.flickberry.flickr;

import java.util.Enumeration;
import java.util.Vector;

public class Util {

	public Util() {

	}

	/*
	 * Sorts String value from Enumeration
	 */
	public static final Enumeration sort(Enumeration e) {
		Vector v = new Vector();
		while (e.hasMoreElements()) {
			String s = (String) e.nextElement();
			int i = 0;
			for (i = 0; i < v.size(); i++) {
				int c = s.compareTo((String) v.elementAt(i));
				if (c < 0) { // s should go before i
					v.insertElementAt(s, i);
					break;
				} else if (c == 0) { // s already there
					break;
				}
			}
			if (i >= v.size()) { // add s at end
				v.addElement(s);
			}
		}
		return v.elements();
	}
}
