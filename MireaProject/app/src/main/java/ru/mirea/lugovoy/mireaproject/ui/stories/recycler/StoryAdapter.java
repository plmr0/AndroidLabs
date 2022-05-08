package ru.mirea.lugovoy.mireaproject.ui.stories.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.mirea.lugovoy.mireaproject.R;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder>
{
    private final LayoutInflater inflater;
    private final List<StoryItem> states;

    public StoryAdapter(Context context, List<StoryItem> states)
    {
        this.states = states;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public StoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StoryAdapter.ViewHolder holder, int position)
    {
        StoryItem state = states.get(position);

        holder.subjectView.setText(state.getSubject());
        holder.textView.setText(state.getText());
    }

    @Override
    public int getItemCount()
    {
        return states.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        final TextView subjectView, textView;

        ViewHolder(View view)
        {
            super(view);

            subjectView = view.findViewById(R.id.item_subject);
            textView = view.findViewById(R.id.item_text);
        }
    }
}
