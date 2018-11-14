package com.mobile.vople.vople;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

public class VopleAdminEvent extends AppCompatActivity {

    private DrawerLayout AdminEventDrawer;
    private ActionBarDrawerToggle NavToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vople_admin_event);

        AdminEventDrawer = (DrawerLayout) findViewById(R.id.main_drawer);
        NavToggle = new ActionBarDrawerToggle(this, AdminEventDrawer, R.string.open, R.string.close);
        AdminEventDrawer.addDrawerListener(NavToggle);
        NavToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView NavlistView = (ListView) findViewById(R.id.Nav_ListView);

        final NavAdapter navAdapter = new NavAdapter();
        NavlistView.setAdapter(navAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (NavToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
