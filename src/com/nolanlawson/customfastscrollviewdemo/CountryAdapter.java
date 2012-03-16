package com.nolanlawson.customfastscrollviewdemo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nolanlawson.customfastscrollviewdemo.CustomFastScrollView.SectionIndexer;

public class CountryAdapter extends ArrayAdapter<Country> implements SectionIndexer {
	
	private Context context;
	private int textViewResourceId;
	private List<Country> objects;

	private SectionIndexer sectionIndexer;
	private CountrySorting countrySorting = CountrySorting.ByContinent; // default
	
	public CountryAdapter(Context context, int textViewResourceId,
			List<Country> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.textViewResourceId = textViewResourceId;
		this.objects = objects;
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(textViewResourceId, parent, false);
		}
		
		TextView textView = (TextView) view.findViewById(android.R.id.text1);
		
		Country country = objects.get(position);
		
		// just use the country's name to display in the list
		textView.setText(country.getName());
		
		return view;
	}


	@Override
	public int getPositionForSection(int section) {
		return getSectionIndexer().getPositionForSection(section);
	}

	@Override
	public int getSectionForPosition(int position) {
		return getSectionIndexer().getSectionForPosition(position);
	}

	@Override
	public Object[] getSections() {
		return getSectionIndexer().getSections();
	}
	
	public void setCountrySorting(CountrySorting countrySorting) {
		this.countrySorting = countrySorting;
	}
	
	private SectionIndexer getSectionIndexer() {
		if (sectionIndexer == null) {
			sectionIndexer = createSectionIndexer(objects);
		}
		return sectionIndexer;
	}
	
	private SectionIndexer createSectionIndexer(List<Country> countries) {
		
		switch (countrySorting) {
			case ByContinent:
				return createByContinentSectionIndexer(countries, new Function<Country,String>(){

					@Override
					public String apply(Country input) {
						return input.getContinent();
					}});
			case ByName:
			default:
				return createByContinentSectionIndexer(countries, new Function<Country,String>(){

					@Override
					public String apply(Country input) {
						// show just the first letter in the title
						return input.getName().substring(0, 1);
					}});
		}
	}


	private SectionIndexer createByContinentSectionIndexer(
			List<Country> countries, Function<Country, String> function) {
	
		List<Country> sortedCountries = new ArrayList<Country>(countries);
		Collections.sort(sortedCountries, countrySorting.getComparator());
		
		List<String> sections = new ArrayList<String>();
		final Map<Integer, Integer> sectionsToPositions = new HashMap<Integer, Integer>();
		final Map<Integer, Integer> positionsToSections = new HashMap<Integer, Integer>();
		
		
		// assume the countries are properly sorted
		for (int i = 0; i < sortedCountries.size(); i++) {
			Country country = sortedCountries.get(i);
			String section = function.apply(country);
			if (sections.isEmpty() || !sections.get(sections.size() - 1).equals(section)) {
				// add a new section
				sections.add(section);
			}
			
			// map section to position and vice versa
			int sectionIndex = sections.size() - 1;
			sectionsToPositions.put(sectionIndex, i);
			positionsToSections.put(i, sectionIndex);
		}
		
		final String[] sectionsArray = sections.toArray(new String[sections.size()]);
		
		return new SectionIndexer() {
			
			@Override
			public Object[] getSections() {
				return sectionsArray;
			}
			
			@Override
			public int getSectionForPosition(int position) {
				return positionsToSections.get(position);
			}
			
			@Override
			public int getPositionForSection(int section) {
				return sectionsToPositions.get(section);
			}
		};
	}

	public void refreshSections() {
		sectionIndexer = null;
		getSectionIndexer();
	}
}
