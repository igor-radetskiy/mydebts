package mydebts.android.app.ui.date

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.widget.DatePicker
import dagger.android.support.AndroidSupportInjection
import io.reactivex.subjects.PublishSubject
import mydebts.android.app.feature.date.Date
import javax.inject.Inject

class DatePickerFragment: DialogFragment(), DatePickerDialog.OnDateSetListener {

    @Inject @field:Date lateinit var dateSubject: PublishSubject<Triple<Int, Int, Int>>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        AndroidSupportInjection.inject(this)
        return DatePickerDialog(activity!!, 0, this,
                arguments!!.getInt(ARG_YEAR),
                arguments!!.getInt(ARG_MONTH),
                arguments!!.getInt(ARG_DAY_OF_MONTH))
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        dateSubject.onNext(Triple(year, month, dayOfMonth))
    }

    companion object {
        internal const val ARG_YEAR = "ARG_YEAR"
        internal const val ARG_MONTH = "ARG_MONTH"
        internal const val ARG_DAY_OF_MONTH = "ARG_DAY_OF_MONTH"

        fun newInstance(year: Int, month: Int, dayOfMonth: Int): DatePickerFragment =
            DatePickerFragment()
                    .apply {
                        arguments = Bundle()
                                .apply {
                                    putInt(ARG_YEAR, year)
                                    putInt(ARG_MONTH, month)
                                    putInt(ARG_DAY_OF_MONTH, dayOfMonth)
                                }
                    }
    }
}