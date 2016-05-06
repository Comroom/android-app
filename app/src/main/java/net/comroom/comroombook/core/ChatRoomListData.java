package net.comroom.comroombook.core;

import android.graphics.drawable.Drawable;

import java.text.Collator;
import java.util.Comparator;

/**
 * Created by younghokim on 16. 5. 6.
 */
public class ChatRoomListData {
    /**
     * 리스트 정보를 담고 있을 객체 생성
     */
    // 아이콘
    public Drawable mRoomIcon;

    // 제목
    public String mRoomName;

    // 날짜
    public String mRoomNumber;
    /**
     * 알파벳 이름으로 정렬
     */
    public static final Comparator<ChatRoomListData> ALPHA_COMPARATOR = new Comparator<ChatRoomListData>() {
        private final Collator sCollator = Collator.getInstance();

        @Override
        public int compare(ChatRoomListData mListDate_1, ChatRoomListData mListDate_2) {
            return sCollator.compare(mListDate_1.mRoomName, mListDate_2.mRoomNumber);
        }
    };
}
