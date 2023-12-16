package com.dixitkumar.mynotesapp;

import static com.dixitkumar.mynotesapp.R.*;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.dixitkumar.mynotesapp.databinding.FragmentToDoListBinding;
import com.dixitkumar.mynotesapp.databinding.TodoItemDialogitemBinding;
import com.dixitkumar.mynotesapp.databinding.TodoListItemsBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.util.ArrayList;

public class ToDoList_Fragment extends Fragment{
    private FragmentToDoListBinding listBinding;
   private BottomSheetDialog dialog;
   private TodoItemDialogitemBinding dialogitemBinding;
   private MyTODODB db;
   private ArrayList<TODOItems> todoItemsArrayList ;
   private   ToDoListAdapter adapter;
    public ToDoList_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        listBinding = FragmentToDoListBinding.inflate(getLayoutInflater());

        db = new MyTODODB(getContext());

        todoItemsArrayList = new ArrayList<>();

        ArrayList<TODOItems> itemsArrayList = new ArrayList<>();
        itemsArrayList.addAll(db.getTODOListItems());

        //Setting up the recycler view
        listBinding.recyclerViewTODOList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ToDoListAdapter(getContext(),todoItemsArrayList);
        listBinding.recyclerViewTODOList.setAdapter(adapter);

        //initializing bottom sheet dialog
        dialog = new BottomSheetDialog(getContext());
        showDialog();
        listBinding.addListItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(dialogitemBinding.getRoot());
                dialogitemBinding.saveTODOListItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db =  new MyTODODB(getContext());
                        boolean isChecked = false;
                        String todolist_itemText = dialogitemBinding.addListItems.getText().toString();
                        db.enterTodoItems(isChecked+"",todolist_itemText);
                        todoItemsArrayList = db.getTODOListItems();
                        adapter = new ToDoListAdapter(getContext(),todoItemsArrayList);
                        listBinding.recyclerViewTODOList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        adapter.notifyItemChanged(0);
                        listBinding.recyclerViewTODOList.scrollToPosition(0);
                        listBinding.totalTODOList.setText("Total Number of TODO items : "+todoItemsArrayList.size()+"");
                        dialog.dismiss();

                    }
                });
                dialog.show();
            }
        });
        listBinding.totalTODOList.setText("Total Number of TODO items : "+todoItemsArrayList.size()+"");

        listBinding.searchViewToDoList.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            dialog.getWindow().setStatusBarColor(ContextCompat.getColor(getContext(),color.white));
            dialog.getWindow().setNavigationBarColor(ContextCompat.getColor(getContext(), color.white));
        }
        return listBinding.getRoot();
    }

    @Override
    public  void onStart() {
        super.onStart();
        todoItemsArrayList = db.getTODOListItems();
        listBinding.recyclerViewTODOList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ToDoListAdapter(getContext(),todoItemsArrayList);
        listBinding.recyclerViewTODOList.setAdapter(adapter);
    }

    protected void showDialog() {
    dialogitemBinding = TodoItemDialogitemBinding.inflate(getLayoutInflater());
    dialogitemBinding.getRoot().setBackgroundColor(ContextCompat.getColor(getContext(),color.white));
    dialogitemBinding.textInputLayout3.setHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(getContext(),color.yellow_note)));
    }
    private void filterList(String newText) {
        ArrayList<TODOItems> filteredList = new ArrayList<>();
        for (TODOItems items : todoItemsArrayList){
            if(items.getTodoItem().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(items);
            }
        }
        if(filteredList.isEmpty()){
            Snackbar.make(listBinding.getRoot(),"No Notes Found ",Snackbar.LENGTH_SHORT).show();
        }else{
           adapter.setFilteredList(filteredList);
        }
    }
}