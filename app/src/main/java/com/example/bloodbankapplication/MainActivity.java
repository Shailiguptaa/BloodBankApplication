package com.example.bloodbankapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bloodbankapplication.Adaptor.UserAdaptor;
import com.example.bloodbankapplication.Model.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private CircleImageView navigationUserImage;
    private TextView navigationUserFullName, navigationUserEmail, navigationUserBloodGroup, navigationUserType;
    private DatabaseReference userRef;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<User> userList;
    private UserAdaptor userAdaptor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);
        setSupportActionBar(toolbar);


        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);
        navigationUserFullName = navigationView.getHeaderView(0).findViewById(R.id.navigationUserFullName);
        navigationUserEmail = navigationView.getHeaderView(0).findViewById(R.id.navigationUserEmail);
        navigationUserImage = navigationView.getHeaderView(0).findViewById(R.id.navigationUserImage);
        navigationUserBloodGroup = navigationView.getHeaderView(0).findViewById(R.id.navigationUserBloodGroup);
        navigationUserType = navigationView.getHeaderView(0).findViewById(R.id.navigationUserType);
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        userList = new ArrayList<>();
        userAdaptor = new UserAdaptor(MainActivity.this, userList);
        recyclerView.setAdapter(userAdaptor);



        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue().toString();
                    navigationUserFullName.setText(name);

                    String email = snapshot.child("email").getValue().toString();
                    navigationUserEmail.setText(email);

                    String bloodgroup = snapshot.child("bloodgroup").getValue().toString();
                    navigationUserBloodGroup.setText(bloodgroup);

                    String type = snapshot.child("type").getValue().toString();
                    navigationUserType.setText(type);
                    if(type.equals("donor")){
                        readRecipients();
                    }else{
                        readDonors();
                    }


                    if(snapshot.hasChild("profilepictureurl")){
                        String profilrpictureurl = snapshot.child("profilepictureurl").getValue().toString();
                        Glide.with(getApplicationContext()).load(profilrpictureurl).into(navigationUserImage);
                    }else{
                        navigationUserImage.setImageResource(R.drawable.bbprofile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

    }

    private void readRecipients() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = reference.orderByChild("type").equalTo("recipient");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    userList.add(user);
                }
                userAdaptor.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

                if(userList.isEmpty()){
                    Toast.makeText(MainActivity.this, "No recipients", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void readDonors() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = reference.orderByChild("type").equalTo("donor");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    userList.add(user);
                }
                userAdaptor.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

                if(userList.isEmpty()){
                    Toast.makeText(MainActivity.this, "No donors", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
        public boolean onNavigationItemSelected (@NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.profile:
                Intent intent = new Intent(MainActivity.this, profileActivity.class);
                startActivity(intent);
                break;
            case R.id.aplus:
                Intent intent1 = new Intent(MainActivity.this, categorySelectedActivity.class);
                intent1.putExtra("group", "A+");
                startActivity(intent1);
                break;
            case(R.id.logout):
                FirebaseAuth.getInstance().signOut();
                Intent intent2 = new Intent(MainActivity.this, loginActivity.class);
                startActivity(intent2);
            case R.id.Opositive:
                Intent intent3 = new Intent(MainActivity.this, categorySelectedActivity.class);
                intent3.putExtra("group", "O+");
                startActivity(intent3);
                break;
            case R.id.bpositive:
                Intent intent4 = new Intent(MainActivity.this, categorySelectedActivity.class);
                intent4.putExtra("group", "B+");
                startActivity(intent4);
                break;
            case R.id.abpositive:
                Intent intent5 = new Intent(MainActivity.this, categorySelectedActivity.class);
                intent5.putExtra("group", "AB+");
                startActivity(intent5);
                break;
            case R.id.anegative:
                Intent intent6 = new Intent(MainActivity.this, categorySelectedActivity.class);
                intent6.putExtra("group", "A-");
                startActivity(intent6);
                break;
            case R.id.onegative:
                Intent intent7 = new Intent(MainActivity.this, categorySelectedActivity.class);
                intent7.putExtra("group", "O-");
                startActivity(intent7);
                break;
            case R.id.bnegative:
                Intent intent8 = new Intent(MainActivity.this, categorySelectedActivity.class);
                intent8.putExtra("group", "B-");
                startActivity(intent8);
                break;
            case R.id.abnegative:
                Intent intent9 = new Intent(MainActivity.this, categorySelectedActivity.class);
                intent9.putExtra("group", "AB-");
                startActivity(intent9);
                break;
            case R.id.compatible:
                Intent intent10 = new Intent(MainActivity.this, categorySelectedActivity.class);
                intent10.putExtra("group", "Compatible with me");
                startActivity(intent10);
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
    }
