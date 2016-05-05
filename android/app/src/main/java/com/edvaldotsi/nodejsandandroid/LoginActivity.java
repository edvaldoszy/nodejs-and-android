package com.edvaldotsi.nodejsandandroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.edvaldotsi.nodejsandandroid.model.User;
import com.edvaldotsi.nodejsandandroid.model.UserAuth;
import com.edvaldotsi.nodejsandandroid.retrofit.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AbstractActivity {

    private CheckBox checkboxRemember;
    private EditText editEmail;
    private EditText editPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkboxRemember = (CheckBox) findViewById(R.id.checkbox_remember);
        editEmail = (EditText) findViewById(R.id.edit_email);
        editPassword = (EditText) findViewById(R.id.edit_password);

        editEmail.setText(preferences.getString("email", ""));
        editPassword.setText(preferences.getString("password", ""));

        Button buttonSignin = (Button) findViewById(R.id.button_signin);
        buttonSignin.setOnClickListener(new ButtonSigninClickListener());

        /* Usuário fictício */
        LOGGED_USER = new User(2, "Edvaldo Szymonek", "edvaldoszy@gmail.com");
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("admin", true);
        startActivity(intent);
    }

    private class ButtonSigninClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("Carregando...");
            dialog.show();

            UserService service = createService(UserService.class);
            Call<UserAuth> caller = service.auth(editEmail.getText().toString(), editPassword.getText().toString());
            caller.enqueue(new Callback<UserAuth>() {
                @Override
                public void onResponse(Call<UserAuth> call, Response<UserAuth> response) {
                    dialog.dismiss();

                    if (response.code() == 200) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("token", response.body().getToken());

                        /* Store user credentials into the phone */
                        if (checkboxRemember.isChecked()) {
                            editor.putString("email", editEmail.getText().toString());
                            editor.putString("password", editPassword.getText().toString());
                        }
                        editor.apply();

                        finish();
                        startMainActivity();
                    } else {
                        Toast.makeText(LoginActivity.this, "Credenciais inválidas", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserAuth> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Falhou, cara =(", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }
    }
}
