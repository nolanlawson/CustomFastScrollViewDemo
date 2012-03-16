package com.nolanlawson.customfastscrollviewdemo;

import java.util.Comparator;

public enum CountrySorting {

	ByContinent (new Comparator<Country>(){

		@Override
		public int compare(Country left, Country right) {
			int continentCompare = left.getContinent().compareToIgnoreCase(right.getContinent());
			if (continentCompare != 0) {
				return continentCompare;
			}
			// same continent, so compare by name
			return left.getName().compareToIgnoreCase(right.getName());
		}}),
		
	ByName (new Comparator<Country>(){

		@Override
		public int compare(Country left, Country right) {
			return left.getName().compareToIgnoreCase(right.getName());
		}}),
	;
	
	private Comparator<Country> comparator;
	
	private CountrySorting(Comparator<Country> comparator) {
		this.comparator = comparator;
	}
	
	public Comparator<Country> getComparator() {
		return comparator;
	}
}
