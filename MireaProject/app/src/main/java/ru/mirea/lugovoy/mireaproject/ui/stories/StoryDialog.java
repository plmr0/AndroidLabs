package ru.mirea.lugovoy.mireaproject.ui.stories;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import ru.mirea.lugovoy.mireaproject.R;
import ru.mirea.lugovoy.mireaproject.ui.stories.db.Story;

public class StoryDialog extends DialogFragment
{
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LinearLayout linearLayout = new LinearLayout(requireContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams layoutParamsEditText =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParamsEditText.setMargins(50, 0, 50, 0);

        LinearLayout.LayoutParams layoutParamsTextView =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParamsTextView.setMargins(50, 50, 50, 0);

        TextView subjectText = new TextView(requireContext());
        subjectText.setText(R.string.story_subject_name);

        TextView textText = new TextView(requireContext());
        textText.setText(R.string.story_text_name);

        EditText subject = new EditText(requireContext());
        subject.setHint(R.string.story_add_subject_placeholder);

        EditText text = new EditText(requireContext());
        text.setHint(R.string.story_add_text_placeholder);

        linearLayout.addView(subjectText, layoutParamsTextView);
        linearLayout.addView(subject, layoutParamsEditText);
        linearLayout.addView(textText, layoutParamsTextView);
        linearLayout.addView(text, layoutParamsEditText);

        builder.setView(linearLayout);

        builder.setTitle(R.string.story_add_title)
                .setIcon(R.drawable.ic_story_add)
                .setPositiveButton(R.string.story_add_ok,
                        (dialog, id) ->
                        {
                            String s = subject.getText().toString();
                            String t = text.getText().toString();

                            Story story = new Story();
                            story.subject = s;
                            story.text = t;

                            StoriesFragment.storyDao.insert(story);
                            StoriesFragment.loadData();
                        });

        return builder.create();
    }
}
