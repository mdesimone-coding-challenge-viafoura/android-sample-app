package com.viafoura.myapplication.activities.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.viafoura.myapplication.R;
import com.viafoura.myapplication.activities.base.BaseActivity;
import com.viafoura.myapplication.activities.comments.CommentsActivity;
import com.viafoura.myapplication.activities.comments.adapter.CommentsActivityIntentKeys;
import com.viafoura.myapplication.activities.components.CustomWebView;
import com.viafoura.myapplication.application.SampleApplication;
import com.viafoura.myapplication.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

    private CustomWebView mWebView;

    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainViewModel = new MainViewModel(((SampleApplication) getApplicationContext()).dependencyManager.provideAuthenticationManager());

        mWebView = findViewById(R.id.main_webview);
        mWebView.loadUrl(MainViewModel.ARTICLE_URL);

        findViewById(R.id.main_comments).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mainViewModel.isUserAuthenticated){
                    Intent intent = new Intent(getApplicationContext(), CommentsActivity.class);
                    intent.putExtra(CommentsActivityIntentKeys.ARTICLE_TITLE, MainViewModel.ARTICLE_NAME);
                    intent.putExtra(CommentsActivityIntentKeys.ARTICLE_ID, MainViewModel.ARTICLE_ID);
                    startActivity(intent);
                }
                else{
                    showLoggedOutAlert();
                }
            }
        });

        findViewById(R.id.main_logout).setVisibility(mainViewModel.isUserAuthenticated ? View.VISIBLE : View.GONE);

        findViewById(R.id.main_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainViewModel.logout(new MainViewModel.LogoutInterface() {
                    @Override
                    public void onSuccess() {
                        findViewById(R.id.main_logout).setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(MainActivity.this, "Error logging out", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showLoggedOutAlert(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setIcon(null);
        alert.setTitle("You must be logged in");
        alert.setMessage("");

        alert.setPositiveButton("Log-in", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
                showLoginAlert();
            }
        });

        alert.setNegativeButton("Sign-up", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
                showSignupAlert();
            }
        });

        alert.create();
        alert.show();
    }

    private void showSignupAlert(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        LinearLayout layoutHolder = new LinearLayout(this);
        layoutHolder.setOrientation(LinearLayout.VERTICAL);

        final EditText nameEditText = new EditText(this);
        final EditText emailEditText = new EditText(this);
        final EditText passwordEditText = new EditText(this);

        nameEditText.setHint("Name");
        emailEditText.setHint("E-mail");
        passwordEditText.setHint("Password");

        layoutHolder.addView(nameEditText);
        layoutHolder.addView(emailEditText);
        layoutHolder.addView(passwordEditText);

        alert.setView(layoutHolder);

        alert.setIcon(null);
        alert.setTitle("Sign-up");

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String name = nameEditText.getText().toString().trim();

                mainViewModel.signup(name, email, password, new MainViewModel.SignUpInterface() {
                    @Override
                    public void onSuccess() {
                        findViewById(R.id.main_logout).setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(MainActivity.this, "Error creating account", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        alert.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });

        alert.create();
        alert.show();
    }

    private void showLoginAlert(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        LinearLayout layoutHolder = new LinearLayout(this);
        layoutHolder.setOrientation(LinearLayout.VERTICAL);

        final EditText emailEditText = new EditText(this);
        final EditText passwordEditText = new EditText(this);

        emailEditText.setHint("E-mail");
        passwordEditText.setHint("Password");

        layoutHolder.addView(emailEditText);
        layoutHolder.addView(passwordEditText);

        alert.setView(layoutHolder);

        alert.setIcon(null);
        alert.setTitle("Login");

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                mainViewModel.login(email, password, new MainViewModel.LoginInterface() {
                    @Override
                    public void onSuccess() {
                        findViewById(R.id.main_logout).setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(MainActivity.this, "Error logging in", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        alert.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
        });

        alert.create();
        alert.show();
    }
}