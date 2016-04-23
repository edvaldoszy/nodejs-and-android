package com.edvaldotsi.nodejsandandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.edvaldotsi.nodejsandandroid.model.Post;
import com.edvaldotsi.nodejsandandroid.retrofit.PostService;
import com.rengwuxian.materialedittext.MaterialEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostActivity extends AbstractActivity {

    private MaterialEditText editPostTitle;
    private MaterialEditText editPostContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        editPostTitle = (MaterialEditText) findViewById(R.id.edit_post_title);
        editPostContent = (MaterialEditText) findViewById(R.id.edit_post_content);

        Button buttonSavePost = (Button) findViewById(R.id.button_save_post);
        buttonSavePost.setOnClickListener(new ButtonSavePostClickListener());
    }

    private class ButtonSavePostClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            Post post = new Post();
            post.setTitle(editPostTitle.getText().toString());
            post.setContent(editPostContent.getText().toString());

            PostService service = retrofit.create(PostService.class);
            Call<Void> call = service.newPost(LOGGED_USER.getId(), post, token);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() == 204) {
                        Toast.makeText(PostActivity.this, "Post salvo com sucesso", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(PostActivity.this, "Falhou =(", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
