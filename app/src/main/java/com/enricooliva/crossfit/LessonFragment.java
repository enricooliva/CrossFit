package com.enricooliva.crossfit;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.enricooliva.crossfit.data.DataContract;
import com.enricooliva.crossfit.data.Lesson;


public class LessonFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LESSON_LOADER = 0;
    private static final String LOG_TAG = "LessonFragment";
    private LessonAdapter mLessonAdapter;

    private static final String[] LESSON_COLUMN = {
            DataContract.LessonEntry.TABLE_NAME + "." + DataContract.LessonEntry._ID,
            DataContract.LessonEntry.ID,
            DataContract.LessonEntry.BOXID,
            DataContract.LessonEntry.DATE,
            DataContract.LessonEntry.MAXATTENDANCE,
            DataContract.LessonEntry.TYPE,
            DataContract.LessonEntry.DURATION
    };
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LessonFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mLessonAdapter = new LessonAdapter(getActivity(), null, 0);
        View rootView = inflater.inflate(R.layout.fragment_lesson, container, false);

        //athleteListView = getListView();
        // Get a reference to the ListView, and attach this adapter to it.
        ListView lessonListView = (ListView) rootView.findViewById(R.id.listLesson);

        //new Utility(this).execute(new String[]{"http://www.enricooliva.com/getAllPlayers.php"});

        lessonListView.setAdapter(mLessonAdapter);

        lessonListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = mLessonAdapter.getCursor();
                if (cursor != null && cursor.moveToPosition(position)) {
                    Lesson lesson = new Lesson(cursor);
                    // Creating an intent to open the activity LessonRegister
                    Intent intent = new Intent(getContext(), LessonRegister.class);

                    // Passing data as a parecelable object to LessonRegister
                    intent.putExtra("lesson",lesson);

                    // Opening the activity
                    startActivity(intent);

                }
            }
        });


        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(LESSON_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        //updateAthlete();
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(LESSON_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(
                getActivity(),
                DataContract.LessonEntry.CONTENT_URI,
                LESSON_COLUMN,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(LOG_TAG, "In onLoadFinished Lesson");
        mLessonAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mLessonAdapter.swapCursor(null);
    }

}
