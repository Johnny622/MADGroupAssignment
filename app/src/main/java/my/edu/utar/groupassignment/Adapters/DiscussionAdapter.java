package my.edu.utar.groupassignment.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.vanniktech.emoji.EmojiTextView;

import java.util.List;

import my.edu.utar.groupassignment.PaperDiscussion;
import my.edu.utar.groupassignment.R;

public class DiscussionAdapter extends ArrayAdapter<PaperDiscussion> {
    private int resource;
    FirebaseAuth auth;
    public DiscussionAdapter(Context context, int resource, List<PaperDiscussion> discussions) {
        super(context, resource, discussions);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LinearLayout layout = new LinearLayout(getContext());

            TextView userView = new TextView(getContext());
            userView.setId(R.id.userTextView); // Ensure ID does not conflict with other IDs
            userView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            layout.addView(userView);

            // TextView for Message
            TextView messageView = new TextView(getContext());
            messageView.setId(R.id.messageTextView); // Ensure ID does not conflict with other IDs
            messageView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            layout.addView(messageView);

            holder = new ViewHolder();
            holder.userTextView = userView;
            holder.messageTextView = messageView;
            convertView = layout;
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PaperDiscussion item = getItem(position);
        if (item != null) {
            auth = FirebaseAuth.getInstance();
            holder.userTextView.setText(auth.getCurrentUser().getDisplayName() + ": ");

            holder.messageTextView.setText(item.getDiscussion());
        }
        return convertView;
    }

    static class ViewHolder {
        TextView messageTextView;
        TextView userTextView;
    }
}
