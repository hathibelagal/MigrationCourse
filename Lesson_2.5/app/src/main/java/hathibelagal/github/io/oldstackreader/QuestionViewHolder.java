package hathibelagal.github.io.oldstackreader;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Hathibelagal on 13/8/16.
 */
public class QuestionViewHolder extends RecyclerView.ViewHolder {
    TextView score;
    TextView title;
    TextView author;
    View question;

    public QuestionViewHolder(View itemView) {
        super(itemView);
    }
}
