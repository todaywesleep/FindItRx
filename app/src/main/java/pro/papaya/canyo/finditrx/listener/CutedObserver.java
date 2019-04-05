package pro.papaya.canyo.finditrx.listener;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class CutedObserver<T> implements Observer<T> {
  @Override
  public void onSubscribe(Disposable d) {

  }

  @Override
  public abstract void onNext(T t);

  @Override
  public abstract void onError(Throwable e);

  @Override
  public void onComplete() {

  }
}
