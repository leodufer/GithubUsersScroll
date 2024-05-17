package py.edu.facitec.githubusers;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {
    public UserAdapter(Context context, List<User> users) {
        super(context,R.layout.item_user,R.id.textviewUsername,users);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        ImageView avatarImageView = view.findViewById(R.id.imageViewUserAvatar);
        TextView urlTextView = view.findViewById(R.id.textviewUrl);
        User u = getItem(position);
        Picasso.get().load(u.getAvatar_url()).into(avatarImageView);
        urlTextView.setText(u.getUrl());

        return view;
    }
}
