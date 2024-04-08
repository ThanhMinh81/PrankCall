package com.lutech.potmanprankcall.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchViewModel extends ViewModel {

    private MutableLiveData<String> keyWordSearch = new MutableLiveData<>();


    public SearchViewModel() {
    }


    public MutableLiveData<String> getKeyWordSearch() {
        return keyWordSearch;
    }

    public void setKeyWordSearch(String keyWordSearch) {
        this.keyWordSearch.setValue(keyWordSearch);
    }
}
