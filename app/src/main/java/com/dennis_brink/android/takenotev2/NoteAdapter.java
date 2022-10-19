package com.dennis_brink.android.takenotev2;

import static android.content.res.Configuration.UI_MODE_NIGHT_MASK;
import static android.content.res.Configuration.UI_MODE_NIGHT_NO;
import static android.content.res.Configuration.UI_MODE_NIGHT_UNDEFINED;
import static android.content.res.Configuration.UI_MODE_NIGHT_YES;

import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {

    private List<Note> notes = new ArrayList<>();
    private onItemClickListener listener;

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // inflate the card view (ViewGroup is actually the recyclerview)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_card, parent, false);
        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note currentNote = notes.get(position);
        holder.textViewTitle.setText(currentNote.getTitle());
        holder.textViewDescription.setText(currentNote.getDescription());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setNotes(List<Note> notes){
        this.notes = notes;
        notifyDataSetChanged(); // this will give a signal if anything changed
    }

    public Note getNotes(int position){
        return notes.get(position); //return the note from position in notes array
    }

    // click listener interface for recyclerview
    public interface onItemClickListener{
        void onItemClick(Note note);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        this.listener = listener;
    }

    // create this inner class first before you click 'implement methods' in order to create the overridden methods
    // like they are now. If you don't they will not be created like this
    class NoteHolder extends RecyclerView.ViewHolder{

        TextView textViewTitle, textViewDescription;
        int nightModeFlags;
        private static final String TAG = "DENNIS_B";
        View itemView;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);

            this.itemView = itemView;

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            setupTheme();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(notes.get(position));
                    }
                }
            });
        }

        private void setupTheme() {

            nightModeFlags = textViewTitle.getContext().getResources().getConfiguration().uiMode & UI_MODE_NIGHT_MASK;

            switch (nightModeFlags) {
                case UI_MODE_NIGHT_YES:
                    Log.d(TAG, "UI_MODE_NIGHT_YES");
                    setupNightMode();
                    break;
                case UI_MODE_NIGHT_NO:
                    Log.d(TAG, "UI_MODE_NIGHT_NO");
                    break;
                case UI_MODE_NIGHT_UNDEFINED:
                    Log.d(TAG, "UI_MODE_NIGHT_UNDEFINED");
                    break;
            }

        }

        private void setupNightMode() {
            CardView card = itemView.findViewById(R.id.card_view);
            card.setCardBackgroundColor(itemView.getContext().getColor(R.color.darkgray));
        }


    }

}
