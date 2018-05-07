LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := librarys
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_SRC_FILES := \
	/Users/zhangdongdong/AndroidStudioProjects/BJMZ/librarys/src/main/jni/Android.mk \
	/Users/zhangdongdong/AndroidStudioProjects/BJMZ/librarys/src/main/jni/luajava/Android.mk \
	/Users/zhangdongdong/AndroidStudioProjects/BJMZ/librarys/src/main/jni/luajava/luajava.c \
	/Users/zhangdongdong/AndroidStudioProjects/BJMZ/librarys/src/main/jni/Application.mk \
	/Users/zhangdongdong/AndroidStudioProjects/BJMZ/librarys/src/main/jni/lua/lauxlib.c \
	/Users/zhangdongdong/AndroidStudioProjects/BJMZ/librarys/src/main/jni/lua/liolib.c \
	/Users/zhangdongdong/AndroidStudioProjects/BJMZ/librarys/src/main/jni/lua/lopcodes.c \
	/Users/zhangdongdong/AndroidStudioProjects/BJMZ/librarys/src/main/jni/lua/lstate.c \
	/Users/zhangdongdong/AndroidStudioProjects/BJMZ/librarys/src/main/jni/lua/lobject.c \
	/Users/zhangdongdong/AndroidStudioProjects/BJMZ/librarys/src/main/jni/lua/Android.mk \
	/Users/zhangdongdong/AndroidStudioProjects/BJMZ/librarys/src/main/jni/lua/lmathlib.c \
	/Users/zhangdongdong/AndroidStudioProjects/BJMZ/librarys/src/main/jni/lua/loadlib.c \
	/Users/zhangdongdong/AndroidStudioProjects/BJMZ/librarys/src/main/jni/lua/lvm.c \
	/Users/zhangdongdong/AndroidStudioProjects/BJMZ/librarys/src/main/jni/lua/lfunc.c \
	/Users/zhangdongdong/AndroidStudioProjects/BJMZ/librarys/src/main/jni/lua/lstrlib.c \
	/Users/zhangdongdong/AndroidStudioProjects/BJMZ/librarys/src/main/jni/lua/linit.c \
	/Users/zhangdongdong/AndroidStudioProjects/BJMZ/librarys/src/main/jni/lua/lstring.c \
	/Users/zhangdongdong/AndroidStudioProjects/BJMZ/librarys/src/main/jni/lua/lundump.c \
	/Users/zhangdongdong/AndroidStudioProjects/BJMZ/librarys/src/main/jni/lua/ltable.c \
	/Users/zhangdongdong/AndroidStudioProjects/BJMZ/librarys/src/main/jni/lua/ldump.c \
	/Users/zhangdongdong/AndroidStudioProjects/BJMZ/librarys/src/main/jni/lua/loslib.c \
	/Users/zhangdongdong/AndroidStudioProjects/BJMZ/librarys/src/main/jni/lua/lgc.c \
	/Users/zhangdongdong/AndroidStudioProjects/BJMZ/librarys/src/main/jni/lua/lzio.c \
	/Users/zhangdongdong/AndroidStudioProjects/BJMZ/librarys/src/main/jni/lua/ldblib.c \
	/Users/zhangdongdong/AndroidStudioProjects/BJMZ/librarys/src/main/jni/lua/lmem.c \
	/Users/zhangdongdong/AndroidStudioProjects/BJMZ/librarys/src/main/jni/lua/lcode.c \
	/Users/zhangdongdong/AndroidStudioProjects/BJMZ/librarys/src/main/jni/lua/ltablib.c \
	/Users/zhangdongdong/AndroidStudioProjects/BJMZ/librarys/src/main/jni/lua/lapi.c \
	/Users/zhangdongdong/AndroidStudioProjects/BJMZ/librarys/src/main/jni/lua/lbaselib.c \
	/Users/zhangdongdong/AndroidStudioProjects/BJMZ/librarys/src/main/jni/lua/ldebug.c \
	/Users/zhangdongdong/AndroidStudioProjects/BJMZ/librarys/src/main/jni/lua/lparser.c \
	/Users/zhangdongdong/AndroidStudioProjects/BJMZ/librarys/src/main/jni/lua/llex.c \
	/Users/zhangdongdong/AndroidStudioProjects/BJMZ/librarys/src/main/jni/lua/ltm.c \
	/Users/zhangdongdong/AndroidStudioProjects/BJMZ/librarys/src/main/jni/lua/ldo.c \

LOCAL_C_INCLUDES += /Users/zhangdongdong/AndroidStudioProjects/BJMZ/librarys/src/main/jni
LOCAL_C_INCLUDES += /Users/zhangdongdong/AndroidStudioProjects/BJMZ/librarys/src/release/jni

include $(BUILD_SHARED_LIBRARY)
