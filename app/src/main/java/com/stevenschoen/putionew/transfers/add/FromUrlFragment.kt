package com.stevenschoen.putionew.transfers.add

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.jakewharton.rxbinding2.widget.RxTextView
import com.stevenschoen.putionew.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject

class FromUrlFragment : BaseFragment(R.id.addtransfer_link_destination_holder) {

  var callbacks: Callbacks? = null

  val link = BehaviorSubject.createDefault("")

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    if (savedInstanceState != null) {

    } else {
      if (arguments != null && arguments!!.containsKey(EXTRA_PRECHOSEN_LINK)) {
        link.onNext(arguments!!.getString(EXTRA_PRECHOSEN_LINK))
      }
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    val view = inflater.inflate(R.layout.addtransfer_link, container, false)

    val linkView = view.findViewById<EditText>(R.id.addtransfer_link_url)
    RxTextView.textChanges(linkView)
        .subscribe {
          link.onNext(it.toString())
        }
    if (savedInstanceState == null && arguments!!.containsKey(EXTRA_PRECHOSEN_LINK)) {
      linkView.setText(arguments!!.getString(EXTRA_PRECHOSEN_LINK))
    }

    val clearLinkView = view.findViewById<View>(R.id.addtransfer_link_clear)
    clearLinkView.setOnClickListener {
      linkView.text = null
    }

    val extractView = view.findViewById<CheckBox>(R.id.addtransfer_link_extract)

    val addView = view.findViewById<View>(R.id.addtransfer_link_add)
    addView.setOnClickListener {
      callbacks?.onLinkSelected(link.value!!, extractView.isChecked)
    }

    val cancelView = view.findViewById<View>(R.id.addtransfer_link_cancel)
    cancelView.setOnClickListener {
      dismiss()
    }

    link.observeOn(AndroidSchedulers.mainThread())
        .subscribe { newLink ->
          if (!newLink.isBlank()) {
            clearLinkView.visibility = View.VISIBLE
            addView.isEnabled = true
          } else {
            clearLinkView.visibility = View.GONE
            addView.isEnabled = false
          }
        }

    return view
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return super.onCreateDialog(savedInstanceState).apply {
      setTitle(R.string.add_transfer)
    }
  }

  interface Callbacks {
    fun onLinkSelected(link: String, extract: Boolean)
  }

  companion object {
    const val EXTRA_PRECHOSEN_LINK = "link"

    fun newInstance(context: Context, preChosenLink: String?): FromUrlFragment {
      val args = Bundle()
      preChosenLink?.let { args.putString(EXTRA_PRECHOSEN_LINK, it) }

      return Fragment.instantiate(context, FromUrlFragment::class.java.name, args) as FromUrlFragment
    }
  }
}
