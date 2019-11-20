package edu.cnm.deepdive.chronometertest.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MainViewModel extends AndroidViewModel {

  private MutableLiveData<Long> accumulatedTime = new MutableLiveData<>(0L);
  private MutableLiveData<Boolean> running = new MutableLiveData<>(false);

  public MainViewModel(@NonNull Application application) {
    super(application);
  }

  public LiveData<Long> getAccumulatedTime() {
    return accumulatedTime;
  }

  public void setAccumulatedTime(long accumulatedTime) {
    this.accumulatedTime.setValue(accumulatedTime);
  }

  public LiveData<Boolean> getRunning() {
    return running;
  }

  public void setRunning(boolean running) {
    this.running.setValue(running);
  }

}
