package com.demo.network.type

import androidx.annotation.IntDef

/**
 * Created by lizhiping on 2023/3/7.
 * <p>
 * description
 */

@Retention(AnnotationRetention.SOURCE)
@IntDef(
    RelationshipType.OTHER,
    RelationshipType.ME,
    RelationshipType.CHILDREN,
    RelationshipType.WIFE,
    RelationshipType.HUSBAND,
    RelationshipType.FATHER,
    RelationshipType.MOTHER,
    RelationshipType.COLLEAGUE,
    RelationshipType.FRIEND,
    RelationshipType.SCHOOLMATE,
    RelationshipType.LADY_BRO,
    RelationshipType.BOY_FRIEND,
    RelationshipType.GIRL_FRIEND,
    RelationshipType.MATERNAL_GRANDFATHER,
    RelationshipType.MATERNAL_GRANDMOTHER,
    RelationshipType.GRANDFATHER,
    RelationshipType.GRANDMOTHER,
    RelationshipType.ELDER_BROTHER,
    RelationshipType.ELDER_SISTER,
    RelationshipType.YOUNGER_BROTHER,
    RelationshipType.YOUNGER_SISTER
)
annotation class RelationshipType {
    companion object {
        const val OTHER                 = 0  // 其它
        const val ME                    = 1  // 我
        const val CHILDREN              = 2  // 孩子
        const val WIFE                  = 3  // 妻子
        const val HUSBAND               = 4  // 丈夫
        const val FATHER                = 5  // 爸爸
        const val MOTHER                = 6  // 妈妈
        const val COLLEAGUE             = 7  // 同事
        const val FRIEND                = 8  // 朋友
        const val SCHOOLMATE            = 9  // 同学
        const val LADY_BRO              = 10 // 闺蜜
        const val BOY_FRIEND            = 11 // 男朋友
        const val GIRL_FRIEND           = 12 // 女朋友
        const val MATERNAL_GRANDFATHER  = 13 // 外公
        const val MATERNAL_GRANDMOTHER  = 14 // 外婆
        const val GRANDFATHER           = 15 // 爷爷
        const val GRANDMOTHER           = 16 // 奶奶
        const val ELDER_BROTHER         = 17 // 哥哥
        const val ELDER_SISTER          = 18 // 姐姐
        const val YOUNGER_BROTHER       = 19 // 弟弟
        const val YOUNGER_SISTER        = 20 // 妹妹
    }

}