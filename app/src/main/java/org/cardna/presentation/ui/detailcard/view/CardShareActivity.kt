package org.cardna.presentation.ui.detailcard.view

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.core.content.ContextCompat
import org.cardna.databinding.ActivityCardShareBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.CardNaApplication
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.R
import org.cardna.data.local.singleton.CardNaRepository
import org.cardna.presentation.util.setSrcWithGlide
import org.cardna.presentation.util.shortToast
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

@AndroidEntryPoint
class CardShareActivity : BaseViewUtil.BaseAppCompatActivity<ActivityCardShareBinding>(R.layout.activity_card_share) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        setClickListener()
    }

    override fun initView() {
        setSrcWithGlide(intent.getStringExtra(BaseViewUtil.CARD_IMG)!!, binding.ivCardShareImg)

        // 카드나, 카드너에 따라
        if(intent.getBooleanExtra(BaseViewUtil.IS_CARD_ME_OR_YOU, BaseViewUtil.CARD_ME)){ // 안넘겨주면, 카드나
            // 배경색 설정
            binding.ctlCardShareImage.setBackgroundResource(R.drawable.bg_cardme)

            // title 색 설정
            binding.tvCardshareTitle.setTextColor(ContextCompat.getColor(this, R.color.main_green))

            // title 설정
           if(CardNaRepository.userSocial == "naver"){
                binding.tvCardshareTitle.text = resources.getString(R.string.cardshare_cardme, CardNaRepository.naverUserfirstName)
            }
            else{
                binding.tvCardshareTitle.text = resources.getString(R.string.cardshare_cardme, CardNaRepository.kakaoUserfirstName)
            }
        }
        else{ // 카드너
            binding.ctlCardShareImage.setBackgroundResource(R.drawable.bg_cardyou)

            // title 색 설정
            binding.tvCardshareTitle.setTextColor(ContextCompat.getColor(this, R.color.main_purple))

            if(CardNaRepository.userSocial == "naver"){
                binding.tvCardshareTitle.text = resources.getString(R.string.cardshare_cardyou, CardNaRepository.naverUserfirstName)
            }
            else{
                binding.tvCardshareTitle.text = resources.getString(R.string.cardshare_cardyou, CardNaRepository.kakaoUserfirstName)
            }
        }

        binding.tvCardshareTitle.text = "임시로"
    }


    fun setClickListener() {

        // 저장하기 버튼 누르면
        binding.ivCardShareSave.setOnClickListener {
            saveCardImageToGallery(binding.ctlCardShareImage, "example")
        }

        // 공유하기 버튼 누르면
        binding.ivCardShare.setOnClickListener {

            //view->bitmap: 공유하고싶은 이미지 ctl view를 bitmap으로 변환 후
            val bitmap = viewToBitmap(binding.ctlCardShareImage)

            //bitmap->url
            val uri: Uri? = getImageUri(this, bitmap)

            //인텐트에 uri 넣어서 시작하기
            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, uri)
                type = "image/jpeg"
            }
            startActivity(Intent.createChooser(shareIntent, ""))
        }
    }

    fun viewToBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        return bitmap
    }

    private fun getImageUri(context: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        val path: String = MediaStore.Images.Media.insertImage(
            context.contentResolver,
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }

    // 특정 뷰 캡쳐해서 저장하기
    private fun saveCardImageToGallery(view: View?, title: String) {
        if (view == null) { // Null Point Exception ERROR 방지
            println("::::ERROR:::: view == NULL")
            return
        }

        view.buildDrawingCache() //캐시 비트 맵 만들기

        // 1
        val bitmap = view.drawingCache

        // 2
        var fos: OutputStream? = null

        // 3
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // 4
            this?.contentResolver?.also { resolver ->

                // 5
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, "$title.png")
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                // 6
                val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                // 7
                fos = imageUri?.let { resolver.openOutputStream(it) }

                Timber.d("Q 이상")
            }
        } else {
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, "$title.png")
            fos = FileOutputStream(image)
            Timber.d("Q 이하")
        }

        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            shortToast("사진이 저장되었습니다.")
        }
    }
}