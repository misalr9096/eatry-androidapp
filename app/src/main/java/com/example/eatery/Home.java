package com.example.eatery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.eatery.databinding.ActivityHomeBinding;
import com.example.eatery.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;

public class Home extends AppCompatActivity {
      ActivityHomeBinding   binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new list());
        binding.bottom.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.menu:
                    replaceFragment(new list());
                    break;
                case R.id.payment:
                    replaceFragment(new payment());
                    break;
                case R.id.logout:
                    replaceFragment(new logout());
                    break;
            }

            return true;
        });


    }
    private  void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }

}