package com.example.android.yamsd;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import static com.google.common.base.Preconditions.checkNotNull;


public class AboutFragment extends Fragment {

    public AboutFragment() {
    }

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                CacheAndListBuffer
                        .getCacheAndListBuffer(
                                getActivity()
                        )
                        .updateArtists(true);
                return true;
            case R.id.action_about:
                AboutFragment aboutFragment =
                        AboutFragment.newInstance();

                android.support.v4.app.FragmentTransaction fragmentTransaction =
                        getFragmentManager().beginTransaction();

                fragmentTransaction.replace(
                        R.id.fragment_container,
                        aboutFragment
                );
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                return true;
            case R.id.action_feedback:
                EmailSender.sendMessage(getContext());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        View sendEmailButton =
                rootView.findViewById(R.id.SEND_EMAIL_BUTTON);

        sendEmailButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EmailSender.sendMessage(getContext());
                    }
                }
        );
        return rootView;

    }


    @Override
    public void onResume() {
        super.onResume();
        checkNotNull(((AppCompatActivity) getActivity())
                .getSupportActionBar())
                .setTitle("О приложении");
    }
}
