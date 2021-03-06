package kaleidot725.michetimer.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.appcompat.widget.PopupMenu
import android.util.Log
import android.view.View
import io.reactivex.disposables.CompositeDisposable
import kaleidot725.michetimer.model.service.TimerIndicator
import kaleidot725.michetimer.R
import kaleidot725.michetimer.model.entity.Timer
import kaleidot725.michetimer.model.repository.TimerRepository
import kaleidot725.michetimer.model.service.TimerService
import kaleidot725.michetimer.model.domain.timer.TimerRunnerState

class MainTimerViewModel(navigator : MainNavigator, timer : Timer, service : TimerService, repository: TimerRepository) : ViewModel() {
    val id : MutableLiveData<Int>
    val name : MutableLiveData<String>
    val state : MutableLiveData<String>
    val stateImage : MutableLiveData<Int>
    val remainSeconds : MutableLiveData<String>

    private val tag : String = "TimerViewModel"
    private val navigator : MainNavigator
    private val service : TimerService
    private val repository : TimerRepository
    private var runner : TimerIndicator
    private val timer : Timer
    private val listener : PopupMenu.OnMenuItemClickListener
    private val compositeDisposable : CompositeDisposable

    init
    {
        this.navigator = navigator
        this.service = service
        this.repository = repository
        this.timer = timer

        this.id = MutableLiveData()
        this.id.postValue(timer.id)
        this.name = MutableLiveData()
        this.name.postValue(timer.name)
        this.state = MutableLiveData()
        this.state.postValue(toStateString(TimerRunnerState.Init))
        this.stateImage = MutableLiveData()
        this.stateImage.postValue(toStateImage(TimerRunnerState.Init))
        this.remainSeconds = MutableLiveData()
        this.remainSeconds.postValue(toRemainSecondsString(timer.seconds))
        this.listener = PopupMenu.OnMenuItemClickListener {
            when(it?.itemId) {
                R.id.delete -> {
                    delete()
                    true
                }
                R.id.edit -> {
                    edit()
                    true
                }
                else -> {
                    false
                }
            }
        }

        this.runner = service.register(timer.id, timer.name, timer.seconds, timer.alarm) as TimerIndicator
        val stateDisposable = this.runner.state.subscribe {
            try {
                this.state.postValue(toStateString(it))
                this.stateImage.postValue(toStateImage(it))
            }
            catch(e : java.lang.Exception) {
                Log.v("TimerViewModel", e.toString())
            }
        }

        val remainSecondsDisposable = this.runner.remainSeconds.subscribe {
            try {
                this.remainSeconds.postValue(toRemainSecondsString(it))
            }
            catch(e : java.lang.Exception) {
                Log.v("TimerViewModel", e.toString())
            }
        }

        compositeDisposable = CompositeDisposable()
        compositeDisposable.addAll(stateDisposable, remainSecondsDisposable)
    }

    fun run(view: View) {
        try {
            when (this.runner.state.value) {
                TimerRunnerState.Init -> {
                    this.runner.run()
                }
                TimerRunnerState.Run -> {
                    this.runner.pause()
                }
                TimerRunnerState.Pause -> {
                    this.runner.run()
                }
                TimerRunnerState.Timeout -> {
                    this.runner.reset()
                }
                else -> {

                }
            }
        }
        catch (e : Exception) {
            Log.d(tag, e.toString())
        }
    }

    fun reset(view: View) {
        this.runner.reset()
    }

    fun display(view: View)
    {
        navigator.onStartDispTimer(timer)
    }

    fun delete()
    {
        service.unregister(timer.id)
        repository.remove(timer)
        compositeDisposable.dispose()
    }

    fun edit()
    {
        navigator.onStartEditTimer(timer)
        service.unregister(timer.id)
        compositeDisposable.dispose()
    }

    fun popupOption(view : View) {
        navigator.onShowOption(view, this.listener)
    }

    private fun toRemainSecondsString(remainSeconds : Long) =
        " ${(remainSeconds / 60).toString().padStart(2,'0')}:" +
        "${(remainSeconds % 60).toString().padStart(2,'0')}"

    private fun toStateString(state : TimerRunnerState) =
            when (state) {
                TimerRunnerState.Run     -> { "Pause"   }
                TimerRunnerState.Timeout -> { "Stop"    }
                TimerRunnerState.Init    -> { "Start"   }
                TimerRunnerState.Pause   -> { "Start"   }
            }

    private fun toStateImage(state : TimerRunnerState) =
            when (state) {
                TimerRunnerState.Run     -> { R.drawable.ic_pause  }
                TimerRunnerState.Timeout -> { R.drawable.ic_stop   }
                TimerRunnerState.Init    -> { R.drawable.ic_play   }
                TimerRunnerState.Pause   -> { R.drawable.ic_play   }
            }
}