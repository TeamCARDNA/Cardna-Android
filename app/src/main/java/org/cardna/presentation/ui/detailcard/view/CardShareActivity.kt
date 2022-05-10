package org.cardna.presentation.ui.detailcard.view

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import org.cardna.databinding.ActivityCardShareBinding
import dagger.hilt.android.AndroidEntryPoint
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.R
import org.cardna.presentation.util.setSrcWithGlide
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class CardShareActivity : BaseViewUtil.BaseAppCompatActivity<ActivityCardShareBinding>(R.layout.activity_card_share) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        setClickListener()
    }

    override fun initView() {
        setSrcWithGlide(intent.getStringExtra(BaseViewUtil.CARD_IMG)!!, binding.ivShareCard)
    }


    fun setClickListener() {
        //공유하기 버튼을 누르면
        binding.tvShare.setOnClickListener {

            //view->bitmap: 공유하고싶은 이미지 ctl view를 bitmap으로 변환 후
            val bitmap = viewToBitmap(binding.ivShareCard)

            //bitmap->url
            val uri: Uri? = getImageUri(this, bitmap)


//            val uri: Uri? = Uri.parse(intent.getStringExtra(BaseViewUtil.CARD_IMG))
            //인텐트에 url넣어서 시작하기
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
}