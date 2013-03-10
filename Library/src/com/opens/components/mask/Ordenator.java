package com.opens.components.mask;

import java.util.Comparator;

/**
 * Perform the masks ordination by the minor to mayor
 * @author Leonardo Rossetto <leonardoxh@gmail.com>
 */
public class Ordenator implements Comparator<String> {

	@Override
	public int compare(String first, String second) {
		return first.length() - second.length();
	}
	
}