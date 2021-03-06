package com.blazingapps.asus.sanskriti19;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

public class HomeActivity extends AppCompatActivity {

    static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
    private static final String TAG = "TAGZ"
            ;
    private RecyclerView recyclerView;
    TextView team1,team2,team3,team4;
    TextView score1,score2,score3,score4;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseDatabase database;
    DatabaseReference myRef;
    private EventsAdapter mAdapter;
    LinearLayout l1,l2,l3,l4;
    ImageButton radio, star;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        database = FirebaseDatabase.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);

        if (!isInternetAvailable()){
            Toast.makeText(getApplicationContext(),"Turn on internet to get updated events",Toast.LENGTH_LONG).show();
        }

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        myRef = database.getReference("events");;
        l1= findViewById(R.id.first_layout);
        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bounceanim(l1);
            }
        });
        l2= findViewById(R.id.l2);
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bounceanim(l2);
            }
        });
        l3= findViewById(R.id.l3);
        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bounceanim(l3);
            }
        });
        l4= findViewById(R.id.l4);
        l4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bounceanim(l4);
            }
        });

        star = findViewById(R.id.star);
        radio = findViewById(R.id.radio);

        entryScoreAnim(l1);
        moveup(l2);
        moveright(l3);
        movedown(l4);

        recyclerView = findViewById(R.id.my_recycler_view);
        team1 = findViewById(R.id.team1);
        team2 = findViewById(R.id.team2);
        team3 = findViewById(R.id.team3);
        team4 = findViewById(R.id.team4);
        score1 = findViewById(R.id.score1);
        score2 = findViewById(R.id.score2);
        score3 = findViewById(R.id.score3);
        score4 = findViewById(R.id.score4);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareCompat.IntentBuilder.from(HomeActivity.this)
                        .setType("text/plain")
                        .setChooserTitle("Chooser title")
                        .setText("Try out Sanskriti19 App\nYour Sanskriti companion\n"
                                +"Mar Athanasius collage of engineering Arts fest\n"+"http://play.google.com/store/apps/details?id=" + HomeActivity.this.getPackageName())
                        .startChooser();
            }
        });
        db.collection("scoreboard").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                List<TeamObj> teams = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    if (doc.get("name") != null) {
                        String name = doc.getString("name");
                        double score = doc.getDouble("score");
                        teams.add(new TeamObj(name,score));
                    }
                }

                updateScore(teams);
            }
        });

//        db.collection("events").addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
//                if (e != null) {
//                    Log.w(TAG, "Listen failed.", e);
//                    return;
//                }
//
//                ArrayList<EventItem> eventItems = new ArrayList<>();
//
//                for (QueryDocumentSnapshot doc : snapshots) {
//                    if (doc.get("name") != null) {
//                        String name = doc.getString("name");
//                        String desc = doc.getString("desc");
//                        String url = doc.getString("url");
//                        String ticketurl = ""+doc.getString("ticket_url");
////                        HashMap<String, String> map = new HashMap<>();
////                        map.put("name",name);
////                        map.put("desc",desc);
////                        map.put("url",url);
////                        map.put("ticket_url",ticketurl);
////                        myRef.child(doc.getId()).setValue(map);
////                        eventItems.add(new EventItem(name,name,desc,url,ticketurl));
//                    }
//                }
////                mAdapter = new EventsAdapter(eventItems, getApplicationContext());
////                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
////                recyclerView.setLayoutManager(mLayoutManager);
////                recyclerView.setItemAnimator(new DefaultItemAnimator());
////                recyclerView.setAdapter(mAdapter);
//            }
//        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<EventItem> eventItems = new ArrayList<>();
                for (DataSnapshot item: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    String name = item.child("name").getValue(String.class);
                    Log.d(TAG,name);
                    String desc = item.child("desc").getValue(String.class);
                    String url = item.child("url").getValue(String.class);
                    String ticketurl = ""+item.child("ticket_url").getValue(String.class);
                    eventItems.add(new EventItem(name,name,desc,url,ticketurl));
                }
                mAdapter = new EventsAdapter(eventItems, getApplicationContext());
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

    }

    public void gotoNotification(View view) {
        startActivity(new Intent(this,NotificationActivity.class));
    }

    public void gotoDev(View view) {
        startActivity(new Intent(this, DevActivity.class));
    }

    public void gotoProfile(View view) {
        startActivity(new Intent(this,ProfileActivity.class));
    }

    public void updateScore(List<TeamObj> teams){

//        List<TeamObj> sortedTeams = new ArrayList<>();
        for (int i = 0; i<teams.size()-1 ; ++i){
            for (int j=i+1; j<teams.size();++j){
                if (teams.get(i).getScore() < teams.get(j).getScore())  {
                    TeamObj temp = new TeamObj(teams.get(i));
                    teams.get(i).setObj(teams.get(j));
                    teams.get(j).setObj(temp);
                }
            }
        }

        for (TeamObj obj : teams){
            Log.d("TAGZ",String.valueOf(obj.getScore()));
        }
        team1.setText(teams.get(0).getName());
        team2.setText(teams.get(1).getName());
        team3.setText(teams.get(2).getName());
        team4.setText(teams.get(3).getName());

        score1.setText(String.valueOf(teams.get(0).getScore()));
        score2.setText(String.valueOf(teams.get(1).getScore()));
        score3.setText(String.valueOf(teams.get(2).getScore()));
        score4.setText(String.valueOf(teams.get(3).getScore()));
    }

    public void sansMusic(View view) {
        highbounceanim(view);
        startActivity(new Intent(this,RadioActivity.class));
    }

    public void staredactivity(View view) {
        highbounceanim(view);
        startActivity(new Intent(this,StarredActivity.class));
    }

    public void bounceanim(LinearLayout view) {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bounce);
        view.startAnimation(animation);
    }
    public void highbounceanim(View view) {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.highbounce);
        view.startAnimation(animation);
    }

    public void entryScoreAnim(LinearLayout view) {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.score_entry);
        view.startAnimation(animation);
    }
    public void moveup(LinearLayout view) {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.moveup);
        view.startAnimation(animation);
    }
    public void movedown(LinearLayout view) {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.movedown);
        view.startAnimation(animation);
    }
    public void moveright(LinearLayout view) {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.moveright);
        view.startAnimation(animation);
    }
    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }
}
