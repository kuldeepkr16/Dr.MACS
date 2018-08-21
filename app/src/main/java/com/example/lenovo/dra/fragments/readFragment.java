package com.example.lenovo.dra.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.lenovo.dra.POJO.ReadPojo;
import com.example.lenovo.dra.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import static android.app.Activity.RESULT_OK;

public class readFragment extends Fragment {
    private RecyclerView mbloglist;
    private DatabaseReference mDatabase;
    private Uri imageUri=null;
    private static final int GALLERY_REQUEST=1;
    private StorageReference mStorage;
    private ImageButton Attach;
    private EditText Topic;
    private FloatingActionButton Send;
    private SharedPreferences sharedPreferences;
    private String USERNAME;


    public readFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_read, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mStorage= FirebaseStorage.getInstance().getReference();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("DrMacsPost");
        mbloglist=(RecyclerView) view.findViewById(R.id.blog_list);
        mbloglist.setHasFixedSize(true);
        mbloglist.setLayoutManager(new LinearLayoutManager(getContext()));
        Attach = (ImageButton) view.findViewById(R.id.imagebtn);
        Send = (FloatingActionButton) view.findViewById(R.id.SubmitPost);
        Topic =(EditText)  view.findViewById(R.id.description);

        //getting values of current user using shared preferences
        sharedPreferences = getActivity().getSharedPreferences("currentUserData", Context.MODE_PRIVATE);
        USERNAME = sharedPreferences.getString("USERNAME","");


        Attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });
        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
                Attach.clearFocus();
                Topic.setText("");
            }
        });

        //show data
        FirebaseRecyclerAdapter<ReadPojo, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ReadPojo, readFragment.BlogViewHolder>(ReadPojo.class,
                R.layout.blog_row,readFragment.BlogViewHolder.class,mDatabase) {
            @Override
            protected void populateViewHolder(readFragment.BlogViewHolder viewHolder, ReadPojo model, int position) {
                viewHolder.setNameWhoPosted(model.getNameWhoPosted());
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getContext(),model.getImage());
            }
        };
        mbloglist.setAdapter(firebaseRecyclerAdapter);
    }
    private void startPosting() {

        final String desc=Topic.getText().toString().trim();
        if(!TextUtils.isEmpty(desc)&&imageUri!=null){
            StorageReference filePath=mStorage.child("ReadPojo").child(imageUri.getLastPathSegment());
            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl=taskSnapshot.getDownloadUrl();
                    DatabaseReference newPost=mDatabase.push();
                    newPost.child("NameWhoPosted").setValue(USERNAME);
                    newPost.child("desc").setValue(desc);
                    newPost.child("Image").setValue(downloadUrl.toString());
                }
            });

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_REQUEST&&resultCode==RESULT_OK){
            imageUri=data.getData();
            Attach.setImageURI(imageUri);
        }
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder{
        View mview;
        public BlogViewHolder(View itemView) {
            super(itemView);
            mview=itemView;
        }
        public void setNameWhoPosted(String name){
            TextView txtNameWhoPosted = (TextView) mview.findViewById(R.id.txtNameWhoPosted);
            txtNameWhoPosted.setText(name);
        }
        public void setTitle(String title){
            TextView post_title=(TextView)mview.findViewById(R.id.postHospital);
            post_title.setText(title);
        }
        public void setDesc(String desc){
            TextView post_desc=(TextView)mview.findViewById(R.id.postdes);
            post_desc.setText(desc);
        }
        public void setImage(Context ctx, String image){
            ImageView post_image=(ImageView)mview.findViewById(R.id.postImage);
            Picasso.with(ctx).load(image).into(post_image);
        }
    }
}
