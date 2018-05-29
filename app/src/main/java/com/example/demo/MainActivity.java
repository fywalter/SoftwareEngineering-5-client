package com.example.demo;

import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.content.Context;
import android.support.design.widget.NavigationView;
import android.content.Intent;
import android.view.ViewGroup;

import com.example.demo.adapter.MyAdapter;
import com.example.demo.adapter.NewsTitleAdapter;
import com.example.demo.model.NewsTitle;
import com.example.demo.model.Connection;
import com.example.demo.utils.NonSlideLinearLayoutManager;
import com.example.demo.model.User;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import android.os.AsyncTask;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public static Context mcontext;
    private NewsTitleTask newsTitleTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mcontext = this;
        Log.i("DevID",User.getInstance().getDevID());
        newsTitleTask = new NewsTitleTask();
        newsTitleTask.execute();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private List<NewsTitle> ntList = null;
        private ListView newsListView;
        private MyAdapter<NewsTitle> myAdapter = null;


        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static MainActivity.PlaceholderFragment newInstance(int sectionNumber) {
            MainActivity.PlaceholderFragment fragment = new MainActivity.PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        private void initNews(){
            ntList = new ArrayList<>();
            for(int i = 0; i < 5; i++){
                NewsTitle nt = new NewsTitle("Trump Is Dust", "CNN", "2018-05-23");
                ntList.add(nt);
                NewsTitle nt2 = new NewsTitle("Tobu Railway Suffered From Human Accidents", "Japan Times", "2018-05-22");
                ntList.add(nt2);
                NewsTitle nt3 = new NewsTitle("Will Xi Jinping Change China?", "The Economist", "2018-05-21");
                ntList.add(nt3);
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_news_categorized, container, false);
            initNews();
            RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
            NonSlideLinearLayoutManager layoutManager = new NonSlideLinearLayoutManager (getActivity());
            layoutManager.setScrollEnabled(false);
            recyclerView.setLayoutManager(layoutManager);
            NewsTitleAdapter nta = new NewsTitleAdapter(ntList, getContext());
            recyclerView.setAdapter(nta);
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return MainActivity.PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_wordlist) {
            Intent intent = new Intent(MainActivity.this, WordListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class NewsTitleTask extends AsyncTask<Void,Void,ArrayList<NewsTitle>> {

        public NewsTitleTask() { }

        @Override
        protected ArrayList<NewsTitle> doInBackground(Void... params) {
            return Connection.getNewsList();
        }

        @Override
        protected void onPostExecute(ArrayList<NewsTitle> newsList) {
            super.onPostExecute(newsList);
            User.getInstance().setNewsTitleList(newsList);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            newsTitleTask = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        newsTitleTask.cancel(true);
    }
}
