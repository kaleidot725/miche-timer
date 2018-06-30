package kaleidot725.michetimer.addtimer

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.NumberPicker
import kaleidot725.michetimer.BR
import kaleidot725.michetimer.models.ViewModelFactory
import kaleidot725.michetimer.R
import kaleidot725.michetimer.databinding.FragmentAddTimerBinding

class AddTimerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        return inflater.inflate(R.layout.fragment_add_timer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProviders.of(this, ViewModelFactory).get(AddTimerViewModel::class.java)

        val nameEdit = view.findViewById<EditText>(R.id.name_edittext)
        nameEdit.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val name = nameEdit.text.toString()
                viewModel.name.postValue(name)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun afterTextChanged(p0: Editable?) { }
        })

        val hourPicker = view.findViewById<NumberPicker>(R.id.hour_picker_value)
        hourPicker.minValue = 0
        hourPicker.maxValue = 24
        hourPicker.setFormatter (object : NumberPicker.Formatter {
            override fun format(value: Int): String {
                return value.toString().padStart(2, '0')
            }
        })

        hourPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            val hour = picker.value.toLong()
            viewModel.hour.postValue(hour)
        }

        var minutePicker = view.findViewById<NumberPicker>(R.id.minute_picker_value)
        minutePicker.minValue = 0
        minutePicker.maxValue = 59
        minutePicker.setFormatter (object : NumberPicker.Formatter {
            override fun format(value: Int): String {
                return value.toString().padStart(2, '0')
            }
        })

        minutePicker.setOnValueChangedListener { picker, oldVal, newVal ->
            val minute = picker.value.toLong()
            viewModel.minute.postValue(minute)
        }

        var secondPicker = view.findViewById<NumberPicker>(R.id.second_picker_value)
        secondPicker.minValue = 0
        secondPicker.maxValue = 59
        secondPicker.setFormatter (object : NumberPicker.Formatter {
            override fun format(value: Int): String {
                return value.toString().padStart(2, '0')
            }
        })

        secondPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            val second = picker.value.toLong()
            viewModel.second.postValue(second)
        }

        val binding = DataBindingUtil.bind<FragmentAddTimerBinding>(view)
        binding?.setVariable(BR.viewmodel, viewModel)
    }
}
