package my.edu.utar.groupassignment.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import my.edu.utar.groupassignment.PaperDiscussion;
import my.edu.utar.groupassignment.R;

public class DiscussionAdapter extends ArrayAdapter<PaperDiscussion> {
    private Context context;
    private int resource;

    public DiscussionAdapter(Context context, int resource, List<PaperDiscussion> discussions) {
        super(context, resource, discussions);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        TextView userTextView = convertView.findViewById(R.id.userTextView);
        TextView messageTextView = convertView.findViewById(R.id.messageTextView);

        PaperDiscussion discussion = getItem(position);

        if (discussion != null) {
            userTextView.setText(discussion.getUser());
            messageTextView.setText(discussion.getDiscussion());
        }

        return convertView;
    }
}
