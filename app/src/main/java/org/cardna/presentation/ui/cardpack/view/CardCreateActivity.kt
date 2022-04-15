package org.cardna.presentation.ui.cardpack.view

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.example.cardna.R
import com.example.cardna.databinding.ActivityCardCreateBinding
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.cardpack.viewmodel.CardCreateViewModel
import org.cardna.presentation.util.initRootClickEvent
import org.cardna.presentation.util.shortToast
import org.cardna.ui.cardpack.BottomDialogImageFragment
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream

// 1. 내 카드팩에서 카드나 작성
// 2. 친구 카드팩에서 카드너 작성

@AndroidEntryPoint
class CardCreateActivity :
    BaseViewUtil.BaseAppCompatActivity<ActivityCardCreateBinding>(R.layout.activity_card_create) {

    private val cardCreateViewModel: CardCreateViewModel by viewModels()
    // 서버 통신시 쓰이는 이 프로퍼티들과, 이를 조작하고, 서버통신 하는 메서드들 viewModel 로 이동해야 할 듯
    // responseData 형식과 같다면, model 에 data class 를 정의하고 viewModel에서 이 type의 객체 하나를 선언해주고, 그 안으로 접근 ?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        initView()
    }

    private fun initViewModel() {  // viewModel 초기화, Intent로 전달받은 data들 viewModel 프로퍼티에 대입
        binding.cardCreateViewModel = cardCreateViewModel
        cardCreateViewModel.setIsCardMeOrYou(
            intent.getBooleanExtra(
                "isCardMeOrYou",
                true
            )
        ) // 안넘겨주면 cardMe
        cardCreateViewModel.setUserId(intent.getIntExtra("id", -1)) // 내 카드나일 경우 null로 setting 되도록
        cardCreateViewModel.setUserName(intent.getStringExtra("name")) // 안넘겨주면 null ?
    }

    override fun initView() {
        setObserver()
        setView() // editText 글자 수에 따라 글자 수 업데이트, 버튼 선택가능하도록
        setChooseCardListener() // 이미지 ctl 눌렀을 때 bottomdialog 띄우도록
        makeCardListener()
    }


    // 일단 카드작성완료 tv 선택 불가능하도록, editText 글자 수에 따라 글자 수 업데이트 해주고, 버튼 선택가능하도록
    private fun setView() {
        initRootClickEvent(binding.tvCardcreateTitle)
        initRootClickEvent(binding.svCardcreateTop)

        binding.ivCardcreateGalleryImg.clipToOutline = true
        binding.tvCardcreateComplete.isClickable = false;  // 일단 카드 작성 완료 textView 클릭 안되도록

        binding.etCardcreateKeyword.addTextChangedListener { // editText 의 내용이 바뀔때마다
            cardCreateViewModel.setEtKeywordLength(binding.etCardcreateKeyword.length())
            // 뷰모델 프로퍼티 etKeyWordLength 값 업데이트 해주기만 하면, xml 레이아웃에 결합된 변수가 자동으로 뷰에 업데이트
            checkCompleteTvClickable()
        }

        binding.etCardcreateDetail.addTextChangedListener {
            cardCreateViewModel.setEtDetailLength(binding.etCardcreateDetail.length())
            checkCompleteTvClickable()
        }
    }

    private fun setObserver() { // addTextChangedListener 대신 이거  ?
        cardCreateViewModel.etKeywordText.observe(this) { // keyword의 값이 바뀔 때, keywordLength의 값을 업데이트
            cardCreateViewModel.setEtKeywordLength(binding.etCardcreateKeyword.length())
            // 뷰모델 프로퍼티 etKeyWordLength 값 업데이트 해주기만 하면, xml 레이아웃에 결합된 변수가 자동으로 뷰에 업데이트
        }

        cardCreateViewModel.etDetailText.observe(this) { // detail의 값이 바뀔 때, detailLength의 값을 업데이트
            cardCreateViewModel.setEtDetailLength(binding.etCardcreateDetail.length())
        }

        // 카드 작성 완료 tv 선택가능하도록 바꿀지 검사하는 상황
        // 1. etKeywordLength의 값이 바뀔 때
        cardCreateViewModel.etKeywordLength.observe(this) {
            checkCompleteTvClickable()
        }

        // 2. etDetailLength의 값이 바뀔 때
        cardCreateViewModel.etDetailLength.observe(this) {
            checkCompleteTvClickable()
        }

        // 3. ifChooseImg의 값이 바뀔 때
        cardCreateViewModel.ifChooseImg.observe(this) {
            checkCompleteTvClickable()
        }
    }

    private fun setChooseCardListener() {
        binding.ctlCardcreateImg.setOnClickListener {
            // 이미지 선택 cl 클릭 시, 이러한 람다를 정의해서, bottomDialogImageFragment 를 생성한 후, 이를 show()
            val bottomDialogImageFragment = BottomDialogImageFragment {
                // image 선택 dialog 에서 심볼이 하나라도 선택이 되어서 완료 버튼을 누르면 dialog가 닫히면 실행되는 함수
                // 바로 갤러리 접근을 누른다면 이것이 실행되지 않으므로 symbolId는 초기값인 null일 것

                with(binding) {
                    // 각 심볼 이미지 띄워주기
                    ivCardcreateGalleryImg.setImageResource(cardCreateViewModel?.imgIndex!!) // bottomDialog 에서 설정해준 imgIndex 를 Iv에 띄워주기
                    ivCardcreateGalleryImg.visibility = View.VISIBLE
                    cardCreateViewModel?.setUri(null) // 갤러리이미지 선택 후, 다시 symbolId 선택할 수도 있으니 uri 는 null 로 해줘야함

                    // 이제 여기서 iv는 클릭 안되도록 하고, cl은 보이진 않지만 계속 클릭되도록 로직 수정

                    checkCompleteTvClickable() // 이미지 선택 되었으니 카드작성완료 tv 누를 수 있는지 다시 검사

                    ctlCardcreateImg.visibility =
                        View.INVISIBLE // visibility말고 background를 검정으로 바꾸면 계속 선택가능하지 않을까
                }
            }
            bottomDialogImageFragment.show(supportFragmentManager, bottomDialogImageFragment.tag)
        }

        // 첫번째 심볼이나 갤러리 이미지 선택 후, 이미지가 보이게 될 것. 그러면 이 이미지뷰를 눌러도 다시 이미지를 선택할 수 있도록
        // 리스너 달아주기
        binding.ivCardcreateGalleryImg.setOnClickListener{
            val bottomDialogImageFragment = BottomDialogImageFragment { // 위와 동일
                with(binding) {
                    ivCardcreateGalleryImg.setImageResource(cardCreateViewModel?.imgIndex!!)
                    ivCardcreateGalleryImg.visibility = View.VISIBLE
                    checkCompleteTvClickable()
                    ctlCardcreateImg.visibility =
                        View.INVISIBLE
                }
            }
            bottomDialogImageFragment.show(supportFragmentManager, bottomDialogImageFragment.tag)
        }
    }


    private fun checkCompleteTvClickable() { // 카드제목과 카드내용이 둘다 입력되어있고 카드 이미지가 선택되었으면, 카드 완료 버튼 클릭 되도록
        // 실행해주는 경우 : 1. etKeyword의 값이 바뀔 때 2. etDetail의 값이 바뀔 때 3. 이미지나 심볼의 값이 바뀔 때
        if (cardCreateViewModel.etKeywordLength.value!! > 0 && cardCreateViewModel.etDetailLength.value!! > 0 && cardCreateViewModel.ifChooseImg.value!!) {
            with(binding) {
                tvCardcreateComplete.isClickable = true // 위
                tvCardcreateComplete.isEnabled = true   // 아래 둘중 하나는 없애도 되지 않을까
                tvCardcreateComplete.setTextColor(resources.getColor(R.color.white_1))
            }
        } else {
            with(binding) {
                tvCardcreateComplete.isClickable = false;
                tvCardcreateComplete.isEnabled = false;
                tvCardcreateComplete.setTextColor(resources.getColor(R.color.white_4))
            }
        }
    }


    /// **************************** 카메라 접근 코드 ****************************
    private fun resToUri(resId: Int): Uri =
        Uri.Builder().scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(resources.getResourcePackageName(resId))
            .appendPath(resources.getResourceTypeName(resId))
            .appendPath(resources.getResourceEntryName(resId))
            .build()


    // 카드나 만들기 버튼 눌렀을 때,
    // 1. 뷰모델의 카드작성 서버통신 메서드 호출해서 서버에 data 전달
    // 2. cardCreateCompleteActivity 인텐트로 이동
    private fun makeCardListener() {
        binding.tvCardcreateComplete.setOnClickListener {
            // 카드나 만들기 버튼을 눌렀을 때
            // 1. 서버로 title, content, symbolId, uri 전송
            // symbolId - 카드 이미지 심볼 id, 이미지가 있는 경우 null을 보내주면 됨
            cardCreateViewModel.makeCard(makeUriToFile())
            // 2. cardCreateCompleteActivity로 인텐트로 이동


            // 이 데이터들을 뷰 모델에 넣어서 뷰모델에 공유하고 싶지만, 액티비티끼리는 공유x => 인텐트로 그냥 해야될듯
            val intent = Intent(this@CardCreateActivity, CardCreateCompleteActivity::class.java)
            intent.putExtra(
                "isCardMeOrYou",
                cardCreateViewModel.isCardMeOrYou
            ) // 현재는 카드나 작성이므로 CARD_ME를 보내줌
            intent.putExtra("symbolId", cardCreateViewModel.symbolId) // 심볼 - symbolId값, 갤러리 - null
            intent.putExtra("cardImg", cardCreateViewModel.uri.toString()) // 심볼 - null, 갤러리 - uri 값
            intent.putExtra("cardTitle", cardCreateViewModel.etKeywordText.value)
            startActivity(intent)
        }
    }


    // 갤러리에서 가져오기 클릭 시, uri 값을 설정해줬을 것이고,
    // 이제 완료 버튼 눌렀을 때, 설정된 uri 값을 서버에 보내기 위해 멀티파트로 바꿔주는 함수
    private fun makeUriToFile(): MultipartBody.Part {
        val options = BitmapFactory.Options()
        val inputStream: InputStream =
            requireNotNull(contentResolver.openInputStream(cardCreateViewModel.uri!!)) // 여기서 문제인가 ?
        val bitmap = BitmapFactory.decodeStream(inputStream, null, options)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap!!.compress(Bitmap.CompressFormat.PNG, 20, byteArrayOutputStream)
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


    // gallery access
    // BottomSheet 에서 갤러리에서 가져오기를 눌렀을 때, 그 이후 과정의 코드
    // 결국 cardPackViewModel 의 uri 값을 세팅해줌
    fun checkPermission() { // BottomSheet 에서 접근해야 하므로 public
        val cameraPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        if (cameraPermission == PackageManager.PERMISSION_GRANTED) {
            //프로그램 진행
            startProcess()
        } else {
            //권한요청
            requestPermission()
        }
    }

    private fun startProcess() {
        val intent = Intent().apply {
            setType("image/*")
            setAction(Intent.ACTION_GET_CONTENT)
        }
        getResultText.launch(intent)
    }

    val getResultText =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) // 이거 deprecated 되지 않았나 ?
        { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                cardCreateViewModel.setUri(result.data?.data)  // Intent를 반환 -> Intent에서 Uri로 get하기
                cardCreateViewModel.setSymbolId(null) // 전에 symbol 선택 후, 다시 갤러리 이미지를 선택했을 경우, 다시 symbolId null로
                cardCreateViewModel.setIfChooseImg(true)
                Glide.with(this).load(cardCreateViewModel.uri).into(binding.ivCardcreateGalleryImg)

                binding.ivCardcreateGalleryImg.visibility = View.VISIBLE // imageView는 보이도록
                binding.ctlCardcreateImg.visibility = View.INVISIBLE // 이제 ctl은 invisible
                checkCompleteTvClickable()
            }
            //else if (result.resultCode == Activity.RESULT_CANCELED) {} =>Activity.RESULT_CANCELED일때 처리코드가 필요하다면
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

    companion object {
        const val SYMBOL_0 = 1
        const val SYMBOL_1 = 2
        const val SYMBOL_2 = 3
        const val SYMBOL_3 = 4
        const val SYMBOL_4 = 5
        const val GALLERY = 6
    }
}