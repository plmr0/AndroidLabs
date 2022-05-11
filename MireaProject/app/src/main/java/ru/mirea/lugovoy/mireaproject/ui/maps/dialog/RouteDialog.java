package ru.mirea.lugovoy.mireaproject.ui.maps.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.yandex.mapkit.geometry.Point;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.lugovoy.mireaproject.R;
import ru.mirea.lugovoy.mireaproject.ui.maps.PointLiveData;
import ru.mirea.lugovoy.mireaproject.ui.maps.Place;

public class RouteDialog extends DialogFragment
{
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Bundle args = getArguments();
        List<Place> places = (List<Place>) args.getSerializable("places");

        List<String> names = new ArrayList<>();

        for (Place place : places)
        {
            names.add(place.getName());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LinearLayout linearLayout = new LinearLayout(requireContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams layoutParamsTextView =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParamsTextView.setMargins(50, 50, 50, 10);

        LinearLayout.LayoutParams layoutParamsSpinner =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParamsSpinner.setMargins(50, 0, 50, 0);

        TextView descriptionTextView = new TextView(requireContext());
        descriptionTextView.setText(R.string.maps_build_route_description);

        TextView destinationTextView = new TextView(requireContext());
        destinationTextView.setText(R.string.maps_build_route_destination);

        Spinner spinnerA = new Spinner(requireContext());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerA.setAdapter(adapter);

        linearLayout.addView(descriptionTextView, layoutParamsTextView);
        linearLayout.addView(destinationTextView, layoutParamsTextView);
        linearLayout.addView(spinnerA, layoutParamsSpinner);

        builder.setView(linearLayout);

        builder.setTitle(R.string.maps_build_route)
                .setIcon(R.drawable.ic_maps_route)
                .setPositiveButton(R.string.maps_confirm, (dialogInterface, i) ->
                {
                    Point point = places.get(spinnerA.getSelectedItemPosition()).getPoint();
                    PointLiveData.setPoint(point);
                })
                .setNegativeButton(R.string.maps_close_dialog, (dialog, id) -> dialog.cancel());

        return builder.create();
    }
}
