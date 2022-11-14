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
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.amplitude.api.Amplitude
import dagger.hilt.android.AndroidEntryPoint
import land.sungbin.systemuicontroller.setSystemBarsColor
import org.cardna.R
import org.cardna.data.local.singleton.CardNaRepository
import org.cardna.databinding.ActivityCardShareBinding
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.util.setSrcWithGlide
import org.cardna.presentation.util.shortToast
import timber.log.Timber
import java.io.*


@AndroidEntryPoint
class CardShareActivity :
    BaseViewUtil.BaseAppCompatActivity<ActivityCardShareBinding>(R.layout.activity_card_share) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        setClickListener()
        this.setSystemBarsColor(Color.BLACK, false)
    }

    override fun initView() {
        setSrcWithGlide(intent.getStringExtra(BaseViewUtil.CARD_IMG)!!, binding.ivCardShareImg)

        binding.tvCardshareTitle.text = intent.getStringExtra(BaseViewUtil.CARD_TITLE)

        if (intent.getStringExtra(BaseViewUtil.IS_CARD_ME_OR_YOU) == "me") { // 카드나
            binding.ctlCardShareImage.setBackgroundResource(R.drawable.bg_cardme)

            binding.tvCardmeOrYou.setTextColor(ContextCompat.getColor(this, R.color.main_green))
            if (CardNaRepository.userSocial == "naver") {
                binding.tvCardmeOrYou.text = resources.getString(
                    R.string.cardshare_cardme,
                    CardNaRepository.naverUserfirstName
                )
            } else {
                binding.tvCardmeOrYou.text = resources.getString(
                    R.string.cardshare_cardme,
                    CardNaRepository.kakaoUserfirstName
                )
            }
        } else {
            binding.ctlCardShareImage.setBackgroundResource(R.drawable.bg_cardyou)

            binding.tvCardmeOrYou.setTextColor(ContextCompat.getColor(this, R.color.main_purple))

            if (CardNaRepository.userSocial == "naver") {
                binding.tvCardmeOrYou.text = resources.getString(
                    R.string.cardshare_cardyou,
                    CardNaRepository.naverUserfirstName
                )
            } else {
                binding.tvCardmeOrYou.text = resources.getString(
                    R.string.cardshare_cardyou,
                    CardNaRepository.kakaoUserfirstName
                )
            }
        }
    }


    fun setClickListener() {

        binding.ctlCardShareSave.setOnClickListener {
            if (intent.getStringExtra(BaseViewUtil.IS_CARD_ME_OR_YOU) == "me") {
                Amplitude.getInstance().logEvent("CardPack_Cardna_Share_Save")
            } else {
                Amplitude.getInstance().logEvent("CardPack_Cardner_Share_Save")
            }

            binding.ctlCardShare.visibility = View.GONE
            binding.ctlCardShareSave.visibility = View.GONE

            saveCardImageToGallery(binding.ctlCardShareCapture, "cardna")

            binding.ctlCardShare.visibility = View.VISIBLE
            binding.ctlCardShareSave.visibility = View.VISIBLE
        }

        binding.ctlCardShare.setOnClickListener {
            if (intent.getStringExtra(BaseViewUtil.IS_CARD_ME_OR_YOU) == "me") {
                Amplitude.getInstance().logEvent("CardPack_Cardna_Share_SNSShare")
            } else {
                Amplitude.getInstance().logEvent("CardPack_Cardner_Share_SNSShare")
            }
            binding.ctlCardShare.visibility = View.GONE
            binding.ctlCardShareSave.visibility = View.GONE

            val screenBitmap = viewToBitmap(binding.ctlCardShareCapture)

            binding.ctlCardShare.visibility = View.VISIBLE
            binding.ctlCardShareSave.visibility = View.VISIBLE

            val cachePath = File(applicationContext.cacheDir, "images")

            if (cachePath.exists().not()) {
                Timber.d("폴더 없음")
                cachePath.mkdirs()
            }

            val stream = FileOutputStream("$cachePath/image.png")
            screenBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.close()


            val newFile = File(cachePath, "image.png")
            val contentUri = FileProvider.getUriForFile(
                applicationContext,
                "org.cardna.fileprovider", newFile
            )

            val Sharing_intent = Intent(Intent.ACTION_SEND)
            Sharing_intent.type = "image/png"
            Sharing_intent.putExtra(Intent.EXTRA_STREAM, contentUri)
            startActivity(Intent.createChooser(Sharing_intent, "Share image"))
        }
    }

    private fun saveCardImageToGallery(view: View?, title: String) {
        if (view == null)
            return

        val bitmap = viewToBitmap(view)

        var fos: OutputStream? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { // Android API Level Q 이상
            Timber.d("Q 이상")


            this?.contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(
                        MediaStore.MediaColumns.DISPLAY_NAME,
                        System.currentTimeMillis().toString() + ".png"
                    )
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/CARDNA")
                }

                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                fos = imageUri?.let { resolver.openOutputStream(it) }

            }
        } else {
            val writePermission = ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )

            if (writePermission == PackageManager.PERMISSION_GRANTED) {
                val externalStorage =
                    Environment.getExternalStorageDirectory().absolutePath
                val path = "$externalStorage/CARDNA"
                val dir = File(path)

                if (dir.exists().not()) {
                    Timber.d("폴더 없음")
                    dir.mkdirs()
                }

                try {
                    val filename = System.currentTimeMillis().toString() + ".png"
                    val fileItem = File("$dir/$filename")
                    fileItem.createNewFile()
                    fos = FileOutputStream(fileItem)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                val requestExternalStorageCode = 1

                val permissionStorage = arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )

                ActivityCompat.requestPermissions(
                    this,
                    permissionStorage,
                    requestExternalStorageCode
                )
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