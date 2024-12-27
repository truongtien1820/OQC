package com.example.laprap001;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.laprap001.Interface.Change_fragment;
import com.example.laprap001.PageFragment.PageDetailPOFragment;
import com.example.laprap001.PageFragment.PageHomeFragment;
import com.example.laprap001.PageFragment.PageSearchFragment;
import com.nafis.bottomnavigation.NafisBottomNavigation;

import android.widget.ImageView;
import android.widget.Toast;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class MainActivity extends AppCompatActivity implements Change_fragment {
    private ImageView imageView;
    private String ID, PO_ID, PO_NO;
    private static NafisBottomNavigation bottomNavigation;
    private static final int ID_HOME = 1;
    private static final int ID_SEARCH = 2;
    private static final int ID_DETAIL_PO = 3;
    private static final int ID_ACCOUNT = 5;
    private Fragment currentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);

        Bundle buldle = getIntent().getExtras();
        ID = buldle.getString("ID",ID);
        addControls();
        addEvents();

//        bottomNavigation.setCount(ID_HOME, "115");

        bottomNavigation.setOnShowListener(new Function1<NafisBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(NafisBottomNavigation.Model model) {
                String name = "";
                if (model.getId() == ID_HOME) {
                    name = "HOME";

                    replaceFragment(new PageHomeFragment());
                } else if (model.getId() == ID_SEARCH) {
                    name = "SEARCH";
                    replaceFragment(new PageSearchFragment());
                }else if (model.getId() == ID_DETAIL_PO) {
                    name = "CHOICE";
                    replaceFragment(new PageDetailPOFragment());
                }


                return null;
            }
        });
        bottomNavigation.setOnClickMenuListener(new Function1<NafisBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(NafisBottomNavigation.Model model) {
                String name = "";

                if (model.getId() == ID_HOME) {
                    name = "HOME";
                } else if (model.getId() == ID_SEARCH) {
                    name = "SEARCH";
                } else if (model.getId() == ID_DETAIL_PO) {
                    name = "ADD";
                }
                return null;
            }
        });

        bottomNavigation.setOnReselectListener(new Function1<NafisBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(NafisBottomNavigation.Model model) {
                Toast.makeText(MainActivity.this, "Item " + model.getId() + " is reselected.", Toast.LENGTH_LONG).show();
                return null;
            }
        });


        bottomNavigation.show(ID_HOME,true);


//
    }

    private void addEvents() {
        bottomNavigation.add(new NafisBottomNavigation.Model(ID_HOME, R.drawable.ic_home));
        bottomNavigation.add(new NafisBottomNavigation.Model(ID_SEARCH, R.drawable.ic_search));
        bottomNavigation.add(new NafisBottomNavigation.Model(ID_DETAIL_PO, R.drawable.ic_detail_po));

    }

    private void addControls(){
        imageView = findViewById(R.id.ima);
        bottomNavigation = findViewById(R.id.bottomNavigation);



    }




    // Animation Transformer

    private void replaceFragment(Fragment fragment) {

        if (currentFragment != null && currentFragment.getClass().equals(fragment.getClass())) {
            return;
        }
        currentFragment = fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fragment_enter,0);
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        Bundle bundle = new Bundle();
        bundle.putString("ID", ID);
        bundle.putString("PO_ID", PO_ID);
        bundle.putString("PO_NO", PO_NO);
        fragment.setArguments(bundle);
        fragmentTransaction.commit();
    }

    @Override
    public void ChangeFragment(String idfragment, String PO_ID, String PO_NO) {
        this.PO_ID = PO_ID;
        this.PO_NO = PO_NO;
        bottomNavigation.show(Integer.parseInt(idfragment),true);

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}