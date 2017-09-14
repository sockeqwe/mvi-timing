package com.hannesdorfmann.mvisnackbar;

import android.util.Log;
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import java.util.concurrent.TimeUnit;

/**
 * @author Hannes Dorfmann
 */

public class CountriesPresenter extends MviBasePresenter<CountriesView, CountriesViewState> {

  private final CountriesRepositroy repositroy = new CountriesRepositroy();

  @Override protected void bindIntents() {

    Observable<RepositoryState> loadingData =
        intent(CountriesView::loadCountriesIntent).switchMap(ignored -> repositroy.loadCountries());

    Observable<RepositoryState> pullToRefreshData =
        intent(CountriesView::pullToRefreshIntent).switchMap(
            ignored -> repositroy.reload().switchMap(repoState -> {
              if (repoState instanceof PullToRefreshError) {
                // Let's show Snackbar for 2 seconds and then dismiss it
                return Observable.timer(2, TimeUnit.SECONDS)
                    .map(ignoredTime -> new ShowCountries()) // Show just the list
                    .cast(RepositoryState.class)
                    .startWith(repoState); // repoState == PullToRefreshError
              } else {
                return Observable.just(repoState);
              }
            }));

    // Show Loading as inital state
    CountriesViewState initialState = CountriesViewState.showLoadingState();

    Observable<CountriesViewState> viewState = Observable.merge(loadingData, pullToRefreshData)
        .scan(initialState, (oldState, repoState) -> repoState.reduce(oldState))
        .doOnNext(vs -> Log.d("CountriesPresenter", vs.toString()))
        .observeOn(AndroidSchedulers.mainThread());

    subscribeViewState(viewState, CountriesView::render);
  }
}
