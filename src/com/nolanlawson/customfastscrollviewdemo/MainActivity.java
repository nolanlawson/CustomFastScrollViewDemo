package com.nolanlawson.customfastscrollviewdemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity implements OnClickListener {
	
	private Button sortByContinentButton, sortByNameButton;
	private ListView listView;
	private CustomFastScrollView fastScrollView;
	
	private CountryAdapter adapter;
	
	private CountrySorting sorting = CountrySorting.ByContinent; // default
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        setUpWidgets();
        
        loadList();
    }
    
    private void loadList() {
		List<Country> countries = readInCountries();
		
		Collections.sort(countries, sorting.getComparator());
		
		adapter = new CountryAdapter(this, android.R.layout.simple_list_item_2, countries);
		listView.setAdapter(adapter);
	}

    private void refreshSortingAndSections() {
    	adapter.setCountrySorting(sorting);
    	adapter.sort(sorting.getComparator());
    	adapter.refreshSections();
		fastScrollView.listItemsChanged();
    }
    
	private void setUpWidgets() {
		sortByContinentButton = (Button) findViewById(R.id.sort_by_continent_button);
		sortByNameButton = (Button) findViewById(R.id.sort_by_name_button);
		
		sortByContinentButton.setOnClickListener(this);
		sortByNameButton.setOnClickListener(this);
		
		listView = (ListView) findViewById(android.R.id.list);
		fastScrollView = (CustomFastScrollView) findViewById(R.id.fast_scroll_view);
	}

	private List<Country> readInCountries() {
    	List<Country> countries = new ArrayList<Country>();
		BufferedReader buff = null;
		try {
			buff = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.countries)));
			while (buff.ready()) {
				String[] line = buff.readLine().split("\t");
				
				Country country = new Country();
				country.setContinent(line[0]);
				country.setName(line[1]);
				
				countries.add(country);
			}
		} catch (IOException e) {
			// ignore
		} finally {
			if (buff != null) {
				try {
					buff.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
		return countries;
    }

	@Override
	public void onClick(View view) {
		// one of the buttons was clicked
		switch (view.getId()) {
			case R.id.sort_by_continent_button:
				sorting = CountrySorting.ByContinent;
				refreshSortingAndSections();
				break;
			case R.id.sort_by_name_button:
				sorting = CountrySorting.ByName;
				refreshSortingAndSections();
				break;
		}
		
	}
}