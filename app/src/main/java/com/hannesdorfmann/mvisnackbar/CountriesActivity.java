package com.hannesdorfmann.mvisnackbar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.hannesdorfmann.mosby3.mvi.MviActivity;
import com.hannesdorfmann.mvisnackbar.details.CountryDetailsActivity;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class CountriesActivity extends MviActivity<CountriesView, CountriesPresenter>
    implements CountriesView {

  private PublishSubject<Boolean> pullToRefreshSubject = PublishSubject.create();
  private PublishSubject<Long> dismissPullToRefreshError = PublishSubject.create();
  private Snackbar snackbar;
  private ArrayAdapter<String> adapter;

  @BindView(R.id.refreshLayout) SwipeRefreshLayout refreshLayout;
  @BindView(R.id.listView) ListView listView;
  @BindView(R.id.progressBar) ProgressBar progressBar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list);
    ButterKnife.bind(this);
    refreshLayout.setOnRefreshListener(() -> pullToRefreshSubject.onNext(true));
    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
    listView.setAdapter(adapter);
    listView.setOnItemClickListener((adapterView, view, i, l) -> {
      String countryName = adapter.getItem(i);
      Intent intent = CountryDetailsActivity.getIntent(CountriesActivity.this, countryName);
      startActivity(intent);
    });
  }

  @NonNull @Override public CountriesPresenter createPresenter() {
    return new CountriesPresenter();
  }

  @Override public Observable<Boolean> loadCountriesIntent() {
    return Observable.just(true);
  }

  @Override public Observable<Boolean> pullToRefreshIntent() {
    return pullToRefreshSubject;
  }

  @Override public Observable<Long> dismissPullToRefreshErrorIntent() {
    return dismissPullToRefreshError;
  }

  @Override public void render(CountriesViewState viewState) {
    if (viewState.isLoading()) {
      progressBar.setVisibility(View.VISIBLE);
      refreshLayout.setVisibility(View.GONE);
    } else {
      // show countries
      progressBar.setVisibility(View.GONE);
      refreshLayout.setVisibility(View.VISIBLE);

      adapter.clear();
      adapter.addAll(viewState.getCountries());

      refreshLayout.post(() -> refreshLayout.setRefreshing(viewState.isPullToRefresh()));

      if (viewState.isPullToRefreshError()) {
        showSnackbar();
      } else {
        dismissSnackbar();
      }
    }
  }

  private void dismissSnackbar() {
    if (snackbar == null) return;
    snackbar.dismiss();
  }

  private void showSnackbar() {
    snackbar = Snackbar.make(refreshLayout, "An Error has occurred", Snackbar.LENGTH_INDEFINITE);
    snackbar.addCallback( new Snackbar.Callback(){
      @Override public void onDismissed(Snackbar transientBottomBar, int event) {
        if (event == DISMISS_EVENT_SWIPE){
          // User swipes the snackbar away
          dismissPullToRefreshError.onNext(0L);
        }
      }
    });
    snackbar.show();
  }
}
