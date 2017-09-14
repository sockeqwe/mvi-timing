package com.hannesdorfmann.mvisnackbar;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Hannes Dorfmann
 */

public class CountriesRepositroy {

  private int counterForError;

  private static final List<String> COUNTRIES =
      Arrays.asList("Argentina", " Australia", "Austria", "Belgium", "Brazil", "Canada", "Chile",
          "China", "Denmark", "France", "Germany", "India", "Italy", "Mexico", "Netherlands",
          "Norway", "Poland", "Portugal", "Sweden", "Switzerland", "Russia", "Turkey",
          "United States");

  private List<String> newRandomOrderedList() {
    List<String> copy = new ArrayList<>(COUNTRIES);
    Collections.shuffle(copy);
    return copy;
  }

  private Observable<List<String>> loadRandomCountryList() {
    return Observable.fromCallable(() -> {

      Thread.sleep(1500); // Simulate network delay

      if (++counterForError % 2 == 0) {
        throw new Exception("Error while loading Countries");
      }

      return newRandomOrderedList();
    });
  }

  public Observable<RepositoryState> loadCountries() {
    return loadRandomCountryList()
        .map(CountriesLoaded::new)
        .cast(RepositoryState.class)
        .startWith(new Loading())
        .subscribeOn(Schedulers.io());
  }

  // Triggered by pull to refresh
  public Observable<RepositoryState> reload() {
    return loadRandomCountryList()
        .map(CountriesLoaded::new)
        .cast(RepositoryState.class)
        .startWith(new PullToRefreshLoading())
        .onErrorReturn(error -> new PullToRefreshError())
        .subscribeOn(Schedulers.io());
  }
}
