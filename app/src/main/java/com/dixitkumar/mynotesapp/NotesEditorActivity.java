package com.dixitkumar.mynotesapp;

import static com.dixitkumar.mynotesapp.Notes_Fragment.EDITABLE;
import static com.dixitkumar.mynotesapp.Notes_Fragment.notesArrayList;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.dixitkumar.mynotesapp.databinding.ActivityNotesEditorBinding;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Calendar;

public class NotesEditorActivity extends AppCompatActivity {
   private ActivityNotesEditorBinding notesEditorBinding;

   private MyNotesAppDB db = new MyNotesAppDB(NotesEditorActivity.this) ;
   private boolean isEditableInNoteEditor = true;
   private boolean checkIfExists = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notesEditorBinding = ActivityNotesEditorBinding.inflate(getLayoutInflater());
        setContentView(notesEditorBinding.getRoot());

        Intent i = getIntent();
        notesEditorBinding.notesEditorBackButton.setOnClickListener(v->{
            finish();
        });

        //Setting up the data form notesFragment to the editor activity
        String title = "",content="",id="";
        boolean isEditable = false,isExists = false;

        id = String.valueOf(i.getIntExtra(Notes_Fragment.NOTES_ID,0));
        title = i.getStringExtra(Notes_Fragment.NOTES_EXTRA);
        content = i.getStringExtra(Notes_Fragment.NOTES_CONTENT);
        isEditable = i.getBooleanExtra(EDITABLE,false);
        isExists = i.getBooleanExtra(Notes_Fragment.IS_EXISTS,false);

        notesEditorBinding.notesEditorTitle.setText(title);
        notesEditorBinding.notesEditorContent.setText(content);

        if(isEditable){
           notesEditorBinding.notesEditorTitle.setEnabled(isEditable);
           notesEditorBinding.notesEditorContent.setEnabled(isEditable);
        }
        boolean finalIsExists = isExists;
        String finalId = id;

        notesEditorBinding.notesEditorEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEditableInNoteEditor){
                    notesEditorBinding.notesEditorTitle.setEnabled(true);
                    notesEditorBinding.notesEditorContent.setEnabled(true);
                    isEditableInNoteEditor = false;
                } else if (!isEditableInNoteEditor) {
                    notesEditorBinding.notesEditorTitle.setEnabled(false);
                    notesEditorBinding.notesEditorContent.setEnabled(false);
                    isEditableInNoteEditor = true;
                }
            }
        });

        notesEditorBinding.notesEditorShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String text_title = notesEditorBinding.notesEditorTitle.getText().toString()+"\n";
                String text_content = notesEditorBinding.notesEditorContent.getText().toString();
                intent.putExtra(Intent.EXTRA_TEXT,"Here notes You Want Buddy ! \n\n"+text_title+text_content+" \n" );
                startActivity(Intent.createChooser(intent, "Share Your Notes Here"));
            }
        });
        notesEditorBinding.notesEditorSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String notes_title ="",notes_content="",note_currentDate="";

                if(!notesEditorBinding.notesEditorTitle.getText().toString().equals("")){
                    notes_title = notesEditorBinding.notesEditorTitle.getText().toString();
                }else{
                    Snackbar.make(notesEditorBinding.getRoot(),"Enter Title",Snackbar.LENGTH_SHORT).show();
                }
                if(!notesEditorBinding.notesEditorContent.getText().toString().equals("")){
                    notes_content = notesEditorBinding.notesEditorContent.getText().toString();
                }else{
                    Snackbar.make(notesEditorBinding.getRoot(),"Enter Notes Detail",Snackbar.LENGTH_SHORT).show();
                }

                if(!notes_title.equals("") && !notes_content.equals("") && finalIsExists){
                    note_currentDate = getCurrentFormattedDate();
                    db.updateNotes(finalId,notes_title,notes_content,note_currentDate);
                    Notes_Fragment.notesArrayList = db.getNotes();
                    Notes_Fragment.adapter = new NotesRecyclerViewAdapter(getApplicationContext(),notesArrayList);
                    Notes_Fragment.adapter.notifyDataSetChanged();
                    Snackbar.make(notesEditorBinding.getRoot(),"Your Notes Has Been Saved",Snackbar.LENGTH_SHORT).show();
                    finish();
                }else if(!notes_title.equals("") && !notes_content.equals("") && !finalIsExists){
                    note_currentDate = getCurrentFormattedDate();
                    db.enterNotes(notes_title,notes_content,note_currentDate);
                    Notes_Fragment.notesArrayList = db.getNotes();
                    Notes_Fragment.adapter = new NotesRecyclerViewAdapter(getApplicationContext(),notesArrayList);
                    Notes_Fragment.adapter.notifyDataSetChanged();
                    Snackbar.make(notesEditorBinding.getRoot(),"Your Notes Has Been Saved",Snackbar.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }
    private String getCurrentFormattedDate(){
        String result = "";
        Calendar calendar = Calendar.getInstance();
        result = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        return result;
    }

    private void addNotes(String id,boolean finalIsExists){

    }
}