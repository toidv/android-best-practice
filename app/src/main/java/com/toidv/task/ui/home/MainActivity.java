package com.toidv.task.ui.home;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.toidv.task.R;
import com.toidv.task.consts.Consts;
import com.toidv.task.ui.adapter.Adapter;
import com.toidv.task.ui.base.BaseActivityWithDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivityWithDialog implements
        NavigationView.OnNavigationItemSelectedListener,
        MainActivityMvpView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    @Inject
    MainActivityPresenter mainActivityPresenter;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        mainActivityPresenter.attachView(this);
        title = getString(R.string.title_main);
        setSupportActionBar(toolbar);
        setupDrawerLayout();
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        getIntialList();


    }

    private void getIntialList() {
        mainActivityPresenter.getInitialList();
    }

    @Override
    protected void setupDialogTitle() {
        alertDialog.setTitle(title);
        progressDialog.setTitle(title);
    }

    private void setupDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.statistics_navigation_menu_item) {
            return true;
        }
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    public FloatingActionButton getFAB() {
        return this.fab;
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
    protected void onDestroy() {

        super.onDestroy();
    }


    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(TaskFragment.newInstance(Consts.TASK_STATE_PENDING), "Pending");
        adapter.addFragment(TaskFragment.newInstance(Consts.TASK_STATE_DONE), "Done");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
    }
}
