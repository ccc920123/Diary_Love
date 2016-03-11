#定位原文件位置，是MK可执行的第一句 call my-dir返回是当前的目录
LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
#打包生成库的名字
    LOCAL_MODULE    := image_jni
    #加载的C文件
    LOCAL_SRC_FILES := mimage.c
#自动导入头文件
LOCAL_C_INCLUDES := \ $(JNI_H_INCLUDE)
#是否预编译
LOCAL_PRELINK_MODULE:=false
#导入用到的其它库
LOCAL_LDLIBS := -llog -ljnigraphics
include $(BUILD_SHARED_LIBRARY)