package edu.cnm.deepdive.chronometertest;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Chronometer;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

  private Chronometer chronometer;
  private boolean running;
  private long pauseOffset;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    getMenuInflater().inflate(R.menu.options, menu);
    MenuItem item = menu.findItem(R.id.time);
    ViewGroup layout = (ViewGroup) item.getActionView();
    if (chronometer == null) {
      chronometer = layout.findViewById(R.id.chronometer);
      resumeTimer(); // Assumes you want to start running as soon as activity is loaded.
    } else {
      layout.removeView(layout.findViewById(R.id.chronometer)); // Remove inflated chronometer from new layout.
      ((ViewGroup) chronometer.getParent()).removeView(chronometer); // Detach previously loaded chronometer from its previous layout.
      layout.addView(chronometer); // Attach previously loaded chronometer to new layout.
    }
    return true;
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);
    menu.findItem(R.id.play).setVisible(!running);
    menu.findItem(R.id.pause).setVisible(running);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    boolean handled = true;
    switch (item.getItemId()) {
      case R.id.play:
        resumeTimer();
        break;
      case R.id.pause:
        pauseTimer();
        break;
      default:
        handled = super.onOptionsItemSelected(item);
        break;
    }
    return handled;
  }

  @Override
  protected void onPause() {
    super.onPause();
    pauseTimer();
  }

  private void pauseTimer() {
    if (running) {
      pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
      chronometer.stop();
      running = false;
      invalidateOptionsMenu();
    }
  }

  private void resumeTimer() {
    if (!running) {
      chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
      chronometer.start();
      running = true;
      invalidateOptionsMenu();
    }
  }


}
