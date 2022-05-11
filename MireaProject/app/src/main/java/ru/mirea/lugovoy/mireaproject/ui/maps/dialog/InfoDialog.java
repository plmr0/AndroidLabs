package ru.mirea.lugovoy.mireaproject.ui.maps.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import ru.mirea.lugovoy.mireaproject.R;

public class InfoDialog extends DialogFragment
{
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Bundle args = getArguments();
        String name = args.getString("name");
        String addr = args.getString("addr");
        String est = args.getString("est");
        String pnt = args.getString("pnt");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LinearLayout linearLayout = new LinearLayout(requireContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams layoutParamsTextView =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParamsTextView.setMargins(50, 10, 50, 10);

        String estStr = String.format("%s: %s", getString(R.string.maps_establishment), est);
        String addrStr = String.format("%s: %s", getString(R.string.maps_address), addr);
        String pntStr = String.format("%s: %s", getString(R.string.maps_coordinates), pnt);

        TextView establishmentTextView = new TextView(requireContext());
        establishmentTextView.setText(estStr);

        TextView addressTextView = new TextView(requireContext());
        addressTextView.setText(addrStr);

        TextView pointTextView = new TextView(requireContext());
        pointTextView.setText(pntStr);

        linearLayout.addView(establishmentTextView, layoutParamsTextView);
        linearLayout.addView(addressTextView, layoutParamsTextView);
        linearLayout.addView(pointTextView, layoutParamsTextView);

        builder.setView(linearLayout);

        builder.setTitle(name)
                .setIcon(R.drawable.ic_maps_university)
                .setNeutralButton(R.string.maps_close_dialog, (dialog, id) -> dialog.cancel());

        return builder.create();
    }
}
