package com.liangyx.undoredo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class DrawActivity extends Activity {

    FingerPaintView mView;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = new FingerPaintView(this);
        setContentView(mView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:
                mView.clear();
                break;
            case R.id.action_undo:
                mView.undo();
                break;
            case R.id.action_redo:
                mView.redo();
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }
}
