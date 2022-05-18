package org.cardna.presentation.ui.detailcard.view

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Environment.DIRECTORY_PICTURES
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.drawToBitmap
import org.cardna.databinding.ActivityCardShareBinding
import dagger.hilt.android.AndroidEntryPoint
import land.sungbin.systemuicontroller.setSystemBarsColor
import org.cardna.CardNaApplication
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.R
import org.cardna.data.local.singleton.CardNaRepository
import org.cardna.presentation.util.StatusBarUtil
import org.cardna.presentation.util.setSrcWithGlide
import org.cardna.presentation.util.shortToast
import timber.log.Timber
import java.io.*

@AndroidEntryPoint
class CardShareActivity : BaseViewUtil.BaseAppCompatActivity<ActivityCardShareBinding>(R.layout.activity_card_share) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        setClickListener()
        this.setSystemBarsColor(Color.BLACK, false)
    }

    override fun initView() {
        // intent 로 넘겨준 카드 이미지 세팅
        setSrcWithGlide(intent.getStringExtra(BaseViewUtil.CARD_IMG)!!, binding.ivCardShareImg)

        // intent 로 넘겨준 카드 title 세팅
        binding.tvCardshareTitle.text = intent.getStringExtra(BaseViewUtil.CARD_TITLE)

        // 카드나, 카드너에 따라
        if(intent.getStringExtra(BaseViewUtil.IS_CARD_ME_OR_YOU) == "me"){ // 카드나
            // 배경색 설정
            binding.ctlCardShareImage.setBackgroundResource(R.drawable.bg_cardme)

            // ?? 님의 카드나 색, 내용 설정
            binding.tvCardmeOrYou.setTextColor(ContextCompat.getColor(this, R.color.main_green))
            if(CardNaRepository.userSocial == "naver"){ // 네이버일 경우
               binding.tvCardmeOrYou.text = resources.getString(R.string.cardshare_cardme, CardNaRepository.naverUserfirstName)
            }
            else{ // 카카오일 경우
               binding.tvCardmeOrYou.text = resources.getString(R.string.cardshare_cardme, CardNaRepository.kakaoUserfirstName)
            }
        }
        else{ // 카드너
            binding.ctlCardShareImage.setBackgroundResource(R.drawable.bg_cardyou)

            // title 색 설정
            binding.tvCardmeOrYou.setTextColor(ContextCompat.getColor(this, R.color.main_purple))

            // ?? 님의 카드너
            if(CardNaRepository.userSocial == "naver"){
                binding.tvCardmeOrYou.text = resources.getString(R.string.cardshare_cardyou, CardNaRepository.naverUserfirstName)
            }
            else{
                binding.tvCardmeOrYou.text = resources.getString(R.string.cardshare_cardyou, CardNaRepository.kakaoUserfirstName)
            }
        }
    }


    fun setClickListener() {

        // 저장하기 버튼 누르면
        binding.ctlCardShareSave.setOnClickListener {
            // 카드 이미지 저장전, 저장하기 공유하기 글자 잠깐 없애기
            binding.ctlCardShare.visibility = View.GONE
            binding.ctlCardShareSave.visibility = View.GONE

            // 이미지 저장 => 사진 이름 설정
            saveCardImageToGallery(binding.ctlCardShareCapture, "cardna")

            // 다시 보이도록
            binding.ctlCardShare.visibility = View.VISIBLE
            binding.ctlCardShareSave.visibility = View.VISIBLE
        }

        // 공유하기 버튼 누르면
        binding.ctlCardShare.setOnClickListener {
            binding.ctlCardShare.visibility = View.GONE
            binding.ctlCardShareSave.visibility = View.GONE

            //view->bitmap: 공유하고싶은 이미지 ctl view를 bitmap으로 변환 후
            val bitmap = viewToBitmap(binding.ctlCardShareCapture)

            binding.ctlCardShare.visibility = View.VISIBLE
            binding.ctlCardShareSave.visibility = View.VISIBLE

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

    // 카드 이미지 저장
    private fun saveCardImageToGallery(view: View?, title: String) {
        if (view == null)  // NPE 방지
            return

        // 원하는 뷰를 비트맵 이미지로 저장
        val bitmap = viewToBitmap(view)

        var fos: OutputStream? = null

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { // Android API Level Q 이상
            Timber.d("Q 이상")
            this?.contentResolver?.also { resolver ->

                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, System.currentTimeMillis().toString() + ".png")
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg") // 왜 여기는 jpg?
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                // 6
                val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                // 7
                fos = imageUri?.let { resolver.openOutputStream(it) }

            }
        } else { // Android API Level Q 미만
            Timber.d("Q 미만")

            // 일단 권한을 요청해야함. 아니면 permission denied error 남
            val writePermission = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

            if(writePermission == PackageManager.PERMISSION_GRANTED) { // 권한 요청 받았을 때
                val externalStorage =
                    Environment.getExternalStorageDirectory().absolutePath // 외부저장소의 절대 경로 찾음
                val dir = File(externalStorage)

                if (dir.exists().not()) { // 폴더 없으면 생성 ?
                    Timber.d("폴더 없음")
                    dir.mkdirs()
                }

                try {
                    val filename = System.currentTimeMillis().toString() + ".png"
                    val fileItem = File("$dir/$filename")
                    fileItem.createNewFile()
                    fos = FileOutputStream(fileItem)
                    //파일 아웃풋 스트림 객체를 통해서 Bitmap 압축.
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            else{
                val requestExternalStorageCode = 1

                val permissionStorage = arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )

                ActivityCompat.requestPermissions(this, permissionStorage, requestExternalStorageCode)
            }
        }

        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            shortToast("사진이 저장되었습니다.")
        }

        fos?.close()
    }


    private fun viewToBitmap(view: View): Bitmap {
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

}