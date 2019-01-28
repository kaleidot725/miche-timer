package kaleidot725.michetimer.main

import android.app.Activity
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kaleidot725.michetimer.BR
import kaleidot725.michetimer.DaggerMicheTimerApplicationComponent
import kaleidot725.michetimer.MainFragmentModule
import kaleidot725.michetimer.R
import kaleidot725.michetimer.app.MicheTimerApplication
import kaleidot725.michetimer.databinding.FragmentMainBinding
import kaleidot725.michetimer.domain.TimerRepository
import kaleidot725.michetimer.domain.TimerRunnerService
import javax.inject.Inject

class MainFragment : Fragment() {

    @Inject
    lateinit var timerRepository : TimerRepository

    @Inject
    lateinit var timerRunnerService : TimerRunnerService

    @Inject
    lateinit var navigator : MainNavigator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activityComponent =  (activity as MainActivity).component
        activityComponent.plus(MainFragmentModule()).inject(this)

        val vm = ViewModelProviders.of(this, MainViewModelFactory()).get(MainViewModel::class.java)
        val binding = DataBindingUtil.bind<FragmentMainBinding>(this.view as View)
        binding?.setVariable(BR.mainViewModel, vm)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = MainTimerListAdapter(vm)
        }

        vm.onRemoveEvent = { i ->
            recyclerView.adapter?.notifyItemRemoved(i)
        }

        vm.onAddEvent = { i->
            recyclerView.adapter?.notifyItemInserted(i)
            recyclerView.adapter?.notifyItemChanged(i)
        }

        vm.onChanged = {
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }

    inner class MainViewModelFactory : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass == MainViewModel::class.java) {
                return MainViewModel(navigator, timerRunnerService, timerRepository) as T
            }

            throw IllegalArgumentException("Unknown ViewModel class : ${modelClass.name}")
        }
    }
}
