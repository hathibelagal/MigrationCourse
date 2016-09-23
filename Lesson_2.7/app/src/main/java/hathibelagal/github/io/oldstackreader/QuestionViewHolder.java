package hathibelagal.github.io.oldstackreader;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Hathibelagal on 13/8/16.
 */
public class QuestionViewHolder extends RecyclerView.ViewHolder {
    TextView score;
    TextView title;
    TextView author;
    RelativeLayout question;

    TextView views;
    TextView date;
    TextView tags;
    CircleImageView photo;

    public QuestionViewHolder(View itemView) {
        super(itemView);
    }
}
