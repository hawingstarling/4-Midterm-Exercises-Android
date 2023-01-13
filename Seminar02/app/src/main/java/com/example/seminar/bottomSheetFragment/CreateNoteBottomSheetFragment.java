package com.example.seminar.bottomSheetFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.seminar.MainActivity;
import com.example.seminar.R;
import com.example.seminar.broadcastReceiver.AlarmBroadcastReceiver;
import com.example.seminar.database.DatabaseClient;
import com.example.seminar.model.Note;
import com.example.seminar.supportConvert.ImageBitmapString;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CreateNoteBottomSheetFragment extends BottomSheetDialogFragment {

    Unbinder unbinder;
    @BindView(R.id.addNoteTitle)
    EditText addNoteTitle;
    @BindView(R.id.addNoteDescription)
    EditText addNoteDescription;
    @BindView(R.id.noteDate)
    EditText noteDate;
    @BindView(R.id.noteTime)
    EditText noteTime;
    @BindView(R.id.noteImage)
    ImageView noteImage;
    @BindView(R.id.addNote)
    Button addNote;

    Bitmap bitmap = null;
    int noteId;
    boolean isEdit;
    Note note;
    int mYear, mMonth, mDay;
    int mHour, mMinute;
    setRefreshListener setRefreshListener;
    AlarmManager alarmManager;
    TimePickerDialog timePickerDialog;
    DatePickerDialog datePickerDialog;
    MainActivity activity;
    public static int count = 0;

    /*
     * @params BottomSheetBehavior cung cấp một Callback khi bottomSheet thay đổi trạng thái của nó.
     * @params STATE_HIDDEN: khi trạng thái ẩn
     * @params dismiss(): ẩn bottomSheet
     */
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallBack = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) dismiss();
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

        }
    };

    public void setNoteId(int noteId, boolean isEdit, setRefreshListener setRefreshListener, MainActivity activity) {
        this.noteId = noteId;
        this.isEdit = isEdit;
        this.setRefreshListener = setRefreshListener;
        this.activity = activity;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"RestrictedApi", "ClickableViewAccessibility"})
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_create_note, null);
        unbinder = ButterKnife.bind(this, contentView);
        dialog.setContentView(contentView);
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        addNote.setOnClickListener(view -> {
            if (validateFields()) {
                createNote();
            }

        });

        if (isEdit) {
            showNoteFromId();
        }

        // Event DIALOG date
        noteDate.setOnTouchListener((view, motionEvent) -> {
           if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
               final Calendar c = Calendar.getInstance();
               mYear = c.get(Calendar.YEAR);
               mMonth = c.get(Calendar.MONTH);
               mDay = c.get(Calendar.DAY_OF_MONTH);
               datePickerDialog = new DatePickerDialog(getActivity(), (view1, year, monthOfYear, dayOfMonth) -> {
                    noteDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    datePickerDialog.dismiss();
               }, mYear, mMonth, mDay);
               datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
               datePickerDialog.show();
           }
           return true;
        });
        // Event DIALOG time
        noteTime.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                timePickerDialog = new TimePickerDialog(getActivity(),
                        (view12, hourOfDay, minute) -> {
                            noteTime.setText(hourOfDay + ":" + minute);
                            timePickerDialog.dismiss();
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
            return true;
        });

        noteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            noteImage.setImageURI(selectedImage);
        }
    }

    public boolean validateFields() {
        if(addNoteTitle.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please enter a valid title", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(addNoteDescription.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please enter a valid description", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(noteDate.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please enter date", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(noteTime.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please enter time", Toast.LENGTH_SHORT).show();
            return false;
        }
//        else if(noteImage.getText().toString().equalsIgnoreCase("")) {
//            Toast.makeText(activity, "Please enter an image", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        else {
            return true;
        }
    }

    public static byte[] convertBitmapToByteArray(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void createNote() {
        class saveNoteInBackend extends AsyncTask<Void, Void, Void> {

            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... voids) {
                String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());

                bitmap = ((BitmapDrawable) noteImage.getDrawable()).getBitmap();
                Note createNote = new Note();
                createNote.setNoteTitle(addNoteTitle.getText().toString());
                createNote.setNoteDescription(addNoteDescription.getText().toString());
                createNote.setDate(noteDate.getText().toString());
                createNote.setAlertTimer(noteTime.getText().toString());
                createNote.setNoteImage(ImageBitmapString.getStringFromBitmap(bitmap));
                createNote.setComplete(false);
                createNote.setDateCurrent(currentDateTimeString);

                if (!isEdit) {
                    DatabaseClient.getInstance(getActivity()).getAppDataBase()
                            .noteDAO()
                            .insertDataInfoNoteList(createNote);
                } else {
                    DatabaseClient.getInstance(getActivity()).getAppDataBase()
                            .noteDAO()
                            .updateAnExistingRow(noteId, addNoteTitle.getText().toString(),
                                    addNoteDescription.getText().toString(),
                                    noteDate.getText().toString(), convertBitmapToByteArray(((BitmapDrawable) noteImage.getDrawable()).getBitmap())); // multi update
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // createAlarm
                    createAlarm();
                }
                setRefreshListener.refresh();
                Toast.makeText(getActivity(), "Your note is been added", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        }
        saveNoteInBackend sn = new saveNoteInBackend();
        sn.execute();
    }
    
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void createAlarm() {
        try {
            String[] dayArray = noteDate.getText().toString().split("-");
            String dd = dayArray[0];
            String month = dayArray[1];
            String year = dayArray[2];

            String[] timeArray = noteTime.getText().toString().split(":");
            String hour = timeArray[0];
            String min = timeArray[1];

            // Set ngày giờ của bộ lịch Dương Lịch
            Calendar cal = new GregorianCalendar();
            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
            cal.set(Calendar.MINUTE, Integer.parseInt(min));
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            cal.set(Calendar.DATE, Integer.parseInt(dd));

            Intent alarmIntent = new Intent(activity, AlarmBroadcastReceiver.class);
            alarmIntent.putExtra("TITLE", addNoteTitle.getText().toString());
            alarmIntent.putExtra("DESC", addNoteDescription.getText().toString());
            alarmIntent.putExtra("DATE", noteDate.getText().toString());
            alarmIntent.putExtra("TIME", noteTime.getText().toString());

            // getBroadcast(Context, pendingIntentRequestCode, Intent, flag)
            // getBroadcast dùng để gửi một broadcast tới BroadcastReceiver
            // requestCode là một unique ID để phân biệt các PendingIntent trong cùng một Intent
            // FLAG_UPDATE_CURRENT: Nếu PendingIntent đã tồn tài thì hãy giữ nó và thay thế bằng dữ
            // liệu bổ sung bằng dữ liệu trong Intent mới

            // PendingIntent là 1 loại intent đặc biệt, thay vì tác vụ đích cần thực hiện ngay Intent
            // khi vừa gửi đi thì với pendingintent tác vụ sẽ thực hiện sau một khoảng thời gian nào đó.
            // Do Google muốn hạn chế việc tiêu thụ pin quá mức nên việc thiết lập thời lên schedule
            // (lịch trình) bởi Alarm sẽ không chính xác
            // set(): lên lịch thời gian nhưng cho phép Android linh động thời điểm kích hoạt
            // setExact(): yêu cầu Android kích hoạt thời điểm chính xác
            PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, count, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            // Từ Android M trở đi, Google có hai khái niệm Doze và App Standby. Hai chế độ này sẽ
            // hủy các alarm được lên lịch trước đó khi thiết bị không được cắm sạc

            // 2 api để khắc phục app vào chế độ Doze là setAndAllowWhileIdle(),
            // setExactAndAllowWhileIdle() - chỉ được sử dụng khi ứng dụng cần alarm chính xác nhất
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                }
                count++;
//
//                PendingIntent intent = PendingIntent.getBroadcast(activity, count, alarmIntent, 0);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() - 60000, intent);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() - 60000, intent);
//                    } else {
//                        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() - 60000, intent);
//                    }
//                }
//                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showNoteFromId() {
        class showNoteFromId extends AsyncTask<Void, Void, Void> {
            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... voids) {
                note = DatabaseClient.getInstance(getActivity()).getAppDataBase()
                        .noteDAO()
                        .selectNoteFromAnId(noteId);
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                setDataInUI();
            }
        }
        showNoteFromId sn = new showNoteFromId();
        sn.execute();
    }

    private void setDataInUI() {
        addNoteTitle.setText(note.getNoteTitle());
        addNoteDescription.setText(note.getNoteDescription());
        noteDate.setText(note.getDate());
        noteTime.setText(note.getAlertTimer());
        noteImage.setImageBitmap(ImageBitmapString.getBitmapFromString(note.getNoteImage()));
    }

    public interface setRefreshListener {
        void refresh();
    }
}
