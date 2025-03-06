package ktb.hackothon.chatgae.domain.pet.dto;

public class PetResponseCode {
        public static final int PET_ALREADY_REGISTERED = -100;  // 이미 등록된 강아지
        public static final int AI_PROCESSING_FAILED = -101;    // 인공지능에서 실패
        public static final int SERVER_ERROR = -102;            // 서버에서 실패
        public static final int PET_NOT_FOUND = -999;           // 존재하지 않는 반려견 고유 번호
        public static final int SUCCESS = 200;                  // 등록 성공
}
