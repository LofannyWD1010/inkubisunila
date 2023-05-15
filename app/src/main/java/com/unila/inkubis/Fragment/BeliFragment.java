package com.unila.inkubis.Fragment;


import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;

import com.unila.inkubis.MainActivity;
import com.unila.inkubis.R;
import com.unila.inkubis.SharedPrefManager;
import com.unila.inkubis.UbahProfilActivity;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class BeliFragment extends Fragment {

    LinearLayout llBantuan, llAkun, llKeluar;
    ProgressDialog progressDialog;

    public BeliFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beli, container, false);
        llBantuan = (LinearLayout) view.findViewById(R.id.ll_bantuan);
        llAkun = (LinearLayout) view.findViewById(R.id.ll_akun);
        llKeluar = (LinearLayout) view.findViewById(R.id.ll_keluar);

        llBantuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).bukaBantuan();
            }
        });

        llAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), UbahProfilActivity.class);
                startActivityForResult(i, 1);
            }
        });

        llKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Apakah anda yakin ingin keluar dari sistem ?").setTitle("Peringatan !");
                AlertDialog alertDialog = builder.create();
                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:

//                                Toast.makeText(getContext(), "Memulai Ulang Aplikasi Sikubis", Toast.LENGTH_SHORT).show();
//
//                                Intent mStartActivity = new Intent(getContext(), MainActivity.class);
//                                int mPendingIntentId = 123456;
//                                PendingIntent mPendingIntent = PendingIntent.getActivity(getContext(), mPendingIntentId, mStartActivity,
//                                        PendingIntent.FLAG_CANCEL_CURRENT);
//                                AlarmManager mgr = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
//                                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
//
//                                if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
//                                    ((ActivityManager)getContext().getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData();
//                                } else {
//                                    String packageName = getContext().getPackageName();
//                                    Runtime runtime = Runtime.getRuntime();
//                                    try {
//                                        runtime.exec("pm clear "+packageName);
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                }

//                                System.exit(0);
                                SharedPrefManager.getInstance(getContext()).clearLoggedInUser();

                                Toast.makeText(getContext(), "Memulai Ulang Aplikasi Sikubis", Toast.LENGTH_SHORT).show();

                                Intent mStartActivity = new Intent(getContext(), MainActivity.class);
                                mStartActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(mStartActivity);
//                                int mPendingIntentId = 123456;
//                                PendingIntent mPendingIntent = PendingIntent.getActivity(getContext(), mPendingIntentId, mStartActivity,
//                                        PendingIntent.FLAG_CANCEL_CURRENT);
//                                AlarmManager mgr = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
//                                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);




                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                };
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Iya", listener);
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Batal", listener);
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onShow(DialogInterface dialog) {
                        AlertDialog alertDialog = (AlertDialog) dialog;
                        Button button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                        button.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    }
                });
                alertDialog.show();

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getActivity()
                .getSupportFragmentManager()
                .findFragmentById(R.id.frame_container).onActivityResult(requestCode,resultCode,data);
    }
}
