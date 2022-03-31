package org.cardna.presentation.ui.cardpack.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cardna.R

class CardCreateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_create)
    }

    // 카드나 작성, 카드너 작성 둘다 구현
}