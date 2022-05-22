package org.cardna.presentation.ui.cardpack.view

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BottomCardLamdaData(
    val BottomCardListener: (Boolean) -> (Unit)
) : Parcelable


//
//private fun showBottomDialog(
//    commentPk: String, data: ResponsePostData.Data.Comment,
//    commentUpdateListener: (ResponsePostData.Data.Comment) -> (Unit),
//    commentDeleteListener: (ResponsePostData.Data.Comment) -> (Unit)
//) {
//
//    val bottomSheetFragment = BottomDialogMyPageFragment()
//    bottomSheetFragment.arguments = Bundle().apply {
//        putString(BottomDialogMyPageFragment.COMMENT_PK, commentPk)
//        putString(
//            BottomDialogMyPageFragment.EDIT_TYPE,
//            BottomDialogMyPageFragment.COMMUNITY_COMMENT
//        )
//        putSerializable(
//            BottomDialogMyPageFragment.COMMENT_DATA,
//            CommentData(data, commentUpdateListener, commentDeleteListener)
//        )
//    }
//    bottomSheetFragment.show(
//        supportFragmentManager,
//        bottomSheetFragment.tag
//    )
//}
//
//private lateinit var commentData: CommentData
//
//if (arguments?.getSerializable(COMMENT_DATA) != null)
//commentData = arguments?.getSerializable(COMMENT_DATA) as CommentData