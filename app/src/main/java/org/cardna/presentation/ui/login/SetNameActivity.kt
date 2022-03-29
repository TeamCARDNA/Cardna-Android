package org.cardna.presentation.ui.login

import android.app.ActivityOptions
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.core.text.set
import androidx.core.text.toSpannable
import androidx.core.widget.addTextChangedListener
import com.example.cardna.R
import com.example.cardna.databinding.ActivitySetNameBinding
import com.example.cardna.databinding.AlertSetNameBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.util.LinearGradientSpan

@AndroidEntryPoint
class SetNameActivity :
    BaseViewUtil.BaseAppCompatActivity<ActivitySetNameBinding>(R.layout.activity_set_name) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
        setClickListener()
        setChangedListener()
    }

    private fun setClickListener() {
        initAlertDialog()
    }

    private fun setChangedListener() {
        editTextChanged(binding.etSignupFirstname)
        editTextChanged(binding.etSignupLastname)

        firstnameCountChanged()
    }

    private fun buttonColorChanged() {
        val isFirstnameCheck = binding.etSignupFirstname.text.isNullOrBlank()
        val isLastnameCheck = binding.etSignupLastname.text.isNullOrBlank()

        with(binding.btnSignUpNameAccess) {
            isClickable = if (isFirstnameCheck || isLastnameCheck) {
                setBackgroundResource(R.drawable.bg_signup_white3_radius_10)
                setTextColor(getColor(R.color.white_2))
                false
            } else {
                setBackgroundResource(R.drawable.bg_signup_gradient_green_purple_radius_10)
                setTextColor(getColor(R.color.main_black))
                true
            }
        }
    }

    private fun initAlertDialog() {
        binding.btnSignUpNameAccess.setOnClickListener {
            val dialog = Dialog(this)
            val dialogBinding = AlertSetNameBinding.inflate(dialog.layoutInflater)
            dialog.setContentView(dialogBinding.root)

            val name = with(binding) { "${etSignupLastname.text}${etSignupFirstname.text}" }
            val firstname = binding.etSignupFirstname.text.toString()

            dialogBinding.tvAlertTitle.text =
                "${name}${getString(R.string.signup_tv_username_check)}"

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            with(dialogBinding) {
                negativeClickListener(btnAlertNegative, dialog)
                positiveClickListener(btnAlertPositive, dialog, firstname)
            }

            dialog.setCancelable(false)
            dialog.show()
        }
    }

    private fun negativeClickListener(button: Button, dialog: Dialog) {
        button.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun positiveClickListener(button: Button, dialog: Dialog, name: String) {
        button.setOnClickListener {
            dialog.dismiss()
            initAnimation(name)
        }
    }

    private fun editTextChanged(editText: EditText) {
        editText.addTextChangedListener {
            if (!editText.text.isNullOrEmpty()) {
                editText.background.setTint(getColor(R.color.white_1))
            } else {
                editText.background.setTint(getColor(R.color.white_4))
            }
            buttonColorChanged()
        }
    }

    private fun firstnameCountChanged() {
        with(binding) {
            etSignupFirstname.addTextChangedListener {
                firstnameCount = if (!it.isNullOrEmpty()) "${it.length}/10" else "0/10"
            }
        }
    }

    private fun initAnimation(name: String) {
        val welcomeText = "${name}님 반가워요!"
        val gradientText = setTextGradient(welcomeText)
        binding.tvSetnameUsername.text = gradientText
        binding.tvSetnameUsername.visibility = View.VISIBLE
        binding.clSetnameContainer.visibility = View.GONE

        startSetNameFinishedActivity(welcomeText)

    }

    private fun setTextGradient(welcomeText: String): Spannable {
        val green = getColor(R.color.main_green)
        val purple = getColor(R.color.main_purple)
        val spannable = welcomeText.toSpannable()
        spannable[0..welcomeText.length] =
            LinearGradientSpan(welcomeText, welcomeText, green, purple)
        return spannable
    }

    private fun startSetNameFinishedActivity(welcomeText: String) {
        val intent = Intent(this, SetNameFinishedActivity::class.java)
        val bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
        intent.putExtra("welcomeText", welcomeText)
        startActivity(intent, bundle)
    }
}
