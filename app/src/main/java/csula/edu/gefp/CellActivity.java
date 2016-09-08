package csula.edu.gefp;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import csula.edu.gefp.model.FlightPlanData;
import csula.edu.gefp.model.User;
import csula.edu.gefp.model.UserData;

public class CellActivity extends AppCompatActivity {

    private Long stageId;
    private ViewPager planViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cell);

        stageId = getIntent().getLongExtra("STAGE_ID", 0);

        planViewPager = (ViewPager) findViewById(R.id.plan_view_pager);
        planViewPager.setAdapter(new PagerAdapter() {

            @Override
            public Object instantiateItem(ViewGroup collection, int position) {
                LayoutInflater inflater = LayoutInflater.from(CellActivity.this);
                ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.view_cell, collection, false);
                collection.addView(layout);

                WebView cellWebView = (WebView) layout.findViewById(R.id.cellWebView);
                cellWebView.getSettings().setJavaScriptEnabled(true);
                User user = UserData.getInstance(CellActivity.this).getUser();
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user.getId().toString());
                params.put("accessKey", user.getAccessKey());
                params.put("stage_id", stageId.toString());
                params.put("runway_id", FlightPlanData.getInstance().getFlightPlan().getRunways().get(position).getId().toString());
                try {
                    cellWebView.loadUrl(WebServiceClient.buildUrl(WebServiceClient.ENDPOINT_CELL, params).toString());
                } catch (IOException e) {
                    Log.e("CellActivity", "URL Error", e);
                }
                return layout;
            }

            @Override
            public void destroyItem(ViewGroup collection, int position, Object view) {
                collection.removeView((View) view);
            }

            @Override
            public int getCount() {
                return FlightPlanData.getInstance().getRunways().size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return FlightPlanData.getInstance().getFlightPlan().getRunways().get(position).getName();
            }
        });
    }
}
