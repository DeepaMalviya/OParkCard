package attender.oparkCard.vehiclelist.activity;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import attender.oparkCard.R;
import attender.oparkCard.base.DimensionUtil;
import attender.oparkCard.vehiclelist.adapter.PatientVehicleAdapter;

public class PatientVehicleWheelerList extends AppCompatActivity {
    private TabLayout tabLayout;
    private TextView textToolHeader;
    private ViewPager viewPager;
    private int statusBarHeight;
    private int normalWindowHeight;
    private int windowHeight;
    private RelativeLayout relativeLayoutHeader;
    PatientVehicleAdapter twoWheelerListAdapter;
    private Toolbar toolBar;
    private int[] tabTitle = {R.string.service_station, R.string.review};
    public static ArrayList<String> queryImageList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four_wheeler_list);
        init();

    }
    private void init() {


        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }

        final TypedArray styledAttributes = PatientVehicleWheelerList.this.getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.actionBarSize}
        );
        normalWindowHeight = DimensionUtil.getScreenHeight(PatientVehicleWheelerList.this);
        windowHeight = (normalWindowHeight - statusBarHeight) / 3;

//        relativeLayoutHeader = (RelativeLayout) findViewById(R.id.relativeLayoutHeader);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, windowHeight);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//        relativeLayoutHeader.setLayoutParams(params);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);

        getSupportActionBar().setTitle("");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        toolBar.setTitle("");
        textToolHeader = (TextView) toolBar.findViewById(R.id.toolbar_title);
        textToolHeader.setText("PatientVehicle List");
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        loadViewPager(viewPager);


    }
    private void loadViewPager(ViewPager viewPager) {
        twoWheelerListAdapter = new PatientVehicleAdapter(getSupportFragmentManager(),
                PatientVehicleWheelerList.this,"other");

        viewPager.setAdapter(twoWheelerListAdapter);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupTabIcons() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setCustomView(prepareTabView(i));
        }
    }

    private View prepareTabView(int pos) {

        View view = getLayoutInflater().inflate(R.layout.layout_view_custom, null);

        TextView textViewTitle = (TextView) view.findViewById(R.id.textViewTitle);

        textViewTitle.setText(tabTitle[pos]);


        return view;
    }

   /* private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OneFragment(), "ONE");
        adapter.addFragment(new TwoFragment(), "TWO");
        viewPager.setAdapter(adapter);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }*/
}
