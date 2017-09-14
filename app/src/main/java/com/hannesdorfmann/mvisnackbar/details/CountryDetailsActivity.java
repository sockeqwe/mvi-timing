package com.hannesdorfmann.mvisnackbar.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.hannesdorfmann.mvisnackbar.R;

public class CountryDetailsActivity extends AppCompatActivity {

  private static final String COUNTRY_NAME = "countryName";

  @BindView(R.id.country) TextView country;

  public static Intent getIntent(Context context, String countryName) {
    Intent i = new Intent(context, CountryDetailsActivity.class);
    i.putExtra(COUNTRY_NAME, countryName);
    return i;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_country_details);
    ButterKnife.bind(this);
    String countryName = getIntent().getStringExtra(COUNTRY_NAME);
    country.setText(countryName);

    getSupportActionBar().setTitle(countryName);
  }
}
