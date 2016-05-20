package com.edvaldotsi.nodejsandandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.edvaldotsi.nodejsandandroid.adapter.PostAdapter;
import com.edvaldotsi.nodejsandandroid.model.Post;
import com.edvaldotsi.nodejsandandroid.retrofit.JSON;
import com.edvaldotsi.nodejsandandroid.retrofit.PostService;
import com.edvaldotsi.nodejsandandroid.retrofit.ServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsListActivity extends AbstractActivity {

    private ProgressBar progress;
    private RecyclerView mRecyclerViewPosts;

    private PostAdapter mPostAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton buttonNewPost = (FloatingActionButton) findViewById(R.id.button_new_post);
        buttonNewPost.setOnClickListener(new ButtonNewPostClickListener());

        StaggeredGridLayoutManager llm = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        llm.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);

        progress = (ProgressBar) findViewById(R.id.progress);

        mPostAdapter = new PostAdapter(this);
        mRecyclerViewPosts = (RecyclerView) findViewById(R.id.list_posts);
        mRecyclerViewPosts.setAdapter(mPostAdapter);
        mRecyclerViewPosts.setLayoutManager(llm);

        mRecyclerViewPosts.setOnCreateContextMenuListener(new ListsPostsCreateContextMenuListener());
    }

    private void showProgress(boolean show) {
        progress.setVisibility(show ? View.VISIBLE : View.GONE);
        mRecyclerViewPosts.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void request() {
        showProgress(true);

        PostService service = ServiceGenerator.createService(PostService.class);
        Call<JSON<Post>> call = service.getUserPosts(LOGGED_USER.getId());
        call.enqueue(new Callback<JSON<Post>>() {
            @Override
            public void onResponse(Call<JSON<Post>> call, Response<JSON<Post>> response) {
                List<Post> posts = response.body().getResults();

                mPostAdapter.setItems(posts);
                mPostAdapter.notifyDataSetChanged();

                showProgress(false);
            }

            @Override
            public void onFailure(Call<JSON<Post>> call, Throwable t) {
                Toast.makeText(PostsListActivity.this, "Fala na conexão", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        request();
    }

    private class ButtonNewPostClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(PostsListActivity.this, PostActivity.class));
        }
    }

    private class ListsPostsCreateContextMenuListener implements View.OnCreateContextMenuListener {
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            Toast.makeText(PostsListActivity.this, "Editar post", Toast.LENGTH_SHORT).show();
        }
    }
}
