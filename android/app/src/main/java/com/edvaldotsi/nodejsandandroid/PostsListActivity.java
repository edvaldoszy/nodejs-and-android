package com.edvaldotsi.nodejsandandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Toast;

import com.edvaldotsi.nodejsandandroid.adapter.PostAdapter;
import com.edvaldotsi.nodejsandandroid.model.Post;
import com.edvaldotsi.nodejsandandroid.retrofit.PostService;
import com.edvaldotsi.nodejsandandroid.retrofit.JSON;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostsListActivity extends AbstractActivity {

    private Retrofit retrofit;

    private PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.25.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FloatingActionButton buttonNewPost = (FloatingActionButton) findViewById(R.id.button_new_post);
        buttonNewPost.setOnClickListener(new ButtonNewPostClickListener());

        StaggeredGridLayoutManager llm = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        llm.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);

        postAdapter = new PostAdapter(this);
        RecyclerView listPosts = (RecyclerView) findViewById(R.id.list_posts);
        listPosts.setAdapter(postAdapter);
        listPosts.setLayoutManager(llm);

        listPosts.setOnCreateContextMenuListener(new ListsPostsCreateContextMenuListener());
    }

    @Override
    protected void onResume() {
        super.onResume();

        PostService service = createService(PostService.class);
        Call<JSON<Post>> call = service.getUserPosts(2, token);
        call.enqueue(new Callback<JSON<Post>>() {
            @Override
            public void onResponse(Call<JSON<Post>> call, Response<JSON<Post>> response) {
                List<Post> posts = response.body().getResults();

                postAdapter.setItems(posts);
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<JSON<Post>> call, Throwable t) {
                Toast.makeText(PostsListActivity.this, "Falhou =(", Toast.LENGTH_SHORT).show();
            }
        });
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

        }
    }
}
