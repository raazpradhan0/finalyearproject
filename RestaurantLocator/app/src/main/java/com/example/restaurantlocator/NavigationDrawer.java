package com.example.restaurantlocator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.restaurantlocator.Model.Restaurant;
import com.example.restaurantlocator.Model.User;
import com.example.restaurantlocator.ViewHolder.RestaurantViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class NavigationDrawer extends AppCompatActivity {
    private DrawerLayout mDrawableLayout;
    private ActionBarDrawerToggle mToggle;

    private DatabaseReference ResturantRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        mDrawableLayout =(DrawerLayout) findViewById(R.id.navigationDrawer);
        mToggle = new ActionBarDrawerToggle(this,mDrawableLayout,R.string.open,R.string.close);
        mDrawableLayout.addDrawerListener(mToggle);
        NavigationView nvDrawer =(NavigationView)  findViewById(R.id.nv);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupDrawersContent(nvDrawer);
        FragmentManager manager =getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fLcontent, new Home()).commit();

        View headerView = nvDrawer.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = headerView.findViewById(R.id.profile_image);


        userNameTextView.setText(User.getUsername());
        Picasso.get().load(User.getImage()).placeholder(R.drawable.profile);

    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Restaurant> options =  new FirebaseRecyclerOptions
                .Builder<Restaurant>()
                .setQuery(ResturantRef,Restaurant.class)
                .build();
        FirebaseRecyclerAdapter<Restaurant, RestaurantViewHolder> adapter = new FirebaseRecyclerAdapter<Restaurant, RestaurantViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position, @NonNull Restaurant model)
            {
                holder.txtRestauranttName.setText(model.getRname());
                holder.txtRestaurantDescription.setText(model.getRdetails());
                holder.txtRestaurantCatagory.setText(model.getCatagory());
                Picasso.get().load(model.getImage()).into(holder.imageView);

            }

            @NonNull
            @Override
            public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_item_layout,parent,false);
                RestaurantViewHolder holder = new RestaurantViewHolder(view);
                return holder;
            }
        };

/*        recyclerView.setAdapter(adapter);
        adapter.startListening();*/



    }

    public void  selectItemDrawer(MenuItem menuItem)
    {
        Fragment myFragment = null;
        Class fragmentClass = null;
        switch (menuItem.getItemId()){
            case R.id.home:
                fragmentClass = Home.class;
                Home home = new Home();
                FragmentManager manager1 = getSupportFragmentManager();
                manager1.beginTransaction().replace(R.id.fLcontent,home).commit();
                break;
            case R.id.categories:
                fragmentClass = Catagories.class;
                break;

            case R.id.map:
                MapsActivity map = new MapsActivity();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.fLcontent,map).commit();
                break;

            case R.id.search:
                fragmentClass = Search.class;
                break;

            case  R.id.profile:
                Profile profile = new Profile();
                FragmentManager manager2 = getSupportFragmentManager();
                manager2.beginTransaction().replace(R.id.fLcontent,profile).commit();
                break;

            case R.id.settings:
                fragmentClass = Settings.class;
                break;

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent= new Intent(NavigationDrawer.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

                break;

                default:
                    fragmentClass = Home.class;

        }

        try{
            myFragment =(Fragment) fragmentClass.newInstance();



        }
        catch (Exception e)
        {
            e.printStackTrace();

        }
        //FragmentManager fragmentManager = getSupportFragmentManager();
       //fragmentManager.beginTransaction().replace(R.id.fLcontent,myFragment).commit();
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawableLayout.closeDrawers();

    }
    private void setupDrawersContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                selectItemDrawer(menuItem);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
       if(mToggle.onOptionsItemSelected(menuItem))
       {
           return true;
       }
        return super.onOptionsItemSelected(menuItem);

    }


    public void onClick(View view) {


    }
}

