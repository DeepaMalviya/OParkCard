package attender.oparkCard.towingoperator.activity;

import android.content.res.TypedArray;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import attender.oparkCard.R;
import attender.oparkCard.towingoperator.adapter.TowedvehicleAdapter;
import attender.oparkCard.base.DimensionUtil;

public class TowingOperator extends AppCompatActivity {

    private TabLayout tabLayout;
    private TextView textToolHeader;
    private ViewPager viewPager;
    private int statusBarHeight;
    private int normalWindowHeight;
    private int windowHeight;
    private RelativeLayout relativeLayoutHeader;
    TowedvehicleAdapter towedvehicleAdapter;
    private Toolbar toolBar;
    private int[] tabTitle = {R.string.towedvehicle, R.string.releasedvehicle};
    public static ArrayList<String> queryImageList = new ArrayList<String>();

   /*http://staggingapi.opark.in/index.php/v1/user/login?username=4444444444&password=1234*/
   /*http://staggingapi.opark.in/index.php/v1/user/login?username=9999999998&password=4321*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_towing_operator);

        init();
    }

    private void init() {

        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }

        final TypedArray styledAttributes = TowingOperator.this.getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.actionBarSize}
        );
        normalWindowHeight = DimensionUtil.getScreenHeight(TowingOperator.this);
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
        textToolHeader.setText("Towing Vehicle");
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
        towedvehicleAdapter = new TowedvehicleAdapter(getSupportFragmentManager(), TowingOperator.this);

        viewPager.setAdapter(towedvehicleAdapter);
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
}
