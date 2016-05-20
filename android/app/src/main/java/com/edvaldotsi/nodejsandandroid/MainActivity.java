package com.edvaldotsi.nodejsandandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.edvaldotsi.nodejsandandroid.model.Auth;
import com.edvaldotsi.nodejsandandroid.retrofit.AuthService;
import com.edvaldotsi.nodejsandandroid.retrofit.ServiceGenerator;
import com.edvaldotsi.nodejsandandroid.util.CircleTransform;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AbstractActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView mImageViewUserPicture;
    private TextView mTextViewUserName;
    private TextView mTextViewUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View view = navigationView.getHeaderView(0);
        mImageViewUserPicture = (ImageView) view.findViewById(R.id.image_user_picture);
        mTextViewUserName = (TextView) view.findViewById(R.id.text_user_name);
        mTextViewUserEmail = (TextView) view.findViewById(R.id.text_user_email);

        final String token = preferences.getString("token", "");
        if ("".equals(token)) {
            finish();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            Toast.makeText(getApplicationContext(), "Seu acesso expirou. Faça login novamente", Toast.LENGTH_SHORT).show();
            return;
        }

        mTextViewUserName.setText(preferences.getString("login_name", "User"));
        mTextViewUserEmail.setText(preferences.getString("login_email", "email@domain.com"));

        String email = preferences.getString("login_email", "");
        String password = preferences.getString("login_password", "");

        AuthService s = ServiceGenerator.createService(AuthService.class);
        Call<Auth> call = s.auth(email, password);
        call.enqueue(new Callback<Auth>() {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response) {
                if (response.code() == 200) {
                    LOGGED_USER = response.body().getUser();
                    Picasso.with(MainActivity.this)
                            .load("http://teste")
                            .error(R.drawable.no_profile_picture)
                            .transform(new CircleTransform())
                            .into(mImageViewUserPicture);

                    mTextViewUserName.setText(LOGGED_USER.getName());
                    mTextViewUserEmail.setText(LOGGED_USER.getEmail());

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("token", response.body().getToken());
                    editor.apply();
                }
            }

            @Override
            public void onFailure(Call<Auth> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Falha na conexão", Toast.LENGTH_SHORT).show();
            }
        });

        //startService(new Intent(this, AlertService.class));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog dialog = new AlertDialog.Builder(this)
            .setTitle("Nodejs and Android")
            .setMessage("Deseja sair do aplicativo?")
            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            })
            .setNegativeButton("Não", null)
                    .create();

            dialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_perfil:
                startActivity(new Intent(this, ProfileActivity.class));
                break;

            case R.id.nav_posts:
                startActivity(new Intent(this, PostsListActivity.class));
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
