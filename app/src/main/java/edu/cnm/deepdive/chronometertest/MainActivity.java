package edu.cnm.deepdive.chronometertest;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Chronometer;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import edu.cnm.deepdive.chronometertest.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {

  private MainViewModel viewModel;
  private Chronometer chronometer;
  private boolean running;
  private long accumulatedTime;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    viewModel.getRunning().observe(this, (running) -> {
      if (this.running != running) { // Change of state.
        if (running) {
          setTimer(accumulatedTime);
        } else {
          viewModel.setAccumulatedTime(getTimer());
        }
        this.running = running;
        invalidateOptionsMenu();
      }
    });
    viewModel.getAccumulatedTime().observe(this, (accumulatedTime) -> {
      this.accumulatedTime = accumulatedTime;
      if (chronometer != null) {
        chronometer.setBase(SystemClock.elapsedRealtime() - accumulatedTime);
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    getMenuInflater().inflate(R.menu.options, menu);
    MenuItem item = menu.findItem(R.id.time);
    ViewGroup layout = (ViewGroup) item.getActionView();
    if (chronometer == null) {
      chronometer = layout.findViewById(R.id.chronometer);
      setTimer(accumulatedTime, running);
    } else {
      layout.removeView(layout.findViewById(R.id.chronometer));
      ((ViewGroup) chronometer.getParent()).removeView(chronometer);
      layout.addView(chronometer);
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
        viewModel.setRunning(true);
        break;
      case R.id.pause:
        viewModel.setRunning(false);
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
    if (running) {
      viewModel.setAccumulatedTime(getTimer());
    }
  }

  private long getTimer() {
    return getTimer(true);
  }

  private long getTimer(boolean stop) {
    long offset = 0;
    if (chronometer != null) {
      if (stop) {
        chronometer.stop();
      }
      offset = SystemClock.elapsedRealtime() - chronometer.getBase();
    }
    return offset;
  }

  private void setTimer(long offset) {
    setTimer(offset, true);
  }

  private void setTimer(long offset, boolean start) {
    if (chronometer != null) {
      chronometer.setBase(SystemClock.elapsedRealtime() - offset);
      if (start) {
        chronometer.start();
      }
    }
  }

}
