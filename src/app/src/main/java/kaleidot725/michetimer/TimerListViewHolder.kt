package kaleidot725.michetimer

import android.arch.lifecycle.LifecycleOwner
import android.support.v7.widget.RecyclerView
import kaleidot725.michetimer.databinding.TimerListViewItemBinding

class TimerListViewHolder(owner: LifecycleOwner, binding: TimerListViewItemBinding) : RecyclerView.ViewHolder(binding.root) {
    private val owner : LifecycleOwner = owner
    private val binding : TimerListViewItemBinding = binding

    fun bind (timerViewModel : TimerViewModel?) {
        binding.timerViewModel = timerViewModel
        binding.executePendingBindings()
        binding.setLifecycleOwner(owner)
    }
}