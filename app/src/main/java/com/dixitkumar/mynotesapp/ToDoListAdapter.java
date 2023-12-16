package com.dixitkumar.mynotesapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.dixitkumar.mynotesapp.databinding.FragmentToDoListBinding;
import com.dixitkumar.mynotesapp.databinding.TodoListItemsBinding;

import java.util.ArrayList;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.TodoListViewHolder> {

    private FragmentToDoListBinding listBinding;
    private Context context;
    private ArrayList<TODOItems> todoItemArrayList;

    private MyTODODB db ;
    boolean isChecked = true;
    protected void setFilteredList(ArrayList<TODOItems> filteredList){
        this.todoItemArrayList = filteredList;
        notifyDataSetChanged();
    }

    public ToDoListAdapter(Context context,ArrayList<TODOItems> todoItemArrayList){
        this.context =context;
        this.todoItemArrayList = todoItemArrayList;
        this.db = new MyTODODB(context);
    }
    @NonNull
    @Override
    public TodoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TodoListViewHolder(TodoListItemsBinding.inflate(LayoutInflater.from(context),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TodoListViewHolder holder, @SuppressLint("RecyclerView") int position) {
     holder.toDoListItem.setText(todoItemArrayList.get(position).getTodoItem());
            holder.TODOlistItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isChecked = Boolean.parseBoolean(todoItemArrayList.get(position).getIsChecked());
                    if(isChecked){
                        String item = false+"";
                        String content = todoItemArrayList.get(position).getTodoItem();
                        if(!content.equals("") && content.contains(((char)10003+" "))){
                            content =  content.substring(2);
                        }
                        db.updateTODOListItems(todoItemArrayList.get(position).getId()+"",item,content);
                        update();
                        Log.d("TAG", String.valueOf(todoItemArrayList.get(position).getIsChecked()));
                        Log.d("TAG",db.getTODOListItems().get(position).getIsChecked());
                    }else{
                        String item = true+"";
                        db.updateTODOListItems(todoItemArrayList.get(position).getId()+"",item,((char)10003+" ") +todoItemArrayList.get(position).getTodoItem());
                        update();
                        Log.d("TAG", String.valueOf(todoItemArrayList.get(position).getIsChecked()));
                        Log.d("TAG",db.getTODOListItems().get(position).getIsChecked());
                    }
                }
            });

        holder.TODOlistItem.setOnLongClickListener(new View.OnLongClickListener() {
         @Override
         public boolean onLongClick(View v) {
             AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext())
                .setIcon(R.drawable.delete_icon)
                .setTitle("Delete Notes ")
                .setMessage("Are You Sure You Want to delete ")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteTODOItems(todoItemArrayList.get(position).getId()+"");
                        update();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        dialog.show();
             return true;
         }
     });
    }

    protected void update(){
        this.todoItemArrayList = db.getTODOListItems();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return todoItemArrayList.size();
    }

    class TodoListViewHolder extends RecyclerView.ViewHolder {
        TextView toDoListItem;
        LinearLayout TODOlistItem;
        public TodoListViewHolder(@NonNull TodoListItemsBinding itemsBinding) {
            super(itemsBinding.getRoot());

            this.toDoListItem = itemsBinding.todolistItem;
            this.TODOlistItem = itemsBinding.TODOlistItem;

        }
    }
}
