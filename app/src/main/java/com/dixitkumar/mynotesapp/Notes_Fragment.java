package com.dixitkumar.mynotesapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.LocaleDisplayNames;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.Toast;

import com.dixitkumar.mynotesapp.databinding.FragmentNotesBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class Notes_Fragment extends Fragment implements ItemClickListener{
   private FragmentNotesBinding notesBinding;

   protected static ArrayList<Notes> notesArrayList  ;
   protected static  NotesRecyclerViewAdapter adapter ;

    protected MyNotesAppDB db = new MyNotesAppDB(getContext());
   protected static final String NOTES_EXTRA = "notes_title_extra";
   protected static final String NOTES_CONTENT = "notes_content_extra";
   protected static final String NOTES_ID = "notes_id";
   protected static final String EDITABLE = "isEditable";
   protected static final String IS_EXISTS = "isExists";
    public Notes_Fragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        notesBinding = FragmentNotesBinding.inflate(getLayoutInflater());

        //Creating the instance of the Database Class
        db = new MyNotesAppDB(getContext());
        //Initializing the Arraylist
        notesArrayList =  new ArrayList<>();
        //AddingAll The Items of Database to the list
        notesArrayList.addAll(db.getNotes());

        //Setting up the recyclerView
        notesBinding.recyclerViewNotes.setLayoutManager(new LinearLayoutManager(getContext()));
        //Setting up the notes Adapter
        adapter = new NotesRecyclerViewAdapter(getContext(),notesArrayList);
        adapter.setItemClickListener(this);
        notesBinding.recyclerViewNotes.setAdapter(adapter);
        notesBinding.recyclerViewNotes.setItemViewCacheSize(13);
        notesBinding.totalNotes.setText("Total Number of Notes :- "+adapter.getItemCount());
        //Creating a new Notes
        notesBinding.writeNotes.setOnClickListener(v->{
            Intent writingNotes = new Intent(getContext(), NotesEditorActivity.class);
            writingNotes.putExtra(EDITABLE,true);
            writingNotes.putExtra(IS_EXISTS,false);
            startActivity(writingNotes);
        });

        notesBinding.searchViewNotes.clearFocus();
        notesBinding.searchViewNotes.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
        return notesBinding.getRoot();
    }

    private void filterList(String newText) {
        ArrayList<Notes> filteredList = new ArrayList<>();
        for (Notes notes : notesArrayList){
            if(notes.getNotes_title().toLowerCase().contains(newText.toLowerCase()) || notes.getNotes_content().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(notes);
            }
        }
        if(filteredList.isEmpty()){
            Snackbar.make(notesBinding.getRoot(),"No Notes Found ",Snackbar.LENGTH_SHORT).show();
        }else{
            adapter.setFilteredList(filteredList);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        notesArrayList = db.getNotes();
        notesBinding.recyclerViewNotes.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NotesRecyclerViewAdapter(getContext(),notesArrayList);

        notesBinding.recyclerViewNotes.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.notifyItemChanged(0);
        notesBinding.recyclerViewNotes.scrollToPosition(0);
        adapter.setItemClickListener(this);
        notesBinding.totalNotes.setText("Total Number of Notes :- "+adapter.getItemCount());
    }

    @Override
    public void onClick(View v, int pos) {
     Intent notesIntent = new Intent(getContext(), NotesEditorActivity.class);
     notesIntent.putExtra(NOTES_ID,notesArrayList.get(pos).getId());
     notesIntent.putExtra(NOTES_EXTRA,notesArrayList.get(pos).getNotes_title());
     notesIntent.putExtra(NOTES_CONTENT,notesArrayList.get(pos).getNotes_content());
     notesIntent.putExtra(EDITABLE,false);
     notesIntent.putExtra(IS_EXISTS,true);
     startActivity(notesIntent);
    }

    @Override
    public void onLongClick(View v, int pos) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.delete_icon)
                .setTitle("Delete Notes ")
                .setMessage("Are You Sure You Want to delete ")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteNotes(notesArrayList.get(pos).getId()+"");
                        onStart();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar.make(notesBinding.getRoot(),"Selected Item has been Deleted",Snackbar.LENGTH_LONG).show();
                            }
                        },1000);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        dialog.show();

    }
}