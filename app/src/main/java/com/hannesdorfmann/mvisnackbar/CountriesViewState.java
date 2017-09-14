package com.hannesdorfmann.mvisnackbar;

import java.util.Collections;
import java.util.List;

/**
 * @author Hannes Dorfmann
 */
public class CountriesViewState {

  // True if progressbar should be displayed
  private final boolean loading;

  // List of countries (country names)  if loaded
  private final List<String> countries;

  // true if pull to refresh indicator should be displayed
  private final boolean pullToRefresh;

  // true if an error has occurred while pull to refresh. Show snackbar.
  private final boolean pullToRefreshError;

  public CountriesViewState(boolean loading, List<String> countries, boolean pullToRefresh,
      boolean pullToRefreshError) {
    this.loading = loading;
    this.countries = countries;
    this.pullToRefresh = pullToRefresh;
    this.pullToRefreshError = pullToRefreshError;
  }

  public boolean isLoading() {
    return loading;
  }

  public List<String> getCountries() {
    return countries;
  }

  public boolean isPullToRefresh() {
    return pullToRefresh;
  }

  public boolean isPullToRefreshError() {
    return pullToRefreshError;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    CountriesViewState that = (CountriesViewState) o;

    if (loading != that.loading) return false;
    if (pullToRefresh != that.pullToRefresh) return false;
    if (pullToRefreshError != that.pullToRefreshError) return false;
    return countries != null ? countries.equals(that.countries) : that.countries == null;
  }

  @Override public int hashCode() {
    int result = (loading ? 1 : 0);
    result = 31 * result + (countries != null ? countries.hashCode() : 0);
    result = 31 * result + (pullToRefresh ? 1 : 0);
    result = 31 * result + (pullToRefreshError ? 1 : 0);
    return result;
  }

  @Override public String toString() {
    return "CountriesViewState{"
        + "loading="
        + loading
        + ", countries="
        + countries
        + ", pullToRefresh="
        + pullToRefresh
        + ", pullToRefreshError="
        + pullToRefreshError
        + '}';
  }

  public static CountriesViewState showLoadingState() {
    return new CountriesViewState(true, Collections.<String>emptyList(), false, false);
  }

  public static CountriesViewState showDataState(List<String> countries) {
    return new CountriesViewState(false, countries, false, false);
  }

  public static CountriesViewState showPullToRefresh(List<String> countries) {
    return new CountriesViewState(false, countries, true, false);
  }

  public static CountriesViewState showPullToRefreshError(List<String> countries) {
    return new CountriesViewState(false, countries, false, true);
  }


}
