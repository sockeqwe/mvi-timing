package com.hannesdorfmann.mvisnackbar;

import java.util.List;

/**
 * @author Hannes Dorfmann
 */

public interface RepositoryState {

  CountriesViewState reduce(CountriesViewState oldState);
}

class Loading implements RepositoryState {
  @Override public CountriesViewState reduce(CountriesViewState oldState) {
    return CountriesViewState.showLoadingState();
  }
}

class CountriesLoaded implements RepositoryState {
  private final List<String> countries;

  public CountriesLoaded(List<String> countries) {
    this.countries = countries;
  }

  @Override public CountriesViewState reduce(CountriesViewState oldState) {
    return CountriesViewState.showDataState(countries);
  }
}

class ShowCountries implements RepositoryState {
  @Override public CountriesViewState reduce(CountriesViewState oldState) {
    return CountriesViewState.showDataState(oldState.getCountries());
  }
}

class PullToRefreshLoading implements RepositoryState {
  @Override public CountriesViewState reduce(CountriesViewState oldState) {
    return CountriesViewState.showPullToRefresh(oldState.getCountries());
  }
}

class PullToRefreshError implements RepositoryState {
  @Override public CountriesViewState reduce(CountriesViewState oldState) {
    return CountriesViewState.showPullToRefreshError(oldState.getCountries());
  }
}
