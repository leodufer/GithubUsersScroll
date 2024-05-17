package py.edu.facitec.githubusers;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements Callback<List<User>> {
    ListView usersListView;
    TextView errorTextView;
    ProgressBar progressBar;
    Button retryButton;
    UserService userService;

    boolean loading = false;

    List<User> users = new ArrayList<>();

    User user = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });;
        usersListView = findViewById(R.id.listviewUsers);
        errorTextView = findViewById(R.id.textviewError);
        progressBar = findViewById(R.id.progressbar);
        retryButton = findViewById(R.id.buttonRetry);

        RestAdapter restAdapter = new RestAdapter.Builder()
                                            .setEndpoint("https://api.github.com")
                                            .build();

       userService = restAdapter.create(UserService.class);

        //userService.getUsers(this);

       usersListView.setOnScrollListener(new AbsListView.OnScrollListener() {
           @Override
           public void onScrollStateChanged(AbsListView view, int scrollState) {

           }

           @Override
           public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastPosition = view.getLastVisiblePosition();
                Log.d("POSITION","Last Position: "+lastPosition+" TotalItem: "+totalItemCount);
                if((lastPosition==(totalItemCount-1))&& (!loading)){
                    loading = true;
                    Log.d("MORE DATA", "load more data");
                    userService.getUsersPagination(user.getId(),MainActivity.this);
                }
           }
       });

    }

    @Override
    public void success(List<User> users, Response response) {
       // ArrayAdapter<User> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,users);
        UserAdapter adapter = new UserAdapter(this,this.users);
        adapter.addAll(users);
        usersListView.setAdapter(adapter);
        errorTextView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        usersListView.setVisibility(View.VISIBLE);
        retryButton.setVisibility(View.GONE);

        this.user = users.get(users.size()-1);
        Log.d("last user id",user.getId()+"");
        loading = false;
    }

    @Override
    public void failure(RetrofitError error) {
        Log.e("RESULT", error.getLocalizedMessage());
        if (users.isEmpty()){
            errorTextView.setText(error.getLocalizedMessage());
             errorTextView.setVisibility(View.VISIBLE);
              progressBar.setVisibility(View.GONE);
              usersListView.setVisibility(View.GONE);
             retryButton.setVisibility(View.VISIBLE);
         }else {
            Toast.makeText(MainActivity.this,"Can't load more data", Toast.LENGTH_SHORT).show();
        }
        loading=false;
    }

    public void retry(View view) {
        progressBar.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.GONE);
        usersListView.setVisibility(View.GONE);
        retryButton.setVisibility(View.GONE);
        userService.getUsers(this);
    }
}