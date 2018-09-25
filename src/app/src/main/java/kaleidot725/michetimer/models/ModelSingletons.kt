package kaleidot725.michetimer.models

import kaleidot725.michetimer.addtimer.AddTimerNavigator
import kaleidot725.michetimer.main.MicheTimerNavigator
import kaleidot725.michetimer.models.timer.*

var micheTimerNavigator : MicheTimerNavigator? = null
var addTimerNavigator : AddTimerNavigator? = null
var timerService : TimerRunnerService?= null
var timerRepository : TimerRepository?= null