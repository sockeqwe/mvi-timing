package com.hannesdorfmann.mvisnackbar;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import io.reactivex.Observable;

/**
 * @author Hannes Dorfmann
 */

public interface CountriesView extends MvpView {

  /**
   * Intent that triggers to load countries
   *
   * @return the emitted item (boolean) can be ignored. Has no meaning wether or not it is true or
   * false
   */
  Observable<Boolean> loadCountriesIntent();

  /**
   * Intent that triggers pull to refresh to reload list of countries
   *
   * @return the emitted item (boolean) can be ignored. Has no meaning wether or not it is true or
   * false
   */
  Observable<Boolean> pullToRefreshIntent();

  /**
   * Renders the state
   *
   * @param viewState The ViewState
   */
  void render(CountriesViewState viewState);
}
