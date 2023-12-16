package com.dixitkumar.mynotesapp;

import static com.dixitkumar.mynotesapp.Notes_Fragment.EDITABLE;
import static com.dixitkumar.mynotesapp.Notes_Fragment.IS_EXISTS;
import static com.dixitkumar.mynotesapp.Notes_Fragment.NOTES_CONTENT;
import static com.dixitkumar.mynotesapp.Notes_Fragment.NOTES_EXTRA;
import static com.dixitkumar.mynotesapp.Notes_Fragment.NOTES_ID;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dixitkumar.mynotesapp.databinding.NotesItemBinding;

import java.util.ArrayList;
import java.util.List;

public class NotesRecyclerViewAdapter extends RecyclerView.Adapter<NotesRecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Notes> notesArrayList;
    private ItemClickListener itemClickListener;

    protected void setFilteredList(ArrayList<Notes> filteredList){
        this.notesArrayList = filteredList;
        notifyDataSetChanged();
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public NotesRecyclerViewAdapter(Context context, ArrayList<Notes> notesArrayList) {
        this.context = context;
        this.notesArrayList = notesArrayList;
    }

    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(NotesItemBinding.inflate(LayoutInflater.from(context),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.notes_title.setText(notesArrayList.get(position).getNotes_title());
        holder.notes_content.setText(notesArrayList.get(position).getNotes_content());
        holder.notes_time.setText(notesArrayList.get(position).getNotes_time());
        holder.notesItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notesIntent = new Intent(context, NotesEditorActivity.class);
                notesIntent.putExtra(NOTES_ID,notesArrayList.get(position).getId());
                notesIntent.putExtra(NOTES_EXTRA,notesArrayList.get(position).getNotes_title());
                notesIntent.putExtra(NOTES_CONTENT,notesArrayList.get(position).getNotes_content());
                notesIntent.putExtra(EDITABLE,false);
                notesIntent.putExtra(IS_EXISTS,true);
                context.startActivity(notesIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notesArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        TextView notes_title;
        TextView notes_content;
        TextView notes_time;
        LinearLayout notesItems;
        public MyViewHolder(@NonNull NotesItemBinding itemBinding) {
            super(itemBinding.getRoot());
            notes_title = itemBinding.notesTitle;
            notes_content = itemBinding.notesContent;
            notes_time = itemBinding.notesTime;
            notesItems = itemBinding.notesItem;
            itemBinding.notesItem.setOnClickListener((View.OnClickListener)this);
            itemBinding.notesItem.setOnLongClickListener((View.OnLongClickListener)this);
        }

        @Override
        public void onClick(View v) {
            if(itemClickListener!=null){
                itemClickListener.onClick(v,getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(itemClickListener!=null){
                itemClickListener.onLongClick(v,getAdapterPosition());
            }
            return true;
        }
    }
}
