package com.grey.niko.firebasetest.livedata;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * <h1>FirebaseQueryLiveData</h1>
 * <p>Uses a Firebase Query to listen for changes in a Firebase Realtime Database. It will notify
 * an observing Activity of changes so the Activity can update its UI.</p>
 *
 * @version 1.0.0
 * @see <a href="https://firebase.googleblog.com/2017/12/using-android-architecture-components.html">
 *     Using Android Architecture Components with Firebase Realtime Database (Part 1)</a>
 */
public class FirebaseQueryLiveData extends LiveData<DataSnapshot> {

    private final String LOG_TAG = "FirebaseQueryLiveData";
    private final Query query;
    private final CustomValueEventListener listener = new CustomValueEventListener();

    /**
     * Overloaded constructor that takes a Query to use as the listener for the Database.
     * @param query The Query to use as a listener on the Firebase Realtime Database.
     */
    public FirebaseQueryLiveData(Query query) {
        this.query = query;
    }

    /**
     * Overloaded constructor that takes a DatabaseReference to use as the listener for the Database.
     * @param dbRef The DatabaseReference to use as a listener on the Firebase Realtime Database.
     */
    public FirebaseQueryLiveData(DatabaseReference dbRef) {
        this.query = dbRef;
    }

    /**
     * Attaches the listener in LiveData's lifecycle-aware onActive method.
     */
    @Override
    protected void onActive() {
        Log.i(LOG_TAG, "onActive");
        query.addValueEventListener(listener);
    }

    /**
     * Detaches the listener in LiveData's lifecycle-aware onInactive method.
     */
    @Override
    protected void onInactive() {
        Log.i(LOG_TAG, "onInactive");
        query.removeEventListener(listener);
    }

    /**
     * Creates a ValueEventListener for the Query which will listen for changes in the Database and
     * set the value of the data upon change. If a DatabaseError occurs, it will be logged to the
     * standard console.
     */
    private class CustomValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            setValue(snapshot);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.i(LOG_TAG, "Listener for query encountered an error: " + error);
        }
    }

}
