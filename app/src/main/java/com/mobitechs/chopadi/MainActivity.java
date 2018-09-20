package com.mobitechs.chopadi;

import android.Manifest;
import android.app.backup.BackupHelper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mobitechs.chopadi.adapter.Home_Menu_Adapter;
import com.mobitechs.chopadi.backup_restore.Backup_Restore;
import com.mobitechs.chopadi.model.Home_Items;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public int[] mThumbIds;
    public String[] title;
    public int[] groupId;

    MainActivity activity;
    //    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager gridLayoutManager;

    public List<Home_Items> listItems = new ArrayList<Home_Items>();
    RecyclerView recyclerView;
    RecyclerView.Adapter reviewAdapter;
    LinearLayoutManager linearLayoutManager;

    private static final int READ_STORAGE_PERMISSION_REQUEST = 1;
    private static final int WRITE_STORAGE_PERMISSION_REQUEST = 2;
    int backupOrRestore;

    String mainFolderName = "Chopadi";
    String strDirectory;

    private AdView adView, adView1;
    SharedPreferences wmbPreference;
    String permission = "main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wmbPreference = PreferenceManager.getDefaultSharedPreferences(this);
//        ShowBannerAds();

        checkReadWritePermission();
        mThumbIds = new int[]{
                R.drawable.home_customer_list, R.drawable.home_product_list, R.drawable.home_product_add,
                R.drawable.home_bill_list, R.drawable.home_income
        };
        title = new String[]{"Customer List \n (ग्राहक सूची)", "Product List \n (वस्तुओं की सूची)", "Add Daily Bill \n (रोज खर्ची)",
                "Daily Bill Report \n (बिल विस्तार)", "My Income \n (कमाई)"};

        groupId = new int[]{1, 2, 3, 4, 5};

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        recyclerView.setHasFixedSize(true);
//        gridLayoutManager = new GridLayoutManager(this, 3);
//        gridLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(gridLayoutManager);
//        recyclerView.setNestedScrollingEnabled(false);
//
//        recyclerView.smoothScrollToPosition(0);
//        listAdapter = new Home_Menu_Adapter(listItems, activity);
//        recyclerView.setAdapter(listAdapter);

        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.smoothScrollToPosition(0);
        reviewAdapter = new Home_Menu_Adapter(listItems, activity);
        recyclerView.setAdapter(reviewAdapter);

        AddGridMenu();

//        File teaDairyFolder = new File(Environment.getExternalStorageDirectory() + mainFolderName);
//
//        File databaseFolder = new File(Environment.getExternalStorageDirectory() +mainFolderName+ "/databases");
//
//        if (!teaDairyFolder.exists()) {
//            if (teaDairyFolder.mkdir()) ; //directory is created;
//        }
//        if (!databaseFolder.exists()) {
//            if (databaseFolder.mkdir()) ; //database directory is created;
//        }

    }

    private void AddGridMenu() {

        int size = mThumbIds.length;

        for (int i = 0; i < size; i++) {
            Home_Items gridViewItems = new Home_Items();
            gridViewItems.setFirstImagePath(mThumbIds[i]);
            gridViewItems.settitle(title[i]);
            gridViewItems.setgridId(groupId[i]);
            listItems.add(gridViewItems);
        }
        reviewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {

    }

    private void ShowBannerAds() {
        String deviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        adView = (AdView) findViewById(R.id.adView);
        adView1 = (AdView) findViewById(R.id.adView1);

        AdRequest adRequest = new AdRequest.Builder()
                //   .addTestDevice(deviceId)
                .build();
        adView.loadAd(adRequest);
        adView1.loadAd(adRequest);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });
        adView1.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "You have gone through issues on maintaining a diary or records about the materials you splashed on customers on credit basis but to keep a note is  pain in the neck.We have designed this application where on an easy touch you can get a list of credit holders and get the report of where and when was the transaction happened. \n\n https://play.google.com/store/apps/details?id=com.mobitechs.chopadi");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;
            case R.id.backup:
                permission = "backupRestore";
                backupOrRestore = 0;
                checkReadWritePermission();
                return true;
            case R.id.restore:
                permission = "backupRestore";
                backupOrRestore = 1;
                checkReadWritePermission();
                return true;
            case R.id.checkUpdate:
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                return true;
            case R.id.showCaseOn:
                SharedPreferences.Editor editor = wmbPreference.edit();
                // editor.putBoolean("customerAddShowCase", true);
                editor.putBoolean("custBillReportShowCase", true);
                //editor.putBoolean("addProductShowCase", true);
                editor.putBoolean("productEditDeleteShowCase", true);
                editor.putBoolean("dateChangeShowCase", true);
                editor.putBoolean("dateMonthChangeShowCase", true);
                editor.putBoolean("billHistoryShowCase", true);
                editor.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void exportOrImportDB() {
        if (backupOrRestore == 0) {
            Backup_Restore backup_restore = new Backup_Restore();
            backup_restore.exportDB(this);
        }
        else {
            Backup_Restore backup_restore = new Backup_Restore();
            backup_restore.importDB(this);
        }
    }

    public void checkReadWritePermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestWriteStoragePermission();
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestReadStoragePermission();
            } else {
                if (!permission.equals("main")) {
                    exportOrImportDB();
                }
            }
        }
    }

    private void requestReadStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            if (!permission.equals("main")) {
                exportOrImportDB();
            }
        } else {
            // Read permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_STORAGE_PERMISSION_REQUEST);
        }
    }

    private void requestWriteStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (!permission.equals("main")) {
                exportOrImportDB();
            }

        } else {
            // Write permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_STORAGE_PERMISSION_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == WRITE_STORAGE_PERMISSION_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestReadStoragePermission();
            } else {
                Toast.makeText(this, "Write storage permission was NOT granted.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == READ_STORAGE_PERMISSION_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (!permission.equals("main")) {
                    exportOrImportDB();
                }
            } else {
                Toast.makeText(this, "Read storage permission was NOT granted.", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
