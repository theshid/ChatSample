package com.shid.chat.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shid.chat.Adapter.UserAdapter;
import com.shid.chat.Model.User;
import com.shid.chat.R;

import java.util.ArrayList;
import java.util.List;


public class UsersFragment extends Fragment {

    private UserAdapter userAdapter;
    private RecyclerView recyclerView;
    private List<User> mUsers;

    EditText search_users;

    public UsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsers = new ArrayList<>();

        readUsers();

        search_users = view.findViewById(R.id.search_users);
        search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                search_users(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void search_users(String s) {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("search")
                .startAt(s)
                .endAt(s + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (search_users.getText().toString().equals("")) {


                    mUsers.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);

                        assert user != null;
                        assert firebaseUser != null;

                        if (!user.getId().equals(firebaseUser.getUid())) {
                            mUsers.add(user);
                        }
                    }

                    userAdapter = new UserAdapter(getContext(), mUsers, false);
                    recyclerView.setAdapter(userAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readUsers() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    assert user != null;
                    assert firebaseUser != null;
                    if (!user.getId().equals(firebaseUser.getUid())) {
                        mUsers.add(user);
                    }
                }

                userAdapter = new UserAdapter(getContext(), mUsers, false);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
