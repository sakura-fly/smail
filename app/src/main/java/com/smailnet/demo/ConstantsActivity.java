package com.smailnet.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.smailnet.demo.com.smailnet.demo.model.Contacts;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class ConstantsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("sh.c")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constants);

        FloatingActionButton fab = findViewById(R.id.addc);
        s = findViewById(R.id.refresh);
        s.setOnRefreshListener(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ConstantsActivity.this, CAddActivity.class));
            }
        });

        cList();
    }

    public void cList() {
        Realm mRealm = Realm.getDefaultInstance();

        RealmResults<Contacts> c = mRealm.where(Contacts.class).findAll();
        List<Contacts> l = mRealm.copyFromRealm(c);
        Log.i("l", l.toString());

    }

    @Override
    public void onRefresh() {
        cList();
    }
}
