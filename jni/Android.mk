#��λԭ�ļ�λ�ã���MK��ִ�еĵ�һ�� call my-dir�����ǵ�ǰ��Ŀ¼
LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
#������ɿ������
    LOCAL_MODULE    := image_jni
    #���ص�C�ļ�
    LOCAL_SRC_FILES := mimage.c
#�Զ�����ͷ�ļ�
LOCAL_C_INCLUDES := \ $(JNI_H_INCLUDE)
#�Ƿ�Ԥ����
LOCAL_PRELINK_MODULE:=false
#�����õ���������
LOCAL_LDLIBS := -llog -ljnigraphics
include $(BUILD_SHARED_LIBRARY)