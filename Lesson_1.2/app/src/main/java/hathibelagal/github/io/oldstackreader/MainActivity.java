package hathibelagal.github.io.oldstackreader;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends Activity {

    private ListView lvQuestions;
    private JSONReader reader;
    private Handler handler;
    private List<Question> questions;
    private ArrayAdapter<Question> adapter;

    // Default site
    private String site = "Stack Overflow";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvQuestions = (ListView)findViewById(R.id.lv_questions);
        reader = new JSONReader();
        handler = new Handler();

        lvQuestions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(questions.get(position).getLink()));
                startActivity(intent);
            }
        });

        // Fetch site from intent extra, if available
        if(getIntent().hasExtra("SITE"))
            site = getIntent().getStringExtra("SITE");

        updateScreen();
    }

    /**
     * Reloads list of questions, and updates the title
     */
    private void updateScreen() {
        updateQuestions(StackExchangeSites.MAP.get(site));
        setTitle(site);
    }

    /**
     * Spawns a new thread and fetches a new list of questions
     * from the StackExchange API.
     * @param site
     */
    private void updateQuestions(final String site) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                questions = reader.fetchQuestionsFrom(site);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        renderList();
                    }
                });
            }
        }).start();
    }

    /**
     * Creates an adapter that can use the list of questions.
     * If adapter exists already, calls only notifyDatasetChanged
     */
    private void renderList() {
        if(adapter == null) {
            adapter = new ArrayAdapter<Question>(this, 0, questions) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    if(convertView == null) {
                        convertView = getLayoutInflater().inflate(R.layout.question, null);
                        QuestionViewHolder viewHolder = new QuestionViewHolder();
                        viewHolder.score = (TextView)convertView.findViewById(R.id.question_score);
                        viewHolder.title = (TextView)convertView.findViewById(R.id.question_title);
                        viewHolder.author = (TextView)convertView.findViewById(R.id.question_author);
                        convertView.setTag(viewHolder);
                    }

                    QuestionViewHolder viewHolder = (QuestionViewHolder)convertView.getTag();
                    viewHolder.score.setText("" + questions.get(position).getScore());
                    viewHolder.title.setText(questions.get(position).getTitle());
                    viewHolder.author.setText("Asked by " + questions.get(position).getAuthorDisplayName());

                    return convertView;
                }
            };
            lvQuestions.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_sites) {
            Intent intent = new Intent(this, SiteSelectionActivity.class);
            startActivity(intent);
        }

        if(item.getItemId() == R.id.menu_refresh) {
            updateScreen();
        }
        return super.onOptionsItemSelected(item);
    }
}
