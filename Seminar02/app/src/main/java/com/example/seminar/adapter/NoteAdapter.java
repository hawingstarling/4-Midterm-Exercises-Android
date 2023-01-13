package com.example.seminar.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seminar.MainActivity;
import com.example.seminar.R;
import com.example.seminar.bottomSheetFragment.CreateNoteBottomSheetFragment;
import com.example.seminar.database.DatabaseClient;
import com.example.seminar.model.Note;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private MainActivity context;
    private LayoutInflater inflater;
    private List<Note> noteList;
    public SimpleDateFormat dateFormat = new SimpleDateFormat("EE dd MMM yyyy", Locale.US);
    public SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-M-yyyy", Locale.US);
    Date date = null;
    String outputDateString = null;
    CreateNoteBottomSheetFragment.setRefreshListener setRefreshListener;

    // contructor NoteAdapter
    public NoteAdapter(MainActivity context, List<Note> noteList, CreateNoteBottomSheetFragment.setRefreshListener setRefreshListener) {
        this.context = context;
        this.noteList = noteList;
        this.setRefreshListener = setRefreshListener;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // Tạo mới một ViewHolder
    @NonNull
    @Override
    public NoteAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_notelist, parent, false);
        return new NoteViewHolder(view);
    }

    // Binding data vào viewHolder
    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.NoteViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.title.setText(note.getNoteTitle());
        holder.description.setText(note.getNoteDescription());
        holder.status.setText(note.isComplete() ? "COMPLETED" : "UPCOMING");
        holder.options.setOnClickListener(view -> showPopUpMenu(view, position));
        holder.time.setText(note.getDateCurrent());

        if (holder.status.getText().equals("COMPLETED")) {
            holder.status.setTextColor(Color.GREEN);
            holder.status.setTypeface(Typeface.DEFAULT_BOLD);
        }

        try {
            date = inputDateFormat.parse(note.getDate());
            outputDateString = dateFormat.format(date);

            String[] items1 = outputDateString.split(" ");
            String day = items1[0];
            String dd = items1[1];
            String month = items1[2];

            holder.day.setText(day);
            holder.date.setText(dd);
            holder.month.setText(month);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Danh sách số lượng item của 1 Recycler View
    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public void showPopUpMenu(View view, int position) {
        final Note note = noteList.get(position);
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.getMenuInflater().inflate(R.menu.itemtodolist, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.itemDelete:
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.AppTheme_Dialog);
                    alertDialogBuilder.setTitle(R.string.delete_confirmation).setMessage(R.string.sureToDelete)
                            .setPositiveButton(R.string.yes, (dialog, which) -> {
                                deleteNoteFromId(note.getNoteId(), position);
                                System.out.println("Note is deleted");
                            })
                            .setNegativeButton(R.string.no, (dialog, which) -> dialog.cancel()).show();
                    break;
                case R.id.itemUpdate:
                    CreateNoteBottomSheetFragment createNoteBottomSheetFragment = new CreateNoteBottomSheetFragment();
                    createNoteBottomSheetFragment.setNoteId(note.getNoteId(), true, context, context);
                    createNoteBottomSheetFragment.show(context.getSupportFragmentManager(), createNoteBottomSheetFragment.getTag());
                    break;
                case R.id.itemComplete:
//                    AlertDialog.Builder completeAlertDialog = new AlertDialog.Builder(context, R.style.AppTheme_Dialog);
//                    completeAlertDialog.setTitle(R.string.confirmation).setMessage(R.string.sureToMarkAsComplete)
//                            .setPositiveButton(R.string.yes, (dialog, which) -> {
//                                showCompleteDialog(note.getNoteId(), position, note.isComplete());
//                                System.out.println("Note is completed");
//                            })
//                            .setNegativeButton(R.string.no, (dialog, which) -> dialog.cancel()).show();
                    updateIsCompleted(note.getNoteId(), true, position);
                    break;
            }
            return false;
        });
        popupMenu.show();
    }

    private void deleteNoteFromId(int noteId, int position) {
        class GetSavedNotes extends AsyncTask<Void, Void, List<Note>> {
            @Override
            protected List<Note> doInBackground(Void... voids) {
                DatabaseClient.getInstance(context)
                        .getAppDataBase()
                        .noteDAO()
                        .deleteNoteFromId(noteId);
                return noteList;
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
                removeAtPosition(position);
                setRefreshListener.refresh();
            }
        }
        GetSavedNotes sn = new GetSavedNotes();
        sn.execute();
    }

    private void updateIsCompleted(int noteId, boolean isCompleted, int position) {
        class UpdateIsCompleted extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(context)
                        .getAppDataBase()
                        .noteDAO()
                        .updateIsComplete(isCompleted, noteId);

                return null;
            }


            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                removeAtPosition(position);
                setRefreshListener.refresh();
            }
        }
        UpdateIsCompleted uic = new UpdateIsCompleted();
        uic.execute();
    }

    private void removeAtPosition(int position) {
        noteList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, noteList.size());
    }

    public void showCompleteDialog(int noteId, int position, boolean isComplete) {
        Dialog dialog = new Dialog(context, R.style.AppTheme);;
//        dialog.setContentView(R.layout.dialog_completed_theme);
//        Button close = dialog.findViewById(R.id.closeButton);
//        close.setOnClickListener(view -> {
//            deleteNoteFromId(noteId, position);
//            dialog.dismiss();
//        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.day)
        TextView day;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.month)
        TextView month;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.description)
        TextView description;
        @BindView(R.id.status)
        TextView status;
        @BindView(R.id.options)
        ImageView options;
        @BindView(R.id.time)
        TextView time;

        // contructor NoteViewHolder
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
