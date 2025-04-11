package com.example.OQC;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.pm.PackageInfoCompat;

import com.example.OQC.Database.TableMain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends AppCompatActivity {
    Button btnlogin, btnback, btnlanguage;
    EditText editID, editPassword;
    CheckBox onlinecheck, SaveCheck;
    TextView tv_ver,signIn;
    String TABLE_NAME = "acc_table";
    String accID = "accID";
    String pass = "pass";
    String g_package = "";
    String ID, PASSWORD;
    private SQLiteDatabase db = null;
    private TableMain Cre_db = null;
    private SetLanguage setL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setL = new SetLanguage(this); // Truyền context vào SetLanguage
        setL.SetLanguage(); // Thiết lập ngôn ngữ trước khi setContentView
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ActionBar actionBar = getSupportActionBar();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (actionBar != null) {
            actionBar.hide(); // Ẩn ActionBar
        }



        addControls();
        addEvents();

    }



    private void addControls() {
        Cre_db = new TableMain(this);
        CardView cardView = findViewById(R.id.loginCard);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        cardView.startAnimation(slideUp);
        btnlogin = (Button) findViewById(R.id.btnLogin);
        btnback = (Button) findViewById(R.id.btnback);
        btnlanguage = (Button) findViewById(R.id.btnlanguage);
        editID = (EditText) findViewById(R.id.editID);
        editPassword = (EditText) findViewById(R.id.editPassword);

        SaveCheck = (CheckBox) findViewById(R.id.SaveCheck);
        tv_ver = (TextView) findViewById(R.id.tv_ver);
        btnlanguage.setOnClickListener(btnlanguageListener);
        btnlogin.setOnClickListener(btnloginListener);
        btnback.setOnClickListener(btnbackListener);
        signIn = (TextView) findViewById(R.id.SignIn);
        signIn.setOnClickListener(text_signUp);
    }
    private void addEvents() {
        Cre_db.open();

        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + accID + " TEXT," + pass + " TEXT)";
        db = getApplicationContext().openOrCreateDatabase("Main.db", 0, null);
        try {
            db.execSQL(CREATE_TABLE);
        } catch (Exception e) {
        }
        Cursor c = db.rawQuery("SELECT accID,pass FROM " + TABLE_NAME + "", null);
        c.moveToFirst();
        int l_cn = c.getCount();
        if (l_cn > 0) {
            editID.setText(c.getString(0));
            editPassword.setText(c.getString(1));
            SaveCheck.setChecked(true);
        } else {
            editID.setText("");
            editPassword.setText("");
            SaveCheck.setChecked(false);
        }

            try {
                PackageManager pm = this.getPackageManager();
                g_package = getPackageName();
                PackageInfo pInfo = pm.getPackageInfo(g_package, 0);



                long verCode = PackageInfoCompat.getLongVersionCode(pInfo); // Sử dụng cho Android mới

                tv_ver.setText("SV: " + Constant_Class.server + " | VerCode: " + verCode );
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                tv_ver.setText("Không lấy được phiên bản");
            }

    }



    private View.OnClickListener btnloginListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ID = editID.getText().toString();
            PASSWORD = editPassword.getText().toString();
            login("http://172.16.40.20/" + Constant_Class.server + "/loginJson.php?ID=" + ID + "&PASSWORD=" + PASSWORD);
        }
    };

    private View.OnClickListener btnbackListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    };
    private View.OnClickListener text_signUp = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.sign_up_layout, null);

            final EditText id_sign_up = view.findViewById(R.id.accountEditText);
            final EditText pass_sign_up = view.findViewById(R.id.passwordEditText);
            final EditText pass_confirm_sign_up = view.findViewById(R.id.passwordconfirmEditText); // Sửa ID ở đây
            final EditText depart_sign_up = view.findViewById(R.id.departmentEditText);
            final EditText email_sign_up = view.findViewById(R.id.MailEditText);
            Button btnRegister = view.findViewById(R.id.btnRegister);

            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = id_sign_up.getText().toString();
                    String pass = pass_sign_up.getText().toString();
                    String pass_confirm = pass_confirm_sign_up.getText().toString();
                    String depart = depart_sign_up.getText().toString();
                    String email = email_sign_up.getText().toString();

                    // Các hành động tiếp theo sau khi thu thập dữ liệu từ người dùng
                }
            });

            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this, R.style.TransparentDialog);;

            builder.setView(view);
            builder.show();
        }
    };
    private Button.OnClickListener btnlanguageListener = new Button.OnClickListener() {
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
            builder.setSingleChoiceItems(new String[]{"中文", "Tiếng Việt"},
                    getSharedPreferences("Language", Context.MODE_PRIVATE).getInt("Language", 0),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {

                            SharedPreferences preferences = getSharedPreferences("Language", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putInt("Language", i);
                            editor.apply();
                            dialogInterface.dismiss();

                            //重新載入APP
                            Intent intent = new Intent(Login.this, Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    };

    private void login(String apiurl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(apiurl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(10000);
                    conn.setReadTimeout(10000);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    //conn.connect();
                    // Kiểm tra kết nối trước khi thực hiện kết nối
                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        String result = reader.readLine();
                        reader.close();
                        // Xử lý dữ liệu kết quả ở đây
                        if (result.contains("PASS")) {
                            if (SaveCheck.isChecked()) {
                                db.execSQL("DELETE FROM " + TABLE_NAME + "");
                                ContentValues args = new ContentValues();
                                args.put(accID, ID);
                                args.put(pass, PASSWORD);
                                db.insert(TABLE_NAME, null, args);
                            } else {
                                db.execSQL("DELETE FROM " + TABLE_NAME + "");
                            }

                            try {
                                JSONArray jsonarray = new JSONArray(result);
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    JSONObject jsonObject = jsonarray.getJSONObject(i);
                                    Constant_Class.UserXuong = jsonObject.getString("TC_QRH003");
                                    Constant_Class.UserKhau = jsonObject.getString("TC_QRH005");
                                    Constant_Class.UserTramQR = jsonObject.getString("TC_QRH006");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Intent login = new Intent();
                            login.setClass(Login.this, MainActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("ID", editID.getText().toString());
                            login.putExtras(bundle);
                            startActivity(login);
                        } else if (result.contains("FALSE")) {
                            runOnUiThread(new Runnable() {
                                @Override

                                public void run() {
                                    Toast alert = Toast.makeText(Login.this, getString(R.string.main_E02), Toast.LENGTH_LONG);
                                    alert.show();
                                }
                            });
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast alert = Toast.makeText(Login.this, getString(R.string.main_E03), Toast.LENGTH_LONG);
                                //alert.show();
                                int g_check = Cre_db.checkUserData(editID.getText().toString().trim());

                                if (g_check == 0) {
                                    Toast alert = Toast.makeText(Login.this, getString(R.string.main_E02), Toast.LENGTH_LONG);
                                    alert.show();
                                } else {
                                    Intent login = new Intent();
                                    login.setClass(Login.this, MainActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("ID", editID.getText().toString());
                                    login.putExtras(bundle);
                                    startActivity(login);
                                }
                            }
                        });
                    }
                    /*if (result.contains("PASS")) {
                        if (SaveCheck.isChecked()) {
                            db.execSQL("DELETE FROM " + TABLE_NAME + "");
                            ContentValues args = new ContentValues();
                            args.put(accID, ID);
                            args.put(pass, PASSWORD);
                            db.insert(TABLE_NAME, null, args);
                        } else {
                            db.execSQL("DELETE FROM " + TABLE_NAME + "");
                        }

                        try {
                            JSONArray jsonarray = new JSONArray(result);
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonObject = jsonarray.getJSONObject(i);
                                Constant_Class.UserXuong = jsonObject.getString("TC_QRH003");
                                Constant_Class.UserKhau = jsonObject.getString("TC_QRH005");
                                Constant_Class.UserTramQR = jsonObject.getString("TC_QRH006");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Intent login = new Intent();
                        login.setClass(Login.this, Menu.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("ID", editID.getText().toString());
                        login.putExtras(bundle);
                        startActivity(login);
                    } else if (result.contains("FALSE")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast alert = Toast.makeText(Login.this, getString(R.string.main_E02), Toast.LENGTH_LONG);
                                alert.show();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast alert = Toast.makeText(Login.this, getString(R.string.main_E03), Toast.LENGTH_LONG);
                                //alert.show();
                                int g_check = Cre_db.checkUserData(editID.getText().toString().trim());
                                if (g_check == 0) {
                                    Toast alert = Toast.makeText(Login.this, getString(R.string.main_E02), Toast.LENGTH_LONG);
                                    alert.show();
                                } else {
                                    Intent login = new Intent();
                                    login.setClass(Login.this, Menu.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("ID", editID.getText().toString());
                                    login.putExtras(bundle);
                                    startActivity(login);
                                }
                            }
                        });
                    }*/
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast alert = Toast.makeText(Login.this, getString(R.string.main_E03), Toast.LENGTH_LONG);
                            //alert.show();
                            int g_check = Cre_db.checkUserData(editID.getText().toString().trim());
                            if (g_check == 0) {
                                Toast alert = Toast.makeText(Login.this, getString(R.string.main_E02), Toast.LENGTH_LONG);
                                alert.show();
                            } else {
                                Intent main = new Intent();
                                main.setClass(Login.this, MainActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("ID", editID.getText().toString());
                                main.putExtras(bundle);
                                startActivity(main);
                            }
                        }
                    });
                }
            }
        }).start();
    }
}
