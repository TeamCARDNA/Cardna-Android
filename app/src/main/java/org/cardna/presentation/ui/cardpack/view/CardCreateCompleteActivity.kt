package org.cardna.presentation.ui.cardpack.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.text.set
import androidx.core.text.toSpannable
import com.bumptech.glide.Glide
import com.example.cardna.R
import com.example.cardna.databinding.ActivityCardCreateCompleteBinding
import org.cardna.presentation.MainActivity
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.util.LinearGradientSpan

class CardCreateCompleteActivity : BaseViewUtil.BaseAppCompatActivity<ActivityCardCreateCompleteBinding>(R.layout.activity_card_create_complete) {
    // 1. 내 카드나 => 카드나를 만들었어요 !
    //  => isCardMeOrYou, symbolId, cardImg, cardTitle 넘겨받음
    // 2. 내 카드너 => 카드너가 추가됐어요 !  <= 카드너보관함에서 카드상세에서 카드너 추가하기 하면 만들어짐
    //  => 카드상세Activity에서 넘겨줌.  isCardMeOrYou, symbolId, cardImg, cardTitle 넘겨받음

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
        setCardMeOrYou()
    }


    private fun setCardMeOrYou() {
        val isCardMeOrYou = intent.getBooleanExtra("isCardMeOrYou", CARD_ME) // 안넘겨줄 경우, CARDME
        val symbolId = intent.getIntExtra("symbolId", -1) // symbolId가 null일 때 -1로
        val cardImg = Uri.parse(intent.getStringExtra("cardImg")) // uri를 string으로 변환한 값을 받아 다시 uri로
        val cardTitle = intent.getStringExtra("cardTitle")
        // 카드나인지, 카드너인지에 따라 뷰 띄워주기
        // textView 바꿔주기
        if (isCardMeOrYou == CARD_ME) { // 카드나 작성 완료 하면 만들기
            binding.tvCardcreateComplete.text = getString(R.string.cardcreate_complete_cardme)
            binding.clCardcreateComplete.setBackgroundResource(R.drawable.bg_cardme)

            if (symbolId == -1) { // uri 값의 이미지를 띄워주면 됨.
                Glide.with(this).load(cardImg).into(binding.ivCardcreateComplete)
            } else { // symbolId가 null이 아니라면 이에 해당하는 각 심볼 이미지 띄워주면 됨 이라면,
                when (symbolId) {
                    SYMBOL_0 -> binding.ivCardcreateComplete.setImageResource(R.drawable.ic_symbol_cardme_0)
                    SYMBOL_1 -> binding.ivCardcreateComplete.setImageResource(R.drawable.ic_symbol_cardme_1)
                    SYMBOL_2 -> binding.ivCardcreateComplete.setImageResource(R.drawable.ic_symbol_cardme_2)
                    SYMBOL_3 -> binding.ivCardcreateComplete.setImageResource(R.drawable.ic_symbol_cardme_3)
                    SYMBOL_4 -> binding.ivCardcreateComplete.setImageResource(R.drawable.ic_symbol_cardme_4)
                }
            }
        }
        else if (isCardMeOrYou == CARD_YOU) { // 카드너 추가 완료 만들기
            binding.tvCardcreateComplete.text = getString(R.string.cardcreate_complete_cardyou)
            binding.clCardcreateComplete.setBackgroundResource(R.drawable.bg_cardyou)

            // 카드너일 경우,
            // 카드너보관함 액티비티에서 카드누르고 그 상세페이지에서 완료버튼을 누르면 현재 액티비티가 띄워지므로
            // 이전액티비티에서 symbol 로 된 이미지도 uri 로 줄것이므로 glide 이용해서 uri 를 imageView 에 띄워주기만 하면 될 듯
            Glide.with(this).load(cardImg).into(binding.ivCardcreateComplete)
        }

        binding.tvCardcreateCompleteTitle.text = cardTitle
        setTextGradient()
        setLottie(isCardMeOrYou)
    }

    private fun setLottie(isCardMeOrYou: Boolean){
        // 로티 띄워주고 인텐트 이용해서 이전 액티비티로 가기
        // onActivityResult? 비스무리한 그 메서드 쓰면 더 좋게 구현할 수 있지 않을까
        val handler = Handler(Looper.getMainLooper())
        if (isCardMeOrYou == CARD_ME) { // 카드나일 경우, MainActivity 로 돌아가줘야 함
            handler.postDelayed({
                // 카드나 작성에서 왔다면 MainActivity 로 돌아가도록 intent 를 연결시켜줘야 하고,
                var intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                // MainActivity 로 갈 때, CardCreateActivity pop 하고 가기
                // 현재 A -> B -> C인데, C -> A로 가도록 intent 써서
            }, LOTTIE_VIEW_TIME) // 이는 CardCreateActivity 가 얼마나 띄워주고 다시 main 으로 갈 건지에 대한 시간, 로티가 뜨는 시간은 아님
        }
        else if(isCardMeOrYou == CARD_YOU) { // 카드너일 경우, 카드너보관함으로 돌아가줘아 함.
            handler.postDelayed({
                    // 카드너추가 액티비티에서 왔다면 OtherWriteActivity 로 돌아가야 한다. 근데 이때 OtherWriteActivity 로 전달해줄 정보는 없고
                    // OtherWriteActivity 에서 서버 통신 다시 하도록 => onResume 메서드 작성해주기
                    var intent = Intent(this, CardYouStoreActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                    // 카드너보관함 액티비티인 OtherWriteActivity로 갈 때, CardCreateActivity pop하고 가기
                    // 현재 A -> B -> C인데, C -> A로 가도록 intent 써서
                }, LOTTIE_VIEW_TIME
            )
        }
    }

    fun setTextGradient() { // text gradation
        val text = binding.tvCardcreateComplete.text.toString()
        val green = getColor(R.color.main_green)
        val purple = getColor(R.color.main_purple)
        val spannable = text.toSpannable()
        spannable[0..text.length] = LinearGradientSpan(text, text, green, purple)
        binding.tvCardcreateComplete.text = spannable
    }

    companion object {
        const val CARD_ME = true
        const val CARD_YOU = false

        const val SYMBOL_0 = 1
        const val SYMBOL_1 = 2
        const val SYMBOL_2 = 3
        const val SYMBOL_3 = 4
        const val SYMBOL_4 = 5

        const val LOTTIE_VIEW_TIME = 1670L
    }


}