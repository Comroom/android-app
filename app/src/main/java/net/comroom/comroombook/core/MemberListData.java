package net.comroom.comroombook.core;

import java.text.Collator;
import java.util.Comparator;

import android.graphics.drawable.Drawable;

public class MemberListData {
    /**
     * 리스트 정보를 담고 있을 객체 생성
     */
    // 아이콘
    public Drawable mIcon;

    // 제목
    public String mName;

    // 날짜
    public String mEmail;
    /**
     * 알파벳 이름으로 정렬
     */
    public static final Comparator<MemberListData> ALPHA_COMPARATOR = new Comparator<MemberListData>() {
        private final Collator sCollator = Collator.getInstance();

        @Override
        public int compare(MemberListData mListDate_1, MemberListData mListDate_2) {
            return sCollator.compare(mListDate_1.mName, mListDate_2.mEmail);
        }
    };
}
