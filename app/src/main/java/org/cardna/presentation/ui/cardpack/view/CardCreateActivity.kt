package org.cardna.presentation.ui.cardpack.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.amplitude.api.Amplitude
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import land.sungbin.systemuicontroller.setSystemBarsColor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.cardna.R
import org.cardna.data.local.singleton.CardNaRepository
import org.cardna.databinding.ActivityCardCreateBinding
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.cardpack.viewmodel.CardCreateViewModel
import org.cardna.presentation.ui.login.view.SetNameFinishedActivity
import org.cardna.presentation.util.MultiPartResolver
import org.cardna.presentation.util.shortToast
import org.cardna.ui.cardpack.BottomDialogImageFragment
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream

@AndroidEntryPoint
class CardCreateActivity :
    BaseViewUtil.BaseAppCompatActivity<ActivityCardCreateBinding>(R.layout.activity_card_create) {

    private val cardCreateViewModel: CardCreateViewModel by viewModels()
    private val multiPartResolver = MultiPartResolver(this)

    val itemClick: () -> Unit = {
        with(binding) {
            ivCardcreateGalleryImg.setImageResource(cardCreateViewModel?.imgIndex!!) // bottomDialog 에서 설정해준 imgIndex 를 Iv에 띄워주기
            ivCardcreateGalleryImg.visibility = View.VISIBLE
            cardCreateViewModel?.setUri(null) // 갤러리이미지 선택 후, 다시 symbolId 선택할 수도 있으니 uri 는 null 로 해줘야함


            checkCompleteTvClickable() // 이미지 선택 되었으니 카드작성완료 tv 누를 수 있는지 다시 검사

            ctlCardcreateImg.visibility =
                View.INVISIBLE // visibility말고 background를 검정으로 바꾸면 계속 선택가능하지 않을까
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        initView()
    }

    private fun initViewModel() {  // viewModel 초기화, Intent 로 전달받은 data 들 viewModel 프로퍼티에 대입
        binding.cardCreateViewModel = cardCreateViewModel
        val isCardMeOrYou = intent.getBooleanExtra(
            BaseViewUtil.IS_CARD_ME_OR_YOU,
            BaseViewUtil.CARD_YOU
        )
        cardCreateViewModel.setIsCardMeOrYou(
            isCardMeOrYou
        )
        cardCreateViewModel.setUserId(
            intent.getIntExtra(
                BaseViewUtil.ID,
                -1
            )
        )

        cardCreateViewModel.setUserName(
            intent.getStringExtra(BaseViewUtil.NAME) ?: CardNaRepository.kakaoUserfirstName
        )
    }

    override fun initView() {
        /** 카드추가 유도뷰 로직 */
        intent.getStringExtra(SetNameFinishedActivity.GO_TO_CARDCREAT_ACTIVITY_KEY)?.let {
            cardCreateViewModel.setIsCardMeOrYou(true) //카드나 작성으로 되어야함
            cardCreateViewModel.setInduceMakeMainCard(true) //유도뷰 분기처리를 위함
            setCardInduceListener()
            Amplitude.getInstance().logEvent("Membership_WritingCardna_Finish")
        } ?: makeCardListener()

        this.setSystemBarsColor(Color.BLACK, false)
        setObserver()
        setView() // editText 글자 수에 따라 글자 수 업데이트, 버튼 선택가능하도록
        setChooseCardListener() // 이미지 ctl 눌렀을 때 bottomDialog 띄우도록
        setEnterKeyEnabled() // 줄바꿈 불가능하도록
    }

    private fun setView() {
        setHideKeyboard()
        binding.ivCardcreateGalleryImg.clipToOutline = true
        binding.tvCardcreateComplete.isClickable = false

    }

    private fun setObserver() {
        cardCreateViewModel.etKeywordText.observe(this) {
            cardCreateViewModel.setEtKeywordLength(binding.etCardcreateKeyword.length())
        }

        cardCreateViewModel.etDetailText.observe(this) {
            cardCreateViewModel.setEtDetailLength(binding.etCardcreateDetail.length())
        }

        cardCreateViewModel.etKeywordLength.observe(this) {
            checkCompleteTvClickable()
        }

        cardCreateViewModel.etDetailLength.observe(this) {
            checkCompleteTvClickable()
        }

        cardCreateViewModel.ifChooseImg.observe(this) {
            checkCompleteTvClickable()
        }
    }

    private fun setChooseCardListener() {
        binding.ctlCardcreateImg.setOnClickListener {
            binding.ctlCardcreateImg.isClickable = false

            val bottomDialogImageFragment = BottomDialogImageFragment()
            bottomDialogImageFragment.arguments = Bundle().apply {
                putParcelable(BaseViewUtil.BOTTOM_IMAGE, BottomImageLamdaData(itemClick))
            }
            bottomDialogImageFragment.show(supportFragmentManager, bottomDialogImageFragment.tag)


            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                binding.ctlCardcreateImg.isClickable = true
            }, 1000)
        }

        binding.ivCardcreateGalleryImg.setOnClickListener {

            binding.ivCardcreateGalleryImg.isClickable = false

            val bottomDialogImageFragment = BottomDialogImageFragment()
            bottomDialogImageFragment.arguments = Bundle().apply {
                putParcelable(BaseViewUtil.BOTTOM_IMAGE, BottomImageLamdaData(itemClick))
            }
            bottomDialogImageFragment.show(supportFragmentManager, bottomDialogImageFragment.tag)


            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                binding.ivCardcreateGalleryImg.isClickable = true
            }, 1000)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setEnterKeyEnabled() {

        binding.etCardcreateDetail.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                Timber.e("Enter key 입력")

                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.etCardcreateDetail.windowToken, 0)

                return@OnKeyListener true
            }
            false
        })

        binding.etCardcreateDetail.setOnTouchListener { view, event ->
            view.parent.requestDisallowInterceptTouchEvent(true)
            if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                view.parent.requestDisallowInterceptTouchEvent(false)
            }
            return@setOnTouchListener false
        }

        binding.tvCardcreateTitle.setOnClickListener {
            binding.etCardcreateKeyword.requestFocus()
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.etCardcreateKeyword, 0)
        }
    }

    private fun checkCompleteTvClickable() {
        if (cardCreateViewModel.etKeywordLength.value!! > 0 && cardCreateViewModel.etDetailLength.value!! > 0 && cardCreateViewModel.ifChooseImg.value!!) {
            with(binding) {
                tvCardcreateComplete.isClickable = true
                tvCardcreateComplete.isEnabled = true
                tvCardcreateComplete.setTextColor(resources.getColor(R.color.white_1))
            }
        } else {
            with(binding) {
                tvCardcreateComplete.isClickable = false
                tvCardcreateComplete.isEnabled = false
                tvCardcreateComplete.setTextColor(resources.getColor(R.color.white_4))
            }
        }
    }


    private fun makeCardListener() {
        binding.tvCardcreateComplete.setOnClickListener {
            binding.tvCardcreateComplete.isClickable = false
            cardCreateViewModel.uri.observe(this) {
                if (it == null) {
                    cardCreateViewModel.makeCard(null)
                } else {
                    cardCreateViewModel.setLoadingState(true)
                    lifecycleScope.launch {
                        CoroutineScope(Dispatchers.IO).launch {
                            cardCreateViewModel.makeCard(
                                multiPartResolver.createImgMultiPart(
                                    cardCreateViewModel.uri.value!!
                                )
                            )
                        }
                    }
                }
            }

            cardCreateViewModel.makeCardSuccess.observe(this) { makeCardSuccess ->
                if (makeCardSuccess)
                    if (cardCreateViewModel.isCardMeOrYou!!) {
                        val intent =
                            Intent(this@CardCreateActivity, CardCreateCompleteActivity::class.java)
                        intent.putExtra(
                            BaseViewUtil.IS_CARD_ME_OR_YOU,
                            cardCreateViewModel.isCardMeOrYou
                        ) // 현재는 카드나 작성이므로 CARD_ME를 보내줌
                        intent.putExtra(
                            BaseViewUtil.SYMBOL_ID,
                            cardCreateViewModel.symbolId
                        ) // 심볼 - symbolId값, 갤러리 - null
                        intent.putExtra(
                            BaseViewUtil.CARD_IMG,
                            cardCreateViewModel.uri.value.toString()
                        ) // 심볼 - null, 갤러리 - uri 값
                        intent.putExtra(
                            BaseViewUtil.CARD_TITLE,
                            cardCreateViewModel.etKeywordText.value
                        )

                        binding.tvCardcreateComplete.isClickable = false
                        startActivity(intent)

                    } else {
                        val isCardPackOrMainCard = intent.getBooleanExtra(
                            BaseViewUtil.IS_CARDPACK_OR_MAINCARD,
                            BaseViewUtil.FROM_MAINCARD
                        )
                        val newIntent =
                            Intent(
                                this@CardCreateActivity,
                                OtherCardCreateCompleteActivity::class.java
                            )
                        newIntent.putExtra(
                            BaseViewUtil.IS_CARDPACK_OR_MAINCARD, isCardPackOrMainCard
                        )
                        newIntent.putExtra(
                            BaseViewUtil.IS_CODE_OR_FRIEND, intent.getBooleanExtra(
                                BaseViewUtil.IS_CODE_OR_FRIEND,
                                BaseViewUtil.FROM_FRIEND  // 안넘겨줬다면 Friend로 부터, 즉 마이페이지에서 친구 클릭
                            )
                        )
                        startActivity(newIntent)
                    }
            }
        }
    }

    /** 카드추가 유도뷰일 때 클릭 이벤트 */
    private fun setCardInduceListener() {
        binding.tvCardcreateComplete.setOnClickListener {
            Amplitude.getInstance().logEvent("Membership_WritingCardna_Finish")
            if (cardCreateViewModel.uri.value == null) {
                cardCreateViewModel.makeCard(null)
            } else {
                cardCreateViewModel.setLoadingState(true)
                lifecycleScope.launch {
                    CoroutineScope(Dispatchers.IO).launch {
                        cardCreateViewModel.makeCard(
                            multiPartResolver.createImgMultiPart(
                                cardCreateViewModel.uri.value!!
                            )
                        )
                    }
                }
            }

            cardCreateViewModel.makeInduceCardSuccess.observe(this) { makeInduceCardSuccess ->
                if (makeInduceCardSuccess)
                    cardCreateViewModel.induceCardId.observe(this) { induceCardId ->
                        makeCardInduceListener(induceCardId)
                    }
            }
        }
    }

    fun makeCardInduceListener(cardId: Int) {
        if (cardCreateViewModel.isCardMeOrYou!!) {
            val intent = Intent(this@CardCreateActivity, CardCreateCompleteActivity::class.java)
            intent.putExtra(
                BaseViewUtil.IS_CARD_ME_OR_YOU,
                cardCreateViewModel.isCardMeOrYou
            )
            intent.putExtra(
                BaseViewUtil.SYMBOL_ID,
                cardCreateViewModel.symbolId
            ) // 심볼 - symbolId값, 갤러리 - null
            intent.putExtra(
                BaseViewUtil.CARD_IMG,
                cardCreateViewModel.uri.value.toString()
            )  // 심볼 - null, 갤러리 - uri 값
            intent.putExtra(
                BaseViewUtil.CARD_TITLE,
                cardCreateViewModel.etKeywordText.value
            )
            intent.putExtra(
                SetNameFinishedActivity.GO_TO_CARDCREAT_ACTIVITY_KEY, true
            )
            intent.putExtra(
                "INDUCE_CARD_ID", cardId
            )
            binding.tvCardcreateComplete.isClickable = false
            startActivity(intent)
        }
    }

    fun checkPermission() { // BottomSheet 에서 접근해야 하므로 public
        val cameraPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        if (cameraPermission == PackageManager.PERMISSION_GRANTED) {
            //프로그램 진행
            Timber.e("접근권한 있음")
            startProcess()
        } else {
            //권한요청
            Timber.e("접근권한 없음")
            requestPermission()
        }
    }

    private fun startProcess() {
        getResultText.launch(
            Intent(
                Intent.ACTION_GET_CONTENT,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
        )
    }

    val getResultText =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) // 이거 deprecated 되지 않았나 ?
        { result: ActivityResult ->
            result.data?.let { intent ->
                intent.data?.let { uri ->
                    cardCreateViewModel.setUri(uri)  // Intent를 반환 -> Intent에서 Uri로 get하기
                    cardCreateViewModel.setSymbolId(null) // 전에 symbol 선택 후, 다시 갤러리 이미지를 선택했을 경우, 다시 symbolId null로
                    cardCreateViewModel.setIfChooseImg(true)
                    Glide.with(this).load(cardCreateViewModel.uri.value)
                        .into(binding.ivCardcreateGalleryImg)
                    Timber.e("uri 값은  : ${cardCreateViewModel.uri.value}")

                    binding.ivCardcreateGalleryImg.visibility = View.VISIBLE // imageView는 보이도록
                    binding.ctlCardcreateImg.visibility = View.INVISIBLE // 이제 ctl은 invisible
                    checkCompleteTvClickable()
                }
            }
        }

    private fun requestPermission() {
        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission())
        { isGranted: Boolean ->

            when (isGranted) {
                true -> startProcess() // 권한이 있다면 진행
                false -> shortToast("갤러리 권한을 허용해주세요.")
            }
        }


    private fun makeUriToFile(): MultipartBody.Part {
        val options = BitmapFactory.Options()
        val inputStream: InputStream =
            requireNotNull(contentResolver.openInputStream(cardCreateViewModel.uri.value!!)) // 여기서 문제인가 ?

        val bitmap = BitmapFactory.decodeStream(inputStream, null, options)

        val byteArrayOutputStream = ByteArrayOutputStream()

        bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)

        val fileBody = RequestBody.create(
            "image/png".toMediaTypeOrNull(),
            byteArrayOutputStream.toByteArray()
        )

        val part = MultipartBody.Part.createFormData(
            "image",
            File("${cardCreateViewModel.uri.toString()}.png").name,
            fileBody
        )

        return part
    }

    private fun setHideKeyboard() {
        binding.clCardcreateScroll.setOnClickListener {
            val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(binding.etCardcreateDetail.windowToken, 0)
        }
    }
}