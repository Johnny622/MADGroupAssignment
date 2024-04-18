package my.edu.utar.groupassignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class YearAdapter extends ArrayAdapter<YearData> {

    public YearAdapter(@NonNull Context context, ArrayList<YearData> dataArrayList) {
        super(context, R.layout.semester_list, dataArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        YearData yearData = getItem(position);
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.semester_list, parent, false);
        }

        TextView listName = view.findViewById(R.id.listName);
        listName.setText(yearData.year);
        return view;
    }
}