package com.example.OQC.Upload;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.OQC.Constant_Class;
import com.example.OQC.Database.TableImageCheck;
import com.example.OQC.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UploadDialog {
    private TableImageCheck db = null;
    private Context context;
    private Dialog dialog;
    private TextView tv_bdate, tv_edate, tv_processing, tv_processMaxValues, tv_updStatus;
    private Spinner sp_POID, sp_PONO;
    private ProgressBar progBar_upload;
    private Button btnOk, btnCancel;
    private LinearLayout linearLayout_process;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
    Cursor cur_getdata;
    List<String> POID_List, PONO_List;
    ArrayAdapter<String> POID_adapter, PONO_adapter;
    String g_today;

    public UploadDialog(Context context) {
        this.context = context;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.data_upload_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        db = new TableImageCheck(context);
        addControls();
        addEvents();
    }

    private void addControls() {
        tv_bdate = dialog.findViewById(R.id.tv_bdate);
        tv_edate = dialog.findViewById(R.id.tv_edate);
        tv_processing = dialog.findViewById(R.id.tv_processing);
        tv_processMaxValues = dialog.findViewById(R.id.tv_processMaxValues);
        tv_updStatus = dialog.findViewById(R.id.tv_updStatus);
        progBar_upload = dialog.findViewById(R.id.progBar_upload);
        sp_POID = dialog.findViewById(R.id.sp_POID);
        sp_PONO = dialog.findViewById(R.id.sp_PONO);
        btnOk = dialog.findViewById(R.id.btnOk);
        btnCancel = dialog.findViewById(R.id.btnCancel);
        linearLayout_process = dialog.findViewById(R.id.linear_processing);

        g_today = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
        tv_bdate.setHint(g_today);
        tv_edate.setHint(g_today);

        POID_List = new ArrayList<>();
        PONO_List = new ArrayList<>();
        POID_List.add("");

        Cursor cur = db.getData("tc_imgcheck_file", "Distinct TC_IMG001", "TC_IMG007 <> 'Y'");
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            try {
                do {
                    POID_List.add(cur.getString(cur.getColumnIndexOrThrow("TC_IMG001")));
                } while (cur.moveToNext());
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                cur.close();
            }
        }
        POID_adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, POID_List);
        PONO_adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, PONO_List);
        sp_POID.setAdapter(POID_adapter);
        sp_PONO.setAdapter(PONO_adapter);

        setStatus("0");
    }

    private void addEvents() {
        tv_bdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerTransferDialog(tv_bdate);
            }
        });

        tv_edate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerTransferDialog(tv_edate);
            }
        });

        sp_POID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               String selectedPOID = sp_POID.getSelectedItem().toString();
                if (!selectedPOID.equals("")) {
                    PONO_List.clear();
                    PONO_List.add("");
                    Cursor cur = db.getData("tc_imgcheck_file", "DISTINCT TC_IMG002", "TC_IMG001 = '" + selectedPOID + "'");
                    if (cur.getCount() > 0) {
                        cur.moveToFirst();
                        try{
                            do {
                                PONO_List.add(cur.getString(cur.getColumnIndexOrThrow("TC_IMG002")));
                                PONO_adapter.notifyDataSetChanged();
                            }while (cur.moveToNext());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }finally {
                            cur.close();
                        }
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showDatePickerTransferDialog(TextView tv_var) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(dialog.getContext());
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Xử lý khi người dùng chọn ngày
                String selectedDate = String.format("%04d/%02d/%02d", year, month + 1, dayOfMonth);
                if (tv_var == tv_bdate) {
                    if (tv_edate.getText().toString() == "") {
                        tv_var.setText(selectedDate);
                        tv_edate.setText(selectedDate);
                    } else {
                        try {
                            if (dateFormat.parse(selectedDate).after(dateFormat.parse(tv_edate.getText().toString()))) {
                                tv_var.setText(tv_edate.getText().toString());
                            } else {
                                tv_var.setText(selectedDate);
                            }
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                if (tv_var == tv_edate) {
                    if (tv_bdate.getText().toString() == "") {
                        tv_var.setText(selectedDate);
                        tv_bdate.setText(selectedDate);
                    } else {
                        try {
                            if (dateFormat.parse(selectedDate).before(dateFormat.parse(tv_bdate.getText().toString()))) {
                                tv_var.setText(tv_bdate.getText().toString());
                            } else {
                                tv_var.setText(selectedDate);
                            }
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });
        datePickerDialog.show();
    }

    public void setStatus(String g_value) {
        switch (g_value) {
            case "0":
                tv_updStatus.setText("Xác nhận cập nhật");
                break;
            case "1":
                tv_updStatus.setText("Tiến hành cập nhật...");
                break;
            case "2":
                tv_updStatus.setText("Cập nhật hoàn tất");
                break;
            default:
                // Xử lý khi giá trị không khớp với bất kỳ trường hợp nào
                tv_updStatus.setText(g_value);
                break;
        }
    }

    public void setVisibleProgressBar(int status) {
        if (status == 1) {
            linearLayout_process.setVisibility(View.VISIBLE);
            progBar_upload.setVisibility(View.VISIBLE);
        } else {
            linearLayout_process.setVisibility(View.GONE);
            progBar_upload.setVisibility(View.GONE);
        }
    }

    public void setProgressBar(int g_value) {
        setStatus("1");
        setVisibleProgressBar(1);
        tv_processMaxValues.setText(" / " + g_value);

        progBar_upload.setMax(g_value);
    }

    public void updateProgressBar(int progress) {
        setEnableBtn(false, false);
        tv_processing.setText(String.valueOf(progress));
        progBar_upload.setProgress(progress);
    }

    public String getSelected_bDate() {
        return tv_bdate.getText().toString().trim();
    }

    public String getSelected_eDate() {
        return tv_edate.getText().toString().trim();
    }

    public String getSelected_sp_POID() {
        return sp_POID.getSelectedItem().toString();
    }

    public String getSelected_sp_PONO() {
        String selectedDepartment = (sp_PONO.getSelectedItem() != null) ? sp_PONO.getSelectedItem().toString() : "";
        return selectedDepartment;
    }

    public void setOkButtonClickListener(View.OnClickListener listener) {
        btnOk.setOnClickListener(listener);
    }

    public void setCancelButtonClickListener(View.OnClickListener listener) {
        btnCancel.setOnClickListener(listener);
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
        db.closeDatabase();
    }

    public void setEnableBtn(boolean bool_OK, boolean bool_Cancel) {
        btnOk.setEnabled(bool_OK);
        btnCancel.setEnabled(bool_Cancel);
    }
}


